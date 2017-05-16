package com.webianks.anotech.database;

import android.provider.BaseColumns;

/**
 * Created by R Ankit on 23-03-2017.
 */

public class Contract {

    public static final String TABLE_ORDER_DETAILS = "orderdetails";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String TABLE_ANOMALY = "anomalies";
    public static final String TABLE_SYSTEM_LOG = "system_log";


    public static final class OrderDetailsEntry implements BaseColumns {

        public static final String ORDER_NUMBER = "orderNumber";
        public static final String PRODUCT_CODE = "productCode";
        public static final String QUANTITY_ORDERED = "quantityOrdered";
        public static final String PRICE_EACH  = "priceEach";
        public static final String ORDER_LINE_NUMBER = "orderLineNumber";

    }

    public static final class ProductsEntry implements BaseColumns {

        public static final String QUANTITY_IN_STOCK = "quantityInStock";
        public static final String PRODUCT_CODE = "productCode";

    }

    public static final class OrdersEntry implements BaseColumns {

        public static final String ORDER_DATE = "orderDate";
        public static final String SHIPPED_DATE = "shippedDate";
        public static final String ORDER_NUMBER = "orderNumber";
        public static final String REQUIRED_DATE = "requiredDate";
        public static final String STATUS = "status";
        public static final String COMMENTS = "comments";
        public static final String CUSTOMER_NUMBER = "customerNumber";

    }

    public static final class PaymentsEntry implements BaseColumns {

        public static final String CARD_NUMBER = "cardNumber";
        public static final String AMOUNT = "amount";
        public static final String PAYMENT_DATE = "paymentDate";
        public static final String CUSTOMER_NUMBER = "customerNumber";

    }



    public class AnomalyEntry implements BaseColumns{

        public static final String TYPE = "type";
        public static final String FILE = "file";
        public static final String REASON = "reason";
        public static final String OUTLIER = "outlier";

    }


    public class SystemEntry implements BaseColumns{

        public static final String TIME = "time";
        public static final String PROCESSOR = "processor";
        public static final String NET_TRAFFIC = "net_traffic";
        public static final String MEMORY_USED = "memory_used";
        public static final String IDLE_TIME = "idle_time";
        public static final String PERFORMANCE = "performance";

    }
}
