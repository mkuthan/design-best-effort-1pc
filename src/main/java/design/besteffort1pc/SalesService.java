package design.besteffort1pc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;

@Component
public class SalesService {

	private static final Logger LOG = LoggerFactory.getLogger(SalesService.class);

	@Autowired
	private OrderRepository orderRepository;

	@Transformer
	public OrderAcceptedEvent processOrder(PlaceOrderCommand placeOrderCommand) {
		LOG.info("Processing: " + placeOrderCommand);

		Order order = new Order(placeOrderCommand.getOrderId());
		LOG.info("Saving: " + order);
		orderRepository.save(order);

		OrderAcceptedEvent orderAcceptedEvent = new OrderAcceptedEvent(order.getId(), "Yogi Bear");
		LOG.info("Publishing: " + orderAcceptedEvent);
		return orderAcceptedEvent;
	}

}
