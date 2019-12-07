package tvg.com.technoandy;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Advertisement {

    private Context context;
    private InterstitialAd interstitialAdmob;
    Logger logger;

    public Advertisement(Context context){
        this.context = context;
    }

    public void LoadAdmobAd(String InterstitialID){
        interstitialAdmob = new InterstitialAd(context);
        interstitialAdmob.setAdUnitId(InterstitialID);
        interstitialAdmob.loadAd(new AdRequest.Builder().build());
    }

//    public void ShowAdmobAd(){
//        interstitialAdmob.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // TODO Auto-generated method stub
//                super.onAdLoaded();
//                if (interstitialAdmob.isLoaded()) {
//                    interstitialAdmob.show();
//                }
//            }
//
//            @Override
//            public void onAdOpened() {
//
//            }
//
//            public void onAdClosed() {
//                setAdaptertoMainScreen();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                //    finish();
//                if (isFirstTime()) {
//                    Intent i = new Intent(context, OnBoardingActivity.class);
//                    context.startActivity(i);
//                } else {
//                    Intent i = new Intent(getBaseContext(), MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//                logger.LOGACTIVITY(Constants.POST_USERSTATUS, "SPLASH_INTERSTITAL_ADMOB");
//            }
//        }
//    }
}
