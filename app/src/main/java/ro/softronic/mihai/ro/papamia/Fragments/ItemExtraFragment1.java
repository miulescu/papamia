package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.AddItemActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.POJOs.Order;
import ro.softronic.mihai.ro.papamia.R;

import static java.lang.Integer.parseInt;
import static ro.softronic.mihai.ro.papamia.Fragments.RestaurantItemsFragment.ADD_ITEMS;
import static ro.softronic.mihai.ro.papamia.R.id.buttonADD;
import static ro.softronic.mihai.ro.papamia.R.id.buttonREMOVE;
import static ro.softronic.mihai.ro.papamia.R.id.item_qty;
import static ro.softronic.mihai.ro.papamia.R.id.nameView;
//import static ro.softronic.mihai.ro.papamia.R.id.textView;
import static ro.softronic.mihai.ro.papamia.R.id.pricetextView;
import static ro.softronic.mihai.ro.papamia.Utils.StringManipulation.getExceptLast4Chars;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class ItemExtraFragment1 extends Fragment implements View.OnClickListener{
    private boolean has_extras;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL = "https://cryptic-harbor-22680.herokuapp.com/extras/aditionale";
    private String URLItem = "";
    private ProgressDialog dialog = null;

    private View rootView;
    private List<String> extras;

//    private ArrayList<Restaurant> restaurantsList;

    private ProgressDialog pDialog;
    private TextView textQTY, textSumaTotala;
    private String price;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    // private ProgressBar mProgressBar;
    private Dialog pd;

    private LinearLayout rg_layout = null;
    private LinearLayout preturi_layout= null;
    private RadioGroup rg;

    private String text_aditional  = " ";
    private int itemQTY = 1;

    NumberFormat formatter = new DecimalFormat("#0.00");

    String text_extra_aditional;

    public ItemExtraFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean has_extra = (boolean)getArguments().get("has_extras");
        preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        rootView = inflater.inflate(R.layout.fragment_extra1, container, false);
        //toggle butonul comanda cu butonul next!!
        LinearLayout comanda_btn = (LinearLayout)rootView.findViewById(R.id.linearLayoutComanda);
//        LinearLayout next_btn = (LinearLayout)rootView.findViewById(R.id.linearLayoutNext);
        if (has_extra){
            comanda_btn.setVisibility(View.VISIBLE);
//            next_btn.setVisibility(View.VISIBLE);
        }
        final ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
        final AddItemActivity activity = (AddItemActivity) getActivity();
//        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        final String itemID = activity.getItemID();

        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        sb.append("/");
        sb.append(itemID);
        URLItem = sb.toString();

//        ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
//        NetworkImageView imageView = (NetworkImageView)convertView
//                .findViewById(R.id.gallery_item_imageView);
//        imageView.setDefaultImageResId(R.drawable.brian_up_close);
//        imageView.setImageUrl(item.getUrl(), mImageLoader);
        rg_layout = (LinearLayout) rootView.findViewById(R.id.radiogrouplayout);
        rg = (RadioGroup)rootView.findViewById(R.id.radiogroup);

        textSumaTotala = (TextView)rootView.findViewById(R.id.textSumaTotala);

        LinearLayout comandaclick =(LinearLayout)rootView.findViewById(R.id.linearLayoutComanda);
        comandaclick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //---pun in baza de date de pe telefon order items

                Intent data = new Intent();

                String name = activity.getmItemName();
                int qty  = activity.getmItemQty();
                double suma = activity.getmItemSumaTotala();
                //---set the data to pass back---

                data.putExtra("orderItemsQty",String.valueOf(qty));

                getActivity().setResult(ADD_ITEMS, data);

                AppController.getInstance().isFabVisible = true;
                AppController.getInstance().fabQty += qty;

                //order_item_id
                //---insert in database
//                OrderDatabaseHandler odb = new OrderDatabaseHandler(getActivity().getApplicationContext());
                OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();

                odb.addOrder(new Order(Integer.parseInt(itemID),
                        name + '-' + text_extra_aditional,
                        qty,
                        suma));
                List<Order> orders = odb.getAllOrders();
                Log.d("orders:", Integer.toString(orders.size()));

                editor.putString("ORDER_NR_ROWS", String.valueOf(orders.size()));
                editor.apply();
                //---close the activity---
                getActivity().finish();
//                startActivityForResult(new Intent(A_My_Galaxy.this,C_Student_Book_Planet.class), 0);
            }
        });
