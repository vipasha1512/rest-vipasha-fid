# rest-vipasha-fid

1. Order.java - POJO class This class is a simple POJO class wih following attributes, constructor and setter , getter methods. Attributes : int order_id; String side; String security; String fund_name; int quantity; double amount;

2. Application.java : SpringBoot starter app. For this class, following annotations were used : @SpringBootApplication @EnableSchedling :- to enable periodic scheduling (per minute) as per requirements

3. data.json : This file contains an array of json objects that is used as raw data.

4. SimpleCORSFilter.java : Configured CORS filter to provide cross platform api accesss.

5. OrderController.java : Created following methods to configure rest api's. public void updateData() public @ResponseBody JSONObject orders() public @ResponseBody JSONObject ordersSummary() public @ResponseBody JSONObject orderSecurity(@RequestParam(value="security", required=false, defaultValue="all") String desc) public @ResponseBody JSONObject orderFund(@RequestParam(value="fund", required=false, defaultValue="all") String desc)

updateData() : Used annotation @Scheduled(fixedRate = 60000) for getting the data from the json file every minute.

orders() : map to /orders , Gives a list of all the data in raw format with a message , providing the status of updates in the data.json file recently in form of a json object.

ordersSummary() : map to /ordersSummary, gives a summary with following details of the raw data as well as a message providing the status of updates in the data.json file recently in form of a json object. • total number of orders • total quantity • average price • total number of combinable orders

orderSecurity() : map to /orderSecurity , Works similarly to the ordersSummary, but provides additional validation to select a specific securtity group. For eg. /ordersSecurity?security=AAPL. If no security is specified , it retuns all the data.

orderFund() : map to /orderFund, Works similarly to the ordersSummary, but provides additional validation to select a specific securtity group. For eg. /ordersFund?fund= F2. If no fund is specified , it retuns all the data.
