package tvg.com.technoandy;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class Advertisement {

    private Context context;
    private InterstitialAd interstitialAdmob;
    private com.facebook.ads.InterstitialAd interstitialAdfb;
    public AdvertisementListener advertismentListener;

    public Advertisement(Context context){
        this.context = context;
    }

    public void ShowBannerAd(ADTYPE type, LinearLayout adlayout, String BannerID){
        switch (type){
            case ADMOB:
                AdView mAdView = new AdView(context);
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId(BannerID);
                AdRequest.Builder builder = new AdRequest.Builder();

                mAdView.loadAd(builder.build());
                adlayout.addView(mAdView);
                break;
            case FACEBOOK:
                com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, BannerID, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                adlayout.addView(adView);
                adView.loadAd();
                break;
            case OFF:
                adlayout.setVisibility(View.GONE);
                break;
        }
    }

    public void LoadInterstitialAd(ADTYPE type, String InterstitialID){
        switch (type){
            case ADMOB:
                interstitialAdmob = new InterstitialAd(context);
                interstitialAdmob.setAdUnitId(InterstitialID);
                interstitialAdmob.loadAd(new AdRequest.Builder().build());
                ADLISTENER(type);
                break;
            case FACEBOOK:
                interstitialAdfb = new com.facebook.ads.InterstitialAd(context, InterstitialID);
                interstitialAdfb.loadAd();
                ADLISTENER(type);
                break;
            case OFF:
                ADLISTENER(type);
                break;

        }
    }

    public void ShowInterstitialAD(ADTYPE type){
        switch (type){
            case ADMOB:
                if (interstitialAdmob.isLoaded() && interstitialAdmob!=null){
                    interstitialAdmob.show();
                }else {
                    advertismentListener.onAdFailed("Ad Not Loaded");
                    if (interstitialAdmob==null){
                        Log.d("ADMOB", "Interstitial Ad is not Initialized");
                    } else {
                        Log.d("ADMOB", "Interstitial Ad is not loaded");
                    }
                }
                break;
            case FACEBOOK:
                if (interstitialAdfb.isAdLoaded() && interstitialAdfb!=null){
                    interstitialAdfb.show();
                }else {
                    advertismentListener.onAdFailed("Ad Not Loaded");
                    if (interstitialAdfb==null){
                        Log.d("FACEBOOK", "Interstitial Ad is not Initialized");
                    } else {
                        Log.d("FACEBOOK", "Interstitial Ad is not loaded");
                    }
                }
                break;
            case OFF:
                advertismentListener.onAdOff();
                Log.d("OFF", "AD IS OFF");
                break;
        }
    }

    private void ADLISTENER(ADTYPE type){
        switch (type){
            case ADMOB:
                interstitialAdmob.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        advertismentListener.onAdLoaded();

                    }

                    @Override
                    public void onAdOpened() {
                        advertismentListener.onAdOpened();
                    }

                    public void onAdClosed() {
                        advertismentListener.onAdClosed();

                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        advertismentListener.onAdFailed("Error: " + String.valueOf(errorCode));
                    }

                    @Override
                    public void onAdClicked() {
                        advertismentListener.onAdClicked();
                    }
                });
                break;
            case FACEBOOK:
                interstitialAdfb.setAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
                        advertismentListener.onAdOpened();
                    }

                    @Override
                    public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                        advertismentListener.onAdClosed();
                    }

                    @Override
                    public void onError(com.facebook.ads.Ad ad, AdError adError) {
                        advertismentListener.onAdFailed(adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(com.facebook.ads.Ad ad) {
                        advertismentListener.onAdLoaded();
                    }

                    @Override
                    public void onAdClicked(com.facebook.ads.Ad ad) {
                        advertismentListener.onAdClicked();
                    }

                    @Override
                    public void onLoggingImpression(com.facebook.ads.Ad ad) {

                    }
                });
                break;
            case OFF:
                break;

        }
    }
}
