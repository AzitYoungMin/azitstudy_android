package com.trams.azit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.trams.azit.LogUtils;
import com.trams.azit.R;
import com.trams.azit.util.DateTimeUtils;
import com.trams.azit.util.PxUltils;

/**
 * Created by ADMIN on 1/12/2016.
 */
public class CircleView extends View {

    private static int strokeWidth;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint pProgressActive;
    private Paint pProgressBackground;

    private static final String textTitle = "나의 공부시간";
    //    private static final String textValue = "21:17:03";
    private static final String textDesTitle = "목표시간";
//    private static final String textDesValue = "70:27:05";

    private Paint pTextTitle;
    private Paint.FontMetrics mFontMetricsTextTitle;

    private Paint pTextValue;
    private Paint.FontMetrics mFontMetricsTextValue;

    private Paint pTextDes;
    private Paint.FontMetrics mFontMetricsTextDes;

    private long totalTime;
    private long progressTime;

    private String strTotalTime = "00:00:00";
    private String strProgressTime = "00:00:00";

    private static final String TAG = CircleView.class.getName();

    private void init() {
        strokeWidth = (int) PxUltils.convertDpToPixel(8, getContext());

        pProgressActive = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(ContextCompat.getColor(getContext(), R.color.circle_progress_bg));
                setStrokeWidth(strokeWidth);
                setAntiAlias(true);
            }
        };

        pProgressBackground = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(ContextCompat.getColor(getContext(), R.color.azit_green_bg));
                setStrokeWidth(strokeWidth);
                setAntiAlias(true);
            }
        };

        pTextTitle = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(ContextCompat.getColor(getContext(), R.color.circle_progress_bg));
                setStrokeWidth(5);
                setTextSize(PxUltils.convertDpToPixel(10, getContext()));
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        };
        mFontMetricsTextTitle = pTextTitle.getFontMetrics();

        pTextValue = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(ContextCompat.getColor(getContext(), R.color.azit_green_bg));
                setStyle(Style.FILL);
                setStrokeWidth(5);
                setTextSize(PxUltils.convertDpToPixel(22, getContext()));
                setAntiAlias(true);
            }
        };
        mFontMetricsTextValue = pTextValue.getFontMetrics();

        pTextDes = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setColor(ContextCompat.getColor(getContext(), R.color.circle_progress_des));
                setStrokeWidth(5);
                setTextSize(PxUltils.convertDpToPixel(10, getContext()));
                setStyle(Style.FILL);
                setAntiAlias(true);
            }
        };
        mFontMetricsTextDes = pTextDes.getFontMetrics();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LogUtils.d(TAG, "CircleView onDraw start");

        RectF rect = new RectF(strokeWidth, strokeWidth, canvas.getWidth() - strokeWidth, canvas.getHeight() - strokeWidth);

        // background full circle arc
        canvas.drawArc(rect, 0, 360, false, pProgressBackground);

        float progressAngle = ((float) progressTime / totalTime) * 360;
        canvas.drawArc(rect, 270, progressAngle, false, pProgressActive);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        //draw value center
        int textValueWidth = (int) Math.ceil(pTextValue.measureText(strProgressTime));
        int textValueHeight = (int) Math.ceil(Math.abs(mFontMetricsTextValue.ascent) + Math.abs(mFontMetricsTextValue.descent) + Math.abs(mFontMetricsTextValue.leading));

//        canvas.drawText(String.valueOf(progressTime), centerX - textValueWidth / 2, centerY, pTextValue);
        canvas.drawText(strProgressTime, centerX - textValueWidth / 2, centerY, pTextValue);

        //draw title
        int textTitleWidth = (int) Math.ceil(pTextTitle.measureText(textTitle));
        int textTitleHeight = (int) Math.ceil(Math.abs(mFontMetricsTextTitle.ascent) + Math.abs(mFontMetricsTextTitle.descent) + Math.abs(mFontMetricsTextTitle.leading));

        int xTitle = centerX - textTitleWidth / 2;
        int yTitle = centerY - textValueHeight / 2 - textTitleHeight / 2;

        canvas.drawText(textTitle, xTitle, yTitle, pTextTitle);

        //draw title description
        int textTitleDesWidth = (int) Math.ceil(pTextDes.measureText(textDesTitle));
        int textTitleDesHeight = (int) Math.ceil(Math.abs(mFontMetricsTextDes.ascent) + Math.abs(mFontMetricsTextDes.descent) + Math.abs(mFontMetricsTextDes.leading));

        int xDesTitle = centerX - textTitleDesWidth / 2;
        int yDesTitle = (int) (centerY + textValueHeight / 2 + textTitleDesHeight / 2 + PxUltils.convertDpToPixel(20, getContext()));

        canvas.drawText(textDesTitle, xDesTitle, yDesTitle, pTextDes);

        //draw title value
        int textDesValueWidth = (int) Math.ceil(pTextDes.measureText(strTotalTime));
        int textDesValueHeight = (int) Math.ceil(Math.abs(mFontMetricsTextDes.ascent) + Math.abs(mFontMetricsTextDes.descent) + Math.abs(mFontMetricsTextDes.leading));

        int xDesValue = centerX - textDesValueWidth / 2;
        int yDesValue = (int) (centerY + textValueHeight / 2 + textTitleDesHeight + PxUltils.convertDpToPixel(30, getContext()));

//        canvas.drawText(String.valueOf(totalTime), xDesValue, yDesValue, pTextDes);
        canvas.drawText(strTotalTime, xDesValue, yDesValue, pTextDes);

    }


    public void updateView(String _strTotalTime, String _strProgressTIme) {

        this.strTotalTime = _strTotalTime;
        this.strProgressTime = _strProgressTIme;

        this.totalTime = DateTimeUtils.getSecondsFromDate(strTotalTime);
        this.progressTime = DateTimeUtils.getSecondsFromDate(strProgressTime);

        invalidate();
    }

//    public void updateView(long _totalTime, long _progressTIme) {
//        this.totalTime = _totalTime;
//        this.progressTime = _progressTIme;
//
//        invalidate();
//    }

//    private String getStrDate(long dateLong) {
//        Date date = new Date(dateLong);
//        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        String dateFormatted = formatter.format(date);
//        return dateFormatted;
//    }
}
