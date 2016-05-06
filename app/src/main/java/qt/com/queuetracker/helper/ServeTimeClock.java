package qt.com.queuetracker.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

import qt.com.queuetracker.R;

/**
 * Created by vinove on 11/4/16.
 */
public class ServeTimeClock extends View {


    private Bitmap bitmap;
    private Drawable mDial;
    private int mDialWidth;
    private int mDialHeight;
    private boolean flag = false;
    Context mContext;
    Drawable mMinuteHand;
    int w1, h1;
    Float angle;
    String minimumDuration;

    Calendar mCalendar = Calendar.getInstance();
    int rotation = 0;

    public ServeTimeClock(Context context) {
        super(context);

    }

    public ServeTimeClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0,"");
    }

    public ServeTimeClock(Context context, AttributeSet attrs,
                          int defStyle, String mininumduration) {
        super(context, attrs, defStyle);

        Resources r = context.getResources();
        mContext = context;
        this.minimumDuration=mininumduration;

        mDial = ContextCompat.getDrawable(mContext, R.drawable.black_whiteclock);
        mMinuteHand = ContextCompat.getDrawable(mContext, R.drawable.black_2);
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        float hScale = 1.5f;
        float vScale = 1.5f;
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }
        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    // get size of layout
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w1 = w;
        this.h1 = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
        int availableWidth = this.getMeasuredWidth();
        int availableHeight = this.getMeasuredHeight();
//

        int x = w1 / 2;
        int y = h1 / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        // Create Canvas
        boolean scaled = false;
        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        dial.draw(canvas);
        canvas.save();


        // Draw Arc on canvas
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#049D01"));
        mPaint.setStyle(Paint.Style.FILL); //fill the background with green color
        mPaint.setColor(Color.parseColor("#049D01"));
        RectF rectf = new RectF(x - (w / 2 - 15), y - (h / 2 - 15), x + (w / 2 - 15), y + (h / 2 - 15));
        canvas.drawArc(rectf, -90, setAngle(), true, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mPaint.setTextSize(60);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextScaleX(1.5f);
        mPaint.setAlpha(250);
        mPaint.setAntiAlias(true);
        canvas.drawText(minimumDuration + "min", x - w / 3, y, mPaint);
//        canvas.drawText(VerifyScreen.minutes_str,20,40,x-w/2,y-h/2,mPaint);

    }

    Float setAngle() {
        if (Integer.parseInt(minimumDuration) <= 5) {

            angle = 30f;
        } else if (Integer.parseInt(minimumDuration) <= 10) {
            angle = 60f;

        } else if (Integer.parseInt(minimumDuration) <= 15) {
            angle = 90f;

        } else if (Integer.parseInt(minimumDuration) <= 20) {
            angle = 120f;


        } else if (Integer.parseInt(minimumDuration) <= 25) {
            angle = 150f;


        } else if (Integer.parseInt(minimumDuration) <= 30) {
            angle = 180f;


        } else if (Integer.parseInt(minimumDuration) <= 35) {
            angle = 210f;


        } else if (Integer.parseInt(minimumDuration) <= 40) {
            angle = 240f;


        } else if (Integer.parseInt(minimumDuration) <= 45) {
            angle = 270f;


        } else if (Integer.parseInt(minimumDuration) <= 50) {
            angle = 300f;


        } else if (Integer.parseInt(minimumDuration) <= 55) {
            angle = 330f;


        } else if (Integer.parseInt(minimumDuration) <= 60) {
            angle = 360f;


        }
        return angle;

    }


}
