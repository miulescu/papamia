package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.CategoriiActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Adapters.RestaurantsAdapter;
import ro.softronic.mihai.ro.papamia.Adapters.SeparatedListAdapter;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.POJOs.Restaurant;
import ro.softronic.mihai.ro.papamia.R;
import ro.softronic.mihai.ro.papamia.Utils.MyRequest;
import ro.softronic.mihai.ro.papamia.Utils.NetworkUtils;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class RestaurantsFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog = null;

//    private ArrayList<Restaurant> restaurantsList;

    private static final String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private ProgressDialog pDialog;
    private List<Restaurant> restaurantsList =  new ArrayList<Restaurant>();
    private List<Restaurant> closed_restaurantsList =  new ArrayList<Restaurant>();
    private List<Restaurant> opened_restaurantsList =  new ArrayList<Restaurant>();
    private ListView listView;
    private RestaurantsAdapter mRestaurantsAdapter;
    private SeparatedListAdapter mRestaurantsSeparatedAdapter;
    private ProgressBar mProgressBar;
    private Dialog pd;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean paused = false;

    public RestaurantsFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        preferences= getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //lista de restaurante
        listView = (ListView)rootView.findViewById(R.id.listOpen);
        listView.setAdapter(null);

        mRestaurantsAdapter = new RestaurantsAdapter(this.getActivity() , restaurantsList);
        listView.setAdapter(mRestaurantsAdapter);

        //dialogul in care alegem orasul si zona
        //TODO: de trasmis orasul si zona in fragmetul cu restaurantele
        pd = new Dialog(this.getActivity(), android.R.style.Theme_Black);
        final View view = inflater.inflate(R.layout.remove_border, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawableResource(R.drawable.background_progress_gradient);
        pd.setContentView(view);
        pd.show();

        executeRequest();

//            execute(inflater);

//        JsonArrayRequest movieReq = new JsonArrayRequest(URL,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        hidePDialog();
//
//                        // Parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//
//                                JSONObject obj = response.getJSONObject(i);
//                                Restaurant restaurant = new Restaurant();
//                                restaurant.setName(obj.getString("rest_nume"));
//                                restaurant.setThumbnailUrl(obj.getString("rest_logo_url"));
////                                restaurant.setRating(((Number) obj.get("Adrese_idAdrese"))
////                                        .doubleValue());
//                                restaurant.setDescriere(obj.getString("rest_descriere"));
////                                int restid  = Integer.parseInt(obj.getString("rest_id"));
//                                restaurant.setRestID(obj.getString("rest_id"));
//                                restaurant.setTimp_livrare(obj.getInt("info_timp_livrare"));
//                                restaurant.setSpecialitati(obj.getString("info_specialitati"));
//                                restaurant.setPret_transport(obj.getDouble("info_transport"));
//                                restaurant.setEmail(obj.getString("adr_email"));
//                                restaurant.setPhone_no(obj.getString("adr_phone"));
//
//                                int ora_deschidere = (Integer.parseInt(obj.getString("info_ora_deschidere")));
//                                int ora_inchidere = (Integer.parseInt(obj.getString("info_ora_inchidere")));
//
//                                Calendar c = Calendar.getInstance();
//                                int hour = c.get(Calendar.HOUR_OF_DAY);
//                                Integer open = (hour <= ora_inchidere & hour >= ora_deschidere) ? 0 : 1 ;
//                                restaurant.setOpen(open);
//
//                                restaurantsList.add(restaurant);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        //mai intai apar cele deschise (fac asta pe client)
//                        Collections.sort(restaurantsList,new Comparator<Restaurant>() {
//                            @Override
//                            public int compare(Restaurant pub1, Restaurant pub2) {
//
//                                return pub1.getOpen().compareTo(pub2.getOpen());
//                            }
//                        });
//
////                        Collections.reverse(restaurantsList);//mai bine schimb 0 cu 1 ia timp
//
//                        // notifying list adapter about data changes
//                        // so that it renders the list view with updated data
//                        mRestaurantsAdapter.notifyDataSetChanged();
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                hidePDialog();
//                //stop circle-- invisible visible button refresh
////                if (!NetworkUtils.isNetworkConnected(getActivity()))
////                {
////
////                    view.findViewById(R.id.progressBar1).setVisibility(View.GONE);
////                    view.findViewById(R.id.textProgress).setVisibility(View.GONE);
////                    Button btn_iesire = (Button)view.findViewById(R.id.iesire);
////                    btn_iesire.setVisibility(View.VISIBLE);
////
////                    Button btn_reincerc = (Button)view.findViewById(R.id.reincerc);
////
////                    btn_reincerc.setVisibility(View.VISIBLE);
////                    btn_iesire.setOnClickListener(new View.OnClickListener() {
////
////                        @Override
////                        public void onClick(View v) {
////                            NetworkUtils.stopApp(getActivity());
////                        }
////                    });
////                    btn_reincerc.setOnClickListener(new View.OnClickListener() {
////
////                        @Override
////                        public void onClick(View v) {
//////                        construct(inflater);
////
////
////                        }
////                    });
////
////
////                }
//
//
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//            }
//        });


//        pot seta dinamic aranjamentul intr-un relativelayout!!
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)listView.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.layout.footer_listview_restaurants);
//        listView.setLayoutParams(params);


        final View footer =inflater.inflate(R.layout.footer_listview_restaurants, null, false);
        listView.addFooterView(footer, null, false);

        //fac asta pentru ca se vede urat la incarcarea listviewului
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                footer.setVisibility(View.VISIBLE);
            }
        }, 4000);

        LinearLayout btn_recomanda = (LinearLayout)footer.findViewById(R.id.recomanda);
        btn_recomanda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // de lucrat pentru a baga intr-o baza de date (REST)
                Toast.makeText(getActivity(), "Vom trimite un SMS cu recomandarea DVS.", Toast.LENGTH_LONG).show();
                String nume_restaurant_recomandat   = ((EditText)footer.findViewById(R.id.edit_restaurant)).getText().toString();
                String locatia_restaurant_recomandat  = ((EditText)footer.findViewById(R.id.edit_locatia)).getText().toString();
                String telefon_restaurant_recomandat  = ((EditText)footer.findViewById(R.id.edit_telefon)).getText().toString();

                StringBuilder sb = new StringBuilder();
                sb.append(nume_restaurant_recomandat);
                sb.append(locatia_restaurant_recomandat);
                sb.append(telefon_restaurant_recomandat);

                try {
                    SmsManager smsManager = SmsManager.getDefault();

                    String str= sb.toString();
                    ArrayList<String> detrimis  = smsManager.divideMessage(str);

                    smsManager.sendMultipartTextMessage("+40746048564", null, detrimis, null, null);
                    Toast.makeText(getActivity(), "Multumim ! ", Toast.LENGTH_LONG).show();
                }

                catch (Exception e) {
                    Toast.makeText(getActivity(), "Nu am putut trimite SMS, incearca mai tarziu", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
//

//        movieReq.setRetryPolicy(new DefaultRetryPolicy(10 *1000, 10,
//                1f));
//        AppController.getInstance().addToRequestQueue(movieReq);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // check restaurant inchis
                SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.preference_file_key), 0);
                boolean filtered = settings.getBoolean("Filtered", false);
                if (listView.getHeaderViewsCount() > 0 ) position--;
