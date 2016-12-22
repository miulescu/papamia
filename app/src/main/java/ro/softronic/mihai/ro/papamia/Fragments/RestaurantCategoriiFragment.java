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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.CategoriiActivity;
import ro.softronic.mihai.ro.papamia.Activities.ItemsActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Adapters.CategoriiAdapter;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.POJOs.Categorie;
import ro.softronic.mihai.ro.papamia.R;

public class RestaurantCategoriiFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog = null;

//    private ArrayList<Restaurant> restaurantsList;

    private static  String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private ProgressDialog pDialog;
    private List<Categorie> categoriiList =  new ArrayList<Categorie>();
    private ListView listView;
    private CategoriiAdapter mCategoriiAdapter;
    private String URLCategorii = "";
    private ProgressBar mProgressBar;


    public RestaurantCategoriiFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_categorii, container, false);



        ListView listView = (ListView)rootView.findViewById(R.id.list_rest_categorii);
        mCategoriiAdapter = new CategoriiAdapter(this.getActivity() , categoriiList);
        listView.setAdapter(mCategoriiAdapter);
        final CategoriiActivity activity = (CategoriiActivity) getActivity();



        final String restID = activity.getRestID();
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        sb.append("/");
        sb.append(restID);
        sb.append("/categorii");
        URLCategorii = sb.toString();
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgress);
        mProgressBar.setVisibility(View.VISIBLE);




        JsonArrayRequest categoriiReq = new JsonArrayRequest(URLCategorii,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());



                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Categorie categorie = new Categorie();
                                categorie.setName(obj.getString("cat_nume"));
                                categorie.setCatID(Integer.parseInt(obj.getString("cat_id")));

                                // Genre is json array


                                // adding movie to movies array
                                categoriiList.add(categorie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        hidePDialog();

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mCategoriiAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        AppController.getInstance().addToRequestQueue(categoriiReq);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Categorie categorie  = (Categorie) mCategoriiAdapter.getItem(position);
                Bundle extras = new Bundle();
                extras.putString("catID",Integer.toString(categorie.getCatID()));
                extras.putString("restID",restID);
                extras.putString("cat_nume", categorie.getName());

                Intent intent = new Intent(getActivity(), ItemsActivity.class).putExtras(extras);
                startActivity(intent);
                Toast.makeText(getActivity(), categorie.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        if (AppController.getInstance().isFabVisible | AppController.getInstance().fabQty != 0)
        {
            FloatingActionButton cartFAB = (FloatingActionButton) rootView.findViewById(R.id.fab);

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
    if (mProgressBar != null) {
        mProgressBar.setVisibility(View.GONE);
        mProgressBar = null;

    }
}

    public List<Categorie> getList(){
        return categoriiList;
    }


}