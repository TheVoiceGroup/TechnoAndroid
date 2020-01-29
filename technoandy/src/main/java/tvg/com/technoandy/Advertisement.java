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
    boolean isFailed = false;

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
                if (interstitialAdmob!=null) {
                    if (interstitialAdmob.isLoaded() && interstitialAdmob != null) {
                        interstitialAdmob.show();
                    } else {
                        if (!isFailed) {
                            if (advertismentListener!=null) advertismentListener.onAdNotLoaded(type);
                        }else {
                            if (advertismentListener!=null) advertismentListener.onAdNotInitialized(type);
                        }
                        if (interstitialAdmob == null) {
                            Log.d("ADMOB", "Interstitial Ad is not Initialized");
                        } else {
                            Log.d("ADMOB", "Interstitial Ad is not loaded");
                        }
                    }
                } else {
                    if (advertismentListener!=null) advertismentListener.onAdNotInitialized(type);
                }
                break;
            case FACEBOOK:
                if (interstitialAdfb!=null) {
                    if (interstitialAdfb.isAdLoaded() && interstitialAdfb != null) {
                        interstitialAdfb.show();
                    } else {
                        if (!isFailed) {
                            if (advertismentListener!=null) advertismentListener.onAdNotLoaded(type);
                        } else {
                            if (advertismentListener!=null) advertismentListener.onAdNotInitialized(type);
                        }
                        if (interstitialAdfb == null) {
                            Log.d("FACEBOOK", "Interstitial Ad is not Initialized");
                        } else {
                            Log.d("FACEBOOK", "Interstitial Ad is not loaded");
                        }
                    }
                } else {
                    if (advertismentListener!=null) advertismentListener.onAdNotInitialized(type);
                }
                break;
            case OFF:
                if (advertismentListener!=null) advertismentListener.onAdOff(type);
                Log.d("OFF", "AD IS OFF");
                break;
        }
    }

    private void ADLISTENER(final ADTYPE type){
        switch (type){
            case ADMOB:
                interstitialAdmob.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        isFailed = false;
                        if (advertismentListener!=null) advertismentListener.onAdLoaded(type);

                    }

                    @Override
                    public void onAdOpened() {
                        if (advertismentListener!=null) advertismentListener.onAdOpened(type);
                    }

                    public void onAdClosed() {
                        if (advertismentListener!=null) advertismentListener.onAdClosed(type);

                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        isFailed = true;
                        if (advertismentListener!=null) advertismentListener.onAdFailed(type,"Error: " + String.valueOf(errorCode));
                    }

                    @Override
                    public void onAdClicked() {
                        if (advertismentListener!=null) advertismentListener.onAdClicked(type);
                    }
                });
                break;
            case FACEBOOK:
                interstitialAdfb.setAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
                        if (advertismentListener!=null) advertismentListener.onAdOpened(type);
                    }

                    @Override
                    public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                        if (advertismentListener!=null) advertismentListener.onAdClosed(type);
                    }

                    @Override
                    public void onError(com.facebook.ads.Ad ad, AdError adError) {
                        isFailed = true;
                        if (advertismentListener!=null) advertismentListener.onAdFailed(type,adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(com.facebook.ads.Ad ad) {
                        isFailed = false;
                        if (advertismentListener!=null) advertismentListener.onAdLoaded(type);
                    }

                    @Override
                    public void onAdClicked(com.facebook.ads.Ad ad) {
                        if (advertismentListener!=null) advertismentListener.onAdClicked(type);
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
