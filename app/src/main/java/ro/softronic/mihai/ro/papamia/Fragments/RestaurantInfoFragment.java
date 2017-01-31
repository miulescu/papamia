package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.CategoriiActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Adapters.CategoriiAdapter;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.POJOs.Categorie;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.POJOs.RestaurantInfo;
import ro.softronic.mihai.ro.papamia.R;

import static ro.softronic.mihai.ro.papamia.R.id.transport;

public class RestaurantInfoFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog = null;

//    private ArrayList<Restaurant> restaurantsList;

    private static  String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private ProgressDialog pDialog;
    private List<Categorie> categoriiList =  new ArrayList<Categorie>();
    private ListView listView;
    private CategoriiAdapter mCategoriiAdapter;
    private String URLRestaurantInfo = "";
    private Dialog pd;
    MenuItem mi;


    public RestaurantInfoFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        setHasOptionsMenu(true);
        final ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

        final CategoriiActivity activity = (CategoriiActivity) getActivity();
        final TextView nameView = (TextView)rootView.findViewById(R.id.restaurant_name);
        final TextView detailsView = (TextView)rootView.findViewById(R.id.restaurant_description);
        final TextView ratingView = (TextView)rootView.findViewById(R.id.restaurant_rating);
        final NetworkImageView imageView = (NetworkImageView)rootView.findViewById(R.id.restaurant_image);
        final TextView transportView = (TextView)rootView.findViewById(transport);
        final TextView comandaView = (TextView)rootView.findViewById(R.id.comanda_minima);

        final String restID = activity.getRestID();
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        sb.append("/");
        sb.append(restID);

        URLRestaurantInfo = sb.toString();

        pd = new Dialog(this.getActivity(), android.R.style.Theme_Black);
        final View view = inflater.inflate(R.layout.remove_border, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawableResource(R.drawable.background_progress_gradient);
        pd.setContentView(view);
        pd.show();



        JsonArrayRequest itemReq = new JsonArrayRequest(URLRestaurantInfo,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        //make info menu icon visible
                        mi.setVisible(true);

                        // Parsing json

                        try {
                            JSONObject obj = response.getJSONObject(0);
                            Item item = new Item();
                            String name = obj.getString("rest_nume");
                            String rating = obj.getString("rest_nume");
                            String description = obj.getString("rest_descriere");
                            String comanda_minima = "RON " + obj.getString("info_comanda_minima");
                            String transport = "RON " + obj.getString("info_transport");
                            Double lat = Double.parseDouble(obj.getString("adr_lat"));
                            Double lng = Double.parseDouble(obj.getString("adr_long"));
                            String info_deschis = obj.getString("info_orar");
                            String ora_deschidere = obj.getString("info_ora_deschidere");
                            String ora_inchidere = obj.getString("info_ora_inchidere");
                            String info_orar = ora_deschidere + ":00-" + ora_inchidere +":00";



                            LatLng rest_gps = new LatLng(lat,lng);

                            activity.setLatLng(rest_gps);
//                                restaurant.setThumbnailUrl(obj.getString("rest_logo_url"));
                            imageView.setImageUrl(obj.getString("rest_logo_url"), mImageLoader);
                            activity.setTitle(name);
                            nameView.setText(name);
                            detailsView.setText(description);
                            transportView.setText(transport);
                            comandaView.setText(comanda_minima);

                            RestaurantInfo ri = new RestaurantInfo(info_deschis, info_orar, 0.0);
                            activity.setRestaurant_info(ri);


                            String has_offers = obj.getString("info_has_offers");





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
//                        mRestaurantsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
//        activity.findViewById(R.id.linlaHeaderProgress).setVisibility(View.GONE);
        AppController.getInstance().addToRequestQueue(itemReq);


//        if (AppController.getInstance().isFabVisible | AppController.getInstance().fabQty != 0)
//        {
//            FloatingActionButton cartFAB = (FloatingActionButton) rootView.findViewById(R.id.fab);
//
//            cartFAB.setVisibility(View.VISIBLE);
//            TextView cartQTY = (TextView) rootView.findViewById(R.id.cartQTY);
//            cartQTY.setText(Integer.toString(AppController.getInstance().fabQty));
//            cartQTY.setVisibility(View.VISIBLE);
//        }
//        else{
//            FloatingActionButton cartFAB = (FloatingActionButton) rootView.findViewById(R.id.fab);
//            cartFAB.setVisibility(View.GONE);
//            TextView cartQTY = (TextView) rootView.findViewById(R.id.cartQTY);
//            cartQTY.setVisibility(View.GONE);
//        }

        return rootView;

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Toast.makeText(getActivity(), "intoarcere", Toast.LENGTH_SHORT).show();
//
//
//    }
    private void hidePDialog() {
        pd.cancel();
        //        getActivity().finish();

}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        mi = menu.getItem(1);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();    //remove all items
        getActivity().getMenuInflater().inflate(R.menu.restaurantfragment, menu);
        mi = menu.getItem(1);
        mi.setVisible(true);
    }

//    @Override
//    public void onResume() {
//        Log.e("DEBUG", "onResume of LoginFragment");
//
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.restaurantfragment, menuF);
//        mi = menuF.findItem(R.id.action_info);
//        mi.setVisible(true);
//        super.onResume();
//
//
//    }






}