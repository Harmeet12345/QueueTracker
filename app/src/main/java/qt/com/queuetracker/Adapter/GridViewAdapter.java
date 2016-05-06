package qt.com.queuetracker.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import qt.com.queuetracker.Model.CompanyItem;
import qt.com.queuetracker.R;


/**
 * Created by vinove on 23/3/16.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<CompanyItem> company_itemlist;
    private List<CompanyItem> companylist = null;
    ArrayList<CompanyItem> mStringFilterList;


    public GridViewAdapter(Context c, ArrayList<CompanyItem> company_item) {
        mContext = c;

        this.company_itemlist = company_item;
        mStringFilterList = company_item;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return company_itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return company_itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        final CompanyItem companydetail = company_itemlist.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {


            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.txt_gridItem);
            ImageView imageView = (ImageView) grid.findViewById(R.id.img_gridIcon);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setScaleX(1.0f);
            imageView.setScaleY(1.0f);
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .showImageOnLoading(R.mipmap.ic_launcher).build();


            textView.setText(companydetail.getCompany_name());

//            Bitmap bit = imageLoader.loadImageSync(companydetail.getCompany_logo());

            imageLoader.displayImage(companydetail.getCompany_logo(), imageView, options);

        } else {
            grid = (View) convertView;
        }


        return grid;
    }


}