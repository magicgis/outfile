package com.naswork.model;

public class TPart {
	private Integer isBlacklist;
	
    private String bsn;

    private String msn;

    private String msnFlag;

    private String partNum;

    private String partName;

    private String shortPartNum;

    private String cageCode;

    private String nsn;

    private String replacedNsn;

    private String weight;

    private String dimentions;

    private String countryOfOrigin;

    private String eccn;

    private String scheduleBCode;

    private Integer shelfLife;

    private Integer ataChapterSection;

    private Integer categoryNo;

    private String usml;

    private String hazmatCode;

    private String imgPath;
    
    private int line;
    
    private String description;
    
    private Integer userId;
    
    private Integer partType;
    
    private String value;
    
    private String manName;
    
    private String code;
    
    private Integer correlationMark;
    
    private Integer sn;
    
    private String correlationBsn;
    
    private String hsCode;
    
    private String remark;
    
    private String weightUnit;
    
    private String dimentionsUnit;
    
    private Integer dimentionsUnitId;
    
    private Integer weightUnitId;
    
    public TPart(String partNum, String partName,int line,
			String cageCode, String nsn, String replacedNsn, String weight,
			String dimentions, String countryOfOrigin, String eccn,
			String scheduleBCode, Integer shelfLife, Integer ataChapterSection,
			Integer categoryNo, String usml, String hazmatCode, String imgPath,String msn) {
		super();
		this.partNum = partNum;
		this.line = line;
		this.partName = partName;
		this.cageCode = cageCode;
		this.nsn = nsn;
		this.replacedNsn = replacedNsn;
		this.weight = weight;
		this.dimentions = dimentions;
		this.countryOfOrigin = countryOfOrigin;
		this.eccn = eccn;
		this.scheduleBCode = scheduleBCode;
		this.shelfLife = shelfLife;
		this.ataChapterSection = ataChapterSection;
		this.categoryNo = categoryNo;
		this.usml = usml;
		this.hazmatCode = hazmatCode;
		this.imgPath = imgPath;
		this.msn = msn;
	}
    
    public Integer getPartType() {
		return partType;
	}

	public void setPartType(Integer partType) {
		this.partType = partType;
	}

	public TPart(){
    	
    };

	public Integer getIsBlacklist() {
		return isBlacklist;
	}

	public void setIsBlacklist(Integer isBlacklist) {
		this.isBlacklist = isBlacklist;
	}

	public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getMsnFlag() {
        return msnFlag;
    }

    public void setMsnFlag(String msnFlag) {
        this.msnFlag = msnFlag;
    }

    public String getPartNum() {
        return partNum;
    }

    public void setPartNum(String partNum) {
        this.partNum = partNum;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getShortPartNum() {
        return shortPartNum;
    }

    public void setShortPartNum(String shortPartNum) {
        this.shortPartNum = shortPartNum;
    }

    public String getCageCode() {
        return cageCode;
    }

    public void setCageCode(String cageCode) {
        this.cageCode = cageCode;
    }

    public String getNsn() {
        return nsn;
    }

    public void setNsn(String nsn) {
        this.nsn = nsn;
    }

    public String getReplacedNsn() {
        return replacedNsn;
    }

    public void setReplacedNsn(String replacedNsn) {
        this.replacedNsn = replacedNsn;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimentions() {
        return dimentions;
    }

    public void setDimentions(String dimentions) {
        this.dimentions = dimentions;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getEccn() {
        return eccn;
    }

    public void setEccn(String eccn) {
        this.eccn = eccn;
    }

    public String getScheduleBCode() {
        return scheduleBCode;
    }

    public void setScheduleBCode(String scheduleBCode) {
        this.scheduleBCode = scheduleBCode;
    }

    public Integer getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(Integer categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getUsml() {
        return usml;
    }

    public void setUsml(String usml) {
        this.usml = usml;
    }

    public String getHazmatCode() {
        return hazmatCode;
    }

    public void setHazmatCode(String hazmatCode) {
        this.hazmatCode = hazmatCode;
    }


    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(Integer shelfLife) {
		this.shelfLife = shelfLife;
	}

	public Integer getAtaChapterSection() {
		return ataChapterSection;
	}

	public void setAtaChapterSection(Integer ataChapterSection) {
		this.ataChapterSection = ataChapterSection;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getManName() {
		return manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Integer getCorrelationMark() {
		return correlationMark;
	}

	public void setCorrelationMark(Integer correlationMark) {
		this.correlationMark = correlationMark;
	}

	public String getCorrelationBsn() {
		return correlationBsn;
	}

	public void setCorrelationBsn(String correlationBsn) {
		this.correlationBsn = correlationBsn;
	}

	public String getHsCode() {
		return hsCode;
	}

	public void setHsCode(String hsCode) {
		this.hsCode = hsCode;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the weightUnit
	 */
	public String getWeightUnit() {
		return weightUnit;
	}

	/**
	 * @param weightUnit the weightUnit to set
	 */
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	/**
	 * @return the dimentionsUnit
	 */
	public String getDimentionsUnit() {
		return dimentionsUnit;
	}

	/**
	 * @param dimentionsUnit the dimentionsUnit to set
	 */
	public void setDimentionsUnit(String dimentionsUnit) {
		this.dimentionsUnit = dimentionsUnit;
	}

	/**
	 * @return the dimentionsUnitId
	 */
	public Integer getDimentionsUnitId() {
		return dimentionsUnitId;
	}

	/**
	 * @param dimentionsUnitId the dimentionsUnitId to set
	 */
	public void setDimentionsUnitId(Integer dimentionsUnitId) {
		this.dimentionsUnitId = dimentionsUnitId;
	}

	/**
	 * @return the weightUnitId
	 */
	public Integer getWeightUnitId() {
		return weightUnitId;
	}

	/**
	 * @param weightUnitId the weightUnitId to set
	 */
	public void setWeightUnitId(Integer weightUnitId) {
		this.weightUnitId = weightUnitId;
	}
}