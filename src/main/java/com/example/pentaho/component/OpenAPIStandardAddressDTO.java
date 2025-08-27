package com.example.pentaho.component;

import io.swagger.v3.oas.annotations.media.Schema;

public class OpenAPIStandardAddressDTO {

    private String zipcode;

    private String county;

    private String town;

    private String village;

    private String neighbor	;

    private String road;

    private String specialArea;

    private String area;

    private String lane;

    private String alley;

    private String subAlley;

    private String numFlr;

    private String room;

    private String parseSuccessed;

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

    public String getSpecialArea() {
        return specialArea;
    }

    public void setSpecialArea(String specialArea) {
        this.specialArea = specialArea;
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

    public String getNumFlr() {
        return numFlr;
    }

    public void setNumFlr(String numFlr) {
        this.numFlr = numFlr;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getParseSuccessed() {
        return parseSuccessed;
    }

    public void setParseSuccessed(String parseSuccessed) {
        this.parseSuccessed = parseSuccessed;
    }
}
