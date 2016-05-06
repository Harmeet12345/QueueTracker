package qt.com.queuetracker.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import qt.com.queuetracker.Adapter.ListViewAdapter;
import qt.com.queuetracker.Interfaces.WebApiResult;
import qt.com.queuetracker.Model.BranchDetails;
import qt.com.queuetracker.Network.WebRequest;
import qt.com.queuetracker.R;
import qt.com.queuetracker.qtapp.QueTrackerBaseActivity;
import qt.com.queuetracker.utils.Constants;
import qt.com.queuetracker.utils.Urls;


/**
 * Created by vinove on 25/3/16.
 */
public class CompanyDetailsActivity extends QueTrackerBaseActivity implements View.OnClickListener, WebApiResult, LocationListener {
    protected LocationManager locationManager;
    protected String latitude, longitude;


    AutoCompleteTextView location_search_tv;
    ListView listview;
    TextView txt_LogoName;
    LinearLayout ly_Logo, ly_mapview;
    ImageView company_logo,img_Location;

    ListViewAdapter list_adapter;

    ArrayList<BranchDetails> branch_itemlist;


    // Other Variable

    Context mContext;
    String user_id;


    Typeface face;
    String companyid, companyName, companylogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.details_company);

        Bundle bundle = getIntent().getExtras();


        companyName = bundle.getString("company_name");
        companylogo = bundle.getString("company_logo");
        companyid = bundle.getString("company_id");
        user_id=bundle.getString("user_id");


        mContext = CompanyDetailsActivity.this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//
        initView();
        setTypeface();
        sendSearchData();
        callScreenData();


        txt_LogoName.setText(companyName);
        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher).build();

//
        imageLoader.displayImage(companylogo, company_logo, options);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CompanyDetailsActivity.this, SelectServices.class);

                Bundle bundle = new Bundle();


                bundle.putString("branch_id", branch_itemlist.get(position).getBranchId());
                bundle.putString("company_id", companyid);
                bundle.putString("user_id",user_id);


                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    protected void initView() {

        ly_Logo = (LinearLayout) findViewById(R.id.ly_logo);
        ly_mapview = (LinearLayout) findViewById(R.id.ly_mapview);

        img_Location = (ImageView) findViewById(R.id.img_Location);
//        img_Search = (ImageView) findViewById(R.id.img_Search);
        company_logo = (ImageView) findViewById(R.id.img_Logo);
        txt_LogoName = (TextView) findViewById(R.id.txt_LogoName);
        listview = (ListView) findViewById(R.id.listView);

        location_search_tv = (AutoCompleteTextView) findViewById(R.id.location_search_tv);
        location_search_tv.setThreshold(1);
        location_search_tv.setOnClickListener(this);
        ly_Logo.setOnClickListener(this);

    }

    @Override
    protected void setTypeface() {
        face = Typeface.createFromAsset(getAssets(), "advent-pro.regular.ttf");
        txt_LogoName.setTypeface(face);
        location_search_tv.setTypeface(face);

    }

    @Override
    protected void callScreenData() {
        location_search_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                final int search_textlength = location_search_tv.getText().length();

                sendSearchData();

                list_adapter = new ListViewAdapter(mContext, branch_itemlist);

                listview.setAdapter(list_adapter);

                list_adapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.txt_search:

                break;
            case R.id.ly_mapview:
                Intent map_intent = new Intent(mContext, MapScreen.class);
                startActivity(map_intent);
                break;

        }

    }

    void sendSearchData() {


        try {

            Hashtable<String, String> params = new Hashtable<>();
            JSONObject jObject = new JSONObject();
            jObject.put("method", "getBranchLocation");
            jObject.put("language", Constants.Language);
            jObject.put("SearchText", location_search_tv.getText().toString());
            jObject.put("CompanyId", companyid);
            jObject.put("startLimit", "0");
            jObject.put("endLimit", "10");
            jObject.put("lat", latitude);
            jObject.put("long", longitude);
            params.put("json_data", jObject.toString());
            WebRequest request = new WebRequest(mContext, Urls.getBranchUrl, null, Constants.SERVICE_TYPE.SEND_DATA, this, jObject);
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
                        branch_itemlist = new ArrayList<BranchDetails>();
                        if (jsonObject.getString("code").equals("200")) {
                            JSONArray branchdetails = jsonObject.getJSONArray("BranchDetail");


                            for (int i = 0; i < branchdetails.length(); i++) {

                                JSONObject jsonObj = branchdetails.getJSONObject(i);
                                BranchDetails branch_items = new BranchDetails();
                                branch_items.setBranch_name(jsonObj.getString("branchName"));
                                branch_items.setAddress(jsonObj.getString("address"));
                                branch_items.setAvailability(jsonObj.getString("availability"));
                                branch_items.setBranchId(jsonObj.getString("BranchId"));
                                branch_items.setCity(jsonObj.getString("city"));
                                branch_items.setCompanyId(jsonObj.getString("CompanyId"));
                                branch_items.setDistance(jsonObj.getString("distance"));
                                branch_items.setLatitude(jsonObj.getString("latitude"));
                                branch_items.setLongitude(jsonObj.getString("longitude"));
                                branch_items.setStatus(jsonObj.getString("Status"));

                                branch_itemlist.add(branch_items);
                            }

                            list_adapter = new ListViewAdapter(mContext, branch_itemlist);

                            listview.setAdapter(list_adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }

    }
}









