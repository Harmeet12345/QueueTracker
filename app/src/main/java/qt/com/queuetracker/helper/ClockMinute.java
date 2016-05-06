package qt.com.queuetracker.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import qt.com.queuetracker.Model.AppointmentTime;
import qt.com.queuetracker.R;
import qt.com.queuetracker.ui.SelectMinute;
import qt.com.queuetracker.ui.SelectTime;

/**
 * Created by vinove on 30/03/16.
 */
public class ClockMinute extends View {

    private static final String TAG = ClockHour.class.getSimpleName();

    // drawing tools
    private RectF rimRect;
    RectF rimRectHR;
    private Paint rimPaint;
    private Paint rimCirclePaint;

    private RectF faceRect, faceRectHR;
    private Paint facePaint;

    private Paint scalePaint, scalePaint_Red, scalePaintHR;
    private RectF scaleRect, scaleRectHR;
    Bitmap bitmap;
    Drawable watchface;
    Drawable mMinuteHand;
    private float mMinutes;
    int rotation = 0;
    private boolean flag = false;
    Context mContext;
    private Paint backgroundPaint;
    // end drawing tools

    private Bitmap background; // holds the cached static part

    // scale configuration
    private static final int totalNicks = 60;
    int totalNicksHR = 12;
    int totalNicksText[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    float degreesPerNicksHR = 360.0f / totalNicksHR;
    private static final float degreesPerNick = 360.0f / totalNicks;

    private static final int centerDegree = 180; // the one in the top center (12 o'clock)
    private static final int minDegrees = 0;
    private static final int maxDegrees = 360;
    private Boolean isAmOrPmSelected;
    ArrayList<AppointmentTime> appointment_list = new ArrayList<>();


    public ClockMinute(Context context, AttributeSet attrs, ArrayList<AppointmentTime> appointment_time,Boolean isAmOrPmSelected) {
        super(context, attrs);
        this.mContext = context;
        this.appointment_list = appointment_time;
        this.isAmOrPmSelected = isAmOrPmSelected;

        initDrawingTools();
        Resources r = context.getResources();
        mMinuteHand = ContextCompat.getDrawable(context, R.drawable.black_2);

        if (!isAmOrPmSelected){
//            for (int i = 0; i < appointment_list.get(0).getTime_slot_list().size(); i++) {
//                appointment_list.get(0).getTime_slot_list().get(i).getBookslot_list();
//                String isFull = appointment_list.get(0).getTime_slot_list().get(i).getIsFull();
//
//            }
        }



    }

    private void initDrawingTools() {
        rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

        rimRectHR = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

        // the linear gradient is a bit skewed for realism
        rimPaint = new Paint();
        rimPaint.setColor(Color.parseColor("#353535"));
        rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setStyle(Paint.Style.STROKE);
        rimCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        rimCirclePaint.setStrokeWidth(0.03f);

        float rimSize = 0.02f;
        faceRect = new RectF();
        faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize,
                rimRect.right - rimSize, rimRect.bottom - rimSize);

        faceRectHR = new RectF();
        faceRectHR.set(rimRectHR.left + rimSize, rimRectHR.top + rimSize,
                rimRectHR.right - rimSize, rimRectHR.bottom - rimSize);

        facePaint = new Paint();
        facePaint.setColor(Color.parseColor("#FFFFFF"));

        scalePaint = new Paint();
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setColor(Color.parseColor("#049D01")); // Green
        scalePaint.setStrokeWidth(0.033f);
        scalePaint.setAntiAlias(true);

        scalePaint_Red = new Paint();
        scalePaint_Red.setStyle(Paint.Style.STROKE);
        scalePaint_Red.setColor(Color.parseColor("#FB3E1B")); //Red
        scalePaint_Red.setStrokeWidth(0.033f);
        scalePaint_Red.setAntiAlias(true);

        scalePaintHR = new Paint();
        scalePaintHR.setStyle(Paint.Style.STROKE);
        scalePaintHR.setColor(Color.parseColor("#353535")); // Green
        scalePaintHR.setStrokeWidth(0.015f);
        scalePaintHR.setAntiAlias(true);


        scalePaintHR.setTextSize(0.5f);
        scalePaintHR.setTypeface(Typeface.SANS_SERIF);
        scalePaintHR.setTextScaleX(0.8f);
        scalePaintHR.setTextAlign(Paint.Align.CENTER);

        float scalePosition = 0.005f;
        float scalePositionHR = 0.03f;

        scaleRect = new RectF();
        scaleRect.set(0, faceRect.top + scalePosition, 0, 0);

        scaleRectHR = new RectF();
        scaleRectHR.set(0, faceRectHR.top + scalePositionHR, 0, 0);

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
        Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = (int) (Math.min(chosenWidth, chosenHeight) / 1.4f);

        setMeasuredDimension(chosenDimension, chosenDimension);
    }

