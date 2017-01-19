package com.zeustel.top9.bean.html;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 暂定版评测 <资讯以后?>
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/27 15:41
 */
@Table(name = "HtmlTemp")
public class HtmlTemp implements Serializable {
    @Id
    @NoAutoIncrement
    private int id;
    @Column(column = "json")
    private String json;
    @Transient
    private DesTemplate htmlDes;
    @Transient
    private List<SystemTemplate> systemTemplates;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public DesTemplate getHtmlDes() {
        return htmlDes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHtmlDes(DesTemplate htmlDes) {
        this.htmlDes = htmlDes;
    }

    public List<SystemTemplate> getSystemTemplates() {
        return systemTemplates;
    }

    public void setSystemTemplates(List<SystemTemplate> systemTemplates) {
        this.systemTemplates = systemTemplates;
    }
}
