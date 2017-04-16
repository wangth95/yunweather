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

    /*private String cityEn;
    private String cityZh;
    private String leaderEn;
    private String Id;

    public String getCityEn(){
        return cityEn;
    }
    public void setCityEn(String cityEn){
        this.cityEn = cityEn;
    }

    public String getCityZh(){
        return cityZh;
    }
    public void setCityZh(String cityZh){
        this.cityZh = cityZh;
    }
    public String getLeaderEn() {
        return leaderEn;
    }
    public void setLeaderEn(String leaderEn) {
        this.leaderEn = leaderEn;
    }
    public String getId() {
        return Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }*/

}
