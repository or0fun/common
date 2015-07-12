package com.fang.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by benren.fj on 6/10/15.
 */
public abstract class AbsSharedPre {

    protected SharedPreferences mSharedPreferences;

    public static class Holder {
        public static SharedPreferencesHelper mInstance = new SharedPreferencesHelper();
    }

    public static SharedPreferencesHelper getInstance() {
        return Holder.mInstance;
    }

    public abstract void init(Context context);

    public String getString(String key) {
        String value = mSharedPreferences.getString(key, "");
        return value;

    }
    public String getString(String key, String defaultValue) {
        String value = mSharedPreferences.getString(key, defaultValue);
        return value;

    }
    public void setString(String key, String value) {

        SharedPreferences.Editor sharedata = mSharedPreferences.edit();
        sharedata.putString(key, value);
        sharedata.commit();

    }
    public int getInt(String key, int i) {

        SharedPreferences sharedata = mSharedPreferences;
        int value = sharedata.getInt(key, i);
        return value;

    }
    public void setInt(String key, int value) {

        SharedPreferences.Editor sharedata = mSharedPreferences.edit();
        sharedata.putInt(key, value);
        sharedata.commit();

    }

    public long getLong(String key, long defaultvalue) {

        SharedPreferences sharedata = mSharedPreferences;
        long value = sharedata.getLong(key, defaultvalue);
        return value;

    }

    public long getLong(String key) {
        long value = mSharedPreferences.getLong(key, 0);
        return value;

    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor sharedata = mSharedPreferences.edit();
        sharedata.putLong(key, value);
        sharedata.commit();

    }
    public boolean getBoolean(String key, boolean defaultvalue) {
        boolean value = mSharedPreferences.getBoolean(key, defaultvalue);
        return value;

    }
    public void setBoolean(String key, boolean value) {

        SharedPreferences.Editor sharedata = mSharedPreferences.edit();
        sharedata.putBoolean(key, value);
        sharedata.commit();

    }
    /**
     * 保存对象信息
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            String productBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            setString(key, productBase64);
        } catch (IOException e) {
            DebugLog.d("SharedPreferencesHelper", e.toString());
        }
    }
    /**
     * 保存对象信息
     * @param key
     */
    public Object getObject(String key) {
        try {
            String productBase64 = mSharedPreferences.getString(key, "");
            byte[] base64Bytes = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            DebugLog.d("SharedPreferencesHelper", e.toString());
        }
        return null;
    }
}
