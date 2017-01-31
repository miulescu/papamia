package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.AddItemActivity;
import ro.softronic.mihai.ro.papamia.Activities.ItemsActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Adapters.ItemsAdapter;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.R;

import static ro.softronic.mihai.ro.papamia.Utils.StringManipulation.toBoolean;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class RestaurantItemsFragment extends Fragment {
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
    private ProgressBar mProgressBar;


    public RestaurantItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.list_rest_categorii_items);
        mItemsAdapter = new ItemsAdapter(this.getActivity() , itemsList);
        listView.setAdapter(mItemsAdapter);

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.clearAnimation();

        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
        fab.startAnimation(scaleAnimation);



        ItemsActivity activity = (ItemsActivity) getActivity();
//        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        final String restID = activity.getRestID();
        final String catID = activity.getCatID();
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        sb.append("/");
        sb.append(restID);
        sb.append("/categorii");
        sb.append("/");
        sb.append(catID);

        URLItemsCategorie = sb.toString();

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgress);
//        mProgressBar.setVisibility(ProgressBar.VISIBLE);


        DecimalFormat df = new DecimalFormat("#.##");
        JsonArrayRequest movieReq = new JsonArrayRequest(URLItemsCategorie,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        //UPDATE UI
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Item item = new Item();
                                item.setItemID(obj.getString("Item_id"));
                                item.setName(obj.getString("item_name"));
                                item.setThumbnailUrl(obj.getString("item_img"));
                                item.setPrice(Double.parseDouble(obj.getString("item_price")));
//                                item.setRating(((Number) obj.get("Adrese_idAdrese"))
//                                        .doubleValue());
                                item.setDescriere(obj.getString("item_descriere"));
                                item.setExtras(toBoolean(obj.getString("item_has_extras")));
//                                item.setRestID(obj.getString("rest_id"));
                                // Genre is json array
                                // adding movie to movies array
                                itemsList.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mItemsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        AppController.getInstance().addToRequestQueue(movieReq);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item item  = (Item) mItemsAdapter.getItem(position);
                String itemID = item.getItemID();
                Bundle extras = new Bundle();
                extras.putString("catID",catID);
                extras.putString("restID",restID);
                extras.putString("itemID", itemID);
                extras.putString("item_nume",item.getName());
                extras.putBoolean("item_has_extras", item.isExtras());
                Intent intent = new Intent(getActivity(), AddItemActivity.class).putExtras(extras);
                startActivityForResult(intent, ADD_ITEMS);
//                Toast.makeText(getActivity(), categorie.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        if (AppController.getInstance().isFabVisible | AppController.getInstance().fabQty !=0)
        {
            FloatingActionButton cartFAB = (FloatingActionButton) rootView.findViewById(R.id.fab);
            cartFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Your FAB click action here...
//                    Toast.makeText(getActivity().getBaseContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            cartFAB.setVisibility(View.VISIBLE);
            TextView cartQTY = (TextView) rootView.findViewById(R.id.cartQTY);
            cartQTY.setText(Integer.toString(AppController.getInstance().fabQty));
            cartQTY.setVisibility(View.VISIBLE);
        }
        else{
            FloatingActionButton cartFAB = (FloatingActionButton) rootView.findViewById(R.id.fab);
            cartFAB.setVisibility(View.GONE);
            TextView cartQTY = (TextView) rootView.findViewById(R.id.cartQTY);
            cartQTY.setVisibility(View.GONE);
        }

        hidePDialog();

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
    private void hidePDialog() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            mProgressBar = null;

        }
    }

    public ItemsAdapter getAdapter(){
        return mItemsAdapter;
    }

    public List<Item> getItemsList(){
        return itemsList;
    }



}






