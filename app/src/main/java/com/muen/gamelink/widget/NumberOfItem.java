package com.muen.gamelink.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muen.gamelink.R;
import com.muen.gamelink.util.PxUtil;


public class NumberOfItem extends RelativeLayout {
    //存储物品的图片描述
    private ImageView show_img = null;
    //物品的图片
    private int srcResource;
    //物品图片的背景样式
    private int imgResource;

    //存储物品的数量描述
    private TextView show_count = null;
    //物品数量的背景样式
    private int countResource;
    //存储物品的数量
    private int count = 0;
    //绘制物品数量的画笔
    private Paint paint;

    /**
     * 构造方法 Java代码创建的时候进入
     * @param context
     */
    public NumberOfItem(Context context, int srcResource, int imgResource, int countResource) {
        super(context);

        init();

        this.srcResource = srcResource;
        this.imgResource = imgResource;
        this.countResource = countResource;

        show_img.setImageResource(srcResource);
        show_img.setBackgroundResource(imgResource);
        show_count.setBackgroundResource(countResource);
    }

    /**
     * 构造方法 Xml代码创建的时候进入
     * @param context
     * @param attrs
     */
    public NumberOfItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

        //解析自定义属性
        if (attrs != null) {
            //获取自定义属性值的集合
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberOfItem);

            //解析单个资源
            srcResource = typedArray.getResourceId(
                    R.styleable.NumberOfItem_srcResource,
                    R.drawable.link_prop_fight
            );
            show_img.setImageResource(srcResource);
            imgResource = typedArray.getResourceId(
                    R.styleable.NumberOfItem_imgResource,
                    R.drawable.img_shape
            );
            show_img.setBackgroundResource(imgResource);
            countResource = typedArray.getResourceId(
                    R.styleable.NumberOfItem_countResource,
                    R.drawable.count_shape
            );
            show_count.setBackgroundResource(countResource);

            //释放资源
            typedArray.recycle();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        //画笔操作
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(PxUtil.spToPx(14, getContext()));
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.WHITE);

        //加载视图
        View inflate = View.inflate(getContext(), R.layout.number_item, this);
        show_img = inflate.findViewById(R.id.show_img);
        show_count = inflate.findViewById(R.id.show_count);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        //获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //设置wrap_content的默认宽 / 高值
        //默认宽/高的设定并无固定依据,根据需要灵活设置
        int mWidth = PxUtil.dpToPx(45, getContext());
        int mHeight = PxUtil.dpToPx(45, getContext());

        //当布局参数设置为wrap_content时，设置默认值
        //宽,高任意一个布局参数为= wrap_content时，都设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {

            //宽度，高度都设置wrap_content
            setMeasuredDimension(mWidth, mHeight);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {

            //宽度设置wrap_content
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {

            //高度设置wrap_content
            setMeasuredDimension(widthSize, mHeight);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        //需要绘制的文本
        String text = String.valueOf(count);

        //计算文本宽度
        float text_width = paint.measureText(text);

        //获取字体fontMetrics
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        //计算文本高度
        float text_height = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;

        //获取绘制的位置
        int pivotX = show_count.getLeft() + (show_count.getRight() - show_count.getLeft()) / 2;
        int pivotY = show_count.getTop() + (show_count.getBottom() - show_count.getTop()) / 2;

        //绘制需要的字体
        canvas.drawText(text,
                pivotX - text_width / 2,
                pivotY + text_height,
                paint);
    }

    //setter，getter方法
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;

        //刷新界面
        invalidate();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public ImageView getShow_img() {
        return show_img;
    }

    public void setShow_img(ImageView show_img) {
        this.show_img = show_img;
    }

    public int getSrcResource() {
        return srcResource;
    }

    public void setSrcResource(int srcResource) {
        this.srcResource = srcResource;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public TextView getShow_count() {
        return show_count;
    }

    public void setShow_count(TextView show_count) {
        this.show_count = show_count;
    }

    public int getCountResource() {
        return countResource;
    }

    public void setCountResource(int countResource) {
        this.countResource = countResource;
    }
}
