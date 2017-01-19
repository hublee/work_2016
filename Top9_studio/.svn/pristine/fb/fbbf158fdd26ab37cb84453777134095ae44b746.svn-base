package com.zeustel.top9.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 九宫格配置
 */
public class GridConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 每张图片的X边距
	private int padingX;
	// 每张图片的Y边距
	private int padingY;
	// 总列数
	private int columns;
	// 总行数
	private int rows;
	// 总宽度（保留字段）
	private int totalWidth;
	// 总高度（保留字段）
	private int totalHeight;
	
	private List<GridConfigItem> items;
	
	public int getItemWidth(){
		if (columns != 0) {
			return (totalWidth - padingX * 2 * columns) / columns;
		}else{
			return 0;
		}
		
	}
	
	public int getItemHeight(){
		if (rows != 0) {
			return (totalHeight - padingY * 2 * rows) / rows;
		}else{
			return 0;
		}
		
	}
	
	public int getPadingX() {
		return padingX;
	}
	public void setPadingX(int padingX) {
		this.padingX = padingX;
	}
	public int getPadingY() {
		return padingY;
	}
	public void setPadingY(int padingY) {
		this.padingY = padingY;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getTotalWidth() {
		return totalWidth;
	}
	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}
	public int getTotalHeight() {
		return totalHeight;
	}
	public void setTotalHeight(int totalHeight) {
		this.totalHeight = totalHeight;
	}

	public List<GridConfigItem> getItems() {
		return items;
	}

	public void setItems(List<GridConfigItem> items) {
		this.items = items;
	}
	
}
