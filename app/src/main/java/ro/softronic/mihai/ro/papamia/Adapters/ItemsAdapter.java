package ro.softronic.mihai.ro.papamia.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.R;

public class ItemsAdapter extends
        BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private List<Item> Items;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public ItemsAdapter(FragmentActivity activity, List<Item> Items){
//        super(activity, resource, ItemItems);
        this.activity = activity;
        this.Items = Items;
    }

    @Override
    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int location) {
        return Items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (inflater==null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }
        Item r = Items.get(position);

        holder.name.setText(r.getName());
        holder.description.setText(r.getDescriere());
        holder.price.setText("RON " +  r.getPrice().toString());
        holder.thumbnail.setImageUrl(r.getThumbnailUrl(), imageLoader);


        return convertView;

    }

    private static class ViewHolder {

        private NetworkImageView thumbnail;

        private TextView name;
        private TextView description;
        private TextView price;


        public ViewHolder(View view) {

            name = (TextView) view.findViewById(R.id.item_name);
            thumbnail = (NetworkImageView) view.findViewById(R.id.item_thumbnail);
            description = (TextView) view.findViewById(R.id.item_description);
            price = (TextView) view.findViewById(R.id.item_price);


        }
    }

    public List<Item> getList(){
        return Items;
    }


}