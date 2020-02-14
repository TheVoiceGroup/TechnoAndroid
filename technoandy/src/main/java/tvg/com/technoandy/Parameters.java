package tvg.com.technoandy;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Parameters {

     private HashMap<String, String> params = new HashMap();

//    public void put(String key, File file){
//
//    }

    public void put(String key, Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStreamObject ;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        String image = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        params.put(key, image);
    }

    public void put(String key, int value){
        params.put(key, String.valueOf(value));
    }

    public void put(String key, String value){
        params.put(key, value);
    }

    public HashMap<String, String> getParams() {
        return params;
    }
}