//        imageView.setDefaultImageResId(R.drawable.retail);


        pd = new Dialog(this.getActivity(), android.R.style.Theme_Black);
        View view = inflater.inflate(R.layout.remove_border_tr, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawableResource(R.drawable.backround_progress_gradient_tr);
        pd.setContentView(view);
        pd.show();

        JsonArrayRequest itemReq = new JsonArrayRequest(URLItem,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        try {
                            JSONObject obj = response.getJSONObject(0);
                            extras = new ArrayList<String>();
                            Item item = new Item();
                            Iterator<?> keys = obj.keys();

                            while( keys.hasNext() ) {
                                String key = (String) keys.next();
                                Log.d("Key: " , key);
                                Log.d("Value: " , obj.getString(key));
                                extras.add(obj.getString(key));

                            }
                            //construiesc radio groupul cu portii

                            if (obj.getString("item_extra1") != "null"){

                                setPortiiLayout(obj,activity);

//                                    rg_layout.setVisibility(View.VISIBLE);
//                                    if (obj.getString("item_mica") != "null"){
//                                        RadioButton rb = new RadioButton(activity);
//                                        rb.setText("portie mica");
//                                        rg.addView(rb);
//                                        TextView tx_pret = new TextView(activity);
//
//                                        tx_pret.setText(obj.getString("item_mica"));
//                                        preturi_layout.addView(tx_pret);
//                                    }
                            }


                            // Genre is json array

                            // adding movie to movies array
//                                restaurantsList.add(restaurant);

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

        AppController.getInstance().addToRequestQueue(itemReq);

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
        pd.cancel();
    }

    @Override
    public void onClick(View view) {
        double pret = 0;
        double pretNou = 0;
    }


    @Override
    public void onResume() {
        super.onResume();
        rg.check(1);
    }

    private void setPortiiLayout(JSONObject obj, final Context activity){
        try {
            rg_layout.setVisibility(View.VISIBLE);
            rg_layout.setPadding(50,20,0,0);

            for (int i = 0;i < extras.size();i++){
                if (extras.get(i) != "null"){
                    RadioButton rb = new RadioButton(activity);
                    rb.setText(extras.get(i));
                    rb.setId(i);
                    rg.addView(rb);
                    if (i == 0){
                        text_extra_aditional = extras.get(i);
                    }
                }
            }



            if (obj.getString("item_extra1") != "null") {
//                RadioButton rb = new RadioButton(activity);
//                rb.setText(obj.getString("item_extra1"));
//                rb.setId(0);
//                rg.addView(rb);
            }

            rg.check(0);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup radioGroup,
                                             int radioButtonID) {
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioBtn = (RadioButton) rootView.findViewById(checkedRadioButtonId);
                    text_extra_aditional = radioBtn.getText().toString();
                    Toast.makeText(activity, radioBtn.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void adjustPrice(int textId){
        TextView tx_pret = (TextView)rg_layout.findViewById(textId);
        Toast.makeText(getActivity(), tx_pret.getText() , Toast.LENGTH_LONG).show();
        price =  getExceptLast4Chars(tx_pret.getText().toString());
        Double price_number = Double.parseDouble(price);

        textSumaTotala.setText(formatter.format(price_number * itemQTY) + " RON");
//        textSumaTotala.setText(String.valueOf(price.valueOf(pretNou)) + " RON");
    }
}






