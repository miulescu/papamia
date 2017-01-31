package ro.softronic.mihai.ro.papamia.Adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Activities.OrderViewActivity;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.POJOs.Order;
import ro.softronic.mihai.ro.papamia.R;

import static java.lang.Integer.parseInt;

public class OrdersCursorAdapter extends
        SimpleCursorAdapter implements OnClickListener {


    static class ViewHolder {


        private TextView qty;
        private TextView name;
        private TextView price;
        private  ImageButton btn_remove;
        private  EditText edit_qty;

        public ViewHolder(View view) {
            qty = (TextView) view.findViewById(R.id.item_qty);
            name = (TextView) view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.items_price);
            btn_remove = (ImageButton)view.findViewById(R.id.item_remove);
            edit_qty = (EditText) view.findViewById(R.id.item_qty_edit);

        }
    }
    private Activity activity;
    private LayoutInflater inflater;
    private LayoutInflater cursorInflater;
    private Cursor cursor;
    private List<Order> Orders;
    private  Context mContext;
    public int isEditMode;

    public OrdersCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
//        super(context, cursor, 0);
        super(context, layout, c, from, to, flags);
//        cursorInflater = (LayoutInflater) context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);
        this.cursor = c;
        inflater = LayoutInflater.from( context );
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        Log.d("bibi", "It works, pos=" + v.getTag());

    }
//    @Override
//    public View getView( int position, View convertView, ViewGroup parent) {
////        final int a=position;
////        View v = convertView;
////        if(v == null ) {
////            v = inflater.inflate(R.layout.order_row, parent, false);
////            ViewHolder viewHolder = new ViewHolder(v);
////            viewHolder.btn_remove.setOnClickListener(new OnClickListener() {
////                public void onClick(View v) {
////                    Log.d("ok", "It works, pos=" + a);
////                }
////            });
////            v.setTag(viewHolder);
////        }
//
////        ViewHolder holder = (ViewHolder) v.getTag();
////        cursor.moveToFirst();
////        String name = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
////        int qty = cursor.getInt(cursor.getColumnIndexOrThrow("item_qty"));
////        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("item_total_price"));
////        // Populate fields with extracted properties
////        holder.name.setText(name);
////        holder.qty.setText(String.valueOf(qty) + " x");
////        holder.price.setText(String.valueOf(price) + " RON");
//        // here you can get viewholder items
//        // eg : holder.btn.setText("button");
////        return v;
////        return super.getView(position, convertView, parent);
//
//
//
////        View v = null;
////
////        if( convertView != null )
////            v = convertView;
////        else
////            v = inflater.inflate( R.layout.order_row, parent, false);
////
////
////
////        ImageButton button = (ImageButton) v
////                .findViewById(R.id.item_remove);
////        button.setTag(position);
////        button.setOnClickListener(this);
////
////        return v;
//        return super.getView(position, convertView, parent);
//    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.order_row, parent, false);
        return view;
//        return null;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
            NumberFormat formatter = new DecimalFormat("#0.00");
//        super.bindView(view, context, cursor);

            final OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();
            int order_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
            int qty = cursor.getInt(cursor.getColumnIndexOrThrow("item_qty"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("item_total_price"));

            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.btn_remove.setTag(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            viewHolder.btn_remove.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    cursor.moveToFirst();
                    Log.d("ok", "It works, pos=" + v.getTag());
                    //get database from application
                    int i = Integer.parseInt(v.getTag().toString());

                    Order o = odb.getOrder(i);


                    Log.d("ntz", "id ul din BD =" + o.getID());


                    if ( odb.getAllOrders().size() > 1 )
                    {
                    odb.deleteOrder(i);
                        SQLiteDatabase db = odb.getWritableDatabase();
                    Cursor new_orderCursor = db.rawQuery("SELECT   * FROM orders", null);
                    changeCursor(new_orderCursor);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setTitle("Confirmare");
                        builder.setMessage("Parasiti Restaurantul? Comanda Dvs. va fi pierduta");

                        builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                AppController.getInstance().isFabVisible = false;
                                AppController.getInstance().fabQty = 0;
                                //----recreez tabela de orders pentru un nou restaurant ----//
                                OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();
                                odb.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + "orders");
                                odb.reCreateTable(odb.getReadableDatabase());
                                AppController.getInstance().fabQty = 0;
                                ((OrderViewActivity)context).finish();
//                            dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                }


                }
            });
