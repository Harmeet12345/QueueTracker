package qt.com.queuetracker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import qt.com.queuetracker.Adapter.CircularAdapter;
import qt.com.queuetracker.CircularList.CircularListView;
import qt.com.queuetracker.Interfaces.WebApiResult;
import qt.com.queuetracker.Model.ServiceItem;
import qt.com.queuetracker.Network.WebRequest;
import qt.com.queuetracker.R;
import qt.com.queuetracker.qtapp.QueTrackerBaseActivity;
import qt.com.queuetracker.utils.Constants;
import qt.com.queuetracker.utils.Urls;


public class SelectServices extends QueTrackerBaseActivity implements WebApiResult {
    Context mContext;

    ImageView step_one_img, step_two_img, step_three_img, step_one_arrow, step_two_arrow;
    TextView step_one_tv, step_two_tv, step_three_tv;
    ArrayList<ServiceItem> service_itemlist;
    public static CircularListView listView;
    public static String companyid, branch_id, service_id;
    int minimum_item = 8;

    int height;
    int width;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_service);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle bundle = getIntent().getExtras();
        companyid = bundle.getString("company_id");
        branch_id = bundle.getString("branch_id");
        user_id = bundle.getString("user_id");
        mContext = this;
        initView();
        sendData();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        listView = (CircularListView) findViewById(R.id.circularListView);
        listView.setFriction(ViewConfiguration.getScrollFriction() * 2);
        listView.setDivider(null);


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
        step_two_img.setImageResource(R.drawable.red_selector);
        step_two_arrow.setImageResource(R.drawable.arrow_orange);

        step_one_tv.setTextColor(ContextCompat.getColor(mContext, R.color.Black));
        step_two_tv.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
    }

    @Override
    protected void setTypeface() {

    }

    @Override
    protected void callScreenData() {

    }


    private void sendData() {
        try {


//            Hashtable<String, String> params = new Hashtable<>();
            JSONObject jObject = new JSONObject();
            jObject.put("method", "getService");
            jObject.put("branchId", branch_id);
            jObject.put("companyId", companyid);
            jObject.put("language", "en");

//
            WebRequest request = new WebRequest(mContext, Urls.getCompanyServiceUrl, null, Constants.SERVICE_TYPE.SEND_DATA, this, jObject);
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
                        service_itemlist = new ArrayList<>();
                        if (jsonObject.getString("code").equals("200")) {
                            JSONArray service_details = jsonObject.getJSONArray("services");
                            for (int i = 0; i < service_details.length(); i++) {
                                JSONObject jsonObj = service_details.getJSONObject(i);
                                ServiceItem service_item = new ServiceItem();
                                service_item.setService_name(jsonObj.getString("ServiceName"));
                                service_item.setService_logo(jsonObj.getString("Logo"));
                                service_item.setService_id(jsonObj.getString("ServiceId"));
                                service_item.setService_durarion(jsonObj.getString("Duration"));

                                service_item.setEmpty(false);
                                service_itemlist.add(service_item);
                            }

                            if (service_itemlist.size() <= minimum_item) {
                                int counter = minimum_item - service_itemlist.size();
                                for (int j = 0; j < counter / 2; j++) {
                                    ServiceItem service_item = new ServiceItem();
                                    service_item.setEmpty(true);
                                    service_itemlist.add(j, service_item);

                                }

                                for (int j = 0; j < counter / 2; j++) {
                                    ServiceItem service_item = new ServiceItem();
                                    service_item.setEmpty(true);
                                    service_itemlist.add(service_item);

                                }
                            }
                            listView.setRadius(Math.min(height, width / 2.5f));

                            listView.setAdapter(new CircularAdapter(this, service_itemlist));
                            listView.setEnableInfiniteScrolling(true);
                            listView.setSelection(service_itemlist.size() / 2 - 2);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }


}
