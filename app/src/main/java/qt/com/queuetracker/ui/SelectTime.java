package qt.com.queuetracker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import qt.com.queuetracker.Interfaces.WebApiResult;
import qt.com.queuetracker.Model.AmPmTimePeriod;
import qt.com.queuetracker.Model.AppointmentTime;
import qt.com.queuetracker.Network.WebRequest;
import qt.com.queuetracker.R;
import qt.com.queuetracker.helper.ClockHour;
import qt.com.queuetracker.qtapp.QueTrackerBaseActivity;
import qt.com.queuetracker.utils.Constants;
import qt.com.queuetracker.utils.Urls;

/**
 * Created by vinove on 30/11/15.
 */
public class SelectTime extends QueTrackerBaseActivity implements WebApiResult {

    // UI Variable
    ImageView step_one_img, step_two_img, step_three_img, step_one_arrow, step_two_arrow;
    TextView step_one_tv, step_two_tv, step_three_tv;


    public static ArrayList<AppointmentTime> appointment_time_list;
    ArrayList<AmPmTimePeriod> am_pm_time_list;
    public static TextView ampm;
    public static EditText choosetime;
    Context mContext = SelectTime.this;
    Switch time_shift_selection;
    LinearLayout digitalcloclayout;
    // ClockHour clockHour;
    String companyid, branch_id, service_id;

    private boolean isAMOrPmSelceted = false;  // am means false -----pm means true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.select_time);
        initView();

        Bundle bundle = getIntent().getExtras();
        companyid = bundle.getString("company_id");
        branch_id = bundle.getString("branch_id");
        service_id = bundle.getString("service_id");

        choosetime = (EditText) findViewById(R.id.choosetime);
        ampm = (TextView) findViewById(R.id.ampm);

        time_shift_selection = (Switch) findViewById(R.id.time_sift_selection_switch);
        callScreenData();

        digitalcloclayout = (LinearLayout) findViewById(R.id.digitalcloclayout);
        ampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectTime.this, SelectMinute.class);
                i.putExtra("hourtime", choosetime.getText().toString());
                i.putParcelableArrayListExtra("appointment_list", appointment_time_list);
                i.putExtra("AmorPmBoolean", isAMOrPmSelceted);
                i.putExtra("companyid", companyid);
                i.putExtra("branch_id", branch_id);
                i.putExtra("service_id", service_id);
//
                startActivity(i);

            }
        });
        /*choosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clockHour.refreshDrawableState();
                clockHour.invalidate();
                clockHour.postInvalidate();


            }
        });
*/

        //set the switch to ON
        time_shift_selection.setChecked(true);
        time_shift_selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAMOrPmSelceted = false;
                } else {
                    isAMOrPmSelceted = true;
                }
            }
        });


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
        step_three_tv.setTextColor(ContextCompat.getColor(mContext, R.color.Red));

    }

    @Override
    protected void setTypeface() {

    }

    @Override
    protected void callScreenData() {
        try {

            Hashtable<String, String> params = new Hashtable<>();
            JSONObject jObject = new JSONObject();
            jObject.put("method", "getTimeSlot");
            jObject.put("language", Constants.Language);
            jObject.put("branchId", branch_id);
            jObject.put("companyId", companyid);
            jObject.put("serviceId", service_id);
            params.put("json_data", jObject.toString());
            WebRequest request = new WebRequest(mContext, Urls.getTimeSlotUrl, null, Constants.SERVICE_TYPE.SEND_DATA, this, jObject);
            request.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void getWebResult(Constants.SERVICE_TYPE type, String result) {
        switch (type) {
            case SEND_DATA:
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        appointment_time_list = new ArrayList<AppointmentTime>();
                        am_pm_time_list = new ArrayList<AmPmTimePeriod>();

                        AppointmentTime appointObjAm = new AppointmentTime();
                        appointObjAm.setType(1);
                        appointObjAm.setMinimumDuration(jsonObject.getString("minDuration")); // this is getting duplicated ignore this one or below one for min duration

                        if (jsonObject.getString("code").equals("200")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("appointmentTime");
//
                            JSONArray time_slot_details_am = jsonObject1.getJSONArray("am");

                            for (int i = 0; i < time_slot_details_am.length(); i++) {
                                JSONObject jsonObj = time_slot_details_am.getJSONObject(i);

                                AmPmTimePeriod am_item = new AmPmTimePeriod();
                                am_item.setBookingHours(jsonObj.getString("Hour"));
                                am_item.setIsFull(jsonObj.getString("IsFull"));
                                JSONArray jsonArray = jsonObj.getJSONArray("bookedSlot");
                                ArrayList<String> myBookedSlots = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    myBookedSlots.add(String.valueOf(jsonArray.get(j)));

                                }

                                am_item.setBookslot_list(myBookedSlots);
                                am_pm_time_list.add(am_item);


                            }
                            appointObjAm.setTime_slot_list(am_pm_time_list);
                            appointment_time_list.add(appointObjAm);

                            AppointmentTime appointObjPm = new AppointmentTime();
                            appointObjPm.setType(2);
                            appointObjPm.setMinimumDuration(jsonObject.getString("minDuration"));


                            JSONArray time_slot_details_pm = jsonObject1.getJSONArray("pm");
                            for (int i = 0; i < time_slot_details_pm.length(); i++) {
                                JSONObject jsonObj = time_slot_details_pm.getJSONObject(i);

                                AmPmTimePeriod pm_item = new AmPmTimePeriod();
                                pm_item.setBookingHours(jsonObj.getString("Hour"));
                                pm_item.setIsFull(jsonObj.getString("IsFull"));
                                JSONArray jsonArray = jsonObj.getJSONArray("bookedSlot");
                                ArrayList<String> myBookedSlots = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    myBookedSlots.add(String.valueOf(jsonArray.get(j)));

                                }
                                pm_item.setBookslot_list(myBookedSlots);
                                am_pm_time_list.add(pm_item);


                            }
                            appointObjPm.setTime_slot_list(am_pm_time_list);
                            appointment_time_list.add(appointObjPm);


                        }


                        LinearLayout clock_layout = (LinearLayout) findViewById(R.id.clock_hour);
                        ClockHour view = new ClockHour(this, null, appointment_time_list, isAMOrPmSelceted);
                        clock_layout.addView(view);
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }
}
