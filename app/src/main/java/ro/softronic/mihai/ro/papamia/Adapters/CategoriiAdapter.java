package ro.softronic.mihai.ro.papamia.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ro.softronic.mihai.ro.papamia.POJOs.Categorie;
import ro.softronic.mihai.ro.papamia.R;

public class CategoriiAdapter extends
        BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private List<Categorie> categoriiItems;



    public CategoriiAdapter(FragmentActivity activity, List<Categorie> categoriiItems){
        this.activity = activity;
        this.categoriiItems = categoriiItems;
    }

    @Override
    public int getCount() {
        return categoriiItems.size();
    }

    @Override
    public Object getItem(int location) {
        return categoriiItems.get(location);
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
            convertView = inflater.inflate(R.layout.categorii_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }
        Categorie c = categoriiItems.get(position);

        holder.cat_name.setText(c.getName());
//        Typeface face= Typeface.createFromAsset(activity.getAssets(), "fonts/Ubuntu-B.ttf");
//        holder.cat_name.setTypeface(face);

        return convertView;

    }

    private static class ViewHolder {

        private TextView cat_name;

        public ViewHolder(View view) {
            cat_name = (TextView) view.findViewById(R.id.cat_name);


        }
    }


}