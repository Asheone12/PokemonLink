package com.muen.gamelink.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;


import com.muen.gamelink.R;
import com.muen.gamelink.util.PxUtil;

import java.text.DecimalFormat;

public class CircleProgress extends RelativeLayout {
    //进度背景的画笔
    private Paint progress_bg_paint;

    //进度的画笔
    private Paint progress_paint;

    //进度值的画笔
    private Paint progress_value_paint;

    //视图的布局
    private RelativeLayout layout;

    //进度值
    private float progress = 0;

    //总的进度值
    private float total_progress = 100;

    //开始的角度
    private int startAngle = 0;

    //结束的角度
    private int endAngle = 360;

    //字体的大小
    private int textSize = 14;

    //字体的颜色
    private String textColor = "#ffffff";

    //绘制字体的左间距
    private int textLeftPadding = 0;

    //绘制字体的上间距
    private int textTopPadding = 0;

    /**
     * 构造方法 Java代码创建进入
     * @param context
     */
    public CircleProgress(Context context, int startAngle, int endAngle, int total_progress) {
        super(context);

        //保存值
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.total_progress = total_progress;

        //初始化
        init();
    }

    /**
     * 构造方法 Xml代码创建进入
     * @param context
     * @param attrs
     */
    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //解析属性
        if (attrs != null){
            //1.获取所有自定义属性值的集合
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);

            //2.依次解析
            progress = typedArray.getFloat(
                    R.styleable.CircleProgress_progress,
                    progress
            );
            setProgress(progress);
            total_progress = typedArray.getFloat(
                    R.styleable.CircleProgress_total_progress,
                    total_progress
            );
            startAngle = typedArray.getInteger(
                    R.styleable.CircleProgress_startAngle,
                    startAngle
            );
            endAngle = typedArray.getInteger(
                    R.styleable.CircleProgress_endAngle,
                    endAngle
            );
            textSize = typedArray.getInteger(
                    R.styleable.CircleProgress_textSize,
                    textSize
            );
            String temp_textColor = typedArray.getString(
                    R.styleable.CircleProgress_textColor
            );
            if (temp_textColor != null) {
                textColor = temp_textColor;
            }
            textLeftPadding = typedArray.getInteger(
                    R.styleable.CircleProgress_textLeftPadding,
                    0
            );
            textTopPadding = typedArray.getInteger(
                    R.styleable.CircleProgress_textTopPadding,
                    0
            );

            //3.释放资源
            typedArray.recycle();
        }

        //初始化
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        //进度的背景画笔
        progress_bg_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progress_bg_paint.setColor(Color.rgb(153,223,159));
        progress_bg_paint.setStrokeWidth(PxUtil.dpToPx(10,getContext()));
        progress_bg_paint.setStyle(Paint.Style.STROKE);
        progress_bg_paint.setStrokeJoin(Paint.Join.ROUND);
        progress_bg_paint.setStrokeCap(Paint.Cap.ROUND);

        //进度的画笔
        progress_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progress_paint.setColor(Color.argb(180,34,52,68));
        progress_paint.setStrokeWidth(PxUtil.spToPx(10,getContext()));
        progress_paint.setStyle(Paint.Style.STROKE);
        progress_paint.setStrokeJoin(Paint.Join.ROUND);
        progress_paint.setStrokeCap(Paint.Cap.ROUND);

        //进度值的画笔
        progress_value_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progress_value_paint.setColor(Color.parseColor(textColor));
        progress_value_paint.setTextSize(PxUtil.dpToPx(textSize,getContext()));
        progress_value_paint.setTypeface(
                Typeface.createFromAsset(
                        getContext().getAssets(),
                        "fonts/造字工房乐真体.ttf"
                )
        );

        //加载视图
        View inflate = View.inflate(getContext(), R.layout.circle_progress, this);
        layout = inflate.findViewById(R.id.root);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        //1.确定绘制的矩形区域
        @SuppressLint("DrawAllocation") RectF rectF = new RectF(
                layout.getLeft(),
                layout.getTop(),
                layout.getRight(),
                layout.getBottom()
        );
        Log.d("CircleProgress",rectF.toString());

        //2.绘制背景
        canvas.drawArc(
                rectF,0,
                360,
                false,
                progress_bg_paint
        );

        //3.绘制进度
        canvas.drawArc(
                rectF,
                startAngle,
                (endAngle-startAngle)*((total_progress-progress)/total_progress),
                false,
                progress_paint
        );

        //4.绘制进度值
        String text = new DecimalFormat("##0.0").format(progress)+"秒";
        //4.1计算文本宽度
        float text_width = progress_value_paint.measureText(text);
        //4.2获取字体fontMetrics
        Paint.FontMetrics fontMetrics = progress_value_paint.getFontMetrics();
        //4.3计算文本高度
        float text_height = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        //4.4绘制需要的字体
        canvas.drawText(
                text,
                PxUtil.dpToPx(textLeftPadding,getContext())+getPivotX()-text_width/2,
                PxUtil.dpToPx(textTopPadding,getContext())+getPivotY()+text_height,
                progress_value_paint
        );
    }

    //setter,getter
    public Paint getProgress_bg_paint() {
        return progress_bg_paint;
    }

    public void setProgress_bg_paint(Paint progress_bg_paint) {
        this.progress_bg_paint = progress_bg_paint;
    }

    public Paint getProgress_paint() {
        return progress_paint;
    }

    public void setProgress_paint(Paint progress_paint) {
        this.progress_paint = progress_paint;
    }

    public Paint getProgress_value_paint() {
        return progress_value_paint;
    }

    public void setProgress_value_paint(Paint progress_value_paint) {
        this.progress_value_paint = progress_value_paint;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;

        //刷新，重绘
        invalidate();
    }

    public float getTotal_progress() {
        return total_progress;
    }

    public void setTotal_progress(float total_progress) {
        this.total_progress = total_progress;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public int getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(int endAngle) {
        this.endAngle = endAngle;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getTextLeftPadding() {
        return textLeftPadding;
    }

    public void setTextLeftPadding(int textLeftPadding) {
        this.textLeftPadding = textLeftPadding;
    }
}