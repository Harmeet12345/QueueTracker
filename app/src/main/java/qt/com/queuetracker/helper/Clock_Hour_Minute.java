package qt.com.queuetracker.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import java.util.TimeZone;

import qt.com.queuetracker.R;
import qt.com.queuetracker.ui.VerifyScreen;

/**
 * Created by vinove on 5/4/16.
 */
public class Clock_Hour_Minute extends View {

        private Drawable mHourHand;
        private Drawable mMinuteHand;
        private Drawable mDial;

        private int mDialWidth;
        private int mDialHeight;




        private float mMinutes;
        private float mHour;
        private boolean mChanged;

        public Clock_Hour_Minute(Context context) {
            this(context, null);
        }

        public Clock_Hour_Minute(Context context, AttributeSet attrs) {
            super(context, attrs);




            if (mDial == null) {
                mDial = ContextCompat.getDrawable(context,R.drawable.black_whiteclock);
            }


            if (mHourHand == null) {
                mHourHand = ContextCompat.getDrawable(context,R.drawable.black_3);
            }


            if (mMinuteHand == null) {
                mMinuteHand = ContextCompat.getDrawable(context,R.drawable.black_3);
            }

            mDialWidth = mDial.getIntrinsicWidth();
            mDialHeight = mDial.getIntrinsicHeight();
        }










        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

            float hScale = 1.0f;
            float vScale = 1.0f;

            if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
                hScale = (float) widthSize / (float) mDialWidth;
            }

            if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
                vScale = (float )heightSize / (float) mDialHeight;
            }

            float scale = Math.min(hScale, vScale)/1.1f;

            setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                    resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mChanged = true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            boolean changed = mChanged;
            if (changed) {
                mChanged = false;
            }

            int hour = Integer.parseInt(VerifyScreen.hrs_str);
            int minute = Integer.parseInt(VerifyScreen.minutes_str);
            int second = 0;

            mMinutes = minute+second/ 60.0f;
            mHour = hour + mMinutes / 60.0f;
            mChanged = true;

            int availableWidth = getMeasuredWidth() ;
            int availableHeight = getMeasuredHeight();

            int x = availableWidth / 2;
            int y = availableHeight / 2;

            final Drawable dial = mDial;
            int w = dial.getIntrinsicWidth();
            int h = dial.getIntrinsicHeight();

            boolean scaled = false;

            if (availableWidth < w || availableHeight < h) {
                scaled = true;
                float scale = Math.min((float) availableWidth / (float) w,
                        (float) availableHeight / (float) h);
                canvas.save();
                canvas.scale(scale, scale, x, y);
            }

            if (changed) {
                dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
            }
            dial.draw(canvas);

            canvas.save();
            canvas.rotate(mHour / 12.0f * 360.0f, x, y);
            final Drawable hourHand = mHourHand;
            if (changed) {
                w = hourHand.getIntrinsicWidth();
                h = hourHand.getIntrinsicHeight();
                hourHand.setBounds(x - (w / 3), (int) (y - (h /3.5f)), x + (w / 3), (int) (y + (h / 3.5f)));
            }
            hourHand.draw(canvas);
            canvas.restore();

            canvas.save();
            canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

            final Drawable minuteHand = mMinuteHand;
            if (changed) {
                w = minuteHand.getIntrinsicWidth();
                h = minuteHand.getIntrinsicHeight();
                minuteHand.setBounds(x -( w / 2), y - (h / 2)+40, x + (w / 2),  y + (h / 2));
            }
            minuteHand.draw(canvas);
            canvas.restore();

            if (scaled) {
                canvas.restore();
            }
        }

        private void onTimeChanged() {




//            updateContentDescription(mCalendar);
        }





    public  float gettimeinfloat(String time)
    {

        double mHour = Double.parseDouble(time.substring(0, 2));
        double minutes= Double.parseDouble(time.substring(3, 5));
        double k=minutes/60;
        k = mHour;
        return (float)(k);

    }
    }
//}