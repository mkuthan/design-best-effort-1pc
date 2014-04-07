package design.besteffort1pc;

public class AggregateRoot {

	private final String id;

	public AggregateRoot(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "AggregateRoot [id=" + id + "]";
	}

}
