package com.zeustel.top9.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zeustel.top9.ExhibitionActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.VideoActivity;
import com.zeustel.top9.base.WrapOneFragment;
import com.zeustel.top9.bean.Compere;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.AnimViewFlipper;
import com.zeustel.top9.widgets.ExhibitionView;

/**
 * 主播页面
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/26
 */
public class CompereFragment extends WrapOneFragment implements View.OnClickListener {
    private static final String EXTRA_NAME = "Compere";
    private Compere compere;
    private TextView compere_manifesto;
    private ExhibitionView compere_exhibition;
    private TextView compere_name;
    private TextView compere_nameEN;
    private TextView compere_birthday;
    //    private TextView compere_bloodType;
    //    private TextView compere_constellation;
    //    private TextView compere_hobby;
    private TextView compere_other;
    //    private VideoPlayer comment_palayer;
    private ImageView comment_palayer;
    private ImageView comment_palayer_start;

    public CompereFragment() {
    }

    public static CompereFragment newInstance(Compere compere) {
        CompereFragment instance = new CompereFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, compere);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.compere = (Compere) getArguments().getSerializable(EXTRA_NAME);
        if (compere == null) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_compere, container, false);
        compere_manifesto = (TextView) contentView.findViewById(R.id.compere_manifesto);
        compere_exhibition = (ExhibitionView) contentView.findViewById(R.id.compere_exhibition);
        comment_palayer = (ImageView) contentView.findViewById(R.id.comment_palayer);
        comment_palayer_start = (ImageView) contentView.findViewById(R.id.comment_palayer_start);
        View compere_layout = contentView.findViewById(R.id.compere_detail);
        compere_name = (TextView) compere_layout.findViewById(R.id.compere_name);
        compere_nameEN = (TextView) compere_layout.findViewById(R.id.compere_nameEN);
        compere_birthday = (TextView) compere_layout.findViewById(R.id.compere_birthday);
      /*  compere_bloodType = (TextView) compere_layout.findViewById(R.id.compere_bloodType);
        compere_constellation = (TextView) compere_layout.findViewById(R.id.compere_constellation);
        compere_hobby = (TextView) compere_layout.findViewById(R.id.compere_hobby);*/
        compere_other = (TextView) compere_layout.findViewById(R.id.compere_other);
        compere_manifesto.setText(compere.getManifesto());


        String photos = compere.getPhotos();
        if (photos != null) {
            compere_exhibition.fillContent(photos.split(","), ImageDownloader.Scheme.DRAWABLE);
        }
        /**
         * test
         */
        comment_palayer.setOnClickListener(this);
        comment_palayer_start.setOnClickListener(this);
        comment_palayer.setImageResource(Integer.valueOf(compere.getVideoCover()));
        compere_name.setText(compere.getNickName());
        compere_nameEN.setText(compere.getNameEN());
        compere_birthday.setText(compere.getBirthday());
        compere_other.setText(getString(R.string.compere_blood, compere.getBloodType()) +
                getString(R.string.space) +
                getString(R.string.space) +
                "|" +
                getString(R.string.space) +
                getString(R.string.space) +
                getString(R.string.compere_constellation, compere.getConstellation()) +
                getString(R.string.space) +
                getString(R.string.space) +
                "|" +
                getString(R.string.space) +
                getString(R.string.space) +
                getString(R.string.compere_hobby, compere.getHobby()));
      /*  compere_bloodType.setText();
        compere_constellation.setText();
        compere_hobby.setText();*/
        compere_exhibition.addOnItemClickCallBack(new AnimViewFlipper.OnItemClickCallBack() {
            @Override
            public void onItemClick(int index) {
                Intent intent = new Intent(getActivity(), ExhibitionActivity.class);
                intent.putExtra(ExhibitionActivity.EXTRA_NAME_PATH, Tools.convertPathArray(compere.getPhotos()));
                intent.putExtra(ExhibitionActivity.EXTRA_NAME_POSITION, index);
                intent.putExtra(ExhibitionActivity.EXTRA_NAME_SCHEME, ImageDownloader.Scheme.DRAWABLE);
                startActivity(intent);
            }
        });
        return contentView;
    }


    @Override
    public String getClassName() {
        return CompereFragment.class.getSimpleName();
    }

    @Override
    public String getFloatTitle() {
        return getString(R.string.title_fm_compere);
    }

    @Override
    public int getBackgroundColor() {
        return getResources().getColor(R.color.yellow);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), VideoActivity.class);
        intent.putExtra(VideoActivity.EXTRA_VIDEO_UR, compere.getVideo());
        intent.putExtra(VideoActivity.EXTRA_VIDEO_COVER, Integer.valueOf(compere.getVideoCover()));
        startActivity(intent);
    }
}
