package com.example.pentaho.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.*;

public class Address {

    private String zipcode;

    private String county;

    private String countyCd;

    private String town;

    private String townCd;

    private String village;

    private String villageCd;

    private String neighbor;

    private String neighborCd;

    private String road;

    private String specialArea;

    private String area;

    private String roadAreaSn;

    private String lane;

    private String laneCd;

    private String alley;

    private String subAlley;

    private String alleyIdSn;

    private String numFlr1;

    private String numFlr1Id;

    private String numFlr2;

    private String numFlr2Id;

    private String numFlr3;

    private String numFlr3Id;

    private String numFlr4;

    private String numFlr4Id;

    private String numFlr5;

    private String numFlr5Id;

    private String numFlrId;

    private String numFlrPos;

    private String JB2NumFlrId;

    private String JB2NumFlrPos;

    private String JB3NumFlrId;

    private String JB3NumFlrPos;

    private String JB4NumFlrId;

    private String JB4NumFlrPos;

    private String JB5NumFlrId;

    private String JB5NumFlrPos;

    private String JC4NumFlrId;

    private String JC4NumFlrPos;

    private String continuousNum;

    private String basementStr;


    private String room;

    private String roomIdSn;

    private String seq;

    private String addrRemains;

    private boolean isParseSuccessed;

    private String originalAddress;

    private String cleanAddress;

    private List<String> mappingId;

    private String segmentExistNumber;

    private Map<String, Boolean> segNumMap;

    private List<LinkedHashMap<String, String>> mappingIdMap;

    private String joinStep;

    private Set<String> seqSet;

    private String numTypeCd;

    private Boolean hasRoadArea;

    private String remark;


    public Address() {
    }

