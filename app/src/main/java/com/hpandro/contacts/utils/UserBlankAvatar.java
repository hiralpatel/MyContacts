package com.hpandro.contacts.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hpandro.contacts.R;

/**
 * Created by hpAndro on 12/8/2016.
 */
public class UserBlankAvatar extends RelativeLayout {

    int bgcolor;
    String name;
    Paint bgPaint;
    TextView nameView;
    Context mContext;
    LayoutParams textParams;
    boolean isCircle = true;

    public UserBlankAvatar(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    public UserBlankAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public UserBlankAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        setWillNotDraw(false);
        name = "";
        nameView = new TextView(mContext);
        textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(nameView, textParams);
        nameView.setTextColor(getResources().getColor(R.color.whiteColor));

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    public void setColor(int color) {
        this.bgcolor = color;
        bgPaint.setColor(bgcolor);
        invalidate();
    }

    public void setName(String names) {
        this.name = names;
        nameView.setText(name);
    }

    public void setIsCircle(boolean a) {
        isCircle = a;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mRadius = getWidth() / 2;
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, px2dip(mRadius));
        if (isCircle)
            canvas.drawCircle(mRadius, mRadius, mRadius, bgPaint);
        else
            canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
    }

    public int px2dip(float pxValue) {
        return (int) (pxValue / getResources().getDisplayMetrics().density + 0.5f);
    }
}
