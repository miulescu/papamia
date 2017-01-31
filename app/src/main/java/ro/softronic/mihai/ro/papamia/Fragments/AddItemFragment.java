package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ro.softronic.mihai.ro.papamia.Activities.AddItemActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.POJOs.MyLinkedHashMap;
import ro.softronic.mihai.ro.papamia.POJOs.Offer;
import ro.softronic.mihai.ro.papamia.POJOs.Order;
import ro.softronic.mihai.ro.papamia.R;

import static java.lang.Integer.parseInt;
import static ro.softronic.mihai.ro.papamia.Fragments.RestaurantItemsFragment.ADD_ITEMS;
import static ro.softronic.mihai.ro.papamia.Utils.StringManipulation.getExceptLast4Chars;

//import static ro.softronic.mihai.ro.papamia.R.id.textView;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class AddItemFragment extends Fragment implements View.OnClickListener{
    private boolean has_extras;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private String URLItem = "";
    private ProgressDialog dialog = null;

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

    private TextView nameView;
    FloatingActionButton fab_cadou;

    private String restID;

//    private LinkedList<Order> bonusList;

    private  MyLinkedHashMap<Integer , Order> bonusList;

    NumberFormat formatter = new DecimalFormat("#0.00");

    public AddItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        bonusList = new LinkedList<Order>();

        bonusList  = new MyLinkedHashMap<Integer, Order>();

        boolean has_extra = (boolean)getArguments().get("has_extras");
        preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        //toggle butonul comanda cu butonul next!!

        LinearLayout comanda_btn = (LinearLayout)rootView.findViewById(R.id.linearLayoutComanda);
        LinearLayout next_btn = (LinearLayout)rootView.findViewById(R.id.linearLayoutNext);

        fab_cadou = (FloatingActionButton)rootView.findViewById(R.id.fab_cadou);
        fab_cadou.setVisibility(View.GONE);
        fab_cadou.clearAnimation();

//        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
//        fab_cadou.startAnimation(scaleAnimation);

        if (has_extra){
            comanda_btn.setVisibility(View.GONE);
            next_btn.setVisibility(View.VISIBLE);
        }

        final ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
        final AddItemActivity activity = (AddItemActivity) getActivity();
//        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        restID = activity.getRestID();
        final String catID = activity.getCatID();
        final String itemID = activity.getItemID();

        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        sb.append("/");
        sb.append(restID);
        sb.append("/categorii");
        sb.append("/");
        sb.append(catID);
        sb.append("/items");
        sb.append("/");
        sb.append(itemID);

        URLItem = sb.toString();

//        ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
//        NetworkImageView imageView = (NetworkImageView)convertView
//                .findViewById(R.id.gallery_item_imageView);
//        imageView.setDefaultImageResId(R.drawable.brian_up_close);
//        imageView.setImageUrl(item.getUrl(), mImageLoader);
        nameView = (TextView)rootView.findViewById(R.id.nameView);
        final TextView detailsView = (TextView)rootView.findViewById(R.id.descriptiontextView);
        final TextView priceView = (TextView)rootView.findViewById(R.id.pricetextView);
        final NetworkImageView imageView = (NetworkImageView)rootView.findViewById(R.id.imageItemView);
        final ImageButton buttonADD = (ImageButton) rootView.findViewById(R.id.buttonADD);
        final ImageButton buttonREMOVE = (ImageButton) rootView.findViewById(R.id.buttonREMOVE);

        rg_layout = (LinearLayout) rootView.findViewById(R.id.radiogrouplayout);
        preturi_layout = (LinearLayout) rootView.findViewById(R.id.preturi_portii);

        rg = (RadioGroup)rootView.findViewById(R.id.radiogroup);

        textQTY = (TextView)rootView.findViewById(R.id.textQTY);
        textSumaTotala = (TextView)rootView.findViewById(R.id.textSumaTotala);



        buttonADD.setOnClickListener(this);
        buttonREMOVE.setOnClickListener(this);

