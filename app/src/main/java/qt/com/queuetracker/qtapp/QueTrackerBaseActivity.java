package qt.com.queuetracker.qtapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




/**
 *
 * Classs use for Custum Header
 * @author Pankaj Pandey
 *
 */
public abstract class QueTrackerBaseActivity extends Activity {



    public QueTrackerBaseActivity() {
        super();
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {

        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * initView() will be used in initializing any view including custom.
     */
    protected abstract void initView();

    /**
     * setTypeface() will be used for setting font, size, color etc.
     */
    protected abstract void setTypeface();

    /**
     * callScreenData() will be used to load data for view from any source i.e. network, db etc.
     */
    protected abstract void callScreenData();





    public void showToast (String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }






}
