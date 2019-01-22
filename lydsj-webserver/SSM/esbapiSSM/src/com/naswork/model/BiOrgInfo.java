package com.naswork.model;

import java.math.BigDecimal;

public class BiOrgInfo {
    private Integer id;

    private Integer areaid;

    private String name;

    private Integer level;

    private BigDecimal realposx;

    private BigDecimal realposy;

    private Integer zoom;

    private BigDecimal disposx;

    private BigDecimal disposy;

    private String indexcode;

    private Integer capacity;

    private Integer parentid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaid() {
        return areaid;
    }

    public void setAreaid(Integer areaid) {
        this.areaid = areaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getRealposx() {
        return realposx;
    }

    public void setRealposx(BigDecimal realposx) {
        this.realposx = realposx;
    }

    public BigDecimal getRealposy() {
        return realposy;
    }

    public void setRealposy(BigDecimal realposy) {
        this.realposy = realposy;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public BigDecimal getDisposx() {
        return disposx;
    }

    public void setDisposx(BigDecimal disposx) {
        this.disposx = disposx;
    }

    public BigDecimal getDisposy() {
        return disposy;
    }

    public void setDisposy(BigDecimal disposy) {
        this.disposy = disposy;
    }

    public String getIndexcode() {
        return indexcode;
    }

    public void setIndexcode(String indexcode) {
        this.indexcode = indexcode;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }
}