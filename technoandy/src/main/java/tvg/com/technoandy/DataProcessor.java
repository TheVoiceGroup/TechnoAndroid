package tvg.com.technoandy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DataProcessor {

    boolean check = true;

    public String HTTPRequest(String requestURL, HashMap<String, String> Data) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            URL url;
            HttpURLConnection httpURLConnectionObject ;
            OutputStream OutPutStream;
            BufferedWriter bufferedWriterObject;
            BufferedReader bufferedReaderObject;
            int RC;
            url = new URL(requestURL);

            httpURLConnectionObject = (HttpURLConnection) url.openConnection();
            httpURLConnectionObject.setReadTimeout(15000);
            httpURLConnectionObject.setConnectTimeout(15000);
            httpURLConnectionObject.setRequestMethod("POST");
            httpURLConnectionObject.setDoInput(true);
            httpURLConnectionObject.setDoOutput(true);
            OutPutStream = httpURLConnectionObject.getOutputStream();
            bufferedWriterObject = new BufferedWriter(new OutputStreamWriter(OutPutStream, "UTF-8"));
            bufferedWriterObject.write(bufferedWriterDataFN(Data));
            bufferedWriterObject.flush();
            bufferedWriterObject.close();
            OutPutStream.close();

            RC = httpURLConnectionObject.getResponseCode();
            if (RC == HttpsURLConnection.HTTP_OK) {
                bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                stringBuilder = new StringBuilder();
                String RC2;
                while ((RC2 = bufferedReaderObject.readLine()) != null){
                    stringBuilder.append(RC2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
        StringBuilder stringBuilderObject;
        stringBuilderObject = new StringBuilder();
        for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
            if (check)
                check = false;
            else
                stringBuilderObject.append("&");
            stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
            stringBuilderObject.append("=");
            stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
        }

        return stringBuilderObject.toString();
    }

}
