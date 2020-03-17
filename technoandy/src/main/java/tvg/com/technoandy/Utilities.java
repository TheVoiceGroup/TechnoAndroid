package tvg.com.technoandy;

import android.content.Context;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Utilities {

    private Context context;

    public Utilities(Context context){
        this.context = context;
    }

    public String EdittextFieldsValidationResult(ArrayList<EditText> list) {
        String checking = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().toString().trim().equalsIgnoreCase("")) {
                list.get(i).setError("Please Fill this field");
                checking = "Failed";
                break;
            } else {
                checking = "Success";
            }
        }
        if (checking.equals("Success")){
            return "Success";
        }else {
            return "Failed";
        }
    }

    public String FormatNumber(String number){
        try {
            double amount = Double.parseDouble(number);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(amount);
        }catch (NumberFormatException e){
            return "N/A";
        }
    }
}
