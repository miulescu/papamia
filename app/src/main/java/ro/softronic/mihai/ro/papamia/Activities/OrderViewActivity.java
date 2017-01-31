package ro.softronic.mihai.ro.papamia.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Adapters.OrdersCursorAdapter;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.Fragments.OrderViewFragment;
import ro.softronic.mihai.ro.papamia.POJOs.Order;
import ro.softronic.mihai.ro.papamia.R;

import static ro.softronic.mihai.ro.papamia.R.id.item_qty;

public class OrderViewActivity extends AppCompatActivity {
    public int isEditMode = View.GONE;
    private SharedPreferences preferences= null;
    private SharedPreferences.Editor editor = null;
    private NumberFormat formatter = new DecimalFormat("#0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new OrderViewFragment(), "order_frag")
                    .commit();
        }
//        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences= getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        setTitle("Comanda ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                finish();
                super.onBackPressed(); // cu finish nu updateaza FAB!!!

//                FragmentManager fragmentManager = getSupportFragmentManager();
//                if(fragmentManager.getBackStackEntryCount() != 0) {
//                    fragmentManager.popBackStack();
//                    break;
//                } else {
//                    super.onBackPressed();
//                    break;
//                }
//                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    public void onClick(View v){
        OrderViewFragment fragment_obj = (OrderViewFragment) getSupportFragmentManager().
                findFragmentByTag("order_frag");


        TextView tx_action = (TextView)fragment_obj.getView().findViewById(R.id.Modifica);
        TextView tx_totalPrice = (TextView)fragment_obj.getView().findViewById(R.id.total_general_numeric);
        ListView lv  = (ListView)fragment_obj.getView().findViewById(R.id.order_list);

        int size = lv.getChildCount();



        if (tx_action.getText().toString().trim().equals("Modifica") ){
            isEditMode = View.VISIBLE;
            tx_action.setText("Aplica");
            for ( int i = 0 ;i < size; i++) {

                View wantedView = lv.getChildAt(i);
                EditText etxt = (EditText) wantedView.findViewById(R.id.item_qty_edit);
                TextView qtyTxt  =(TextView) wantedView.findViewById(item_qty);
                ImageView rem = (ImageView)wantedView.findViewById(R.id.item_remove) ;

//                if (i == 0) {
//                    etxt.requestFocus();
//                }
                etxt.setVisibility(View.VISIBLE);
                etxt.setPaintFlags(etxt.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                qtyTxt.setVisibility(View.GONE);
                etxt.setTextColor(Color.parseColor("#479e09"));
                rem.setVisibility(View.VISIBLE);
            }
        }else{
            tx_action.setText("Modifica");
            //Ca sa dispara tastatura
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);


            isEditMode = View.GONE;
            OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();
            ListView lv_new  = (ListView)fragment_obj.getView().findViewById(R.id.order_list);
            int size_new  = lv_new.getChildCount();

            LinearLayout l = (LinearLayout)fragment_obj.getView().findViewById(R.id.lista);
            ViewGroup.LayoutParams list = (ViewGroup.LayoutParams) l.getLayoutParams();

            int nr_rows = Integer.parseInt(preferences.getString("ORDER_NR_ROWS", "0"));
            list.height= nr_rows *230 - (nr_rows *5);//like int  200
            l.setLayoutParams(list);
            editor.putString("ORDER_NR_ROWS", String.valueOf(size_new));
            editor.apply();

            for ( int i = 0 ;i < size_new; i++) {

                View wantedView = lv.getChildAt(i);

                EditText etxt = (EditText) wantedView.findViewById(R.id.item_qty_edit);
                TextView qtyTxt  =(TextView) wantedView.findViewById(item_qty);
                ImageView rem = (ImageView)wantedView.findViewById(R.id.item_remove) ;
                TextView nameTxt  =(TextView) wantedView.findViewById(R.id.item_name);
                TextView priceTxt  =(TextView) wantedView.findViewById(R.id.items_price);
//                double price = Double.parseDouble(priceTxt.getText().toString());
//                totalPrice+= price;



//
////                o.setID(i+1);
//                o.setItemQTY(Integer.parseInt(etxt.getText().toString()));
//                int new_qty = Integer.parseInt(etxt.getText().toString());
//                int item_qty = o.getItemQTY();
//                double total_price = o.getItemTotalPrice();
//                double item_price = total_price/item_qty;
//                o.setItemTotalPrice(item_price * new_qty);
//
//                odb.updateOrder(o);


            }
            if (odb == null){
                odb = AppController.getOrdersDatabaseHelper();
            }


            SQLiteDatabase db = odb.getWritableDatabase();
            Cursor new_orderCursor = db.rawQuery("SELECT   * FROM orders", null);
            String[] columns = new String[] { "item_qty", "item_name", "item_total_price"};
            int[] to = new int[]{item_qty, R.id.item_name, R.id.item_price};
            OrdersCursorAdapter odc = new OrdersCursorAdapter(this,R.layout.order_row,new_orderCursor,columns,to,0);

            lv.setAdapter(odc);
            nr_rows = lv.getChildCount();
            if (new_orderCursor.getCount() > 0) {

                List<Order> orders  = odb.getAllOrders();
                double totalPrice = 0.0;
                for (Order o:orders){
                    totalPrice+= o.getItemTotalPrice();
                }

                int totalOrders = 0;
                for (Order o:orders){
                    totalOrders+= o.getItemQTY();
                }

                //            AppController.getInstance().isFabVisible = false;
                AppController.getInstance().fabQty  = totalOrders;

                list.height= size_new * 230  -(size_new * 5);//like int  200

                l.setLayoutParams(list);
                //            LinearLayout l = (LinearLayout)fragment_obj.getView().findViewById(R.id.lista);
                //            ViewGroup.LayoutParams list = (ViewGroup.LayoutParams) l.getLayoutParams();
                Log.d("total price ", String.valueOf(totalPrice));
                tx_totalPrice.setText("RON " + formatter.format(totalPrice));
            }

//            new_orderCursor.close();

        }




    }

}