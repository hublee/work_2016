package com.zeustel.top9.fragments;

import android.os.Bundle;
import android.os.Handler;

import com.zeustel.top9.bean.AmountNote;
import com.zeustel.top9.result.AmountNoteListResult;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBBaseOperate;
import com.zeustel.top9.utils.operate.DataAmountNote;
import com.zeustel.top9.utils.operate.DataBaseOperate;

import java.util.List;

/**
 * 积分和商城记录
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/4 15:23
 */
public class AmountChoiceFragment extends ChoiceFragment<AmountNote> {
    //获币记录
    public static final int NOTE_TYPE_CURRENCY = 1;
    //商城记录
    public static final int NOTE_TYPE_CONVERSION = 2;
    private static final String EXTRA_NAME = "noteType";
    private int noteType;
    //路径
    private String url = null;

    public static AmountChoiceFragment newInstance(int noteType) {
        AmountChoiceFragment fragment = new AmountChoiceFragment();
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
        Tools.log_i(AmountChoiceFragment.class, "onCreate", "url : " + url);
    }

    @Override
    protected DataBaseOperate<AmountNote> getDataOperate(Handler handler, DBBaseOperate<AmountNote> dbOperate) {
        return new DataAmountNote(handler, dbOperate);
    }

    @Override
    protected DBBaseOperate<AmountNote> getDbOperate() {
        return /*new DBAmountNote(getActivity())*/ null;
    }

    @Override
    protected Class<? extends Result<List<AmountNote>>> getListResultCls() {
        return AmountNoteListResult.class;
    }

    @Override
    protected String getListUrl() {
        return url;
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
