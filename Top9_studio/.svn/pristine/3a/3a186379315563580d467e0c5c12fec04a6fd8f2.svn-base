package com.zeustel.top9.bean;

import android.graphics.Rect;

import java.io.Serializable;

/**
 九宫格每项配置
 */
public class GridConfigItem implements Serializable {
    private int id;
    //列的下标 从1开始 代表第几列
    private int columnIndex;
    //行的下标 从1开始 代表第几行
    private int rowIndex;
    //代表宽度占几列
    private int widthSize;
    //代表高度占几行
    private int heightSize;
    //游戏id
    private int gameId;
    //图片路径
    private String url;
    private GridConfig gridConfig;
    public Rect getRect() {
        int itemWidth = gridConfig.getItemWidth();
        int itemHeight = gridConfig.getItemHeight();
        int startX = (gridConfig.getPadingX() * 2 + itemWidth) * (columnIndex - 1)
                + gridConfig.getPadingX();
        int startY = (gridConfig.getPadingY() * 2 + itemHeight) * (rowIndex - 1)
                + gridConfig.getPadingY();
        Rect rect = new Rect(startX, startY,
                startX + itemWidth * widthSize + gridConfig.getPadingX() * (widthSize -1) * 2,
                startY + itemHeight * heightSize + gridConfig.getPadingY() * (heightSize -1) * 2
        );
        return rect;
    }
    public GridConfigItem(int columnIndex, int rowIndex, int widthSize, int heightSize, int gameId, String url, int id) {
        super();
        this.columnIndex = columnIndex;
        this.gameId = gameId;
        this.heightSize = heightSize;
        this.rowIndex = rowIndex;
        this.url = url;
        this.widthSize = widthSize;
        this.id = id;
    }

}
