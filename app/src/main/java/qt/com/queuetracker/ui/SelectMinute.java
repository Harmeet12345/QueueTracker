package qt.com.queuetracker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import qt.com.queuetracker.Adapter.CircularAdapter;
import qt.com.queuetracker.Interfaces.WebApiResult;
import qt.com.queuetracker.Model.AppointmentTime;
import qt.com.queuetracker.Model.BranchDetails;
import qt.com.queuetracker.Model.ServiceItem;
import qt.com.queuetracker.Network.WebRequest;
import qt.com.queuetracker.R;
import qt.com.queuetracker.helper.ClockMinute;
import qt.com.queuetracker.qtapp.QueTrackerBaseActivity;
import qt.com.queuetracker.utils.Constants;
import qt.com.queuetracker.utils.Urls;

/**
 * Created by vinove on 28/3/16.
 */
public class SelectMinute extends QueTrackerBaseActivity implements WebApiResult {

    ImageView step_one_img, step_two_img, step_three_img, step_one_arrow, step_two_arrow;
    TextView step_one_tv, step_two_tv, step_three_tv;


    public static TextView set_minute_tv, set_hour_time, ticket_nuber_tv;
    public static String hourtime;
    FrameLayout ticket_layout;
    ArrayList<AppointmentTime> appointment_list = new ArrayList<>();
    Boolean AmorPmBoolselected;
    String user_id = "1";
    String company_id, branch_id, service_id, deviceId;
    Context mContext = SelectMinute.this;
    String ticket_Id, securityCode, serviceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.select_minute);
        initView();

        //

        hourtime = getIntent().getStringExtra("hourtime");
        appointment_list = getIntent().getParcelableArrayListExtra("appointment_list");
        AmorPmBoolselected = getIntent().getExtras().getBoolean("AmorPmBoolean");
        company_id = getIntent().getExtras().getString("company_id");
        branch_id = getIntent().getExtras().getString("branch_id");
        service_id = getIntent().getExtras().getString("service_id");
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        //

        set_minute_tv = (TextView) findViewById(R.id.set_minutes_time);
        set_hour_time = (TextView) findViewById(R.id.set_hour_time);
        ticket_nuber_tv = (TextView) findViewById(R.id.ticket_number);
        ticket_layout = (FrameLayout) findViewById(R.id.ticket_layout);
        ticket_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData();
                ticket_nuber_tv.setY(5000); //TODO: change to (current X + textview.witdh)
                ticket_nuber_tv.animate().translationY(0).setDuration(1000).start();
                ticket_nuber_tv.setText(ticket_Id);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // TODO: Your application init goes here.
                        Intent verify_intent = new Intent(SelectMinute.this, VerifyScreen.class);
                        verify_intent.putExtra("hour", hourtime);
                        verify_intent.putExtra("minute", set_minute_tv.getText().toString());
                        verify_intent.putExtra("serviceTime", serviceTime);
                        verify_intent.putExtra("ticket_Id", ticket_Id);
                        verify_intent.putExtra("AmorPmBoolean", AmorPmBoolselected);
                        verify_intent.putParcelableArrayListExtra("appointment_list", appointment_list);
                        startActivity(verify_intent);
                        finish();

                    }
                }, 5000);



            }
        });

        set_hour_time.setText(hourtime);


        set_hour_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        LinearLayout ll = (LinearLayout) findViewById(R.id.clock_minute);
        ClockMinute clockMinute = new ClockMinute(this, null, appointment_list, AmorPmBoolselected);
        ll.addView(clockMinute);
    }

    @Override
    protected void initView() {
        step_one_img = (ImageView) findViewById(R.id.step_one_img);
        step_two_img = (ImageView) findViewById(R.id.step_two_img);
        step_three_img = (ImageView) findViewById(R.id.step_three_img);

        step_one_arrow = (ImageView) findViewById(R.id.step_one_arrow);
        step_two_arrow = (ImageView) findViewById(R.id.step_two_arrow);

        step_one_tv = (TextView) findViewById(R.id.step_one_tv);
        step_two_tv = (TextView) findViewById(R.id.step_two_tv);
        step_three_tv = (TextView) findViewById(R.id.step_three_tv);

        step_one_img.setImageResource(R.drawable.grey_selector);
        step_one_arrow.setImageResource(R.drawable.arrow_gray);
        step_two_img.setImageResource(R.drawable.grey_selector);
        step_three_img.setImageResource(R.drawable.red_selector);
        step_two_arrow.setImageResource(R.drawable.arrow_gray);

        step_one_tv.setTextColor(ContextCompat.getColor(mContext, R.color.Black));
        step_two_tv.setTextColor(ContextCompat.getColor(mContext, R.color.Black));
        step_three_tv.setTextColor(ContextCompat.getColor(mContext, R.color.Black));
    }

    void sendData() {
        try {
         
            JSONObject json_timeObject = new JSONObject();
            json_timeObject.put("hour",hourtime);
            json_timeObject.put("initial_time",set_minute_tv.getText().toString());
            json_timeObject.put("deviceId",deviceId);
//         
            Hashtable<String, String> params = new Hashtable<>();
            JSONObject jObject = new JSONObject();
            jObject.put("method", "getTicketNo");
            jObject.put("companyId", company_id);
            jObject.put("branchId", branch_id);
            jObject.put("serviceId", service_id);

            jObject.put("isAM", AmorPmBoolselected);
            jObject.put("language", Constants.Language);

            jObject.put("myTime", json_timeObject);


            params.put("json_data", jObject.toString());
            WebRequest request = new WebRequest(mContext, Urls.getTicketUrl, null, Constants.SERVICE_TYPE.SEND_DATA, this, jObject);
            request.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setTypeface() {

    }

    @Override
    protected void callScreenData() {


    }

    @Override
    public void getWebResult(Constants.SERVICE_TYPE type, String result) {
        switch (type) {
            case SEND_DATA:
                if (result != null) {
                    try {
//

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("code").equals("200")) {
                            ticket_Id = jsonObject.getString("ticketId");
                            securityCode = jsonObject.getString("securityCode");
                            serviceTime = jsonObject.getString("serviceTime");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }


}
