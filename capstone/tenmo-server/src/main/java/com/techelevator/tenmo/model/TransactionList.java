package com.techelevator.tenmo.model;

import java.util.ArrayList;
import java.util.List;

public class TransactionList {

    private List<Transaction> transactions;

    public TransactionList(List<Transaction> transactions) {
        this.transactions = new ArrayList<>();
    }

    public TransactionList() {
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
