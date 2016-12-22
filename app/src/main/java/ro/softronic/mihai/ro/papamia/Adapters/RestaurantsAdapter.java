package ro.softronic.mihai.ro.papamia.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.POJOs.Restaurant;
import ro.softronic.mihai.ro.papamia.R;
import ro.softronic.mihai.ro.papamia.Utils.StringManipulation;

public class RestaurantsAdapter extends
        BaseAdapter implements Filterable{

    private Activity activity;
    private LayoutInflater inflater;
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    private List<Restaurant>originalData = null;
    private List<Restaurant>filteredData = null;
    private ItemFilter mFilter = new ItemFilter();


    private List<Restaurant> restaurantItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public RestaurantsAdapter(FragmentActivity activity, List<Restaurant> restaurantItems){
//        super(activity, resource, restaurantItems);
        this.activity = activity;
        this.restaurantItems = restaurantItems;
        this.filteredData = restaurantItems ;
        this.originalData = restaurantItems ;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int location) {
        return filteredData.get(location);
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
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
//            convertView.setBackgroundResource(R.color.colorPrimary);
//            convertView.setBackgroundColor(Color.parseColor("#848888"));

        } else {
            holder = (ViewHolder) convertView.getTag();
//            convertView.setBackgroundColor(Color.BLUE);
            //holder.ratingBar.getTag(position);
        }
        Restaurant r = filteredData.get(position);



        holder.name.setText(r.getName());
        TextView tx = holder.description;
        TextView tx_inchis = holder.timp_livrare;
        holder.description.setText(r.getDescriere());

        //????????
        if (r.getOpen() == 1){
            int color = Integer.parseInt("bdbdbd", 16)+0xFF000000;
            holder.name.setTextColor(color);
            holder.description.setTextColor(color);
            tx_inchis.setText("Inchis");
            tx_inchis.setTextColor(color);
        }
        else{
            holder.name.setTextColor(((MainActivity)this.activity).getResources().getColor(R.color.restaurant_deschis));
            holder.description.setTextColor(((MainActivity)this.activity).getResources().getColor(R.color.restaurant_deschis));
            String ajunge_in = "ajunge in " + r.getTimp_livrare() + " minute";
            tx_inchis.setText(ajunge_in);
            tx_inchis.setTextColor(((MainActivity)this.activity).getResources().getColor(R.color.restaurant_deschis));

        }
//        holder.name.setTextColor(((MainActivity)this.activity).getResources().getColor(R.color.inchis));}

        // fac liniuta la ajunge in 60minut


        if (r.getOpen() == 1) {
//            String s = r.getDescriere();
////            tx.setPaintFlags(tx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            tx.setText(s,TextView.BufferType.SPANNABLE);
//            Spannable spannable = (Spannable) tx.getText();
//            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);





//            tx_inchis.setTextColor(((MainActivity)this.activity).getResources().getColor(R.color.inchis));

        }else{

        }
        holder.thumbnail.setImageUrl(r.getThumbnailUrl(), imageLoader);

//        holder.rating.setText("Rating:" + String.valueOf(r.getRating()));
//        convertView.setBackgroundColor(Color.BLUE);


        return convertView;

    }
    public void sortData(int i){
        switch (i){
            case 0:
                Collections.sort(filteredData,Restaurant.Comparators.NAME);
                break;
            case 1:
                Collections.sort(filteredData, Restaurant.Comparators.TIMP_LIVRARE);
                break;
        }

        notifyDataSetChanged();
    }
    public void setOriginalData(){
        filteredData = originalData;
        notifyDataSetChanged();
    }

    public List<Restaurant> getList(){
        return filteredData;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private static class ViewHolder {

        private NetworkImageView thumbnail;
        private TextView rating;
        private TextView name;
        private TextView description;
        private  TextView timp_livrare;

        public ViewHolder(View view) {
//            rating = (TextView) view.findViewById(R.id.rating);
            name = (TextView) view.findViewById(R.id.name);
            thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
            description = (TextView) view.findViewById(R.id.description);
            timp_livrare = (TextView) view.findViewById(R.id.timp_livrare);


        }
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Restaurant> list = originalData;

            int count = list.size();
            final ArrayList<Restaurant> nlist = new ArrayList<Restaurant>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getSpecialitati();
                String[] specialitati_tokens = filterableString.split(",");
                Set<String> specialitati_tokens_set = new HashSet<String>(Arrays.asList(specialitati_tokens));

                Set filterString_set = new HashSet<String>(Arrays.asList(filterString.split(",")));
//                filterString_set.retainAll(specialitati_tokens_set);

                if (StringManipulation.isSetContainedInAnother(filterString_set,specialitati_tokens_set))
                    nlist.add(list.get(i));

//                specialitati_tokens_set.retainAll(filterString_set);

//                if (specialitati_tokens_set.size() == filterString_set.size())  nlist.add(list.get(i));
//                if (filterString_set.size() == 0) nlist.add(list.get(i));

//                if (filterableString.toLowerCase().contains(filterString)) {
//
//                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Restaurant>) results.values;
            notifyDataSetChanged();
        }

    }




}