package design.besteffort1pc;

import java.io.Serializable;

public class OrderAcceptedEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String orderId;

	private final String authority;
	
	public OrderAcceptedEvent(String orderId, String authority) {
		this.orderId = orderId;
		this.authority = authority;
	}

	public String getOrderId() {
		return orderId;
	}
	
	public String getAuthority() {
		return authority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
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
		OrderAcceptedEvent other = (OrderAcceptedEvent) obj;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
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
		return "OrderAcceptedEvent [orderId=" + orderId + ", authority=" + authority + "]";
	}

}
