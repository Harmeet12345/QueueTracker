package qt.com.queuetracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import qt.com.queuetracker.ui.SelectServices;
import qt.com.queuetracker.ui.SelectTime;

/**
 * Created by vinove on 8/4/16.
 */
public class CircularAdapter extends BaseAdapter {

    private ArrayList<ServiceItem> service_itemlist;
    private static final String TAG = CircularAdapter.class.getSimpleName();
    Context mContext;


    public CircularAdapter(Context context, ArrayList<ServiceItem> service_itemlist) {
        this.mContext = context;
        this.service_itemlist = service_itemlist;

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

        final LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //  if (convertView == null) {

        convertView = mInflater.inflate(R.layout.circular_list_item, null);
        holder = new ViewHolder();
        holder.service_name = (TextView) convertView.findViewById(R.id.service_name);

        holder.service_img = (RoundImageView) convertView.findViewById(R.id.service_img);


        convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }


        final ServiceItem rowItem = service_itemlist.get(position);
        if (!rowItem.getEmpty()) {

            holder.service_name.setText(rowItem.getService_name());

            ImageLoader imageLoader = ImageLoader.getInstance();

            Bitmap servicelogo_bitmap = imageLoader.loadImageSync(rowItem.getService_logo());
            holder.service_img.setImageBitmap(servicelogo_bitmap);
            holder.service_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectTime.class);
                    Bundle bundle = new Bundle();


                    bundle.putString("branch_id", SelectServices.branch_id);
                    bundle.putString("company_id", SelectServices.companyid);
                    bundle.putString("service_id", rowItem.getService_id());

                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
//                    mContext.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



                }
            });
        }

        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        RoundImageView service_img;
        TextView service_name;

    }


}