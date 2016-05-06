package qt.com.queuetracker.Model;

import java.io.Serializable;

/**
 * Created by vinove on 23/3/16.
 */
public class CompanyItem implements Serializable{


    private String company_name;
    private String company_logo;

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    private String company_id;

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }


    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }




}