//                if (filtered) position--;  //adica avem header (are valoare 0)

                Restaurant restaurant  = (Restaurant)mRestaurantsAdapter.getItem(position);
                Bundle extras = new Bundle();
                extras.putString("restID", restaurant.getRestID());
                extras.putDouble("rest_pretTransport", restaurant.getPret_transport());
                editor.putString("restaurant_timpLivrare", String.valueOf(restaurant.getTimp_livrare()));
                editor.putString("restaurant_id", restaurant.getRestID());
                editor.putFloat("restaurant_transport", (float)restaurant.getPret_transport());
                editor.putString("restaurant_telefon", restaurant.getPhone_no());
                editor.putString("restaurant_email", restaurant.getEmail());
                editor.putBoolean("restaurant_has_offers", restaurant.getHas_offers());
                editor.apply();

                if (restaurant.getOpen() == 1){
                    extras.putInt("flag_open",1);
                }else{
                    extras.putInt("flag_open", 0);
                }

                Intent intent = new Intent(getActivity(), CategoriiActivity.class).putExtras(extras);
                // ("restID", restaurant.getRestID());
                startActivity(intent);
//                Toast.makeText(getActivity(), restaurant.getName(), Toast.LENGTH_SHORT).show();
            }
        });

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
            mProgressBar.setVisibility(View.GONE);
            mProgressBar = null;

        }
        pd.cancel();
//        getActivity().finish();

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public RestaurantsAdapter getAdapter(){
        return this.mRestaurantsAdapter;
    }
    public ListView getListView(){return this.listView;}

    public void execute(final LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.remove_border, null);
        JsonArrayRequest movieReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Restaurant restaurant = new Restaurant();
                                restaurant.setName(obj.getString("rest_nume"));
                                restaurant.setThumbnailUrl(obj.getString("rest_logo_url"));
//                                restaurant.setRating(((Number) obj.get("Adrese_idAdrese"))
//                                        .doubleValue());
                                restaurant.setDescriere(obj.getString("rest_descriere"));
