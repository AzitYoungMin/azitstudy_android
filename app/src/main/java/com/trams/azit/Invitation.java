package com.trams.azit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by Administrator on 2015-10-27.
 */
public class Invitation extends Fragment {
    private KakaoLink kakaoLink = null;;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = null;
    ImageView go_kakao1, go_kakao2, go_kakao3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, null);
        try {
            kakaoLink = KakaoLink.getKakaoLink(getContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        }catch (KakaoParameterException e) {
            e.printStackTrace();
        }
        final String[] text = {""};
        final int width = 500;
        final int height = 500;
        go_kakao1 = (ImageView)view.findViewById(R.id.go_kakao1);
        go_kakao2 = (ImageView)view.findViewById(R.id.go_kakao2);
        go_kakao3 = (ImageView)view.findViewById(R.id.go_kakao3);
        final String stringImage = "http://i.imgur.com/rBc4Kaf.jpg";
        final String stringUrl = "market://details?id=com.azitcompany.azit";
        go_kakao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    text[0] = "친구야! 같이 공부하자!\n" +
                            "공부 좀 하는 얘들은 다 쓰는 어플리케이션이래!\n" +
                            "게다가 사용하는 학생이 많은 학교에는 새학기에 간식이 온대!!\n" +
                            "\n" +
                            "< 스케줄러, 스톱워치 à 학습 비교/분석 >\n" +
                            "교재 보관함을 이용해서, 클릭 한두번만으로 ‘스케줄 추가’ 완료!\n" +
                            "스톱워치를 써도 되고, 공부끝나고 기록 정리용으로 써도 되고!\n" +
                            "자동으로 내 공부를 분석해주고,\n" +
                            "경쟁자들과 비교해주는 아지트스터디!\n" +
                            "-과목별 공부비중, 학습 방식 분석, 학습 내용 분석, 일간/주간 학습 분석 등\n" +
                            "\n" +
                            "< Q&A, 학습 상담>\n" +
                            "명문대를 진학한 선배들이 질문도 받아주고, 상담도 해준대!\n" +
                            "\n" +
                            "같이 목표를 달성해보자!\n" +
                            "\n" +
                            "아지트스터디를 이용하는 학생이 가장 많은 학교에, 새학기를 응원하는 간식을 보내드립니다.";
                    kakaoTalkLinkMessageBuilder.addImage(stringImage, width, height)
                            .addText(text[0])
                            .addAppButton(stringUrl)
                            .build();

                    kakaoLink.sendMessage(Invitation.this.kakaoTalkLinkMessageBuilder, getContext());
                    kakaoLink = KakaoLink.getKakaoLink(getContext());
                    kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });

        go_kakao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    text[0] = "후배들에게는 피와 살이 되는 조언과 팁을!\n" +
                            "나에게는 피와 살이 되는 기프티콘을!\n" +
                            "\n" +
                            "너무나도 훌륭한 당신을 아지트스터디의 멘토로 모시고 싶습니다.\n" +
                            "\n" +
                            "나만 알고 있기엔 너무 아까운 공부 노하우와 꿀팁!\n" +
                            "\n" +
                            "후배들에게 나눠주고 마음도 풍성해지고, 주머니도 풍성해지세요!\n" +
                            "\n" +
                            "고등학생 예비 후배님들의 학습 질문/상담에 답변을 달아주시면\n" +
                            "질문 수에 비례해서 기프티콘을 드립니다.\n" +
                            "\n" +
                            "아지트스터디의 멘토로 활약해주세요! ";
                    kakaoTalkLinkMessageBuilder.addImage("http://i.imgur.com/ysiUSB6.png", width, height)
                            .addText(text[0])
                            .addAppButton(stringUrl)
                            .build();

                    kakaoLink.sendMessage(Invitation.this.kakaoTalkLinkMessageBuilder, getContext());
                    kakaoLink = KakaoLink.getKakaoLink(getContext());
                    kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });

        go_kakao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    text[0] = "학생들 학습관리 어플리케이션이에요\n" +
                            "상담하시고 아이들 독려/지도 하시는 데에 작은 도움이 되지 않을까 해서 추천해드립니다!\n" +
                            "\n" +
                            "< 학생 학습분석자료 >\n" +
                            "담당 학생을 추가하셔서\n" +
                            "학습 데이터 분석자료를 통해 학생 상담과 지도에 도움이 되실 거에요!\n" +
                            "-과목별 공부비중, 학습 방식 분석, 학습 내용 분석, 공부 목표량 달성 여부 등\n" +
                            "\n" +
                            "< 학생 성적분석자료 >\n" +
                            "학생 성적의 변화추이를 한눈에 언제 어디서든 파악하실 수 있습니다.\n" +
                            "\n" +
                            "< 개별/전체 공지 메시지 >\n" +
                            "개별/전체 메시지를 통해 언제 어디서든 학생의 상황에 맞는 격려와 조언을 해주세요!\n" +
                            "\n" +
                            "선생님을 응원합니다!\n" +
                            "\n" +
                            "곧, 컴퓨터로도 사용 가능하다고 하네요!";
                    kakaoTalkLinkMessageBuilder.addImage(stringImage, width, height)
                            .addText(text[0])
                            .addAppButton(stringUrl)
                            .build();

                    kakaoLink.sendMessage(Invitation.this.kakaoTalkLinkMessageBuilder, getContext());
                    kakaoLink = KakaoLink.getKakaoLink(getContext());
                    kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;

    }
}
