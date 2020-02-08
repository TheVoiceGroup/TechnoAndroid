package tvg.com.technoandyexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import tvg.com.technoandy.ADTYPE;
import tvg.com.technoandy.Advertisement;
import tvg.com.technoandy.AdvertisementListener;
import tvg.com.technoandy.PreferenceHelper;

public class MainActivity extends AppCompatActivity {

    PreferenceHelper preferenceHelper;
    Button btn_showad;
    Advertisement advertisement;
    LinearLayout ad_view, admob_ad_layout, native_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_showad = findViewById(R.id.btn_showad);
        ad_view = findViewById(R.id.ad_view);
        advertisement = new Advertisement(this);
        advertisement.LoadInterstitialAd(ADTYPE.ADMOB, "ca-app-pub-3940256099942544/1033173712");
        advertisement.ShowBannerAd(ADTYPE.ADMOB, ad_view, "ca-app-pub-3940256099942544/6300978111");

        btn_showad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertisement.ShowInterstitialAD(ADTYPE.ADMOB);
            }
        });

        admob_ad_layout = findViewById(R.id.admob_ad_layout);
        native_container = findViewById(R.id.native_container);

        advertisement.ShowNativeAd(MainActivity.this,
                ADTYPE.FACEBOOK,
                native_container,
                R.id.native_ad_media,
                R.id.native_ad_icon,
                R.layout.native_ad_layout,
                R.id.ad_choices_container,
                R.id.native_ad_title,
                R.id.native_ad_social_context,
                R.id.native_ad_body,
                R.id.native_ad_sponsored_label,
                R.id.native_ad_call_to_action,
                R.id.native_container,
                0,
                0,
                0);

//        advertisement.ShowNativeAd(MainActivity.this,
//                ADTYPE.ADMOB,
//                admob_ad_layout,
//                R.id.ad_media,
//                R.id.ad_app_icon,
//                R.layout.native_ad_admob_layout,
//                0,
//                R.id.ad_headline,
//                0,
//                R.id.ad_body,
//                R.id.ad_advertiser,
//                R.id.ad_call_to_action,
//                0,
//                R.id.ad_price,
//                R.id.ad_stars,
//                R.id.ad_store);

//        advertisement.advertismentListener = new AdvertisementListener() {
//            @Override
//            public void onAdLoaded() {
//                Toast.makeText(MainActivity.this, "Ad Loaded", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdClicked() {
//                Toast.makeText(MainActivity.this, "Ad Clicked", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdClosed() {
//                Toast.makeText(MainActivity.this, "Ad Closed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailed(String Error) {
//                Toast.makeText(MainActivity.this, "Ad Failed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOff() {
//                Toast.makeText(MainActivity.this, "Ad Off", Toast.LENGTH_SHORT).show();
//            }
//        };
    }
}
