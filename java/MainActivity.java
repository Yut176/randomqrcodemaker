package yut_sk_dev.java_conf.gr.jp.showqrcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.graphics.Bitmap;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.widget.ImageView;
//import android.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.google.firebase.analytics.FirebaseAnalytics;

import android.graphics.Point;

public class MainActivity extends AppCompatActivity {
    private  AdView mAdView;

    private final MainActivity self = this;
    private FirebaseAnalytics mAnalytics;

    private String makeSTR(){

        String ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuffer buf = new StringBuffer();
        for(int i=0; i<200; i++){
            Random rnd = new Random();
            buf.append(ch.charAt(rnd.nextInt(ch.length())));
        }

        return buf.toString();
    }

    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnalytics = FirebaseAnalytics.getInstance(self);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title);

        MobileAds.initialize(this, "***********************");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = makeSTR();
                button.setText(R.string.button_text);
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(st, BarcodeFormat.QR_CODE, size, size);

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    throw new AndroidRuntimeException("Barcode Error.", e);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged( boolean hasFocus ){
        super.onWindowFocusChanged(hasFocus);
        LinearLayout l = (LinearLayout) findViewById(R.id.lLayout);

        Point v = DisplaySizeCheck.getViewSize( l );

        size = (int)((v.x)*3/5);

    }

}
