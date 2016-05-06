package qt.com.queuetracker.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import qt.com.queuetracker.Interfaces.WebApiResult;
import qt.com.queuetracker.Model.CompanyItem;
import qt.com.queuetracker.Network.WebRequest;
import qt.com.queuetracker.R;
import qt.com.queuetracker.Adapter.GridViewAdapter;
import qt.com.queuetracker.qtapp.QueTrackerBaseActivity;
import qt.com.queuetracker.utils.Constants;
import qt.com.queuetracker.utils.Urls;

/**
 * @auther Pankaj Pandey
 */
public class DashBoard extends QueTrackerBaseActivity implements View.OnClickListener, WebApiResult {

    // UI Variable
    AutoCompleteTextView et_Search;
    TextView txt_LogoName;
    LinearLayout ly_Logo;
    ImageView img_Search;
    GridView grid;
    GridViewAdapter grid_adapter;
    ArrayList<CompanyItem> company_itemlist;
    String deviceId;
    private List<CompanyItem> company_list;
    String user_id="1";

    // Other Variable

    Context mContext;
    Typeface face;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.dashboard);
        mContext = DashBoard.this;
        initView();
        setTypeface();

        sendCompanyData();
        callScreenData();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                company_itemlist.get(position).getCompany_logo();
                company_itemlist.get(position).getCompany_name();

                Intent companyIntent = new Intent(mContext, CompanyDetailsActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("company_name", company_itemlist.get(position).getCompany_name());
                bundle.putString("company_logo", company_itemlist.get(position).getCompany_logo());
                bundle.putString("company_id", company_itemlist.get(position).getCompany_id());
                bundle.putString("user_id",user_id);


                companyIntent.putExtras(bundle);
                startActivity(companyIntent);
            }
        });


    }

    @Override
    protected void initView() {
        ly_Logo = (LinearLayout) findViewById(R.id.ly_logo);
        img_Search = (ImageView) findViewById(R.id.img_Search);
        txt_LogoName = (TextView) findViewById(R.id.txt_LogoName);


        grid = (GridView) findViewById(R.id.gridView);

        et_Search = (AutoCompleteTextView) findViewById(R.id.txt_search);
        et_Search.setThreshold(1);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


    }


    @Override
    protected void setTypeface() {
        face = Typeface.createFromAsset(getAssets(), "advent-pro.regular.ttf");
        et_Search.setTypeface(face);
    }


    @Override
    protected void callScreenData() {
//        company_list = new ArrayList<>();

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//                final int search_textlength = et_Search.getText().length();

                sendSearchData();

                grid_adapter = new GridViewAdapter(mContext, company_itemlist);

                grid.setAdapter(grid_adapter);

                grid_adapter.notifyDataSetChanged();


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
        }

    }

    private void sendCompanyData() {
        try {


            WebRequest request = new WebRequest(mContext, Urls.getCompanyUrl, null, Constants.SERVICE_TYPE.SEND_DATA, this);
            request.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendSearchData() {
        try {

            Hashtable<String, String> params = new Hashtable<>();
            JSONObject jObject = new JSONObject();
            jObject.put("method", "getSearchResult");
            jObject.put("SearchText", et_Search.getText().toString());
            jObject.put("language", Constants.Language);
            jObject.put("DeviceId", deviceId);

            params.put("json_data", jObject.toString());
            WebRequest request = new WebRequest(mContext, Urls.getCompanySearchUrl, null, Constants.SERVICE_TYPE.SEND_DATA, this, jObject);
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
                        company_itemlist = new ArrayList<CompanyItem>();
                        if (jsonObject.getString("code").equals("200")) {
                            JSONArray companydetails = jsonObject.getJSONArray("result");


                            for (int i = 0; i < companydetails.length(); i++) {

                                JSONObject jsonObj = companydetails.getJSONObject(i);
                                CompanyItem company_item = new CompanyItem();
                                company_item.setCompany_name(jsonObj.getString("CompanyName"));
                                company_item.setCompany_logo(jsonObj.getString("Logo"));
                                company_item.setCompany_id(jsonObj.getString("CompanyId"));
                                company_itemlist.add(company_item);
                            }
                            grid_adapter = new GridViewAdapter(mContext, company_itemlist);

                            grid.setAdapter(grid_adapter);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }

    }
}
