package tvg.com.technoandy;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Advertisement {

    private Context context;
    private InterstitialAd interstitialAdmob;
    private com.facebook.ads.InterstitialAd interstitialAdfb;
    UnifiedNativeAd nativeAd;
    public AdvertisementListener advertismentListener;
    public NativeListener nativeListener;
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

    public void ShowNativeAd(final Activity activity,
                             ADTYPE adtype,
                             final LinearLayout ad_layout,
                             final int media,
                             final int icon,
                             final int nativelayout,
                             final int adchoicescontainer,
                             final int title,
                             final int context,
                             final int body,
                             final int advertiser,
                             final int button,
                             final int native_container,
                             final int price,
                             final int stars,
                             final int store) {

        switch (adtype){
            case ADMOB:

                final UnifiedNativeAdView nativeAdView = (UnifiedNativeAdView) activity.getLayoutInflater().inflate(R.layout.native_admob_layout, null);
                LayoutInflater inflater = LayoutInflater.from(this.context);
                LinearLayout adView = (LinearLayout) inflater.inflate(nativelayout, nativeAdView, false);
                nativeAdView.addView(adView);


                AdLoader.Builder builder = new AdLoader.Builder(this.context, "ca-app-pub-3940256099942544/2247696110");

                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    // OnUnifiedNativeAdLoadedListener implementation.
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;

                        populateUnifiedNativeAdView(unifiedNativeAd, nativeAdView, media, icon, title, body, advertiser, button, price, stars, store);
                        ad_layout.addView(nativeAdView);
                    }

                });


                AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        ad_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (nativeListener!=null) nativeListener.onNativeClicked();
                    }
                }).build();

                adLoader.loadAd(new AdRequest.Builder().build());
                break;
            case FACEBOOK:
                final NativeAd nativeAd = new NativeAd(this.context, "IMG_16_9_LINK#YOUR_PLACEMENT_ID");

                nativeAd.setAdListener(new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        adError.getErrorMessage();
                        ad_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Race condition, load() called again before last ad was displayed
                        if (nativeAd == null || nativeAd != ad) {
                            return;
                        }
                        // Inflate Native Ad into Container
                        inflateAd(nativeAd,
                                media,
                                icon,
                                nativelayout,
                                adchoicescontainer,
                                title,
                                context,
                                body,
                                advertiser,
                                button,
                                native_container);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        if (nativeListener!=null) nativeListener.onNativeClicked();
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                });

                // Request an ad
                nativeAd.loadAd();
                break;
            case OFF:
                break;
        }

    }

    private void inflateAd(NativeAd nativeAd,
                           int media,
                           int icon,
                           int nativelayout,
                           int adchoicescontainer,
                           int title,
                           int context,
                           int body,
                           int advertiser,
                           int button,
                           int native_container) {

        nativeAd.unregisterView();

        NativeAdLayout nativeAdLayout = ((Activity) this.context).findViewById(native_container);

        LayoutInflater inflater = LayoutInflater.from(this.context);
        LinearLayout adView = (LinearLayout) inflater.inflate(nativelayout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        LinearLayout adChoicesContainer = adView.findViewById(adchoicescontainer);
        AdOptionsView adOptionsView = new AdOptionsView(this.context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        AdIconView nativeAdIcon = null;
        LinearLayout nativeadicon = null;
        TextView nativeAdTitle = null;
        MediaView nativeAdMedia = null;
        LinearLayout AdMedia = null;
        TextView nativeAdSocialContext = null;
        TextView nativeAdBody = null;
        TextView sponsoredLabel = null;
        Button nativeAdCallToAction = null;

        if (icon!=0) {
            nativeAdIcon = new AdIconView(this.context);
            nativeadicon = adView.findViewById(icon);
            nativeadicon.addView(nativeAdIcon);
        }
        if (title!=0) {
            nativeAdTitle = adView.findViewById(title);
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
        }
        if (media!=0) {
            nativeAdMedia = new MediaView(this.context);
            AdMedia = adView.findViewById(media);
            AdMedia.addView(nativeAdMedia);
        }
        if (context!=0) {
            nativeAdSocialContext = adView.findViewById(context);
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        }
        if (body!=0) {
            nativeAdBody = adView.findViewById(body);
            nativeAdBody.setText(nativeAd.getAdBodyText());
        }
        if (advertiser!=0) {
            sponsoredLabel = adView.findViewById(advertiser);
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        }
        if (button!=0) {
            nativeAdCallToAction = adView.findViewById(button);
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        }


        List<View> clickableViews = new ArrayList<>();
        if (title!=0) {
            clickableViews.add(nativeAdTitle);
        }
        if (button!=0) {
            clickableViews.add(nativeAdCallToAction);
        }
        if (media!=0){
            clickableViews.add(AdMedia);
        }

        if (media!=0 && icon!=0) {
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews);
        }else if (icon==0){
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    clickableViews);
        } else if (media==0){
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdIcon,
                    clickableViews);
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd,
                                             UnifiedNativeAdView adView,
                                             int media,
                                             int icon,
                                             int title,
                                             int body,
                                             int advertiser,
                                             int button,
                                             int price,
                                             int stars,
                                             int store) {

        LinearLayout med_layout = adView.findViewById(media);
        com.google.android.gms.ads.formats.MediaView mediaView = new com.google.android.gms.ads.formats.MediaView(this.context);
        med_layout.addView(mediaView);
        adView.setMediaView(mediaView);

        if (title!=0) {
            adView.setHeadlineView(adView.findViewById(title));
        }
        if (body!=0) {
            adView.setBodyView(adView.findViewById(body));
        }
        if (button!=0) {
            adView.setCallToActionView(adView.findViewById(button));
        }
        if (icon!=0) {
            adView.setIconView(adView.findViewById(icon));
        }
        if (price!=0) {
            adView.setPriceView(adView.findViewById(price));
        }
        if (stars!=0) {
            adView.setStarRatingView(adView.findViewById(stars));
        }
        if (store!=0) {
            adView.setStoreView(adView.findViewById(store));
        }
        if (advertiser!=0) {
            adView.setAdvertiserView(adView.findViewById(advertiser));
        }

        if (title!=0) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (body!=0) {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if (button!=0) {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        if (icon!=0) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if (price!=0) {
            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }
        }

        if (store!=0) {
            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }
        }

        if (stars!=0) {
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        if (advertiser!=0) {
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        }

        adView.setNativeAd(nativeAd);
    }
}
