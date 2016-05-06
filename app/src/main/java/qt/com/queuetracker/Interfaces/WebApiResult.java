package qt.com.queuetracker.Interfaces;

import org.json.JSONObject;

import qt.com.queuetracker.utils.Constants;

/**
 * Created by vinove on 23/3/16.
 */
public interface WebApiResult {
    void getWebResult(Constants.SERVICE_TYPE type,String result);
}