//        LinearLayout comandaclick =(LinearLayout)rootView.findViewById(R.id.linearLayoutComanda);
        comanda_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //---pun in baza de date de pe telefon order items

                Intent data = new Intent();
                String orderItemsQty = textQTY.getText().toString();
                String text = "Result to be returned....";
                //---set the data to pass back---

                data.putExtra("orderItemsQty",orderItemsQty);

                getActivity().setResult(ADD_ITEMS, data);

                AppController.getInstance().isFabVisible = true;
                AppController.getInstance().fabQty += Integer.parseInt(orderItemsQty);

                //order_item_id
                //---insert in database
//                OrderDatabaseHandler odb = new OrderDatabaseHandler(getActivity().getApplicationContext());
                OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();
                odb.addOrder(new Order(Integer.parseInt(itemID),
                                       nameView.getText().toString() + " " + text_aditional,
                                       Integer.parseInt(textQTY.getText().toString()),
                                       Double.parseDouble(getExceptLast4Chars(textSumaTotala.getText().toString()))));

                // TODO:add Bonus!

                Set set = bonusList.entrySet();

                // adica avem o oferta  in lista de 1 element ; puteam sa scriu bonusList.size == 1
                if ( bonusList.size() > 0){
                        Iterator iterator = set.iterator();
                        Map.Entry me = (Map.Entry)iterator.next();
                        Order bonus = (Order)me.getValue();
                        odb.addOrder(bonus);
                }

                List<Order> orders = odb.getAllOrders();
                Log.d("orders:", Integer.toString(orders.size()));

                editor.putString("ORDER_NR_ROWS", String.valueOf(orders.size()));
                editor.apply();
                //---close the activity---
                getActivity().finish();
