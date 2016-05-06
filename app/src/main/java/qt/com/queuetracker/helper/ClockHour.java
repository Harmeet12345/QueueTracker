package qt.com.queuetracker.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
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
import qt.com.queuetracker.ui.SelectTime;

public final class ClockHour extends View {
    private static final String TAG = ClockHour.class.getSimpleName();
    // drawing tools
    private RectF rimRect;
    RectF rimRectHR;
    private Paint rimPaint;
    private Paint rimCirclePaint;

    private RectF faceRect, faceRectHR;
    private Paint facePaint;
    int rotation = 0;
    private boolean flag = false;

    private Paint scalePaint, scalePaint_Red, scalePaintHR, scalePaintHR_Red, scalePaintHR_Green;
    private RectF scaleRect, scaleRectHR;

    private Paint backgroundPaint;
    // end drawing tools
    private Bitmap background;
    // scale configuration
    private static final int totalNicks = 60;
    int totalNicksHR = 12;
    float degreesPerNicksHR = 360.0f / totalNicksHR;
    private static final float degreesPerNick = 360.0f / totalNicks;
    Bitmap bitmap;
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private float mMinutes;
    private float mHour;
    Drawable d;
    String isFull, hour;
    ArrayList<String> book_slot;
    Context mContext;
    int minuteCount;
    int tempMinuteCount;
    private Boolean isAmOrPmSelected;
    ArrayList<AppointmentTime> time_slot_item = new ArrayList<AppointmentTime>();
    boolean value = false;

    ArrayList<String> tempHours = new ArrayList<>();

    public ClockHour(Context context, AttributeSet attrs, ArrayList<AppointmentTime> slot_list, Boolean isAmOrPmSelected) {
        super(context, attrs);

        this.mContext = context;
        this.time_slot_item = slot_list;
        this.isAmOrPmSelected = isAmOrPmSelected; // false am true pm

        if (!isAmOrPmSelected) {

            for (int i = 0; i < time_slot_item.get(0).getTime_slot_list().size(); i++) {
                String getBookingHours = time_slot_item.get(0).getTime_slot_list().get(i).getBookingHours();
                isFull = time_slot_item.get(0).getTime_slot_list().get(i).getIsFull();

                tempHours.add(getBookingHours);
            }

        } else {
            for (int i = 0; i < time_slot_item.get(1).getTime_slot_list().size(); i++) {
                String getBookingHours = time_slot_item.get(1).getTime_slot_list().get(i).getBookingHours();
                tempHours.add(getBookingHours);
            }
        }


        initDrawingTools();
        Resources r = context.getResources();
        mHourHand = ContextCompat.getDrawable(context, R.drawable.black_4);
        mMinuteHand = ContextCompat.getDrawable(context, R.drawable.black_2);

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
        rimCirclePaint.setStrokeWidth(0.005f);

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
        scalePaint.setStrokeWidth(0.009f);
        scalePaint.setAntiAlias(true);


        scalePaint_Red = new Paint();
        scalePaint_Red.setStyle(Paint.Style.STROKE);
        scalePaint_Red.setColor(Color.parseColor("#FB3E1B")); //Red
        scalePaint_Red.setStrokeWidth(0.009f);
        scalePaint_Red.setAntiAlias(true);

        scalePaintHR = new Paint();
        scalePaintHR.setStyle(Paint.Style.STROKE);
        scalePaintHR.setColor(Color.parseColor("#353535")); // Green
        scalePaintHR.setStrokeWidth(0.02f);
        scalePaintHR.setAntiAlias(true);

        scalePaintHR_Red = new Paint();
        scalePaintHR_Red.setStyle(Paint.Style.STROKE);
        scalePaintHR_Red.setColor(Color.parseColor("#FB3E1B")); // Red
        scalePaintHR_Red.setStrokeWidth(0.02f);
        scalePaintHR_Red.setAntiAlias(true);

        scalePaintHR_Green = new Paint();
        scalePaintHR_Green.setStyle(Paint.Style.STROKE);
        scalePaintHR_Green.setColor(Color.parseColor("#049D01")); // Green
        scalePaintHR_Green.setStrokeWidth(0.02f);
        scalePaintHR_Green.setAntiAlias(true);

        float scalePosition = 0.03f;
        float scalePositionHR = 0.06f;

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

        int chosenDimension = (int) (Math.min(chosenWidth, chosenHeight) / 1.7f);

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
        for (int i = 0; i < totalNicks; i++) {

            float y1 = scaleRect.top;
            float y2 = y1 - 0.020f;


            canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

            canvas.rotate(degreesPerNick, 0.5f, 0.5f);
        }
        canvas.restore();
    }


