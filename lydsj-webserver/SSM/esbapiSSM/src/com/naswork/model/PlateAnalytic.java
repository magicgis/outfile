package com.naswork.model;

import java.util.Date;

public class PlateAnalytic {
    private String area;

    private Date recordTime;

    private String province;

    private String city;

    private String plate;

    private Byte sourcescope;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Byte getSourcescope() {
        return sourcescope;
    }

    public void setSourcescope(Byte sourcescope) {
        this.sourcescope = sourcescope;
    }
}