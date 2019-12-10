package tvg.com.technoandy;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.text.format.Formatter;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.WIFI_SERVICE;

public class Logger {

    private Context context;
    private String API_LEVEL, DEVICE, MODEL, PRODUCT, FINGERPRINT, TYPE, BRAND, DISPLAY, MANUFACTURER, ANDROIDID, CLIENTID, IPADDRESS, LOCATION;
    PreferenceHelper preferenceHelper;
    private String ARRAYNAME;
    RootBeer rootBeer;

    public Logger(Context context, String PreferenceHelperMainKEY, String ARRAYNAME){
        this.context = context;
        preferenceHelper = new PreferenceHelper(context, PreferenceHelperMainKEY);
        this.ARRAYNAME = ARRAYNAME;
        rootBeer = new RootBeer(context);
    }

    public void LOGUSERDEVICEINFO(String USERURL, String TOKEN){
        ANDROIDID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        API_LEVEL = Build.VERSION.SDK;
        DEVICE = Build.DEVICE;
        MODEL = Build.MODEL;
        PRODUCT = Build.PRODUCT;
        FINGERPRINT = Build.FINGERPRINT;
        TYPE = Build.TYPE;
        BRAND = Build.BRAND;
        DISPLAY = Build.DISPLAY;
        MANUFACTURER = Build.MANUFACTURER;
        IPADDRESS = getIP();
        LOCATION = latlng();
        try {
            CLIENTID = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        LOGUSER(USERURL, TOKEN, ANDROIDID, CLIENTID, API_LEVEL, DEVICE, MODEL, PRODUCT, FINGERPRINT, TYPE, BRAND, DISPLAY, MANUFACTURER, IPADDRESS, LOCATION);
    }


    public void LOGUSER(String USERURL, String Device_ID, String ANDROIDID, String CLIENTID, String API_LEVEL, String DEVICE, String MODEL, String PRODUCT, String FINGERPRINT, String TYPE, String BRAND, String DISPLAY, String MANUFACTURER, String IPADDRESS, String LOCATION){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("device_id", Device_ID);
        params.put("android_id", ANDROIDID);
        params.put("client_id", CLIENTID);
        params.put("api_level", API_LEVEL);
        params.put("device", DEVICE);
        params.put("model", MODEL);
        params.put("product", PRODUCT);
        params.put("fingerprint", FINGERPRINT);
        params.put("type", TYPE);
        params.put("brand", BRAND);
        params.put("display", DISPLAY);
        params.put("manufacturer", MANUFACTURER);
        params.put("ip_address", IPADDRESS);
        params.put("location", LOCATION);
        params.put("device_rooted", rootBeer.isRooted());

        client.get(USERURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(ARRAYNAME);
                    JSONObject objJson;
                    objJson = jsonArray.getJSONObject(0);
                    String UserID = objJson.getString("ID");
                    String Status = objJson.getString("STATUS");
                    preferenceHelper.SaveString("ADUSERID",UserID);
                    preferenceHelper.SaveString("ADSTATUS",Status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void LOGACTIVITY(String URL, String ACTIVITYTYPE){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", preferenceHelper.GetString("ADUSERID", "0"));
        params.put("activity_type", ACTIVITYTYPE);

        client.get(URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(ARRAYNAME);
                    JSONObject objJson;
//                    for (int i = 0; i < jsonArray.length(); i++) {
                    objJson = jsonArray.getJSONObject(0);
//                    String UserID = objJson.getString("ID");
//                    String Status = objJson.getString("STATUS");
//                    preferenceHelper.SAVEUSERID(UserID);
//                    preferenceHelper.SAVEADSTATUS(Status);
//                    Ad();
//                        strMessage = objJson.getString(Constant.MSG);
//                        Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public String RETURNADSTATUS(){
        return preferenceHelper.GetString("ADSTATUS", "ACTIVE");
    }


//    public String getWifiIP() {
//        WifiManager wifiMgr = (WifiManager) context.getSystemService(WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//        int ip = wifiInfo.getIpAddress();
//        return  Formatter.formatIpAddress(ip);
//    }

    public String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String latlng(){
        String service = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) context.getSystemService(service);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        String CurrentLocation = "Lat:" + latitude + " , " + "Lng:" + longitude;
        return CurrentLocation;
    }
}
