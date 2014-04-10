package design.besteffort1pc;

import java.io.Serializable;

public class PlaceOrderCommand implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String orderId;
	
	private final String details;

	public PlaceOrderCommand(String orderId, String details) {
		this.orderId = orderId;
		this.details = details;
	}

	public String getOrderId() {
		return orderId;
	}
	
	public String getDetails() {
		return details;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((details == null) ? 0 : details.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceOrderCommand other = (PlaceOrderCommand) obj;
		if (details == null) {
			if (other.details != null)
				return false;
		} else if (!details.equals(other.details))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlaceOrderCommand [orderId=" + orderId + ", details=" + details + "]";
	}

}
