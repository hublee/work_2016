package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderNote;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.NoteFragment;
import com.zeustel.top9.bean.AmountNote;
import com.zeustel.top9.result.AmountNoteListResult;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBAmountNote;
import com.zeustel.top9.utils.operate.DataAmountNote;

/**
 * 带数量的记录
 */
public class AmountFragment extends NoteFragment<AmountNote> {
    public static final int NOTE_TYPE_CURRENCY = 1;
    public static final int NOTE_TYPE_CONVERSION = 2;
    private static final String EXTRA_NAME = "noteType";
    private int noteType;
    private String url = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public AmountFragment() {

    }

    public static AmountFragment newInstance(int noteType) {
        AmountFragment fragment = new AmountFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_NAME, noteType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteType = getArguments().getInt(EXTRA_NAME);
        }
        if (NOTE_TYPE_CONVERSION == noteType) {
            url = Constants.URL_SHOP_NOTE;
        } else {
            url = Constants.URL_INTEGRAL_NOTE;
        }
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDbOperate(new DBAmountNote(getActivity()));
        setDataOperate(new DataAmountNote(getHandler(), getDbOperate()));
        try {
            setAdapter(new OverallRecyclerAdapter<AmountNote>(getData(), R.layout.list_item_note, HolderNote.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        updateUI();
    }

    @Override
    public void OnResumeCorrect() {
        super.OnResumeCorrect();
        updateUI();
    }

    private void updateUI() {
        if (getNote_title() != null) {
            getNote_title().setText((Html.fromHtml(getString(R.string.html_font, "#484D57"/*color*/, getString(R.string.note_balance)) + getString(R.string.html_font, "#FD4C3C"/*color*/, Constants.USER.getIntegralAmount()))));
            imageLoader.displayImage(Constants.TEST_IMG + Constants.USER.getIcon(), getNote_icon(), Tools.options);
        }
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onPullDownRefresh() {
        AmountNote amountNote = Tools.getFirstData(getData());
        long time = 0;
        if (amountNote != null) {
            time = amountNote.getTime();
        }
        try {
            getDataOperate().postListData(getDataOperate().createListBundle(url, time, NetHelper.Flag.UPDATE), AmountNoteListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {
        if (result != null) {
            Constants.USER.setIntegralAmount(result.getCount());
            updateUI();
        }
    }

    @Override
    public void onHandleListHistory(Result result, Object obj) {
        if (result != null) {
            Constants.USER.setIntegralAmount(result.getCount());
            updateUI();
        }
    }

    @Override
    public void onPullUpRefresh() {
        AmountNote amountNote = Tools.getLastData(getData());
        long time = 0;
        if (amountNote != null) {
            time = amountNote.getTime();
        }
        try {
            getDataOperate().postListData(getDataOperate().createListBundle(url, time, NetHelper.Flag.HISTORY), AmountNoteListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
