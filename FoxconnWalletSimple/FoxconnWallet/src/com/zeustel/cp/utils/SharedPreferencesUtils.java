package com.zeustel.cp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zeustel.cp.bean.AccountInfo;
import com.zeustel.cp.wallet.WalletActivity;

import java.util.Map;

/**
 * SharedPreferencesCompat 缓存
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/29 09:39
 */
public class SharedPreferencesUtils {
    private static SharedPreferences preferences;
    private static final String PREFERENCES_NAME = "zeustel_Preferences";

    /**
     * 更新参数
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void updatePreferences(Context context, String key, String value) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        final SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key,value).commit();
        if (key.startsWith(WalletActivity.ACCOUNT_PREFIX)) {
            Gson gson = new Gson();
            final AccountInfo accountInfo = gson.fromJson(value, AccountInfo.class);
            SdkUtils.SetToken(accountInfo.getToken());
            UserManager.getInstance(context).setOnline(true);
        }
    }

    /**
     * 获取参数
     * @param context 上下文
     * @param key 键
     * @return 值
     */
    public static String getPreferences(Context context, String key) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        if (preferences.contains(key)) {
            return preferences.getString(key, "");
        }
        return"";
    }
    public static Map<String, ?> getAllPreferences(Context context){
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        return preferences.getAll();
    }
}
