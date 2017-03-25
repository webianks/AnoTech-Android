package com.webianks.anotech.database;

/**
 * Created by R Ankit on 23-03-2017.
 */

public class Projections {

    public static final String[] ORDER_DETAILS_COLUMNS = {

            Contract.OrderDetailsEntry.ORDER_NUMBER,
            Contract.OrderDetailsEntry.PRODUCT_CODE,
            Contract.OrderDetailsEntry.QUANTITY_ORDERED,
            Contract.OrderDetailsEntry.PRICE_EACH,
            Contract.OrderDetailsEntry.ORDER_LINE_NUMBER
    };

    public static final String[] ORDERS_COLUMNS = {

            Contract.OrdersEntry.ORDER_NUMBER,
            Contract.OrdersEntry.SHIPPED_DATE,
            Contract.OrdersEntry.ORDER_DATE
    };

}
