package com.zeustel.top9.utils;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.zeustel.top9.bean.CommunityInfo;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.bean.ExchangeGoods;
import com.zeustel.top9.bean.Game;
import com.zeustel.top9.bean.GameEvaluating;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/20 17:31
 */
public class DButilsHelper {
    private DbUtils dbUtils;
    private Context context = null;
    private static DButilsHelper mDButilsHelper = null;

    private DButilsHelper(Context context) {
        this.context = context;
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
        config.setDbName(Constants.DATABASE_NAME);
        config.setDbVersion(Constants.DATABASE_VERSION);
        config.setDbUpgradeListener(new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
                Tools.log_i(DButilsHelper.class, "onUpgrade", "");
            }
        });
        dbUtils = DbUtils.create(config);
        dbUtils.configDebug(Constants.DEBUG);
        try {
            dbUtils.createTableIfNotExist(GameEvaluating.class);
            dbUtils.createTableIfNotExist(Game.class);
            dbUtils.createTableIfNotExist(ExchangeGoods.class);
            dbUtils.createTableIfNotExist(CommunityTopic.class);
            dbUtils.createTableIfNotExist(CommunityInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static DbUtils getDbUtils(Context context) {
        if (mDButilsHelper == null) {
            synchronized (DButilsHelper.class) {
                if (mDButilsHelper == null)
                    mDButilsHelper = new DButilsHelper(context);
            }
        }
        return mDButilsHelper.dbUtils;
    }

}
