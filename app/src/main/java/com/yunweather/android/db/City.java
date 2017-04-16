package com.yunweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ASUS on 2017/4/14.
 */

public class City extends DataSupport {

    private int id;

    private String cityName;

    private int cityCode;

    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    //服务器崩溃
    /*private String leaderEn;
    private String leaderZh;
    private String provinceEn;

    public String getLeaderEn(){
        return leaderEn;
    }
    public void setLeaderEn(String leaderEn){
        this.leaderEn = leaderEn;
    }

    public String getLeaderZh(){
        return leaderZh;
    }
    public void setLeaderZh(String leaderZh){
        this.leaderZh = leaderZh;
    }
    public String getProvinceEn() {
        return provinceEn;
    }
    public void setProvinceEn(String provinceEn) {
        this.provinceEn = provinceEn;
    }*/
}
