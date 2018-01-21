package org.sellyoursoul.fitness;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mdnah on 1/20/2018.
 */

public class Pool {

    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private boolean isPrivate;
    private float entryFee;
    private ArrayList<String> days;
    private Date timeEnd;
    private float totalBalance;

    public Pool() {}

    public Pool(String id, String name, double longitude, double latitude, boolean isPrivate, float entryFee, ArrayList<String> days, Date timeEnd, float totalBalance) {

        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isPrivate = isPrivate;
        this.entryFee = entryFee;
        this.days = days;
        this.timeEnd = timeEnd;
        this.totalBalance = totalBalance;}

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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public float getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(float entryFee) {
        this.entryFee = entryFee;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(float totalBalance) {
        this.totalBalance = totalBalance;
    }
}
