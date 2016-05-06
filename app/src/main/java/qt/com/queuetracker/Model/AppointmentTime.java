package qt.com.queuetracker.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class AppointmentTime implements Parcelable {

    String minimumDuration;
   private static ArrayList<AmPmTimePeriod> time_slot_list;
    int type;


    public ArrayList<AmPmTimePeriod> getTime_slot_list() {
        return time_slot_list;
    }

    public void setTime_slot_list(ArrayList<AmPmTimePeriod> time_slot_list) {
        this.time_slot_list = time_slot_list;
    }


    public String getMinimumDuration() {
        return minimumDuration;
    }


    public void setMinimumDuration(String minimumDuration) {
        this.minimumDuration = minimumDuration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public static final Parcelable.Creator<AppointmentTime> CREATOR = new Creator<AppointmentTime>() {
        public AppointmentTime createFromParcel(Parcel source) {
            AppointmentTime appointment_time_list = new AppointmentTime();
            appointment_time_list.minimumDuration = source.readString();
            appointment_time_list.type = source.readInt();
//            appointment_time_list.time_slot_list=source.readTypedList(time_slot_list,AmPmTimePeriod.CREATOR);

//            source.readTypedList(time_slot_list, AmPmTimePeriod.CREATOR);
//


            return appointment_time_list;
        }

        public AppointmentTime[] newArray(int size) {
            return new AppointmentTime[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(minimumDuration);
        parcel.writeInt(type);
//        parcel.writeTypedList(time_slot_list);


    }
}
