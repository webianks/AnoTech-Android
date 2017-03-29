package com.webianks.anotech.data;

import java.util.Vector;

/**
 * Created by R Ankit on 29-03-2017.
 */

public class TransactionInMonth {

    String cardNumber;
    Vector<Integer> count;

    public void setCount(int count) {
        this.count.add(count);
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Vector<Integer> getCount() {
        return count;
    }
}
