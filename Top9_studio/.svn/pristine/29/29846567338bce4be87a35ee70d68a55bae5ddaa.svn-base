package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 兑换商品
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/24 16:04
 */
@Table(name = "ExchangeGoods")
public class ExchangeGoods implements Serializable {
    //商品id
    @Id
    @NoAutoIncrement
    private int id;
    //名称
    @Column(column = "name")
    private String name;
    //价钱
    @Column(column = "price")
    private int price;
    //图像
    @Column(column = "icon")
    private String icon;
    //展览图片 ,分割
    @Column(column = "exhibition")
    private String exhibition;
    //商品描述
    @Column(column = "describe")
    private String describe;
    //发布时间
    @Column(column = "time")
    private long time;
    //上限
    @Column(column = "upperLimit")
    private int upperLimit;
    //剩余
    @Column(column = "residue")
    private int residue;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getExhibition() {
        return exhibition;
    }

    public void setExhibition(String exhibition) {
        this.exhibition = exhibition;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getResidue() {
        return residue;
    }

    public void setResidue(int residue) {
        this.residue = residue;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ExchangeGoods that = (ExchangeGoods) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ExchangeGoods{" +
                "describe='" + describe + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", icon='" + icon + '\'' +
                ", exhibition='" + exhibition + '\'' +
                ", time=" + time +
                ", upperLimit=" + upperLimit +
                ", residue=" + residue +
                '}';
    }
}
