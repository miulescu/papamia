package ro.softronic.mihai.ro.papamia.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.POJOs.FilterItem;
import ro.softronic.mihai.ro.papamia.R;

public class FilterAdapter extends
        BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private List<FilterItem> filterItems;
    private Context context;

    public FilterAdapter(Context context, List<FilterItem> filterItems){
        this.context = context;
        this.filterItems = filterItems;
        this.activity = (Activity)context;
    }

    @Override
    public int getCount() {
        return filterItems.size();
    }

    @Override
    public Object getItem(int location) {
        return filterItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if (inflater==null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.filter_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

            holder.chk.setTag(filterItems.get(position));
            //holder.ratingBar.getTag(position);
        }
        FilterItem f = filterItems.get(position);

        holder.filter_name.setText(f.getName());
        holder.chk.setSelected(f.getSelected());
        holder.chk.setOnCheckedChangeListener((MainActivity)context);

        //pot si asa dar nu mai am acces asa de usor la liste din mainactivity

//        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                FilterItem element  = (FilterItem)holder.chk.getTag();
//                element.setSelected(compoundButton.isChecked());
//
////                int pos = drawerView.getPositionForView(compoundButton) - 1 ;
//                CheckBox chk =(CheckBox)compoundButton;
//                if (chk.isChecked()){
//                    Log.d("checked", "true");
//                }
//                else{
//                    Log.d("checked", "false");
//                }
//                String filterString = element.getName();
////                Log.d("Elemente checked----->", String.valueOf(drawerView.getCheckedItemPosition()));
//
//                //aici o sa construiesc filterstring din mai multe
//                //este un array global unde pun la click si scot la unclick
//                //il fac empty la select all (radio button)
//
//                RestaurantsFragment fragment_obj = (RestaurantsFragment)((MainActivity)context).getSupportFragmentManager().
//                        findFragmentByTag("restaurants_frag");
//                Log.d("filterString---------->", filterString);
//                fragment_obj.getAdapter().getFilter().filter(filterString);
////               / Log.d("pozitia", String.valueOf(pos));
//
//
//            }
//        });

        holder.chk.setTag(f);




//        Typeface face= Typeface.createFromAsset(activity.getAssets(), "fonts/Ubuntu-B.ttf");
//        holder.cat_name.setTypeface(face);

        return convertView;

    }

    private static class ViewHolder {

        private TextView filter_name;
        private CheckBox chk;

        public ViewHolder(View view) {
            filter_name = (TextView) view.findViewById(R.id.filter_name);
            chk = (CheckBox)view.findViewById(R.id.chk);


        }
    }


}