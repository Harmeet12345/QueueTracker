package qt.com.queuetracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import qt.com.queuetracker.CustomView.RoundImageView;
import qt.com.queuetracker.Model.ServiceItem;
import qt.com.queuetracker.R;
import qt.com.queuetracker.ui.SelectTime;

/**
 * Created by vinove on 8/4/16.
 */
public class CircularAdapter extends BaseAdapter {

    private ArrayList<ServiceItem> service_itemlist;
    private static final String TAG = CircularAdapter.class.getSimpleName();
    Context mContext;
    int actual_size;
    int middle_point;

    boolean checkCountBoolean=true;

    int incrementCounter,tempCounter=0,startCounter=0;

    public CircularAdapter(Context context, ArrayList<ServiceItem> service_itemlist) {
        this.mContext = context;
        this.service_itemlist = service_itemlist;
        this.actual_size = service_itemlist.size();
        middle_point = service_itemlist.size() / 2;
//        this.serviceIcon = ls_ServiceIcons;
    }

    @Override
    public int getCount() {
        return service_itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return service_itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.circular_list_item, null);
            holder = new ViewHolder();
            holder.service_name = (TextView) convertView.findViewById(R.id.service_name);

            holder.service_img = (RoundImageView) convertView.findViewById(R.id.service_img);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.service_name.setVisibility(View.VISIBLE);
        holder.service_img.setVisibility(View.VISIBLE);


        ServiceItem rowItem = service_itemlist.get(position);

        int center_Point=service_itemlist.size()/rowItem.getCount();

        int count=Math.round(center_Point);

        Toast.makeText(mContext,""+count,Toast.LENGTH_SHORT).show();

/*        if (position >= rowItem.getCount()) {

            holder.service_name.setVisibility(View.INVISIBLE);
            holder.service_img.setVisibility(View.INVISIBLE);
        }*/
        incrementCounter=rowItem.getCount();
        if (position>=count && startCounter==0)
        {
            holder.service_name.setVisibility(View.VISIBLE);
            holder.service_img.setVisibility(View.VISIBLE);

            if (tempCounter<=incrementCounter)
            {
                tempCounter++;
                if (tempCounter==incrementCounter)
                    startCounter=1;
            }

        }
        else
        {
            holder.service_name.setVisibility(View.INVISIBLE);
            holder.service_img.setVisibility(View.INVISIBLE);
        }

            holder.service_name.setText(rowItem.getService_name());

            ImageLoader imageLoader = ImageLoader.getInstance();

            Bitmap servicelogo_bitmap = imageLoader.loadImageSync(rowItem.getService_logo());
            holder.service_img.setImageBitmap(servicelogo_bitmap);
            holder.service_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectTime.class);
                    mContext.startActivity(intent);
                }
            });


        return convertView;
    }


    /*private view holder class*/
    private class ViewHolder {
        RoundImageView service_img;
        TextView service_name;

    }


}