# TechnoAndroid

#### Note: This is the private library of TheVoiceGroup. Only TheVoiceGroup's Employees are allowed to use this. Any issue or for further details email at support@thevoicegroup.com

### Advertisement

    Advertisement advertisement = new Advertisement(this);
        advertisement.LoadInterstitialAd(ADTYPE.ADMOB, "ca-app-pub-3940256099942544/1033173712");
        advertisement.ShowBannerAd(ADTYPE.ADMOB, ad_view, "ca-app-pub-3940256099942544/6300978111"); //ad_view must be LinearLayout

        void ShowAd(){
                advertisement.ShowInterstitialAD(ADTYPE.ADMOB);
        }

        advertisement.advertismentListener = new AdvertisementListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(MainActivity.this, "Ad Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                Toast.makeText(MainActivity.this, "Ad Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(MainActivity.this, "Ad Closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailed(String Error) {
                Toast.makeText(MainActivity.this, "Ad Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOff() {
                Toast.makeText(MainActivity.this, "Ad Off", Toast.LENGTH_SHORT).show();
            }
        };
