package com.zeustel.top9.bean;

/**
 * 订单商品
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/17 09:53
 */
public class OrderGoods extends ExchangeGoods {
    //客户姓名
    private String clientName;
    //地址
    private String address;
    //联系电话
    private String tel;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
