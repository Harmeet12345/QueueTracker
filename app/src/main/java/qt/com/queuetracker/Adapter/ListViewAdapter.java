package qt.com.queuetracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import qt.com.queuetracker.R;
import qt.com.queuetracker.Model.BranchDetails;
import qt.com.queuetracker.ui.MapScreen;

/**
 * Created by vinove on 25/3/16.
 */
public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<BranchDetails> listItems;
    Typeface face;

    public ListViewAdapter(Context context, ArrayList<BranchDetails> items) {
        this.mContext = context;
        this.listItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imgStatus,availability_img;
        TextView txtName, txtAddress, txtDistance;
        LinearLayout ly_mapview;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_Name);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txt_Address);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txt_distance);
            holder.imgStatus = (ImageView) convertView.findViewById(R.id.img_open);
            holder.availability_img = (ImageView) convertView.findViewById(R.id.txt_book);

            holder.ly_mapview = (LinearLayout) convertView.findViewById(R.id.ly_mapview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BranchDetails branch_item = listItems.get(position);

        holder.txtName.setText(branch_item.getBranch_name());
        face = Typeface.createFromAsset(mContext.getAssets(), "advent-pro.regular.ttf");
        holder.txtName.setTypeface(face);

        holder.txtDistance.setText(branch_item.getDistance());
        holder.txtDistance.setTypeface(face);


        holder.txtAddress.setText(Html.fromHtml(branch_item.getAddress() + "&#160" + branch_item.getCity()));
        face = Typeface.createFromAsset(mContext.getAssets(), "advent-pro.bold.ttf");
        holder.txtAddress.setTypeface(face);

        if (branch_item.getStatus().equals("open"))
            holder.imgStatus.setImageResource(R.drawable.clock_green);
        else
            holder.imgStatus.setImageResource(R.drawable.clock_red);

        if (branch_item.getAvailability().equals("Y"))
            holder.availability_img.setImageResource(R.drawable.enter);
        else
            holder.availability_img.setImageResource(R.drawable.exit);

        holder.ly_mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    double latitude = 40.714728;
                    double longitude = -73.998672;
                    String label = "Queue Tracker";
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = latitude + "," + longitude + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(mContext, MapScreen.class);
                    mContext.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
