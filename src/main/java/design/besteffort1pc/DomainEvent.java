package design.besteffort1pc;

import java.io.Serializable;

public class DomainEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	private String payload;

	public DomainEvent(String payload) {
		this.payload = payload;
	}

	public String getPayload() {
		return payload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
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
		DomainEvent other = (DomainEvent) obj;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DomainEvent [payload=" + payload + "]";
	}

}
