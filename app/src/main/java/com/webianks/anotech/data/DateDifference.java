package com.webianks.anotech.data;

/**
 * Created by R Ankit on 30-03-2017.
 */

public class DateDifference {

    private long days;
    private String orderNumber;

    public void setDays(long days) {
        this.days = days;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getDays() {
        return days;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
