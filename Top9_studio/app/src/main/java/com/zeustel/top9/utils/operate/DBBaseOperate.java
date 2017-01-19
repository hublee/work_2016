package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.zeustel.top9.utils.DButilsHelper;

import java.util.List;

/**
 * 数据库操作原型
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/22 14:20
 */
public abstract class DBBaseOperate<T> {//expected
    public static final int LIMIT_PAGE_SIZE = 10;

    private DbUtils dbUtils = null;

    public DBBaseOperate(Context context) {
        dbUtils = DButilsHelper.getDbUtils(context);
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    //单个对象添加
    public boolean insertSingle(T data) throws Exception {
        getDbUtils().save(data);
        return true;
    }

    //集合对象添加
    public int insertList(List<T> data) throws Exception {
        getDbUtils().saveAll(data);
        return 0;
    }

    //单个对象更新
    public boolean updateSingle(T data) throws Exception {
        getDbUtils().update(data);
        return true;
    }

    //单个对象删除
    public boolean deleteSingle(T data) throws Exception {
        getDbUtils().delete(data);
        return true;
    }

    //单个对象删除
    public boolean deleteSingle(int id) throws Exception {
        getDbUtils().delete(getEntity(), WhereBuilder.b("id", "=", id));
        return true;
    }

    //单个对象查询
    public T queueSingle(int id) throws Exception {
        return getDbUtils().findById(getEntity(), id);
    }

    //查询所有
    public List<T> queueList(long time, boolean isHistory) throws Exception {
        return getDbUtils().findAll(Selector.from(getEntity()).where("time", isHistory ? "<" : ">", time).orderBy("time", true).limit(LIMIT_PAGE_SIZE));
    }

    //    entity
    public abstract Class<T> getEntity();

}
