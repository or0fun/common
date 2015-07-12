package com.fang.common.util;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fang.common.CustomConstant;

/**
 * Created by benren.fj on 6/30/15.
 */
public class MapUtil {

    private Context context;
    public LocationClient mLocationClient = null;
    public BDLocationListener listener;

    private static MapUtil instance;

    public static MapUtil getInstance() {
        return instance;
    }

    public static void init(Context context, BDLocationListener listener) {
        instance = new MapUtil(context, listener);
    }

    private MapUtil(Context context, BDLocationListener listener) {
        this.context = context;
        this.listener = listener;
        mLocationClient = new LocationClient(context);     //声明LocationClient类
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setProdName("知号码");
        option.setScanSpan((int)CustomConstant.THREE_HOUR);
        mLocationClient.setLocOption(option);

        mLocationClient.registerLocationListener( listener );    //注册监听函数
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    public LocationClient getLocationClient() {
        return mLocationClient;
    }

}
