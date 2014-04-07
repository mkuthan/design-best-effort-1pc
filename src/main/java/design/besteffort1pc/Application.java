package design.besteffort1pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private static final String QUEUE_NAME = "besteffort1pc";

	@Autowired
	private AggregateRootRepository aggregateRootRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String sourceAggregateRootId = "src_id";

		LOG.info("==== Save Source Aggregate Root '{}' ====", sourceAggregateRootId);
		aggregateRootRepository.save(new AggregateRoot(sourceAggregateRootId));

		DomainEvent domainEvent = new DomainEvent("payload");

		LOG.info("==== Publish '{}' ====", domainEvent);
		rabbitTemplate.convertAndSend(QUEUE_NAME, domainEvent);
	}

	public void handleMessage(DomainEvent domainEvent) {
		LOG.info("==== Receive '{}' ====", domainEvent);

		String targetAggregateRootId = "trg_id";

		LOG.info("==== Save Target Aggregate Root '{}' ====", targetAggregateRootId);
		aggregateRootRepository.save(new AggregateRoot(targetAggregateRootId));
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter() {
		return new MessageListenerAdapter(this);
	}

	@Bean
	Queue queue() {
		boolean durable = false;
		return new Queue(QUEUE_NAME, durable);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
	}

}
