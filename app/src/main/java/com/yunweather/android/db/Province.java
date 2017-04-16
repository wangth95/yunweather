package com.yunweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ASUS on 2017/4/14.
 */

public class Province extends DataSupport {

    private int id;

    private String provinceName;

    private int provinceCode;   //省的代号
    //private String provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    /*private String provinceEn;
    private String provinceZh;

    public String getProvinceEn(){
        return provinceEn;
    }
    public void setProvinceEn(String provinceEn){
        this.provinceEn = provinceEn;
    }

    public String getProvinceZh(){
        return provinceZh;
    }
    public void setProvinceZh(String provinceZh){
        this.provinceZh = provinceZh;
    }*/

}
