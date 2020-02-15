package tvg.com.technoandy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;

public class TechnoPostData extends AsyncTask<Void,Void,String> {

    ProgressDialog progressDialog;
    private boolean progress = false;
    private Context context;
    private String progresstitle = "Uploading";
    private String progressmessage = "Please wait while your data is uploading";
    private String PostUrl;
    private HashMap<String, String> parameters;
    public PostResult postResult;

    public TechnoPostData(Context context, String PostUrl){
        this.context = context;
        this.PostUrl = PostUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progress){
            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, progresstitle, progressmessage, false, false);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progress){
            progressDialog.dismiss();
            postResult.onPost(s);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        DataProcessor dataProcessor = new DataProcessor();
        String FinalData = dataProcessor.HTTPRequest(PostUrl, parameters);
        return FinalData;
    }

    public void setProgress(boolean progress) {
        this.progress = progress;
    }

    public void setProgresstitle(String progresstitle) {
        this.progresstitle = progresstitle;
    }

    public void setProgressmessage(String progressmessage) {
        this.progressmessage = progressmessage;
    }

    public void Data(Parameters parameters){
        this.parameters = parameters.getParams();
    }

    public interface PostResult{
        void onPost(String s);
    }
}