    private int chooseDimension(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredSize();
        }
    }

    // in case there is no size specified
    private int getPreferredSize() {
        return 300;
    }

    private void drawRim(Canvas canvas) {
        // first, draw the metallic body
        canvas.drawOval(rimRect, rimPaint);
    }

    private void drawFace(Canvas canvas) {
        canvas.drawOval(faceRect, facePaint);
        // draw the inner rim circle
        canvas.drawOval(faceRect, rimCirclePaint);
    }

    private void drawScale(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        for (int i = 0; i < totalNicks; ++i) {
            float y1 = scaleRect.top;
            float y2 = y1 - 0.020f;

            canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

            canvas.rotate(degreesPerNick, 0.5f, 0.5f);
        }
        canvas.restore();
    }


    private void drawScaleHR(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        for (int i = 0; i <= totalNicksHR; i++) {
            float y1 = scaleRectHR.top;
            float y2 = y1 - 0.050f;

            canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaintHR);

            if (i % 5 == 0) {
                int value = nickToDegree(i);

                System.out.println("pankaj : " + i + " :: " + value);

                if (value >= minDegrees && value <= maxDegrees) {
                    String valueString = Integer.toString(value);
//                    canvas.drawText(valueString, 0.5f, y2 - 0.015f, scalePaint);
//                    canvas.drawText(valueString, 0.001f, 0.151f, scalePaintHR);
                }
            }

            canvas.rotate(degreesPerNicksHR, 0.5f, 0.5f);
        }
        canvas.restore();
    }

    private void drawScaleText(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        for (int i = 0; i <= totalNicksText.length; i++) {
            float y1 = scaleRectHR.top;
            float y2 = y1 - 0.050f;

            canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaintHR);

            if (i % 5 == 0) {
                int value = nickToDegree(i);

                System.out.println("pankaj : " + i + " :: " + value);

                if (value >= minDegrees && value <= maxDegrees) {
                    String valueString = Integer.toString(value);
//                    canvas.drawText(valueString, 0.5f, y2 - 0.015f, scalePaint);
//                    canvas.drawText(valueString, 0.001f, 0.151f, scalePaintHR);
                }
            }

            canvas.rotate(degreesPerNicksHR, 0.5f, 0.5f);
        }
        canvas.restore();
    }

    private int nickToDegree(int nick) {
        int rawDegree = ((nick < totalNicks / 2) ? nick : (nick - totalNicks)) * 2;
        int shiftedDegree = rawDegree + centerDegree;
        return shiftedDegree;
    }

    private void drawBackground(Canvas canvas) {
        if (background == null) {
            Log.w(TAG, "Background not created");
        } else {
            canvas.drawBitmap(background, 0, 0, backgroundPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        drawBackground(canvas);
        watchface = new BitmapDrawable(getResources(), background);

        int availableWidth = watchface.getIntrinsicWidth();
        int availableHeight = watchface.getIntrinsicHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        int w = watchface.getIntrinsicWidth();
        int h = watchface.getIntrinsicHeight();
        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth,
                    (float) availableHeight);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        watchface.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        watchface.draw(canvas);

        canvas.save();

//        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
//
        float r;
        if (flag == false) {
            // Get current time
            float result = gettimeinfloat("00:00");
//      set rotation of current time
            r = (result * 360.0f) / 60.0f;

            flag = true;
        } else {
            r = rotation;
        }
//
//
        bitmap = ((BitmapDrawable) mMinuteHand).getBitmap();
//
//        canvas.save();
        Matrix matrix = new Matrix();
        //Set canvas centre
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2 + 40);
        // set rotation of image

        matrix.postRotate(r);
        matrix.postTranslate(x, y);
        canvas.drawBitmap(bitmap, matrix, null);
        if (scaled) {
            canvas.restore();
        }
//

        canvas.save();
        float scale = (float) getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "Size changed to " + w + "x" + h);

        regenerateBackground();
    }

    private void regenerateBackground() {
        // free the old bitmap
        if (background != null) {
            background.recycle();
        }

        background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(background);
        float scale = (float) getWidth();
        backgroundCanvas.scale(scale, scale);

        drawRim(backgroundCanvas);

        drawFace(backgroundCanvas);

        drawScale(backgroundCanvas);

        drawScaleHR(backgroundCanvas);
        drawScaleText(backgroundCanvas);

    }

    public boolean onTouchEvent(MotionEvent e) {

        // get motion cordinate
        float x = e.getX();
        float y = e.getY();

        //Update rotation
        updateRotation(x, y);
        invalidate();
        return true;
    }

    private void updateRotation(float x, float y) {

        // Get rotation angle
        final Drawable dialer = watchface;
        double r = Math.atan2(x - dialer.getIntrinsicWidth() / 2, dialer.getIntrinsicHeight() / 2 - y);
        rotation = (int) Math.toDegrees(r);
        if (rotation < 0) {
            rotation += 360;
        }

        System.out.println("My Rotaion" + " " + rotation);
        // Get time from ratation angle
        String minutes = SelectMinute.hourtime.substring(0, 2), hrs = null;
        float hour = rotation * 60.0f / 360.0f;
        int hours = (int) hour;
//

        if (hours <= 9) {
            hrs = "0" + Integer.toString(hours);
        } else {
            hrs = Integer.toString(hours);
        }
//
        String time = hrs;
        SelectMinute.set_minute_tv.setText(time);
//

    }

    public float gettimeinfloat(String time) {

        double mHour = Double.parseDouble(time.substring(0, 2));
//
        return (float) (mHour);

    }
}
