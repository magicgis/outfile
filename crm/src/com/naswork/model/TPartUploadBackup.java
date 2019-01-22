package com.naswork.model;

public class TPartUploadBackup {
	
	private Integer id;
	
    private Integer userId;

    private String partNum;

    private String partName;

    private String cageCode;

    private String nsn;

    private String replacedNsn;

    private String weight;

    private String dimentions;

    private String countryOfOrigin;

    private String eccn;

    private String scheduleBCode;

    private Short shelfLife;

    private Short ataChapterSection;

    private Integer categoryNo;

    private String usml;

    private String hazmatCode;

    private String imgPath;
    
    private String description;
    
    private Integer line;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Short getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Short shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Short getAtaChapterSection() {
        return ataChapterSection;
    }

    public void setAtaChapterSection(Short ataChapterSection) {
        this.ataChapterSection = ataChapterSection;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}
}