package com.example.pentaho.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataStandardAndRespositoryDTO {

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

    @JsonProperty("ALLEY")
    private String alley;

    @JsonProperty("ALLEY_ID_SN")
    private String alleyIdSn;

    @JsonProperty("AREA")
    private String area;

    @JsonProperty("BASEMENT_STR")
    private String basementStr;

    @JsonProperty("CREATE_DT")
    private String createDt;

    @JsonProperty("DATA_SOURCE")
    private String dataSource;

    @JsonProperty("LANE")
    private String lane;

    @JsonProperty("LANE_ID_SN")
    private String laneIdSn;

    @JsonProperty("NEIGHBOR_CD")
    private String neighborCd;

    @JsonProperty("NUM_FLR1")
    private String numFlr1;

    @JsonProperty("NUM_FLR2")
    private String numFlr2;

    @JsonProperty("NUM_FLR3")
    private String numFlr3;

    @JsonProperty("NUM_FLR4")
    private String numFlr4;

    @JsonProperty("NUM_FLR5")
    private String numFlr5;

    @JsonProperty("NUM_FLR_ID")
    private String numFlrId;

    @JsonProperty("NUM_FLR_ID1")
    private String numFlrId1;

    @JsonProperty("NUM_FLR_ID2")
    private String numFlrId2;

    @JsonProperty("NUM_FLR_ID3")
    private String numFlrId3;

    @JsonProperty("NUM_FLR_ID4")
    private String numFlrId4;

    @JsonProperty("NUM_FLR_ID5")
    private String numFlrId5;

    @JsonProperty("NUM_FLR_POS")
    private String numFlrPos;

    @JsonProperty("NUM_TYPE")
    private String numType;

    @JsonProperty("NUM_TYPE_CD")
    private String numTypeCd;

    @JsonProperty("RELIABILITY")
    private BigDecimal reliability;

    @JsonProperty("REMARK")
    private String remark;

    @JsonProperty("ROAD")
    private String road;

    @JsonProperty("ROAD_AREA_SN")
    private String roadAreaSn;

    @JsonProperty("ROOM")
    private String room;

    @JsonProperty("ROOM_ID_SN")
    private String roomIdSn;

    @JsonProperty("SUB_ALLEY")
    private String subAlley;

    @JsonProperty("UPDATE_TYPE")
    private String updateType;

    @JsonProperty("VILLAGE")
    private String village;

    @JsonProperty("VILLAGE_CD")
    private String villageCd;


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

    public String getAlley() {
        return alley;
    }

    public void setAlley(String alley) {
        this.alley = alley;
    }

    public String getAlleyIdSn() {
        return alleyIdSn;
    }

    public void setAlleyIdSn(String alleyIdSn) {
        this.alleyIdSn = alleyIdSn;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBasementStr() {
        return basementStr;
    }

    public void setBasementStr(String basementStr) {
        this.basementStr = basementStr;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getLaneIdSn() {
        return laneIdSn;
    }

    public void setLaneIdSn(String laneIdSn) {
        this.laneIdSn = laneIdSn;
    }

    public String getNeighborCd() {
        return neighborCd;
    }

    public void setNeighborCd(String neighborCd) {
        this.neighborCd = neighborCd;
    }

    public String getNumFlr1() {
        return numFlr1;
    }

    public void setNumFlr1(String numFlr1) {
        this.numFlr1 = numFlr1;
    }

    public String getNumFlr2() {
        return numFlr2;
    }

    public void setNumFlr2(String numFlr2) {
        this.numFlr2 = numFlr2;
    }

    public String getNumFlr3() {
        return numFlr3;
    }

    public void setNumFlr3(String numFlr3) {
        this.numFlr3 = numFlr3;
    }

    public String getNumFlr4() {
        return numFlr4;
    }

    public void setNumFlr4(String numFlr4) {
        this.numFlr4 = numFlr4;
    }

    public String getNumFlr5() {
        return numFlr5;
    }

    public void setNumFlr5(String numFlr5) {
        this.numFlr5 = numFlr5;
    }

    public String getNumFlrId() {
        return numFlrId;
    }

    public void setNumFlrId(String numFlrId) {
        this.numFlrId = numFlrId;
    }

    public String getNumFlrId1() {
        return numFlrId1;
    }

    public void setNumFlrId1(String numFlrId1) {
        this.numFlrId1 = numFlrId1;
    }

    public String getNumFlrId2() {
        return numFlrId2;
    }

    public void setNumFlrId2(String numFlrId2) {
        this.numFlrId2 = numFlrId2;
    }

    public String getNumFlrId3() {
        return numFlrId3;
    }

    public void setNumFlrId3(String numFlrId3) {
        this.numFlrId3 = numFlrId3;
    }

    public String getNumFlrId4() {
        return numFlrId4;
    }

    public void setNumFlrId4(String numFlrId4) {
        this.numFlrId4 = numFlrId4;
    }

    public String getNumFlrId5() {
        return numFlrId5;
    }

    public void setNumFlrId5(String numFlrId5) {
        this.numFlrId5 = numFlrId5;
    }

    public String getNumFlrPos() {
        return numFlrPos;
    }

    public void setNumFlrPos(String numFlrPos) {
        this.numFlrPos = numFlrPos;
    }

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
    }

    public String getNumTypeCd() {
        return numTypeCd;
    }

    public void setNumTypeCd(String numTypeCd) {
        this.numTypeCd = numTypeCd;
    }

    public BigDecimal getReliability() {
        return reliability;
    }

    public void setReliability(BigDecimal reliability) {
        this.reliability = reliability;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getRoadAreaSn() {
        return roadAreaSn;
    }

    public void setRoadAreaSn(String roadAreaSn) {
        this.roadAreaSn = roadAreaSn;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoomIdSn() {
        return roomIdSn;
    }

    public void setRoomIdSn(String roomIdSn) {
        this.roomIdSn = roomIdSn;
    }

    public String getSubAlley() {
        return subAlley;
    }

    public void setSubAlley(String subAlley) {
        this.subAlley = subAlley;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillageCd() {
        return villageCd;
    }

    public void setVillageCd(String villageCd) {
        this.villageCd = villageCd;
    }

    @Override
    public String toString() {
        return "DataStandardAndRespositoryDTO{" +
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
                ", alley='" + alley + '\'' +
                ", alleyIdSn='" + alleyIdSn + '\'' +
                ", area='" + area + '\'' +
                ", basementStr='" + basementStr + '\'' +
                ", createDt='" + createDt + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", lane='" + lane + '\'' +
                ", laneIdSn='" + laneIdSn + '\'' +
                ", neighborCd='" + neighborCd + '\'' +
                ", numFlr1='" + numFlr1 + '\'' +
                ", numFlr2='" + numFlr2 + '\'' +
                ", numFlr3='" + numFlr3 + '\'' +
                ", numFlr4='" + numFlr4 + '\'' +
                ", numFlr5='" + numFlr5 + '\'' +
                ", numFlrId='" + numFlrId + '\'' +
                ", numFlrId1='" + numFlrId1 + '\'' +
                ", numFlrId2='" + numFlrId2 + '\'' +
                ", numFlrId3='" + numFlrId3 + '\'' +
                ", numFlrId4='" + numFlrId4 + '\'' +
                ", numFlrId5='" + numFlrId5 + '\'' +
                ", numFlrPos='" + numFlrPos + '\'' +
                ", numType='" + numType + '\'' +
                ", numTypeCd='" + numTypeCd + '\'' +
                ", reliability=" + reliability +
                ", remark='" + remark + '\'' +
                ", road='" + road + '\'' +
                ", roadAreaSn='" + roadAreaSn + '\'' +
                ", room='" + room + '\'' +
                ", roomIdSn='" + roomIdSn + '\'' +
                ", subAlley='" + subAlley + '\'' +
                ", updateType='" + updateType + '\'' +
                ", village='" + village + '\'' +
                ", villageCd='" + villageCd + '\'' +
                '}';
    }
}
