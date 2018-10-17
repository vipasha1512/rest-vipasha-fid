/**
*
* @author  Vipasha Rana
* @version 1.0
*/
package com.fidelity.demo;

public class Order {

	int order_id;
	String side;
	String security;
	String fund_name;
	int quantity;
	double amount;

	public Order(int order_id, String side, String security, String fund_name, int quantity, double amount) {
		super();
		this.order_id = order_id;
		this.side = side;
		this.security = security;
		this.fund_name = fund_name;
		this.quantity = quantity;
		this.amount = amount;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getFund_name() {
		return fund_name;
	}

	public void setFund_name(String fund_name) {
		this.fund_name = fund_name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
