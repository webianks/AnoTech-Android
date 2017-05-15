package com.webianks.anotech.adapters;

/**
 * Created by R Ankit on 15-05-2017.
 */

public class Anomaly {

    private String type;
    private String file;
    private String reason;
    private String outlier;

    public void setType(String type) {
        this.type = type;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setOutlier(String outlier) {
        this.outlier = outlier;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public String getFile() {
        return file;
    }

    public String getOutlier() {
        return outlier;
    }

    public String getReason() {
        return reason;
    }

}
