package com.yunweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ASUS on 2017/5/3.
 */

public class District extends DataSupport {

    private int id;

    private String ditrictName;

    private String weatherId;

    public int getId() {
            return id;
        }

    public void setId(int id) {
            this.id = id;
        }

    public String getDitrictName() {
            return ditrictName;
        }

    public void setDitrictName(String ditrictName) {
            this.ditrictName = ditrictName;
        }

    public String getWeatherId() {
            return weatherId;
        }

    public void setWeatherId(String weatherId) {
            this.weatherId = weatherId;
        }


    private String countyZh;
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
        }



}
