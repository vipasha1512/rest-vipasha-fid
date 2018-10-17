/**
*
* @author  Vipasha Rana
* @version 1.0
*/


package com.fidelity.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

@Controller
public class OrderController {

	ArrayList<Order> list = new ArrayList<Order>();
	Object getFileData;
	JSONArray fileDataArray;
	String updateDetails;
	int dataLength = 0;
	
	
	/**
	 *  updateData()  : Used annotation @Scheduled(fixedRate = 60000)
	 *  for getting the data from the json file every minute. 
	 *  
	 *  @return void
	 */
	@Scheduled(fixedRate = 60000)
	public void updateData() {

		
		try {
			dataLength = list.size();
			list.clear();
			getFileData = new JSONParser().parse(new FileReader("data.json"));
			fileDataArray = (JSONArray) getFileData;
			for (int index = 0; index < fileDataArray.size(); index++) {
				JSONObject jsonobject = (JSONObject) fileDataArray.get(index);
				Order order = new Order((int) (long) jsonobject.get("orderid"), (String) jsonobject.get("side"),
						(String) jsonobject.get("security"), (String) jsonobject.get("fundname"),
						(int) (long) jsonobject.get("quantity"), (double) (long) jsonobject.get("price"));

				list.add(order);

			}
			if (list.size() > dataLength) {
				updateDetails = "New Data Added";
			} else {
				updateDetails = "No New Data Added";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * orders() : map to /orders ,  Gives a list of all the data in raw format with a  message
	 *providing the status of updates in the data.json file recently in form of a json object.
	 *
	 * @return raw data-JSON Object
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/orders")
	public @ResponseBody JSONObject orders() {
		JSONObject message = new JSONObject();
		message.put("message", updateDetails);
		message.put("data", list);
		return message;
	}

	
	
	/**
	 * ordersSummary() : map to /ordersSummary, gives a summary
	 * with prcessed details of the raw data as well as a message  providing the 
	 * status of updates in the data.json file recently in form of a json object.
	 *
	 * @return processed data in JSON format
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ordersSummary")
	public @ResponseBody JSONObject ordersSummary() {
		int totalOrders = 0;
		int totalQuantity = 0;
		double totalPrice = 0;
		double avg = 0;
		Map<String, Integer> combination = new HashMap<>();
		String combi = "";
		int combiCounter = 0;
		JSONObject message = new JSONObject();
		JSONObject output = new JSONObject();
		

		for (Order item : list) {

			String data = "(" + item.getSide() + "+" + item.getSecurity() + "+" + item.getFund_name() + ")";
			if (combination.containsKey(data)) {
				combination.put(data, combination.get(data) + 1);

			} else
				combination.put(data, 1);
		}

		for (Map.Entry<String, Integer> entry : combination.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value > 1) {
				combi = combi + key;
				combiCounter++;
			}

		}

		combi = Integer.toString(combiCounter) + combi;

		for (int index = 0; index < list.size(); index++) {
			Order order = list.get(index);
			totalPrice = totalPrice + order.getAmount();
			totalQuantity = totalQuantity + order.getQuantity();
			totalOrders++;

		}

		avg = totalPrice / totalOrders;
		output.put("total number of orders", totalOrders);
		output.put("total quantity", totalQuantity);
		output.put("average price", avg);
		output.put("total number of combinable orders", combi);

		message.put("message", updateDetails);
		message.put("data", output);

		return message;
	}

	
	
	/**
	 * map to /orderSecurity , Works similarly to the ordersSummary, 
	 * but provides additional validation to select a specific securtity group. 
	 * For eg. /ordersSecurity?security=AAPL. If no security is specified , 
	 * it retuns all the data. 

	 * @param desc = SecurityType
	 * @return = JSONObject of processed data
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ordersSecurity")
	public @ResponseBody JSONObject orderSecurity(
			@RequestParam(value = "security", required = false, defaultValue = "all") String desc) {

		int totalOrders = 0;
		int totalQuantity = 0;
		double totalPrice = 0;
		double avg = 0;
		Map<String, Integer> combination = new HashMap<>();

		String combi = "";
		int combiCounter = 0;
		JSONObject message = new JSONObject();
		JSONObject output = new JSONObject();

		for (Order item : list) {
			if (item.getSecurity().equals(desc) || desc.equals("all")) {
				String data = "(" + item.getSide() + "+" + item.getSecurity() + "+" + item.getFund_name() + ")";
				if (combination.containsKey(data)) {
					combination.put(data, combination.get(data) + 1);

				} else
					combination.put(data, 1);
			}

		}

		for (Map.Entry<String, Integer> entry : combination.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value > 1) {
				combi = combi + key;
				combiCounter++;
			}

		}

		combi = Integer.toString(combiCounter) + combi;

		for (int index = 0; index < list.size(); index++) {
			Order order = list.get(index);
			if (order.getSecurity().equals(desc) || desc.equals("all")) {
				totalPrice = totalPrice + order.getAmount();
				totalQuantity = totalQuantity + order.getQuantity();
				totalOrders++;
			}

		}
		
		avg = totalPrice / totalOrders;
		output.put("total number of orders", totalOrders);
		output.put("total quantity", totalQuantity);
		output.put("average price", avg);
		output.put("total number of combinable orders", combi);

		message.put("message", updateDetails);
		message.put("data", output);

		return message;
	}

	
	
	/**
	 * 
	 * orderFund() : map to /orderFund, Works similarly to the ordersSummary, 
	 * but provides additional validation to select a specific securtity group. 
	 * For eg. /ordersFund?fund= F2. If no fund is specified , it retuns all the data. 
	 * 
	 * @param desc = FundType  
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ordersFund")
	public @ResponseBody JSONObject orderFund(
			@RequestParam(value = "fund", required = false, defaultValue = "all") String desc) {

		int totalOrders = 0;
		int totalQuantity = 0;
		double totalPrice = 0;
		double avg = 0;
		Map<String, Integer> combination = new HashMap<>();

		String combi = "";
		int combiCounter = 0;
		JSONObject message = new JSONObject();
		JSONObject output = new JSONObject();

		for (Order item : list) {
			if (item.getFund_name().equals(desc) || desc.equals("all")) {
				String data = "(" + item.getSide() + "+" + item.getSecurity() + "+" + item.getFund_name() + ")";
				if (combination.containsKey(data)) {
					combination.put(data, combination.get(data) + 1);

				} else
					combination.put(data, 1);
			}

		}

		for (Map.Entry<String, Integer> entry : combination.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value > 1) {
				combi = combi + key;
				combiCounter++;
			}

		}

		combi = Integer.toString(combiCounter) + combi;

		for (int index = 0; index < list.size(); index++) {
			Order order = list.get(index);
			if (order.getFund_name().equals(desc) || desc.equals("all")) {
				totalPrice = totalPrice + order.getAmount();
				totalQuantity = totalQuantity + order.getQuantity();
				totalOrders++;
			}

		}
		
		avg = totalPrice / totalOrders;
		output.put("total number of orders", totalOrders);
		output.put("total quantity", totalQuantity);
		output.put("average price", avg);
		output.put("total number of combinable orders", combi);

		message.put("message", updateDetails);
		message.put("data", output);

		return message;
	}

}
