package com.webianks.anotech.data;

/**
 * Created by R Ankit on 29-03-2017.
 */

public class TransactionData {

    String cardNumber;
    int[] count = new int[365];


    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    public int[] getCount() {
        return count;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setDayNumber(int day_of_year) {
        count[day_of_year] = 1;
    }
}
