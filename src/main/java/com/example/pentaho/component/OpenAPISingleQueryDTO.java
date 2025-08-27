package com.example.pentaho.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OpenAPISingleQueryDTO {

    @JsonProperty("ADDRESS_ID")
    private String addressId;

    @JsonProperty("FULL_ADDRESS")
    private String fullAddress;

    @JsonProperty("VALIDITY")
    private String validity;

    @JsonProperty("COUNTY")
    private String county;

    @JsonProperty("COUNTY_CD")
    private String countyCd;

    @JsonProperty("TOWN")
    private String town;

    @JsonProperty("TOWN_CD")
    private String townCd;

    @JsonProperty("POSTCODE")
    private String postcode;

    @JsonProperty("POST_CODE_DT")
    private String postCodeDt;

    @JsonProperty("ROAD_ID_DT")
    private String roadIdDt;

    @JsonProperty("X")
    private BigDecimal X;

    @JsonProperty("Y")
    private BigDecimal Y;

    @JsonProperty("WGS_X")
    private BigDecimal wgsX;

    @JsonProperty("WGS_Y")
    private BigDecimal wgsY;

    @JsonProperty("GEOHASH")
    private String geohash;

    @JsonProperty("ADR_VERSION")
    private String adrVersion;

    @JsonProperty("JOIN_STEP")
    private String joinStep;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountyCd() {
        return countyCd;
    }

    public void setCountyCd(String countyCd) {
        this.countyCd = countyCd;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTownCd() {
        return townCd;
    }

    public void setTownCd(String townCd) {
        this.townCd = townCd;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPostCodeDt() {
        return postCodeDt;
    }

    public void setPostCodeDt(String postCodeDt) {
        this.postCodeDt = postCodeDt;
    }

    public String getRoadIdDt() {
        return roadIdDt;
    }

    public void setRoadIdDt(String roadIdDt) {
        this.roadIdDt = roadIdDt;
    }



    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getAdrVersion() {
        return adrVersion;
    }

    public void setAdrVersion(String adrVersion) {
        this.adrVersion = adrVersion;
    }

    public String getJoinStep() {
        return joinStep;
    }

    public void setJoinStep(String joinStep) {
        this.joinStep = joinStep;
    }

    public BigDecimal getX() {
        return X;
    }

    public void setX(BigDecimal x) {
        X = x;
    }

    public BigDecimal getY() {
        return Y;
    }

    public void setY(BigDecimal y) {
        Y = y;
    }

    public BigDecimal getWgsX() {
        return wgsX;
    }

    public void setWgsX(BigDecimal wgsX) {
        this.wgsX = wgsX;
    }

    public BigDecimal getWgsY() {
        return wgsY;
    }

    public void setWgsY(BigDecimal wgsY) {
        this.wgsY = wgsY;
    }

    @Override
    public String toString() {
        return "OpenAPISingleQueryDTO{" +
                "addressId='" + addressId + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", validity='" + validity + '\'' +
                ", county='" + county + '\'' +
                ", countyCd='" + countyCd + '\'' +
                ", town='" + town + '\'' +
                ", townCd='" + townCd + '\'' +
                ", postcode='" + postcode + '\'' +
                ", postCodeDt='" + postCodeDt + '\'' +
                ", roadIdDt='" + roadIdDt + '\'' +
                ", X='" + X + '\'' +
                ", Y='" + Y + '\'' +
                ", wgsX='" + wgsX + '\'' +
                ", wgsY='" + wgsY + '\'' +
                ", geohash='" + geohash + '\'' +
                ", adrVersion='" + adrVersion + '\'' +
                ", joinStep='" + joinStep + '\'' +
                '}';
    }
}