//            viewHolder.edit_qty.setText("1");

            viewHolder.edit_qty.setTag(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            view.setTag(viewHolder);



        TextView txtQty = (TextView) view.findViewById(R.id.item_qty);
        TextView txtName = (TextView) view.findViewById(R.id.item_name);
        TextView txtPrice = (TextView) view.findViewById(R.id.items_price);
//        ImageButton delimageButton = (ImageButton) view.findViewById(R.id.item_remove);


        final EditText edtTxtQty = (EditText) view.findViewById(R.id.item_qty_edit);


        if(mContext instanceof OrderViewActivity)
        {
            this.isEditMode = ((OrderViewActivity)mContext).isEditMode;
            if (this.isEditMode == View.GONE) viewHolder.btn_remove.setVisibility(View.GONE);
            else {
                viewHolder.btn_remove.setVisibility(View.VISIBLE);
                viewHolder.edit_qty.setText(viewHolder.qty.getText().toString());
                viewHolder.edit_qty.setPaintFlags(viewHolder.edit_qty.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                viewHolder.edit_qty.setTextColor(Color.parseColor("#479e09"));
                viewHolder.edit_qty.setVisibility(View.VISIBLE);
                viewHolder.edit_qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                viewHolder.qty.setVisibility(View.GONE);


            }
        }

        else{
            viewHolder.btn_remove.setVisibility(View.GONE);
        }

        edtTxtQty.setOnFocusChangeListener(new OnFocusChangeListener() {
            OrderDatabaseHandler odb = AppController.getOrdersDatabaseHelper();
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
                if (!hasFocus) {
//                    Log.d("Focus",edtTxtQty.getText().toString() );
                    if (parseInt(v.getTag().toString()) == 0)
                        Log.d("00000000000000000000000", String.valueOf("0"));
                    Order o = odb.getOrder(parseInt(v.getTag().toString()));
//                    Log.d("orderrrrrrrrr", o.getItemName());

                    //validateInput(v);
                    int new_qty = parseInt(edtTxtQty.getText().toString());
                    if (new_qty!= o.getItemQTY()) {
                        Log.d("orderrrrrrrrr new_qty", String.valueOf(new_qty));
                        double item_price = o.getItemPrice();
                        o.setItemTotalPrice(item_price * new_qty);
                        o.setItemQTY(new_qty);
                        odb.updateOrder(o);


                    }

//                    FireBaseMessageOrderObject o = odb.getOrder(i+1);

//                   o.setID(i+1);
//
//                    int new_qty = Integer.parseInt(edtTxtQty.getText().toString());
//                    o.setItemQTY(new_qty);
//
//                    int item_qty = o.getItemQTY();
//                    double total_price = o.getItemTotalPrice();
//                    double item_price = total_price/item_qty;
//                    o.setItemTotalPrice(item_price * new_qty);
//
//                    odb.updateOrder(o);


//
                }
//                SQLiteDatabase db = odb.getWritableDatabase();
//                Cursor new_orderCursor = db.rawQuery("SELECT   * FROM orders", null);
//                changeCursor(new_orderCursor);
            }
        });

        ViewHolder holder = (ViewHolder) view.getTag();
        // Extract properties from cursor

        // Populate fields with extracted properties

        holder.name.setText(name);
        holder.qty.setText(String.valueOf(qty));
        holder.price.setText(formatter.format(price));
        holder.edit_qty.setText(String.valueOf(qty));

//        holder.btn_remove.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
//        holder.price.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
//        txtName.setText(name);
//        txtQty.setText(String.valueOf(qty) + " x");
//        txtPrice.setText(String.valueOf(price) + " RON");
//        delimageButton.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));


    }

}