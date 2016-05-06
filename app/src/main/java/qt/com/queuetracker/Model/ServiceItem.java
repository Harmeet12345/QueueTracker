package qt.com.queuetracker.Model;

/**
 * Created by vinove on 28/3/16.
 */
public class ServiceItem {
    private String service_name;
    private String service_logo;
    private String service_id;
    private String service_durarion;
    private boolean isEmpty;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }




    public boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }



    public String getService_durarion() {
        return service_durarion;
    }

    public void setService_durarion(String service_durarion) {
        this.service_durarion = service_durarion;
    }


    public String getService_logo() {
        return service_logo;
    }

    public void setService_logo(String service_logo) {
        this.service_logo = service_logo;
    }


    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }


}
