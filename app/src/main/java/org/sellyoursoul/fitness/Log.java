package org.sellyoursoul.fitness;

import java.util.Date;

/**
 * Created by mdnah on 1/20/2018.
 */

public class Log {

    private String userId;
    private String poolId;
    private String logId;
    private Date datetime;
    private double longitude;
    private double latitude;
    private boolean isChecked;
    private boolean isVerified;

    public Log() {}

    public Log(String userId, String poolId, String logId, Date datetime, double longitude, double latitude, boolean isChecked, boolean isVerified) {
        this.userId = userId;
        this.poolId = poolId;
        this.logId = logId;
        this.datetime = datetime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isChecked = isChecked;
        this.isVerified = isVerified;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
