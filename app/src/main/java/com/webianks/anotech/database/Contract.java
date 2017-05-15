package com.webianks.anotech.database;

import android.provider.BaseColumns;

/**
 * Created by R Ankit on 23-03-2017.
 */

public class Contract {

    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_EMPLOYEES = "employees";
    public static final String TABLE_OFFICES = "offices";
    public static final String TABLE_ORDER_DETAILS = "orderdetails";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_PAYMENTS = "payments";
    public static final String TABLE_ANOMALY = "anomalies";
    public static final String TABLE_PRODUCT_LINES = "productlines";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String TABLE_TICKETS = "tickets";
    public static final String TABLE_MOVIE_TICKETS = "movie_tickets";
    public static final String TABLE_MOIVE_TRANSACTIONS = "movie_transactions";


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



    public static final class TransactionEntry implements BaseColumns {

        public static final String PNR_NUMBER = "pnr_number";
        public static final String AMOUNT = "amount";
        public static final String TRANSACTION_NUMBER = "transaction_number";
        public static final String MODE = "payment_mode";

    }

    public static final class TicketEntry implements BaseColumns {

        public static final String PNR_NUMBER = "pnr_number";
        public static final String TRAIN_NUMBER = "train_number";
        public static final String TRANSACTION_NUMBER = "transaction_number";
        public static final String FROM = "from";
        public static final String TO = "to";

    }

    public static final class MovieTransactionEntry implements BaseColumns {

        public static final String TICKET_NUMBER = "ticket_number";
        public static final String AMOUNT = "amount";
        public static final String TRANSACTION_NUMBER = "transaction_number";
        public static final String MODE = "payment_mode";

    }

    public static final class MovieTicketEntry implements BaseColumns {

        public static final String TICKET_NUMBER = "ticket_number";
        public static final String SEAT_NUMBER = "seat_number";
        public static final String MOVIE = "movie";
        public static final String AUDI = "audi";

    }

    public class AnomalyEntry implements BaseColumns{

        public static final String TYPE = "type";
        public static final String FILE = "file";
        public static final String REASON = "reason";
        public static final String OUTLIER = "outlier";

    }
}
