package com.example.pentaho.component;


import com.fasterxml.jackson.annotation.JsonProperty;

public class IbdTbIhChangeDoorplateHis {

    @JsonProperty("ADDRESS_ID")
    private String addressId;

    @JsonProperty("HIS_ADR")
    private String hisAdr;

    @JsonProperty("WGS_X")
    private String wgsX;

    @JsonProperty("WGS_Y")
    private String wgsY;

    @JsonProperty("UPDATE_TYPE")
    private String updateType;

    @JsonProperty("UPDATE_DT")
    private String updateDt;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("HISTORY_SEQ")
    private Integer historySeq;

    @JsonProperty("ADR_VERSION")
    private String adrVersion;

    @JsonProperty("UPDATE_CODE")
    private String updateCode;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getHistorySeq() {
        return historySeq;
    }

    public void setHistorySeq(Integer historySeq) {
        this.historySeq = historySeq;
    }

    public String getAdrVersion() {
        return adrVersion;
    }

    public void setAdrVersion(String adrVersion) {
        this.adrVersion = adrVersion;
    }

    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode;
    }

    @Override
    public String toString() {
        return "IbdTbIhChangeDoorplateHis{" +
                "addressId='" + addressId + '\'' +
                ", hisAdr='" + hisAdr + '\'' +
                ", wgsX='" + wgsX + '\'' +
                ", wgsY='" + wgsY + '\'' +
                ", updateType='" + updateType + '\'' +
                ", updateDt='" + updateDt + '\'' +
                ", status='" + status + '\'' +
                ", historySeq=" + historySeq +
                ", adrVersion='" + adrVersion + '\'' +
                ", updateCode='" + updateCode + '\'' +
                '}';
    }
}