//                                int restid  = Integer.parseInt(obj.getString("rest_id"));
                                restaurant.setRestID(obj.getString("rest_id"));
                                restaurant.setTimp_livrare(obj.getInt("info_timp_livrare"));
                                restaurant.setSpecialitati(obj.getString("info_specialitati"));
                                restaurant.setPret_transport(obj.getDouble("info_transport"));
                                restaurant.setEmail(obj.getString("adr_email"));
                                restaurant.setPhone_no(obj.getString("adr_phone"));

                                restaurant.setHas_offers(obj.getString("info_has_offers"));


                                int ora_deschidere = (Integer.parseInt(obj.getString("info_ora_deschidere")));
                                int ora_inchidere = (Integer.parseInt(obj.getString("info_ora_inchidere")));

                                Calendar c = Calendar.getInstance();
                                int hour = c.get(Calendar.HOUR_OF_DAY);
                                Integer open = (hour <= ora_inchidere & hour >= ora_deschidere) ? 0 : 1 ;
                                restaurant.setOpen(open);

                                restaurantsList.add(restaurant);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //mai intai apar cele deschise (fac asta pe client)
                        Collections.sort(restaurantsList,new Comparator<Restaurant>() {
                            @Override
                            public int compare(Restaurant pub1, Restaurant pub2) {

                                return pub1.getOpen().compareTo(pub2.getOpen());
                            }
                        });

//                        Collections.reverse(restaurantsList);//mai bine schimb 0 cu 1 ia timp

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mRestaurantsAdapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                hidePDialog();
                //stop circle-- invisible visible button refresh
//                if (!NetworkUtils.isNetworkConnected(getActivity()))
//                {

                    ProgressBar pb = (ProgressBar)view.findViewById(R.id.progressBar1);
                    pb.setVisibility(View.GONE);
                    TextView tx_incarc = (TextView)view.findViewById(R.id.textProgress);
                    tx_incarc.setVisibility(View.GONE);
                    Button btn_iesire = (Button)view.findViewById(R.id.iesire);
                    btn_iesire.setVisibility(View.VISIBLE);

                    Button btn_reincerc = (Button)view.findViewById(R.id.reincerc);

                    btn_reincerc.setVisibility(View.VISIBLE);
                    btn_iesire.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            NetworkUtils.stopApp(getActivity());


                        }
                    });
                    btn_reincerc.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            execute(inflater);
                        }
                    });

//                }


                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public  void executeRequest() {
        AppController.getInstance().addToRequestQueue(new MyRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                restaurantsList.clear();
                hidePDialog();
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        Restaurant restaurant = new Restaurant();
                        restaurant.setName(obj.getString("rest_nume"));
                        restaurant.setThumbnailUrl(obj.getString("rest_logo_url"));
//                                restaurant.setRating(((Number) obj.get("Adrese_idAdrese"))
//                                        .doubleValue());
                        restaurant.setDescriere(obj.getString("rest_descriere"));
//                                int restid  = Integer.parseInt(obj.getString("rest_id"));
                        restaurant.setRestID(obj.getString("rest_id"));
                        restaurant.setTimp_livrare(obj.getInt("info_timp_livrare"));
                        restaurant.setSpecialitati(obj.getString("info_specialitati"));
                        restaurant.setPret_transport(obj.getDouble("info_transport"));
                        restaurant.setEmail(obj.getString("adr_email"));
                        restaurant.setPhone_no(obj.getString("adr_phone"));



                        int ora_deschidere = (Integer.parseInt(obj.getString("info_ora_deschidere")));
                        int ora_inchidere = (Integer.parseInt(obj.getString("info_ora_inchidere")));

                        Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        Integer open = (hour <= ora_inchidere & hour >= ora_deschidere) ? 0 : 1 ;
                        restaurant.setOpen(open);

                        restaurantsList.add(restaurant);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //mai intai apar cele deschise (fac asta pe client)
                Collections.sort(restaurantsList,new Comparator<Restaurant>() {
                    @Override
                    public int compare(Restaurant pub1, Restaurant pub2) {

                        return pub1.getOpen().compareTo(pub2.getOpen());
                    }
                });
//                        Collections.reverse(restaurantsList);//mai bine schimb 0 cu 1 ia timp

                // notifying list adapter about data changes
                // so that it renders the list view with updated data

                mRestaurantsAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error instanceof TimeoutError) {
                    // note : may cause recursive invoke if always timeout.
                    executeRequest();
//                }
            }
        }));
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
       paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Get the Camera instance as the activity achieves full user focus
        if (paused){
            executeRequest();
            paused = false;
        }
    }

    @Override
    public void onDestroyView() {
        //mContainer.removeAllViews();
        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.container);
        mContainer.removeAllViews();
        super.onDestroyView();
    }


}








