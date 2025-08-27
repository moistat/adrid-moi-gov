package com.example.pentaho.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Date;

public class IbdTbAddrCodeOfDataStandardDTO {

    @JsonProperty("SEQ")
    private Integer seq;

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

    @JsonProperty("POST_CODE")
    private String postCode;

    @JsonProperty("POST_CODE_DT")
    private String postCodeDt;

    @JsonProperty("TC_ROAD")
    private String tcRoad;

    @JsonProperty("ROAD_ID")
    private String roadId;

    @JsonProperty("ROAD_ID_DT")
    private String roadIdDt;

    @JsonProperty("X")
    private BigDecimal x;

    @JsonProperty("Y")
    private BigDecimal y;

    @JsonProperty("WGS_X")
    private BigDecimal wgsX;

    @JsonProperty("WGS_Y")
    private BigDecimal wgsY;

    @JsonProperty("GEOHASH")
    private String geohash;

    @JsonProperty("XY_YEAR")
    private String xyYear;

    @JsonProperty("ADR_VERSION")
    private String adrVersion;

    @JsonProperty("ETLDT")
    private Date etldt;

    @JsonProperty("JOIN_STEP")
    private String joinStep;

    @JsonProperty("NEIGHBOR_CD")
    private String neighborCd;

    @JsonProperty("NEIGHBOR")
    private String neighbor;

    @JsonProperty("VILLAGE_CD")
    private String villageCd;

    @JsonProperty("VILLAGE")
    private String village;

    @JsonProperty("NUM_FLR_ID")
    private String numFlrId;

    @JsonProperty("NUM_FLR_POS")
    private String numFlrPos;

    @JsonProperty("BASEMENT_STR")
    private String basementStr;

    @JsonProperty("ROOM_ID_SN")
    private String roomIdSn;

    @JsonProperty("ROOM")
    private String room;

    @JsonProperty("ROAD_AREA_SN")
    private String roadAreaSn;

    @JsonProperty("ROAD")
    private String road;

    @JsonProperty("AREA")
    private String area;

    public String getBasementStr() {
        return basementStr;
    }

    public void setBasementStr(String basementStr) {
        this.basementStr = basementStr;
    }

    public String getNeighborCd() {
        return neighborCd;
    }

    public void setNeighborCd(String neighborCd) {
        this.neighborCd = neighborCd;
    }

    public String getVillageCd() {
        return villageCd;
    }

    public void setVillageCd(String villageCd) {
        this.villageCd = villageCd;
    }

    public String getRoadAreaSn() {
        return roadAreaSn;
    }

    public void setRoadAreaSn(String roadAreaSn) {
        this.roadAreaSn = roadAreaSn;
    }

    public String getNumFlrId() {
        return numFlrId;
    }

    public void setNumFlrId(String numFlrId) {
        this.numFlrId = numFlrId;
    }

    public String getNumFlrPos() {
        return numFlrPos;
    }

    public void setNumFlrPos(String numFlrPos) {
        this.numFlrPos = numFlrPos;
    }

    public String getRoomIdSn() {
        return roomIdSn;
    }

    public void setRoomIdSn(String roomIdSn) {
        this.roomIdSn = roomIdSn;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

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

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostCodeDt() {
        return postCodeDt;
    }

    public void setPostCodeDt(String postCodeDt) {
        this.postCodeDt = postCodeDt;
    }

    public String getTcRoad() {
        return tcRoad;
    }

    public void setTcRoad(String tcRoad) {
        this.tcRoad = tcRoad;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRoadIdDt() {
        return roadIdDt;
    }

    public void setRoadIdDt(String roadIdDt) {
        this.roadIdDt = roadIdDt;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
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

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getXyYear() {
        return xyYear;
    }

    public void setXyYear(String xyYear) {
        this.xyYear = xyYear;
    }

    public String getAdrVersion() {
        return adrVersion;
    }

    public void setAdrVersion(String adrVersion) {
        this.adrVersion = adrVersion;
    }

    public Date getEtldt() {
        return etldt;
    }

    public void setEtldt(Date etldt) {
        this.etldt = etldt;
    }

    public String getJoinStep() {
        return joinStep;
    }

    public void setJoinStep(String joinStep) {
        this.joinStep = joinStep;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(String neighbor) {
        this.neighbor = neighbor;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "IbdTbAddrCodeOfDataStandardDTO{" +
                "seq=" + seq +
                ", addressId='" + addressId + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", validity='" + validity + '\'' +
                ", county='" + county + '\'' +
                ", countyCd='" + countyCd + '\'' +
                ", town='" + town + '\'' +
                ", townCd='" + townCd + '\'' +
                ", postCode='" + postCode + '\'' +
                ", postCodeDt='" + postCodeDt + '\'' +
                ", tcRoad='" + tcRoad + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadIdDt='" + roadIdDt + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", wgsX=" + wgsX +
                ", wgsY=" + wgsY +
                ", geohash='" + geohash + '\'' +
                ", xyYear='" + xyYear + '\'' +
                ", adrVersion='" + adrVersion + '\'' +
                ", etldt=" + etldt +
                ", joinStep='" + joinStep + '\'' +
                ", neighborCd='" + neighborCd + '\'' +
                ", neighbor='" + neighbor + '\'' +
                ", villageCd='" + villageCd + '\'' +
                ", village='" + village + '\'' +
                ", numFlrId='" + numFlrId + '\'' +
                ", numFlrPos='" + numFlrPos + '\'' +
                ", basementStr='" + basementStr + '\'' +
                ", roomIdSn='" + roomIdSn + '\'' +
                ", room='" + room + '\'' +
                ", roadAreaSn='" + roadAreaSn + '\'' +
                ", road='" + road + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}