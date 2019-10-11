package tvg.com.technoandyexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import tvg.com.technoandy.PreferenceHelper;

public class MainActivity extends AppCompatActivity {

    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceHelper = new PreferenceHelper(this, "APPNAME");
        preferenceHelper.Save("Key", "Value");
        String savedvalue = preferenceHelper.GetSavedValue("Key");
    }
}
