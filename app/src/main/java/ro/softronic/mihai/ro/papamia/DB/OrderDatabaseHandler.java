package ro.softronic.mihai.ro.papamia.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.POJOs.Offer;
import ro.softronic.mihai.ro.papamia.POJOs.Order;

public class OrderDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "ordersManager";

    // Contacts table name
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_OFFERS = "offers";

    // Orders Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_QTY = "item_qty";
    private static final String KEY_ITEM_TOTAL_PRICE = "item_total_price";

    // Offers Table Column Names
    private static final String KEY_TABLE_ID = "_id";
    private static final String KEY_OFFER_ID = "offer_id";
    private static final String KEY_OFFER_SUBJECT_NAME = "subiect_oferta_name";
    private static final String KEY_OFFER_SUBJECT_QTY = "subiect_item_qty";
    private static final String KEY_ITEM_DE_OFFERIT_NAME = "de_oferit_item_name";


    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ITEM_ID + " INTEGER,"
            + KEY_ITEM_NAME + " TEXT,"
            + KEY_ITEM_QTY + " INTEGER,"
            + KEY_ITEM_TOTAL_PRICE + " REAL" + " )";

    private static final String CREATE_OFFERS_TABLE = "CREATE TABLE " + TABLE_OFFERS + " ("
            + KEY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_OFFER_ID + " INTEGER,"
            + KEY_OFFER_SUBJECT_NAME + " TEXT,"
            + KEY_OFFER_SUBJECT_QTY + " INTEGER,"
            + KEY_ITEM_DE_OFFERIT_NAME + " TEXT" + " )";

    public OrderDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_OFFERS_TABLE);
    }

    public void reCreateTable(SQLiteDatabase db) {

//        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " ("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_ITEM_ID + " INTEGER,"
//                + KEY_ITEM_NAME + " TEXT,"
//                + KEY_ITEM_QTY + " INTEGER,"
//                + KEY_ITEM_TOTAL_PRICE + " REAL" + " )";
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_OFFERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);

        // Create tables again
        onCreate(db);
    }

    public void dropTable (SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
    }
    public void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, order.getID());
        values.put(KEY_ITEM_ID, order.getItemID());
        values.put(KEY_ITEM_NAME, order.getItemName());
        values.put(KEY_ITEM_QTY, order.getItemQTY());
        values.put(KEY_ITEM_TOTAL_PRICE, order.getItemTotalPrice());

        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    public Order getOrder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS, new String[] { KEY_ID,
                        KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_QTY, KEY_ITEM_TOTAL_PRICE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Order order = new Order(Integer.parseInt(cursor.getString(0)),
                                Integer.parseInt(cursor.getString(1)),
                                cursor.getString(2),
                                Integer.parseInt(cursor.getString(3)),
                                Double.parseDouble(cursor.getString(4)));
        cursor.close();
//        db.close();//?
        return order;
    }

    // Getting All Contacts
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<Order>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setID(Integer.parseInt(cursor.getString(0)));
                    order.setItemID(Integer.parseInt(cursor.getString(1)));
                    order.setItemName(cursor.getString(2));
                    order.setItemQTY(Integer.parseInt(cursor.getString(3)));
                    order.setItemTotalPrice(Double.parseDouble(cursor.getString(4)));
                    // Adding contact to list
                    orderList.add(order);
                } while (cursor.moveToNext());
            }
        }
//        db.close();
        return orderList;

    }

    public int updateOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, order.getID());
        values.put(KEY_ITEM_ID, order.getItemID());
        values.put(KEY_ITEM_NAME, order.getItemName());
        values.put(KEY_ITEM_QTY, order.getItemQTY());
        values.put(KEY_ITEM_TOTAL_PRICE, order.getItemTotalPrice());

        // updating row
        return db.update(TABLE_ORDERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(order.getID()) });
    }


    public void deleteOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, KEY_ID + " = ?",
                new String[] { String.valueOf(order.getID()) });
        db.close();
    }

    public void deleteOrder(int rowId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ORDERS, KEY_ID + " = ?",
                new String[] { String.valueOf(rowId) });
        db.close();
    }

    public int getOrdersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ORDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void addOffer(Offer offer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TABLE_ID, offer.getID());
        values.put(KEY_OFFER_ID, offer.getOfferId());
        values.put(KEY_OFFER_SUBJECT_NAME, offer.get_subiect_oferta_name());
        values.put(KEY_OFFER_SUBJECT_QTY, offer.get_subiect_item_qty());
        values.put(KEY_ITEM_DE_OFFERIT_NAME, offer.get_de_oferit_item_name());

        db.insert(TABLE_OFFERS, null, values);
        db.close();
    }

    public List<Offer> getAllOffers() {
        List<Offer> offerList = new ArrayList<Offer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OFFERS ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Offer offer = new Offer();
                    offer.setID(Integer.parseInt(cursor.getString(0)));
                    offer.setOfferId(Integer.parseInt(cursor.getString(1)));
                    offer.set_subiect_oferta_name(cursor.getString(2));
                    offer.set_subiect_item_qty(Integer.parseInt(cursor.getString(3)));
                    offer.set_de_oferit_item_name(cursor.getString(4));
                    // Adding contact to list
                    offerList.add(offer);
                } while (cursor.moveToNext());
            }
        }
//        db.close();
        return offerList;

    }

}