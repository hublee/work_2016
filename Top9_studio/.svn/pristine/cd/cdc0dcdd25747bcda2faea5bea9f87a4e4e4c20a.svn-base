package com.zeustel.top9.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zeustel.top9.bean.LoginNote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 本地缓存
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/8 13:38
 */
public class SharedPreferencesUtils {
    public static final String PREFERENCE_USER = "user";
    public static final String PREFERENCE_SEARCH = "search";
    public static final String PREFERENCE_LAUNCH = "launch";

    /**
     * 获取最后登录过的账号
     */
    @Deprecated
    public static LoginNote getLastLoginNote(Context context) {
        SharedPreferences preferenceUser = context.getSharedPreferences(PREFERENCE_USER, Context.MODE_PRIVATE);
        Collection<?> values = preferenceUser.getAll().values();
        if (values != null && !values.isEmpty()) {
            Iterator<?> iterator = values.iterator();
            if (iterator != null) {
                Gson gson = new Gson();
                List<LoginNote> data = new ArrayList();
                while (iterator.hasNext()) {
                    LoginNote mLoginNote = gson.fromJson(iterator.next().toString(), LoginNote.class);
                    data.add(mLoginNote);
                }
                if (!Tools.isEmpty(data)) {
                    if (data.size() > 1) {
                        Collections.sort(data);
                    }
                    LoginNote mLoginNote = data.get(0);
                    return mLoginNote;
                }
            }
        }
        return null;
    }

    /**
     * 获取最后登录过的账号token
     */
    @Deprecated
    public static String getLastToken(Context context) {
        if (context == null) {
            return null;
        }
        LoginNote lastLoginNote = getLastLoginNote(context);
        if (lastLoginNote != null) {
            return lastLoginNote.getToken();
        } else {
            return null;
        }
    }

    /**
     * 获取token
     */
    public static String getToken(Context context) {
        if (context == null) {
            return null;
        }
        LoginNote lastLoginNote = getLoginNote(context);
        if (lastLoginNote != null) {
            return lastLoginNote.getToken();
        } else {
            return null;
        }
    }

    /**
     * 获取本地登录记录
     */
    public static LoginNote getLoginNote(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences preferenceUser = context.getSharedPreferences(PREFERENCE_USER, Context.MODE_PRIVATE);
        String userStr = preferenceUser.getString("user", null);
        if (userStr == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(userStr, LoginNote.class);
    }

    /**
     * 保存账号
     */
    public static void saveUser(Context context, LoginNote mLoginNote) {
        SharedPreferences preferenceUser = context.getSharedPreferences(PREFERENCE_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferenceUser.edit();
        Gson gson = new Gson();
        edit.putString("user", gson.toJson(mLoginNote));
        edit.commit();
    }

    /**
     * 保存搜索记录
     */
    public static void saveSearch(Context context, String key) {
        SharedPreferences preferenceUser = context.getSharedPreferences(PREFERENCE_SEARCH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferenceUser.edit();
        edit.putBoolean(key, true).commit();
    }

    /**
     * 获取搜索记录
     */
    public static Set<String> getSearch(Context context) {
        SharedPreferences preferenceUser = context.getSharedPreferences(PREFERENCE_SEARCH, Context.MODE_PRIVATE);
        if (preferenceUser.getAll() != null) {
            return preferenceUser.getAll().keySet();
        }
        return null;
    }

    /**
     * 是否第一次启动应用
     */
    public static boolean isFirstlaunch(Context context) {
        SharedPreferences preferenceLaunch = context.getSharedPreferences(PREFERENCE_LAUNCH, Context.MODE_PRIVATE);
        if (preferenceLaunch.contains("launch")) {
            return false;
        }
        return true;
    }

    /**
     * 第一次启动调用
     *
     * @param context
     */
    public static void firstLaunch(Context context) {
        SharedPreferences preferenceLaunch = context.getSharedPreferences(PREFERENCE_LAUNCH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferenceLaunch.edit();
        edit.putBoolean("launch", true).commit();
    }
}
