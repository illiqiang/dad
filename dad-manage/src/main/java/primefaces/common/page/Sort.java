package primefaces.common.page;

public class Sort {
	
	public Sort(String field, Order order) {
		
	}
	
	public Sort() {
		
	}
	
	private String field;
	private Order order;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
