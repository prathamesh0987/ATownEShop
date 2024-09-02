package com.svr.atown.db;

public class Tables {

    public static class user{
        public static final String tableName = "user";
        public static final String name = "name";
        public static final String mail = "mail";
        public static final String mobile = "mobile";
        public static final String house_no = "house_no";
        public static final String society = "society";
        public static final String area = "area";
        public static final String landmark = "landmark";
        public static final String pinCode = "pinCode";
        public static final String password = "password";
        public static final String status = "status";
    }

    public static class cart{
        public static final String tableName="cart";
        public static final String name="name";
        public static final String image="image";
        public static final String finalCost="finalCost";
        public static final String quantity="quantity";
    }

    public static class orderHistory{
        public static final String tableName="order_history";
        public static final String order_no="order_no";
        public static final String finalCost="finalCost";
        public static final String order_time="order_time";
        public static final String status="status";
    }
}
