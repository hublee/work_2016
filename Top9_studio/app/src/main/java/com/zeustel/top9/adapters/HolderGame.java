package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.Game;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/12 14:31
 */
public class HolderGame extends OverallRecyclerHolder<Game> {
    private ImageView grid9_item_icon;
    private ProgressBar grid9_item_progress;
    private TextView grid9_item_title;
    private TextView grid9_item_download;
    private TextView grid9_item_des;

    public HolderGame(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        grid9_item_icon = (ImageView) itemView.findViewById(R.id.grid9_item_icon);
        grid9_item_progress = (ProgressBar) itemView.findViewById(R.id.grid9_item_progress);
        grid9_item_title = (TextView) itemView.findViewById(R.id.grid9_item_title);
        grid9_item_download = (TextView) itemView.findViewById(R.id.grid9_item_download);
        grid9_item_des = (TextView) itemView.findViewById(R.id.grid9_item_des);
        grid9_item_download.setOnClickListener(this);
        grid9_item_des.setOnClickListener(this);
    }

    @Override
    public void set(OverallRecyclerAdapter<Game> adapter, int position) {
        Game game = adapter.getItem(position);
        getImageLoader().displayImage(Constants.TEST_IMG + game.getBanner(), grid9_item_icon,
                Tools.options);
        grid9_item_progress.setProgress(0);
        grid9_item_title.setText(game.getDescribe());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (getOnElementClickListener() != null) {
            switch (v.getId()) {
                case R.id.grid9_item_des:
                    getOnElementClickListener().onElementClick(getAdapterPosition(), "des");
                    break;
                case R.id.grid9_item_download:
                    getOnElementClickListener().onElementClick(getAdapterPosition(), "download");
                    break;
            }

        }
    }
}
