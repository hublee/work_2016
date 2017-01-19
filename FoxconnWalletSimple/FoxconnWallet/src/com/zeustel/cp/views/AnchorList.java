package com.zeustel.cp.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

public class AnchorList extends LinearLayout{
	private ListView listView;

	public AnchorList(Context context) {
		super(context);
		
		initView();
	}
	
	private void initView(){
		inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_anchorlist"), this);
		
		listView = (ListView)findViewById(R.id.anchor_listView);
	}
	
	public void addData(){
		
	}
	
}
