package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.CategoriiActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Adapters.ItemsAdapter;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.POJOs.RestaurantInfo;
import ro.softronic.mihai.ro.papamia.R;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class RestaurantInfoMapFragment extends Fragment {
    public static final int ADD_ITEMS = 1;

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog = null;

//    private ArrayList<Restaurant> restaurantsList;

    private static final String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private ProgressDialog pDialog;
    private List<Item> itemsList =  new ArrayList<Item>();
    private ListView listView;
    private ItemsAdapter mItemsAdapter;
    private String URLItemsCategorie = "";
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng GARLESTI = new LatLng(44.322017, 23.839724);
    private GoogleMap map;
    private LatLng RESTAURANT;

    public RestaurantInfoMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_restaurant_info_map, container, false);

        final TextView infodeschisView = (TextView)rootView.findViewById(R.id.info_deschis);
        final TextView infoorarView = (TextView)rootView.findViewById(R.id.info_orar);


        final CategoriiActivity activity = (CategoriiActivity) getActivity();
        RESTAURANT = activity.getLatLng();
        RestaurantInfo ri = activity.getRestaurantInfo();
        if (ri != null){
            infodeschisView.setText(ri.getInfo_deschis());
            infoorarView.setText(ri.getInfo_orar());
        }

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                .getMap();

        Marker hamburg = map.addMarker(new MarkerOptions().position(RESTAURANT)
                .title("Garlesti"));
        Marker kiel = map.addMarker(new MarkerOptions()
                .position(RESTAURANT)
                .title("Garlesti")
                .snippet("Garleti is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_delete_forever_black_24dp)));

        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(RESTAURANT, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

        //...



        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.clear();
//    }





}






