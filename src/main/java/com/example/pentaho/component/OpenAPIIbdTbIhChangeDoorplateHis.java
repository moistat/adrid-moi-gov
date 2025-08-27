package com.example.pentaho.component;


import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenAPIIbdTbIhChangeDoorplateHis {


    @JsonProperty("ADDRESS_ID")
    private String addressId;

    @JsonProperty("HIS_ADR")
    private String hisAdr;

    @JsonProperty("FULL_ADDRESS")
    private String fullAddress;

    @JsonProperty("WGS_X")
    private String wgsX;

    @JsonProperty("WGS_Y")
    private String wgsY;

    @JsonProperty("UPDATE_TYPE")
    private String updateType;

    @JsonProperty("UPDATE_DT")
    private String updateDt;

    @JsonProperty("ADR_VERSION")
    private String adrVersion;


    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getHisAdr() {
        return hisAdr;
    }

    public void setHisAdr(String hisAdr) {
        this.hisAdr = hisAdr;
    }

    public String getWgsX() {
        return wgsX;
    }

    public void setWgsX(String wgsX) {
        this.wgsX = wgsX;
    }

    public String getWgsY() {
        return wgsY;
    }

    public void setWgsY(String wgsY) {
        this.wgsY = wgsY;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public String getAdrVersion() {
        return adrVersion;
    }

    public void setAdrVersion(String adrVersion) {
        this.adrVersion = adrVersion;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public String toString() {
        return "OpenAPIIbdTbIhChangeDoorplateHis{" +
                "addressId='" + addressId + '\'' +
                ", hisAdr='" + hisAdr + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", wgsX='" + wgsX + '\'' +
                ", wgsY='" + wgsY + '\'' +
                ", updateType='" + updateType + '\'' +
                ", updateDt='" + updateDt + '\'' +
                ", adrVersion='" + adrVersion + '\'' +
                '}';
    }
}
