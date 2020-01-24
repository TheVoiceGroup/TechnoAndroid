package tvg.com.technoandy;

public interface AdvertisementListener {
    void onAdLoaded();
    void onAdClicked();
    void onAdClosed();
    void onAdFailed(String Error);
    void onAdOff();
}
