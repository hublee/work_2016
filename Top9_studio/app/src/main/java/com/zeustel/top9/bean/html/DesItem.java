package com.zeustel.top9.bean.html;

import java.util.List;

/**
 * 如详细信息
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 09:22
 */
@Deprecated
public class DesItem {
    private String icon;
    private String name;
    private List<ItemInfo> detailInfos;

    public List<ItemInfo> getDetailInfos() {
        return detailInfos;
    }

    public void setDetailInfos(List<ItemInfo> detailInfos) {
        this.detailInfos = detailInfos;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
