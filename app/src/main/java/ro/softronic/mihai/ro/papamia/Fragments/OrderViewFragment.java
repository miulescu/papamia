package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.InformatiiLivrareActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Activities.MultiLoginActivity;
import ro.softronic.mihai.ro.papamia.Activities.OrderViewActivity;
import ro.softronic.mihai.ro.papamia.Adapters.OrdersCursorAdapter;
import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.POJOs.Order;
import ro.softronic.mihai.ro.papamia.R;

public class OrderViewFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog = null;

//    private ArrayList<Restaurant> restaurantsList;

    private static  String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private ProgressDialog pDialog;
    private List<Order> orderList =  new ArrayList<Order>();
    private ListView listView;
    private OrdersCursorAdapter odc;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor ;
    private FirebaseAuth firebaseAuth;

    public OrderViewFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences= getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_order_view, container, false);


        listView = (ListView)rootView.findViewById(R.id.order_list);

        OrderViewActivity activity = (OrderViewActivity) getActivity();

        final OrderDatabaseHandler handler = new OrderDatabaseHandler(this.getActivity());

        SQLiteDatabase db = handler.getWritableDatabase();

        Cursor orderCursor = db.rawQuery("SELECT   * FROM orders", null);

        String[] columns = new String[] { "item_qty", "item_name", "item_total_price"};
        int[] to = new int[]{R.id.item_qty, R.id.item_name, R.id.item_price};
        odc = new OrdersCursorAdapter(this.getActivity(),R.layout.order_row,orderCursor,columns,to,0);

        listView.setAdapter(odc);


        LinearLayout l = (LinearLayout)rootView.findViewById(R.id.lista);
        ViewGroup.LayoutParams list = (ViewGroup.LayoutParams) l.getLayoutParams();

        preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        editor = preferences.edit();
        int nr_rows = Integer.valueOf(preferences.getString("ORDER_NR_ROWS", "0"));
        list.height= nr_rows  * 260 - (nr_rows * 2);//like int  200
        l.setLayoutParams(list);

        TextView tx_transport = (TextView)rootView.findViewById(R.id.transport_numeric);
        tx_transport.setText("RON " + String.valueOf(preferences.getFloat("restaurant_transport", 0)));
        TextView tx_pretTotal = (TextView)rootView.findViewById(R.id.total_general_numeric);

        List<Order> orders  = handler.getAllOrders();
        double totalPrice = 0.0;
        for (Order o:orders){
            totalPrice+= o.getItemTotalPrice();
        }

        tx_pretTotal.setText("RON " + String.valueOf(totalPrice));
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Categorie categorie  = (Categorie) mCategoriiAdapter.getItem(position);
//                Bundle extras = new Bundle();
//                extras.putString("catID",Integer.toString(categorie.getCatID()));
//                extras.putString("restID",restID);
//
//                Intent intent = new Intent(getActivity(), ItemsActivity.class).putExtras(extras);
//                startActivity(intent);
//                Toast.makeText(getActivity(), categorie.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

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

        RelativeLayout pasul_urmator = (RelativeLayout) rootView.findViewById(R.id.pasul_urmator_layout);
        pasul_urmator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                List<Order> orders = handler.getAllOrders();
                StringBuilder sb = new StringBuilder();
                String str = "";
                double suma_totala = 0.0;
                for (Order o : orders){
                    sb.append(o.getItemQTY() + "x");
                    sb.append(o.getItemName() + "-" + o.getItemTotalPrice() + " RON");
                    suma_totala += o.getItemTotalPrice();
                    sb.append("\n");
                }


                //TODO: de facut cu doua zecimale !!!!
                sb.append("\n Suma totala = " + String.valueOf(suma_totala));

//                sb.append("\n Adresa de livrare Garlesti nr 98"); //get address din Bundle
                str = sb.toString();
//                try {
//
//                    SmsManager smsManager = SmsManager.getDefault();
//
//
//                    ArrayList<String> detrimis  = smsManager.divideMessage(str);
//
//                    smsManager.sendMultipartTextMessage("+40746048564", null, detrimis, null, null);
//                    Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
//                }
//
//                catch (Exception e) {
//                    Toast.makeText(getActivity(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage("0746048564", null, "sms", null, null);
                Bundle extras = new Bundle();
                extras.putString("str_email_de_trimis", str);
                extras.putString("suma_totala", String.valueOf(suma_totala));

                firebaseAuth = FirebaseAuth.getInstance();

                //if the objects getcurrentuser method is not null
                //means user is already logged in
                if(firebaseAuth.getCurrentUser() != null){
                    //close this activity
                    //opening profile activity
                    Intent intent = new Intent(getActivity(), InformatiiLivrareActivity.class).putExtras(extras);
                    startActivity(intent);
                }else{
                    editor.putInt("loginOrder", 1);
                    editor.commit();
                    startActivity(new Intent(getActivity().getApplicationContext(), MultiLoginActivity.class));
                }



            }
        });

        return rootView;

    }



//    View v = myList.getAdapter().getView(i, null, null);
//    EditText et = (EditText) v.findViewById(nameOfTheView);

//    @Override
//    public void onResume() {
//        super.onResume();
//        Toast.makeText(getActivity(), "intoarcere", Toast.LENGTH_SHORT).show();
//
//
//    }


}