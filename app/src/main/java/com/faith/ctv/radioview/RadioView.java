package com.faith.ctv.radioview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hongbing on 2017/3/6.
 * 自定义单选控件
 */
public class RadioView extends View implements View.OnClickListener{

    private static final String OUT_COLOR = "#F6574F"; // 外圆颜色
    private static final String INNER_COLOR = "#F6574F"; // 内圆颜色
    private static final String TXT_COLOR = "#F6574F";// 文本颜色

    /**
     * 内容对齐方式
     */
    public interface Gravity{
        int GRAVITY_LEFT = 0x03;
        int GRAVITY_CENTER = 0x04;
        int GRAVITY_RIGHT = 0x05;
    }

    private static final String TAG = "RadioView";
    private Context mContext;
    private Paint mOutPaint = new Paint();
    private Paint mInnerPaint = new Paint();
    private TextPaint mTxtPaint = new TextPaint();
    private Rect mRect = new Rect();
    private int mScreenW;
    private int mOutRaduis; // 外圆半径
    private int mInnerRaduis; // 内圆半径
    private String mStr = "我的中国人"; // 绘制的文本内容
    private int mTxtToRadionDisc = 0; // 文本和圆形view的水平间距
    private int mTxtSize; // 文本字体大小
    private int mTxtColor; // 文本颜色值
    private int mOutColor; // 外圆颜色值
    private int mInnerColor; // 内圆颜色值
    private int mStrokeW; // 外圆圆环宽度
    private boolean mIsSel; // 默认是否选中；true：选中；false：未选中
    private int mGravity; // 对齐方式
    private boolean mWidthIsFill; // 控件的宽度是否是充满

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public RadioView(Context context) {
        this(context,null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public RadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadionView);
        mStr = a.getString(R.styleable.RadionView_txt);
        mTxtToRadionDisc = a.getDimensionPixelSize(R.styleable.RadionView_txt_distance, dp2px(context, 6));
        mTxtSize = a.getDimensionPixelSize(R.styleable.RadionView_txt_size, sp2px(context, 13));
        mTxtColor = a.getColor(R.styleable.RadionView_txt_color, Color.parseColor(TXT_COLOR));
        mOutRaduis = a.getDimensionPixelOffset(R.styleable.RadionView_out_raduis,dp2px(context,10));
        mInnerRaduis = a.getDimensionPixelOffset(R.styleable.RadionView_inner_raduis,dp2px(context,5));
        mOutColor = a.getColor(R.styleable.RadionView_out_color, Color.parseColor(OUT_COLOR));
        mInnerColor = a.getColor(R.styleable.RadionView_inner_color, Color.parseColor(INNER_COLOR));
        mStrokeW = a.getDimensionPixelOffset(R.styleable.RadionView_out_strokeW,dp2px(context,2));
        mIsSel = a.getBoolean(R.styleable.RadionView_is_sel, Boolean.FALSE);
        mGravity = a.getInt(R.styleable.RadionView_gravity, RadioView.Gravity.GRAVITY_LEFT);
        a.recycle();
        init();
    }

    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    private void init(){
        mScreenW = getResources().getDisplayMetrics().widthPixels;

        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeWidth(mStrokeW);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setColor(mOutColor);

        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStyle(Paint.Style.FILL);
        mInnerPaint.setColor(mInnerColor);

        if(!TextUtils.isEmpty(mStr)){ // 有文本
            mTxtPaint.setTextSize(mTxtSize);
            mTxtPaint.setColor(mTxtColor);
            mTxtPaint.setAntiAlias(true);
            mTxtPaint.setTextAlign(Paint.Align.LEFT);
            mTxtPaint.getTextBounds(mStr, 0, mStr.length(), mRect);
        }else{
            mOutRaduis = 0; // 只有有文本的时候，这个值才会生效
        }


        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(true,widthMeasureSpec),measure(false,heightMeasureSpec));
    }

    private int measure(boolean isWidth,int measureSpec) {
        mWidthIsFill = false;
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
            mWidthIsFill = true;
        } else {
            if (isWidth) {
                result = mRect.width() + mOutRaduis * 2 + mTxtToRadionDisc + getPaddingLeft() + getPaddingRight() + dp2px(mContext,4);
            } else {
                result = mOutRaduis * 2 + getPaddingTop() + getPaddingBottom() + dp2px(mContext,4);
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mGravity) {
            case RadioView.Gravity.GRAVITY_LEFT:
                canvas.translate(mOutRaduis + dp2px(mContext, 1), 0);
                break;
            case RadioView.Gravity.GRAVITY_CENTER:
                canvas.translate(getWidth() / 2 - (mOutRaduis + mTxtToRadionDisc + mRect.width()) / 2 + dp2px(mContext, 1), 0);
                break;
            case RadioView.Gravity.GRAVITY_RIGHT:
                int vW = mOutRaduis + mRect.width() + mTxtToRadionDisc;
                canvas.translate(getWidth() - vW, 0);
                break;
        }
        // 绘制文本，x = 左填充值 + 大圆的半径 + 水平间距, y = 上填充值 + 半径 + 文本高度的一半，因为文本绘制是从基线(baseline)开始绘制的
        canvas.drawText(mStr, getPaddingLeft() + mOutRaduis + mTxtToRadionDisc, getPaddingTop() + mOutRaduis + mRect.height() / 2, mTxtPaint);
        // 绘制外圆
        canvas.drawCircle(getPaddingLeft(), getHeight() / 2, mOutRaduis, mOutPaint);
        // 绘制内圆
        if (mIsSel) {
            canvas.drawCircle(getPaddingLeft(), getHeight() / 2, mInnerRaduis, mInnerPaint);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, "点击了单选按钮...");
        mIsSel = !mIsSel;
        setCheck(mIsSel);
        if (mListener != null)
            mListener.clickRadion(v,mIsSel);
    }


    public void setText(String text) {
        mStr = text;
        invalidate();
    }

    public String getText(){
        return mStr;
    }

    public void setCheck(boolean isCheck){
        mIsSel = isCheck;
        invalidate();
    }

    public boolean isCheck(){
        return mIsSel;
    }

    public void setGravity(int gravity){
        mGravity = gravity;
        invalidate();
    }

    public int getGravity(){
        return mGravity;
    }

    private OnRadionViewListener mListener;
    public void setOnRadionViewListener(OnRadionViewListener listener) {
        mListener = listener;
    }
    public interface OnRadionViewListener {
        void clickRadion(View v,boolean isSel);
    }
}
