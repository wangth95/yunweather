package com.yunweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ASUS on 2017/4/14.
 */

public class County extends DataSupport {

    private int id;

    private String countyName;

    private String weatherId;

    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    /*private String countyZh;
    private String leaderZh;
    private String provinceZh;

    public String getCountyZh(){
        return countyZh;
    }
    public void setCountyZh(String countyZh){
        this.countyZh = countyZh;
    }
    public String getleaderZh(){
        return leaderZh;
    }
    public void setleaderZh(String leaderZh){
        this.leaderZh = leaderZh;
    }
    public String getprovinceZh(){
        return provinceZh;
    }
    public void setprovinceZh(String provinceZh){
        this.provinceZh = provinceZh;
    }*/

}