    public Address(String address) {
        this.originalAddress = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(String neighbor) {
        this.neighbor = neighbor;
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

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getAlley() {
        return alley;
    }

    public void setAlley(String alley) {
        this.alley = alley;
    }

    public String getSubAlley() {
        return subAlley;
    }

    public void setSubAlley(String subAlley) {
        this.subAlley = subAlley;
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

    public String getContinuousNum() {
        return continuousNum;
    }

    public void setContinuousNum(String continuousNum) {
        this.continuousNum = continuousNum;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }


    public String getAddrRemains() {
        return addrRemains;
    }

    public void setAddrRemains(String addrRemains) {
        this.addrRemains = addrRemains;
    }

    public boolean isParseSuccessed() {
        return isParseSuccessed;
    }

    public void setParseSuccessed(boolean parseSuccessed) {
        isParseSuccessed = parseSuccessed;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public String getBasementStr() {
        return basementStr;
    }

    public void setBasementStr(String basementStr) {
        this.basementStr = basementStr;
    }

    public String getNumFlrPos() {
        return numFlrPos;
    }

    public void setNumFlrPos(String numFlrPos) {
        this.numFlrPos = numFlrPos;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    public String getSegmentExistNumber() {
        return segmentExistNumber;
    }

    public void setSegmentExistNumber(String segmentExistNumber) {
        this.segmentExistNumber = segmentExistNumber;
    }

    public Map<String, Boolean> getSegNumMap() {
        return segNumMap;
    }

    public void setSegNumMap(Map<String, Boolean> segNumMap) {
        this.segNumMap = segNumMap;
    }

    public String getJoinStep() {
        return joinStep;
    }

    public void setJoinStep(String joinStep) {
        this.joinStep = joinStep;
    }

    public String getCountyCd() {
        return countyCd;
    }

    public void setCountyCd(String countyCd) {
        this.countyCd = countyCd;
    }

    public String getTownCd() {
        return townCd;
    }

    public void setTownCd(String townCd) {
        this.townCd = townCd;
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

    public String getLaneCd() {
        return laneCd;
    }

    public void setLaneCd(String laneCd) {
        this.laneCd = laneCd;
    }

    public String getAlleyIdSn() {
        return alleyIdSn;
    }

    public void setAlleyIdSn(String alleyIdSn) {
        this.alleyIdSn = alleyIdSn;
    }

    public String getNumFlr1Id() {
        return numFlr1Id;
    }

    public void setNumFlr1Id(String numFlr1Id) {
        this.numFlr1Id = numFlr1Id;
    }

    public String getNumFlr2Id() {
        return numFlr2Id;
    }

    public void setNumFlr2Id(String numFlr2Id) {
        this.numFlr2Id = numFlr2Id;
    }

    public String getNumFlr3Id() {
        return numFlr3Id;
    }

    public void setNumFlr3Id(String numFlr3Id) {
        this.numFlr3Id = numFlr3Id;
    }

    public String getNumFlr4Id() {
        return numFlr4Id;
    }

    public void setNumFlr4Id(String numFlr4Id) {
        this.numFlr4Id = numFlr4Id;
    }

    public String getNumFlr5Id() {
        return numFlr5Id;
    }

    public void setNumFlr5Id(String numFlr5Id) {
        this.numFlr5Id = numFlr5Id;
    }


    public String getNumFlrId() {
        return numFlrId;
    }

    public void setNumFlrId(String numFlrId) {
        this.numFlrId = numFlrId;
    }

    public String getRoomIdSn() {
        return roomIdSn;
    }

    public void setRoomIdSn(String roomIdSn) {
        this.roomIdSn = roomIdSn;
    }

    public String getNeighborCd() {
        return neighborCd;
    }

    public void setNeighborCd(String neighborCd) {
        this.neighborCd = neighborCd;
    }

    public Set<String> getSeqSet() {
        return seqSet;
    }

    public void setSeqSet(Set<String> seqSet) {
        this.seqSet = seqSet;
    }


    public String getNumTypeCd() {
        return numTypeCd;
    }

    public void setNumTypeCd(String numTypeCd) {
        this.numTypeCd = numTypeCd;
    }

    public Boolean getHasRoadArea() {
        return hasRoadArea;
    }

    public void setHasRoadArea(Boolean hasRoadArea) {
        this.hasRoadArea = hasRoadArea;
    }

    public List<String> getMappingId() {
        return mappingId;
    }

    public void setMappingId(List<String> mappingId) {
        this.mappingId = mappingId;
    }

    public List<LinkedHashMap<String, String>> getMappingIdMap() {
        return mappingIdMap;
    }

    public void setMappingIdMap(List<LinkedHashMap<String, String>> mappingIdMap) {
        this.mappingIdMap = mappingIdMap;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCleanAddress() {
        return cleanAddress;
    }

    public void setCleanAddress(String cleanAddress) {
        this.cleanAddress = cleanAddress;
    }

    public String getSpecialArea() {
        return specialArea;
    }

    public void setSpecialArea(String specialArea) {
        this.specialArea = specialArea;
    }


    public String getJB2NumFlrId() {
        return JB2NumFlrId;
    }

    public void setJB2NumFlrId(String JB2NumFlrId) {
        this.JB2NumFlrId = JB2NumFlrId;
    }

    public String getJB2NumFlrPos() {
        return JB2NumFlrPos;
    }

    public void setJB2NumFlrPos(String JB2NumFlrPos) {
        this.JB2NumFlrPos = JB2NumFlrPos;
    }

    public String getJB3NumFlrId() {
        return JB3NumFlrId;
    }

    public void setJB3NumFlrId(String JB3NumFlrId) {
        this.JB3NumFlrId = JB3NumFlrId;
    }

    public String getJB3NumFlrPos() {
        return JB3NumFlrPos;
    }

    public void setJB3NumFlrPos(String JB3NumFlrPos) {
        this.JB3NumFlrPos = JB3NumFlrPos;
    }

    public String getJB4NumFlrId() {
        return JB4NumFlrId;
    }

    public void setJB4NumFlrId(String JB4NumFlrId) {
        this.JB4NumFlrId = JB4NumFlrId;
    }

    public String getJB4NumFlrPos() {
        return JB4NumFlrPos;
    }

    public void setJB4NumFlrPos(String JB4NumFlrPos) {
        this.JB4NumFlrPos = JB4NumFlrPos;
    }

    public String getJB5NumFlrId() {
        return JB5NumFlrId;
    }

    public void setJB5NumFlrId(String JB5NumFlrId) {
        this.JB5NumFlrId = JB5NumFlrId;
    }

    public String getJB5NumFlrPos() {
        return JB5NumFlrPos;
    }

    public void setJB5NumFlrPos(String JB5NumFlrPos) {
        this.JB5NumFlrPos = JB5NumFlrPos;
    }

    public String getJC4NumFlrId() {
        return JC4NumFlrId;
    }

    public void setJC4NumFlrId(String JC4NumFlrId) {
        this.JC4NumFlrId = JC4NumFlrId;
    }

    public String getJC4NumFlrPos() {
        return JC4NumFlrPos;
    }

    public void setJC4NumFlrPos(String JC4NumFlrPos) {
        this.JC4NumFlrPos = JC4NumFlrPos;
    }

    @Override
    public String toString() {
        return "Address{" + "zipcode='" + zipcode + '\'' + ", county='" + county + '\'' + ", countyCd='" + countyCd + '\'' + ", town='" + town + '\'' + ", townCd='" + townCd + '\'' + ", village='" + village + '\'' + ", villageCd='" + villageCd + '\'' + ", neighbor='" + neighbor + '\'' + ", neighborCd='" + neighborCd + '\'' + ", road='" + road + '\'' + ", specialArea='" + specialArea + '\'' + ", area='" + area + '\'' + ", roadAreaSn='" + roadAreaSn + '\'' + ", lane='" + lane + '\'' + ", laneCd='" + laneCd + '\'' + ", alley='" + alley + '\'' + ", subAlley='" + subAlley + '\'' + ", alleyIdSn='" + alleyIdSn + '\'' + ", numFlr1='" + numFlr1 + '\'' + ", numFlr1Id='" + numFlr1Id + '\'' + ", numFlr2='" + numFlr2 + '\'' + ", numFlr2Id='" + numFlr2Id + '\'' + ", numFlr3='" + numFlr3 + '\'' + ", numFlr3Id='" + numFlr3Id + '\'' + ", numFlr4='" + numFlr4 + '\'' + ", numFlr4Id='" + numFlr4Id + '\'' + ", numFlr5='" + numFlr5 + '\'' + ", numFlr5Id='" + numFlr5Id + '\'' + ", continuousNum='" + continuousNum + '\'' + ", basementStr='" + basementStr + '\'' + ", numFlrPos='" + numFlrPos + '\'' + ", room='" + room + '\'' + ", roomIdSn='" + roomIdSn + '\'' + ", seq='" + seq + '\'' + ", addrRemains='" + addrRemains + '\'' + ", isParseSuccessed=" + isParseSuccessed + ", originalAddress='" + originalAddress + '\'' + ", cleanAddress='" + cleanAddress + '\'' + ", mappingId=" + mappingId + ", segmentExistNumber='" + segmentExistNumber + '\'' + ", mappingIdMap=" + mappingIdMap + ", joinStep='" + joinStep + '\'' + ", seqSet=" + seqSet + ", numTypeCd='" + numTypeCd + '\'' + ", hasRoadArea=" + hasRoadArea + ", remark='" + remark + '\'' + '}';
    }
}

