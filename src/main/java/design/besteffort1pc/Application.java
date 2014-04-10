package design.besteffort1pc;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@EnableAutoConfiguration(exclude = { DataSourceTransactionManagerAutoConfiguration.class })
@ImportResource("/integration.xml")
@ComponentScan
public class Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	@Value("${application.incomingQueue}")
	private String incomingQueueName;

	@Value("${application.outgoingQueue}")
	private String outgoingQueueName;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private AnnotationConfigApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand("some order id", "some order details");

		LOG.info("Sending " + placeOrderCommand);
		rabbitTemplate.convertAndSend(incomingQueueName, placeOrderCommand);

		Thread.sleep(3000);

		OrderAcceptedEvent orderAcceptedEvent = (OrderAcceptedEvent) rabbitTemplate
				.receiveAndConvert(outgoingQueueName);
		LOG.info("Received " + orderAcceptedEvent);

		context.close();
	}

	@Bean
	DataSourceTransactionManager dbTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	RabbitTransactionManager amqpTransactionManager(ConnectionFactory connectionFactory) {
		return new RabbitTransactionManager(connectionFactory);
	}

	@Bean
	ChainedTransactionManager inboundTransactionManager(RabbitTransactionManager amqpTransactionManager,
			DataSourceTransactionManager dbTransactionManager) {
		return new ChainedTransactionManager(amqpTransactionManager, dbTransactionManager);
	}

	@Bean
	ChainedTransactionManager outboundTransactionManager(DataSourceTransactionManager dbTransactionManager,
			RabbitTransactionManager amqpTransactionManager) {
		return new ChainedTransactionManager(dbTransactionManager, amqpTransactionManager);
	}

	@Bean
	Queue incomingQueue() {
		return new Queue(incomingQueueName);
	}

	@Bean
	Queue outgoingQueue() {
		return new Queue(outgoingQueueName);
	}

	@Bean
	TopicExchange exchange(@Value("${application.exchange}") String exchangeName) {
		return new TopicExchange(exchangeName);
	}

	@Bean
	Binding incomingQueueBinding(TopicExchange exchange, @Qualifier("incomingQueue") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(incomingQueueName);
	}

	@Bean
	Binding outgoingQueueBinding(TopicExchange exchange, @Qualifier("outgoingQueue") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(outgoingQueueName);
	}

}