    private void drawScaleHR(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);


        int myCounter = 0;
        for (int i = 0; i <= totalNicksHR; i++) {
            float y1 = scaleRectHR.top;
            float y2 = y1 - 0.050f;


            Paint myColor = null;

            if (tempHours.contains("" + myCounter)) {
                myColor = scalePaintHR_Red;
            } else
                myColor = scalePaintHR_Green;


            canvas.drawLine(0.5f, y1, 0.5f, y2, myColor);

            canvas.rotate(degreesPerNicksHR, 0.5f, 0.5f);
            myCounter++;

        }

        canvas.restore();
    }


    private void drawHours(int index) {
        time_slot_item.get(index).getTime_slot_list();
        time_slot_item.get(index).getMinimumDuration();
        for (int j = 0; j < time_slot_item.get(index).getTime_slot_list().size(); j++) {
            hour = time_slot_item.get(index).getTime_slot_list().get(j).getBookingHours();
            isFull = time_slot_item.get(index).getTime_slot_list().get(j).getIsFull();
            book_slot = time_slot_item.get(index).getTime_slot_list().get(j).getBookslot_list();
        }
    }


    private void drawBackground(Canvas canvas) {
        if (background == null) {
            Log.w(TAG, "Background not created");
        } else {
            canvas.drawBitmap(background, 0, 0, backgroundPaint);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
//   drawBackground(canvas);

        d = new BitmapDrawable(getResources(), background);


        int availableWidth = d.getIntrinsicWidth();
        int availableHeight = d.getIntrinsicHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        int w = d.getIntrinsicWidth();
        int h = d.getIntrinsicHeight();
        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        d.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        d.draw(canvas);

        canvas.save();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        w = mMinuteHand.getIntrinsicWidth();
        h = mMinuteHand.getMinimumHeight();
        mMinuteHand.setBounds(x - (w / 2), y - (h / 2) + 60, x + (w / 3), y + (h / 2));
        mMinuteHand.draw(canvas);
        canvas.save();
        canvas.restore();


        float r;
        if (flag == false) {
            // Get current time
            float result;
            if(!isAmOrPmSelected) {
                 result = gettimeinfloat("12:00");
            }
            else{
                result = gettimeinfloat("12:00");
            }
//      set rotation of current time
            r = (result * 360.0f) / 12.0f;

            flag = true;
        } else {
            r = rotation;
        }

        bitmap = ((BitmapDrawable) mHourHand).getBitmap();
        canvas.save();
        final Matrix matrix = new Matrix();

        //Set canvas centre
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2.5f);
        // set rotation of image

        matrix.postRotate(r);
        matrix.postTranslate(x, y);
        canvas.drawBitmap(bitmap, matrix, null);

        if (scaled) {
            canvas.restore();
        }
//
        float scale = (float) getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);

        canvas.restore();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "Size changed to " + w + "x" + h);


        //    if(isConstructorCalled) {
        regenerateBackground();
        // }
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
//

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
        final Drawable dialer = d;
        double r = Math.atan2(x - dialer.getIntrinsicWidth() / 2, dialer.getIntrinsicHeight() / 2 - y);
        rotation = (int) Math.toDegrees(r);
        if (rotation < 0) {
            rotation += 360;
        }

        System.out.println("My Rotaion" + " " + rotation);
        // Get time from ratation angle
        String hrs = null;
        float hour = rotation * 12.0f / 360.0f;
        int hours = (int) hour;
        String s = Float.toString(hour);
        String numberD = s.substring(s.indexOf("."));
        double min = Double.parseDouble(numberD) * 60;
        Double d = new Double(min);

        if (hours <= 9) {
            hrs = "0" + Integer.toString(hours);
        } else {
            hrs = Integer.toString(hours);
        }
        String time = hrs + ":";
        SelectTime.choosetime.setText(time);


    }


    public float gettimeinfloat(String time) {
        double mHour = Double.parseDouble(time.substring(0, 2));
        double k = mHour;
        return (float) (k);

    }
}
