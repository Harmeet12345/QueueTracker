package qt.com.queuetracker.Network;

import android.content.Context;
import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import qt.com.queuetracker.Interfaces.WebApiResult;
import qt.com.queuetracker.utils.Constants;

public class WebRequestSearch  extends AsyncTask<String, String, String> {

    String requestApi;
    Context mContext;
    ConnectionDetector status;
    boolean showProcessing;
    HttpURLConnection connection;
    Constants.SERVICE_TYPE mType;
    WebApiResult webServiceResult;
    Hashtable<String, String> postParameters;
    String request="";

    /**
     * Initizalize web address calling object
     * @param mContext
     * @param url
     * @param parameters
     * @param type
     * @param webResultInterface
     */
    public WebRequestSearch(Context mContext, String url, Hashtable<String, String> parameters, Constants.SERVICE_TYPE type,
                      WebApiResult webResultInterface) {
        this.mContext = mContext;
        this.requestApi = url;
        this.mType = type;
        this.postParameters = parameters;
        this.webServiceResult = webResultInterface;
        status = new ConnectionDetector(mContext);
    }
    public WebRequestSearch(Context mContext, String url, String request, Constants.SERVICE_TYPE type,
                      WebApiResult webResultInterface,String str) {
        this.mContext = mContext;
        this.requestApi = url;
        this.mType = type;
        this.request = request;
        this.webServiceResult = webResultInterface;
        status = new ConnectionDetector(mContext);
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            URL url = new URL(this.requestApi);
            System.out.println("REQUEST : " + url);

            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);







            //Send request
            if(postParameters != null) {

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(getPostParamString(postParameters));
                outputStream.flush();
                outputStream.close();
            }else if (!request.equals("")) {
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(request);
                outputStream.flush();
                outputStream.close();
            }

            //Get Response
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            bufferedReader.close();
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * Pass Hashtable of key value pair we need to post
     * @param params
     * @return
     */
    private String getPostParamString(Hashtable<String, String> params) {
        if(params.size() == 0)
            return "";
        StringBuffer buf = new StringBuffer();
        Enumeration<String> keys = params.keys();
        while(keys.hasMoreElements()) {
            buf.append(buf.length() == 0 ? "" : "&");
            String key = keys.nextElement();
            buf.append(key).append("=").append(params.get(key));
        }
        return buf.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(!(result==null)){
            webServiceResult.getWebResult(mType, result);
            System.out.println("RESPONSE : " + result);
        }
        else{
            if(!new ConnectionDetector(mContext).isConnectingToInternet()) {
//                .showAlertDialog(mContext, "No Network Connection", "Please check your internet connection.");
            }
        }
    }
}