//                startActivityForResult(new Intent(A_My_Galaxy.this,C_Student_Book_Planet.class), 0);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // salvez widgeturile
                activity.setmItemName( nameView.getText().toString() + " " + text_aditional );
                activity.setmItemQty( Integer.parseInt(textQTY.getText().toString()) );
                activity.setmItemSumaTotala(Double.parseDouble(getExceptLast4Chars(textSumaTotala.getText().toString())));
                // tabul 2
                TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.sliding_tabs);
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
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
                                Item item = new Item();
                                Iterator<?> keys = obj.keys();
                                while( keys.hasNext() ) {
                                    String key = (String) keys.next();
                                    Log.d("Key: " , key);
                                    Log.d("Value: " , obj.getString(key));
                                }
                                //construiesc radio groupul cu portii

                                if (obj.getString("items_id") != "null"){
                                    text_aditional = " - portie mica";
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
                                String name = obj.getString("item_name");
                                price  = obj.getString("item_price");
                                String description = obj.getString("item_descriere");
//                                restaurant.setThumbnailUrl(obj.getString("rest_logo_url"));
                                imageView.setImageUrl(obj.getString("item_img"), mImageLoader);
                                nameView.setText(name);
                                priceView.setText(formatter.format(Double.parseDouble(price)) + " RON");
                                detailsView.setText(description);
                                textSumaTotala.setText(formatter.format(Double.parseDouble(price)) + " RON");

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

        switch(view.getId())
        {
            case R.id.buttonADD :
                itemQTY = parseInt(textQTY.getText().toString());
                itemQTY++;
                textQTY.setText(String.valueOf(itemQTY));

                pret = Double.parseDouble(getExceptLast4Chars(textSumaTotala.getText().toString()));
                pretNou = Double.parseDouble(price) * itemQTY;
                textSumaTotala.setText(String.format("%.2f",pretNou) + " RON");

                String item_with_offer = nameView.getText().toString();

                //Introducem in comanda si oferta! cu valoare 0
                Order oferta_order = CheckItemQtyIsOfferSuitable(itemQTY, item_with_offer);

                OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();


                if (oferta_order != null){
                    bonusList.put(itemQTY, oferta_order);
//                    odb.addOrder(oferta_order);
                    fab_cadou.setVisibility(View.VISIBLE);
                    Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
                    fab_cadou.startAnimation(scaleAnimation);
                }

//                if (oferta_order != null ){
//                    Snackbar.make(view, "Ai primit cadou  "  , Snackbar.LENGTH_LONG)
//                            .setAction("Vezi", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            })
//                            .show();
//                }

                //check if item in oferte si construieste  un FAB cu un cadouash


                break;
            case R.id.buttonREMOVE :
                itemQTY = parseInt(textQTY.getText().toString());
                if (itemQTY == 1)
                    break;

                itemQTY--;
                textQTY.setText(String.valueOf(itemQTY));
                pret = Double.parseDouble(getExceptLast4Chars(textSumaTotala.getText().toString()));
                pretNou = pret - Double.parseDouble(price);
                textSumaTotala.setText(String.format("%.2f",pretNou) + " RON");



                //TODO: check conditions sa dispara cadouashul
//                textSumaTotala.setText(String.valueOf(price.valueOf(pretNou)) + " RON");
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        rg.check(1);
    }

    private void setPortiiLayout(JSONObject obj, Context activity){
        try {
            rg_layout.setVisibility(View.VISIBLE);
            rg_layout.setPadding(50,20,0,0);

            if (obj.getString("item_mica") != "null") {
                RadioButton rb = new RadioButton(activity);
                rb.setText("portie mica");
                rb.setId(0);
                rg.addView(rb);
                TextView tx_pret = new TextView(activity);
                tx_pret.setId(R.id.text_portie_mica);
                tx_pret.setText(formatter.format(Double.parseDouble(obj.getString("item_mica"))) + " RON");
                tx_pret.setPadding(0,30,0,16);
                tx_pret.setTypeface(null, Typeface.BOLD);
                preturi_layout.addView(tx_pret);
            }

            if (obj.getString("item_medie") != "null") {
                RadioButton rb = new RadioButton(activity);
                rb.setText("portie medie");
                rb.setId(1);
                rg.addView(rb);
                TextView tx_pret = new TextView(activity);
                tx_pret.setId(R.id.text_portie_medie);
                tx_pret.setPadding(0,20,0,16);
                tx_pret.setTypeface(null, Typeface.BOLD);
                tx_pret.setText(formatter.format(Double.parseDouble(obj.getString("item_medie"))) + " RON");
                preturi_layout.addView(tx_pret);
            }

            if (obj.getString("item_mare") != "null") {
                RadioButton rb = new RadioButton(activity);
                rb.setText("portie mare");
                rb.setId(2);
                rg.addView(rb);
                TextView tx_pret = new TextView(activity);
                tx_pret.setId(R.id.text_portie_mare);
                tx_pret.setPadding(0,20,0,16);
                tx_pret.setTypeface(null, Typeface.BOLD);
                tx_pret.setText(formatter.format(Double.parseDouble(obj.getString("item_mare"))) + " RON");
                preturi_layout.addView(tx_pret);

            }

            rg.check(0);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup radioGroup,
                                             int radioButtonID) {
                    switch(radioButtonID) {
                        case 0 :
                            adjustPrice(R.id.text_portie_mica);
//                            Toast.makeText(getActivity(), "Vom trimite un SMS cu recomandarea DVS.", Toast.LENGTH_LONG).show();
                            text_aditional = " - portie mica";
                            break;
                        case 1 :
                            adjustPrice(R.id.text_portie_medie);
                            text_aditional = " - portie medie";
                            break;
                        case 2 :
                            adjustPrice(R.id.text_portie_mare);
                            text_aditional = " - portie mare";
                            break;
                    }
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
    }


    //  vede daca din toate bonusurile restaurantului avem si una care se potriveste itemului din activitatea curenta
    //  adica qty itemului curent este mai mare decat conditia ca itemul sa se califice ca oferta
    //  daca da atunci il voi intoarce
    //
    //  se vor  introduce intr-o lista de unde voi introduce ca si !!!ORDER!!! pe cel cu qty mai mare

    private Order CheckItemQtyIsOfferSuitable(int qty , String offer){
        OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();
        List<Offer> offers = odb.getAllOffers();

        for (int i = 0; i < offers.size(); i++) {
            if (offer.toLowerCase().contains(offers.get(i).get_subiect_oferta_name().toLowerCase())) {
                boolean is_offer_candidate_state = qty % offers.get(i).get_subiect_item_qty() == 0 ? true : false;
                int qty_offer =  qty / offers.get(i).get_subiect_item_qty();

                if (is_offer_candidate_state) {
                    return new Order(offers.get(i).get_de_oferit_item_id(),
                                     offers.get(i).get_de_oferit_item_name(),
                                     qty_offer,
                                     0.0);
                }
            }
        }
        return null;
    }
}









