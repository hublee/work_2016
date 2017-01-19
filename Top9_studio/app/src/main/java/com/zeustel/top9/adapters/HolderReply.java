package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zeustel.top9.R;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.bean.community.Content;
import com.zeustel.top9.bean.community.Reply;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataUser;

import org.apache.http.Header;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 15:59
 */
public class HolderReply extends OverallRecyclerHolder<Reply> {
    private ImageView reply_icon;
    private TextView reply_name;
    private TextView reply_time;
    private TextView reply_good;
    private TextView reply_content;
    private View animGood;
    private Gson gson = new Gson();
    private Reply item;
    private SubUserInfo commentUser;
    private String icon;
    private String nickName;
    private int goodCount;
    private Content content;
    private String msg;
    private boolean gooded;
    private Result result;

    public HolderReply(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        reply_icon = (ImageView) itemView.findViewById(R.id.reply_icon);
        reply_name = (TextView) itemView.findViewById(R.id.reply_name);
        reply_time = (TextView) itemView.findViewById(R.id.reply_time);
        reply_good = (TextView) itemView.findViewById(R.id.reply_good);
        reply_content = (TextView) itemView.findViewById(R.id.reply_content);
        animGood = itemView.findViewById(R.id.animGood);
        reply_good.setOnClickListener(this);
        reply_icon.setOnClickListener(this);
    }

    @Override
    public void set(OverallRecyclerAdapter<Reply> adapter, int position) {
        animGood.setVisibility(View.INVISIBLE);
        item = adapter.getItem(position);
        if (item != null) {
            commentUser = item.getPublishUser();
            if (commentUser != null) {
                icon = commentUser.getIcon();
                getImageLoader().displayImage(Constants.TEST_IMG + icon, reply_icon, Tools.options);
            }
            nickName = commentUser.getNickName();
            goodCount = item.getGoodCount();
            content = item.getContent();
            gooded = item.isGooded();
            reply_name.setText(nickName);
            //            reply_time.setText();
            reply_good.setText(String.valueOf(goodCount));
            //            item.getContentType()
            if (content != null) {
                msg = content.getMsg();
                reply_content.setText(msg);
            }

            reply_good.setSelected(gooded);
            if (!item.isGooded()) {
                DataUser.checkGoodState(item.getId(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        if (bytes != null) {
                            result = gson.fromJson(new String(bytes), Result.class);
                            if (result != null) {
                                if (result.getError() == 10017/*已经点赞*/) {
                                    reply_good.setSelected(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }
                });
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (getOnElementClickListener() != null) {
            switch (v.getId()) {
                case R.id.reply_icon:
                    getOnElementClickListener().onElementClick(getAdapterPosition(), "icon");
                    break;
                case R.id.reply_good:
                    getOnElementClickListener().onElementClick(getAdapterPosition(), "good");
                    break;
            }
        }
    }

    public void goodChange(int position) {
        if (getAdapterPosition() == position) {
            Tools.startGoodOne(getContext(), animGood);
            Tools.log_i(HolderReply.class, "goodChange", "");
        }
    }
}
