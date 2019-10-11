package tvg.com.technoandy;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private Context context;
    private String MainKey;

    public PreferenceHelper(Context context, String MainKey){
        this.context = context;
        this.MainKey = MainKey;
    }

    public SharedPreferences Save(String Key, String Value){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }

    public String GetSavedValue(String Key){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        return preferences.getString(Key, null);
    }
}
