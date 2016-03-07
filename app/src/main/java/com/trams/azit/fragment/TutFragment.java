package com.trams.azit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.adapter.TutAdapter;
import com.trams.azit.dialog.ReportSubcribeDialog;
import com.trams.azit.model.TutModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 25/01/2016.
 */
public class TutFragment extends Fragment implements View.OnClickListener {

    private ViewPager vpTut;
    private ImageView imgNext, imgBack;
    private Button btnSkip;
    private TutAdapter tutAdapter;
    private ImageView imgDescription;
    private TextView description;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tut_layout, null);

        vpTut = (ViewPager) view.findViewById(R.id.vp_tut);

        imgNext = (ImageView) view.findViewById(R.id.img_next);
        imgNext.setOnClickListener(this);

        imgBack = (ImageView) view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        btnSkip = (Button) view.findViewById(R.id.btn_skip);
        btnSkip.setOnClickListener(this);

        imgDescription = (ImageView) view.findViewById(R.id.img_description);
        description = (TextView) view.findViewById(R.id.description);
        description.setText("맞춤리포트란?\n학습 데이터 분석을 실핼하여 자기 분석과 학습 가이드라인을 통해 내가 목표한 바를 이루기 위하여 실행해야 할 학습 전략을 제시합니다.");

        ArrayList<TutModel> tutModels = new ArrayList<>();

        tutModels.add(new TutModel(R.drawable.img_tut1_icon, R.drawable.img_tut1_des));
        tutModels.add(new TutModel(R.drawable.img_tut2_icon, R.drawable.img_tut2_des));
        tutModels.add(new TutModel(R.drawable.img_tut3_icon, R.drawable.img_tut3_des));
        tutModels.add(new TutModel(R.drawable.img_tut4_icon, R.drawable.img_tut4_des));

        tutAdapter = new TutAdapter(getContext(), tutModels);
        vpTut.setAdapter(tutAdapter);

        vpTut.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position) {
                    case 0:
                        imgBack.setVisibility(View.INVISIBLE);
                        imgNext.setVisibility(View.VISIBLE);
                        description.setText("맞춤리포트란?\n학습 데이터 분석을 실핼하여 자기 분석과 학습 가이드라인을 통해 내가 목표한 바를 이루기 위하여 실행해야 할 학습 전략을 제시합니다.");
                        imgDescription.setImageResource(R.drawable.img_tut1_des);
                        break;
                    case 1:
                        imgBack.setVisibility(View.VISIBLE);
                        imgNext.setVisibility(View.VISIBLE);
                        description.setText("학습 패턴을 분석해드려요!");
                        imgDescription.setImageResource(R.drawable.img_tut2_des);
                        break;
                    case 2:
                        imgBack.setVisibility(View.VISIBLE);
                        imgNext.setVisibility(View.VISIBLE);
                        description.setText("입시 정보를 알려드려요!");
                        imgDescription.setImageResource(R.drawable.img_tut3_des);
                        break;
                    case 3:
                        imgBack.setVisibility(View.VISIBLE);
                        imgNext.setVisibility(View.INVISIBLE);
                        description.setText("경쟁자와 학습비교 분석과\n나에게 맞는 교재, 강의를 추천해드려요!");
                        imgDescription.setImageResource(R.drawable.img_tut4_des);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_next:
                vpTut.setCurrentItem(vpTut.getCurrentItem() + 1);
                break;

            case R.id.img_back:
                vpTut.setCurrentItem(vpTut.getCurrentItem() - 1);
                break;

            case R.id.btn_skip:
                ReportSubcribeDialog reportSubcribeDialog = new ReportSubcribeDialog(getActivity());
                reportSubcribeDialog.showView();
                break;
        }
    }
}
