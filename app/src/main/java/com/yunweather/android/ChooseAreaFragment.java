package com.yunweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.yunweather.android.db.City;
import com.yunweather.android.db.County;
import com.yunweather.android.db.District;
import com.yunweather.android.db.Province;
import com.yunweather.android.util.HttpUtil;
import com.yunweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ASUS on 2017/4/14.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    public static final int LEVEL_SEARCH = 3;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    //省列表
    private List<Province> provinceList;

    //市列表
    private List<City> cityList;

    //县列表
    private List<County> countyList;

    //搜索中的城市列表
    private List<District> districtList;

    //选中的省份
    private Province selectedProvince;

    //选中的城市
    private City selectedCity;

    //当前选中的级别
    private int currentLevel;

    private int jugement;

    public static final int SEARCH = 0;
    public static final int NOTSEARCH = 1;


    private EditText inputText;
    private Button search;
    private District selectedDistrict;
    static String cityZh = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        inputText = (EditText) view.findViewById(R.id.input_text);
        search = (Button) view.findViewById(R.id.search);
        jugement = SEARCH;
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityZh = inputText.getText().toString();
                String address = "https://cdn.heweather.com/china-city-list.json";
                if (cityZh.equals("")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    District district = new District();
                    Cursor cursor = DataSupport.findBySQL("select * from District");
                    if(cursor.getCount() == 0)
                    {
                        //执行插入语句
                        querySearch(address, cityZh);
                    }else {
                        searchDistrict(cityZh);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                } else if (!cityZh.equals("")){
                    selectedDistrict = districtList.get(position);
                    searchDistrict(cityZh);
                    String weatherId = districtList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity) {
                        inputText.setText("");
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        inputText.setText("");
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                } else if (currentLevel == LEVEL_SEARCH) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);

        inputText.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);

        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

        inputText.setVisibility(View.GONE);
        search.setVisibility(View.GONE);

        cityList = DataSupport.where("provinceid = ?", String.
                valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        inputText.setVisibility(View.GONE);
        search.setVisibility(View.GONE);

        countyList = DataSupport.where("cityid = ?", String.
                valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据。
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*private void searchCounties(String cityZh) {
        //titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

        //inputText.setVisibility(View.GONE);
        //search.setVisibility(View.GONE);

        *//*countyList = DataSupport.where("cityid = ?", String.
                valueOf(selectedCity.getId())).find(County.class);*//*
        districtList = DataSupport.where("countyZh = ?", String.valueOf(cityZh))
                .limit(3181)
                .find(District.class);

        if (districtList.size() > 0) {
            dataList.clear();
            for (District district : districtList) {
                    dataList.add(district.getCountyZh() + ", "
                            + district.getleaderZh() + ", " + district.getprovinceZh() + ", " + district.getId());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_SEARCH;
        } else {
            //int provinceCode = selectedProvince.getProvinceCode();
            //int cityCode = selectedCity.getCityCode();
            //String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            String address = "https://cdn.heweather.com/china-city-list.json";
            querySearch(address, cityZh);
        }
    }*/

    private void searchDistrict(String cityZh) {
        titleText.setText("搜索结果");
        backButton.setVisibility(View.VISIBLE);

        //inputText.setVisibility(View.GONE);
        //search.setVisibility(View.GONE);


            districtList = DataSupport.where("countyZh = ?", String.valueOf(cityZh)).find(District.class);

            if (districtList.size() > 0) {
                dataList.clear();
                for (District district : districtList) {
                    dataList.add(district.getCountyZh() + ", "
                            + district.getleaderZh() + ", " + district.getprovinceZh()/* + ", " + district.getId()*/);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel = LEVEL_SEARCH;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        backButton.setVisibility(View.GONE);
                        closeProgressDialog();
                        Toast.makeText(getContext(), "没有找到这个地方", Toast.LENGTH_SHORT).show();
                    }
                });
            }

    }

    private void querySearch(String address, final String type) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("这可能需要一点时间，请稍等...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //showProgress();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                result = Utility.handleSearchResponse(responseText, type);
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            searchDistrict(type);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    //显示进度对话框
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void showProgress(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
