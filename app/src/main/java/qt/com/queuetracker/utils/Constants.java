package qt.com.queuetracker.utils;

/**
 * Created by vinove on 23/3/16.
 */
public class Constants {

    public static Boolean Splash=false;
    public static String Language="en";






    public static enum SERVICE_TYPE{
        GET_DATA,SEND_DATA,GET_LOCATION,SEND_LOCATION_PIN{
            @Override
        public String toString(){
                return "Policy";
            }
        }
    }
}
