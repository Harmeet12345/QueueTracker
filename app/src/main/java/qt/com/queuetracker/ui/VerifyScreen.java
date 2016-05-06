package qt.com.queuetracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

import qt.com.queuetracker.Model.AppointmentTime;
import qt.com.queuetracker.R;
import qt.com.queuetracker.helper.ServeTimeClock;
import qt.com.queuetracker.helper.WaitingTimeClock;
import qt.com.queuetracker.qtapp.QueTrackerBaseActivity;

/**
 * Created by vinove on 4/4/16.
 */
public class VerifyScreen extends QueTrackerBaseActivity implements View.OnClickListener{

    ImageView mapview,waitingtimeView,servetimeView;
 public static String hrs_str;
    public static String minutes_str,servicetime,ticket_Id;
    TextView digital_clock_tv,tv_generated_ticketno;
    ArrayList<AppointmentTime> appointment_list = new ArrayList<>();
LinearLayout serve_clock_ll,waiting_clock_ll;
    String minmumDuration;
    Boolean AmorPmBoolselected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.verify_screen);
        hrs_str=getIntent().getStringExtra("hour");
        minutes_str=getIntent().getStringExtra("minute");
        servicetime=getIntent().getStringExtra("serviceTime");
        ticket_Id=getIntent().getStringExtra("ticket_Id");
        AmorPmBoolselected = getIntent().getExtras().getBoolean("AmorPmBoolean");
        appointment_list= getIntent().getParcelableArrayListExtra("appointment_list");


        initView();
        callScreenData();
        WaitingTimeClock view=new WaitingTimeClock(this,null,0,minmumDuration);
        ServeTimeClock serve_clockView=new ServeTimeClock(this,null,0,servicetime);
//
        waiting_clock_ll.addView(view);
        serve_clock_ll.addView(serve_clockView);
    }



    @Override
    protected void initView() {
        mapview=(ImageView)findViewById(R.id.im_locate_chart);
        waitingtimeView=(ImageView)findViewById(R.id.im_waitTime);

        servetimeView=(ImageView)findViewById(R.id.im_serve_timer);
        digital_clock_tv=(TextView)findViewById(R.id.tv_digitalclock);
        tv_generated_ticketno=(TextView)findViewById(R.id.tv_generated_ticketno);

        serve_clock_ll=(LinearLayout)findViewById(R.id.serve_clock);
        waiting_clock_ll=(LinearLayout)findViewById(R.id.waiting_clock);


    }

    @Override
    protected void setTypeface() {

    }

    @Override
    protected void callScreenData() {
        String AmOrPm;
        if(!AmorPmBoolselected){
            AmOrPm="AM";
        }else
            AmOrPm="PM";


        digital_clock_tv.setText(hrs_str + ":" + minutes_str + " " + AmOrPm);
        tv_generated_ticketno.setText(ticket_Id);
        mapview.setOnClickListener(this);

        for (int i=0;i<appointment_list.size();i++){
            minmumDuration=appointment_list.get(i).getMinimumDuration();
        }



    }


    @Override
    public void onClick(View v) {

        Intent map_intent=new Intent(this,MapScreen.class);
        startActivity(map_intent);

    }
}
