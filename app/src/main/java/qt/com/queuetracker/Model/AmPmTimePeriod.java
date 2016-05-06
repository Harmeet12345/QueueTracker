package qt.com.queuetracker.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vinove on 21/4/16.
 */
public class AmPmTimePeriod implements Parcelable {


    String isFull;
    String bookingHours;
    public static ArrayList<String> bookslot_list;


    // type 1= am and 2= pm


    public String getBookingHours() {
        return bookingHours;
    }

    public void setBookingHours(String bookingHours) {
        this.bookingHours = bookingHours;
    }


    public ArrayList getBookslot_list() {
        return bookslot_list;
    }

    public void setBookslot_list(ArrayList bookslot_list) {
        this.bookslot_list = bookslot_list;
    }


    public String getIsFull() {
        return isFull;
    }

    public void setIsFull(String isFull) {
        this.isFull = isFull;
    }

    public static final Parcelable.Creator<AmPmTimePeriod> CREATOR = new Creator<AmPmTimePeriod>() {
        public AmPmTimePeriod createFromParcel(Parcel source) {
            AmPmTimePeriod amPmTimePeriod = new AmPmTimePeriod();
            amPmTimePeriod.isFull = source.readString();
            amPmTimePeriod.bookingHours = source.readString();
//            amPmTimePeriod.bookslot_list = source.readArrayList(bookslot_list.getClass().getClassLoader());
            return amPmTimePeriod;
        }

        public AmPmTimePeriod[] newArray(int size) {
            return new AmPmTimePeriod[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(isFull);
        parcel.writeString(bookingHours);
//        parcel.writeList(bookslot_list);


    }

}
