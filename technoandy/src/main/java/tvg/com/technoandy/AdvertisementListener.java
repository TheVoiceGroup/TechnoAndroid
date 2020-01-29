package tvg.com.technoandy;

public interface AdvertisementListener {
    void onAdOpened(ADTYPE adtype);
    void onAdLoaded(ADTYPE adtype);
    void onAdClicked(ADTYPE adtype);
    void onAdClosed(ADTYPE adtype);
    void onAdFailed(ADTYPE adtype, String Error);
    void onAdNotLoaded(ADTYPE adtype);
    void onAdNotInitialized(ADTYPE adtype);
    void onAdOff(ADTYPE adtype);
}
