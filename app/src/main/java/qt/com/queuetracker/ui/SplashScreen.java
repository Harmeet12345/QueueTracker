package qt.com.queuetracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import qt.com.queuetracker.R;
import qt.com.queuetracker.helper.GifView;


public class SplashScreen extends Activity {

    int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        LinearLayout gifLayout = (LinearLayout) findViewById(R.id.ly_gif);
        Display display = getWindowManager().getDefaultDisplay();
        GifView view = new GifView(this, display);
        gifLayout.addView(view);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, DashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
