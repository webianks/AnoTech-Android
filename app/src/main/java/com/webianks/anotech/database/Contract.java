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
    public static final String TABLE_PRODUCT_LINES = "productlines";
    public static final String TABLE_PRODUCTS = "products";


    public static final class OrderDetailsEntry implements BaseColumns {


        public static final String ORDER_NUMBER = "orderNumber";
        public static final String PRODUCT_CODE = "productCode";
        public static final String QUANTITY_ORDERED = "quantityOrdered";
        public static final String PRICE_EACH  = "priceEach";
        public static final String ORDER_LINE_NUMBER = "orderLineNumber";

    }

    public static final class ProductsEntry implements BaseColumns {


        public static final String QUANTITY_IN_STOCK = "quantityInStock";

    }


}
