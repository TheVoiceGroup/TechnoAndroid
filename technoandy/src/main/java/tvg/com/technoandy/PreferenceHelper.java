package tvg.com.technoandy;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public class PreferenceHelper {

    private Context context;
    private String MainKey;
    private Gson JSON = new Gson();
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();


    public PreferenceHelper(Context context, String MainKey){
        this.context = context;
        this.MainKey = MainKey;
    }

    public SharedPreferences SaveString(String Key, String Value){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }

    public SharedPreferences SaveInt(String Key, int Value){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }

    public SharedPreferences SaveBoolean(String Key, boolean Value){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }

    public SharedPreferences SaveFloat(String Key, float Value){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }

    public SharedPreferences SaveLong(String Key, long Value){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }

    public SharedPreferences SaveArray(String Key, Object array){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String Value = JSON.toJson(array);
        editor.putString(Key, Value);
        editor.apply();
        editor.commit();
        return preferences;
    }


    public <object> object GetArray(String Key, Class<object> array){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        String json = preferences.getString(Key, null);
        return JSON.fromJson(json, array);
    }

    public String GetString(String Key, String DefaultValue){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        return preferences.getString(Key, DefaultValue);
    }

    public int GetInt(String Key, int DefaultValue){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        return preferences.getInt(Key, DefaultValue);
    }

    public boolean GetBoolean(String Key, boolean DefaultValue){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        return preferences.getBoolean(Key, DefaultValue);
    }

    public float GetFloat(String Key, float DefaultValue){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        return preferences.getFloat(Key, DefaultValue);
    }

    public long GetLong(String Key, long DefaultValue){
        SharedPreferences preferences = context.getSharedPreferences(MainKey, Context.MODE_PRIVATE);
        return preferences.getLong(Key, DefaultValue);
    }
}
