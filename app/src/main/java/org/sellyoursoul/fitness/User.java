package org.sellyoursoul.fitness;

import java.util.ArrayList;

/**
 * Created by mdnah on 1/20/2018.
 */

public class User {

    private String id;
    private String name;
    private float balance;
    private String currentPool;
    private String capitalOneAccount;
    private String logId;


    public User() {}


    public User(String id, String name, float balance, String currentPool, String capitalOneAccount, String logId) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currentPool = currentPool;
        this.capitalOneAccount = capitalOneAccount;
        this.logId = logId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getCurrentPool() {
        return currentPool;
    }

    public void setCurrentPool(String currentPool) {
        this.currentPool = currentPool;
    }

    public String getCapitalOneAccount() {
        return capitalOneAccount;
    }

    public void setCapitalOneAccount(String capitalOneAccount) {
        this.capitalOneAccount = capitalOneAccount;
    }

    public String getLogs() {
        return logId;
    }

    public void setLogs(String logs) {
        this.logId = logs;
    }


}
