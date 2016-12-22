package ro.softronic.mihai.ro.papamia.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ro.softronic.mihai.ro.papamia.POJOs.Order;

public class OrderDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "ordersManager";

    // Contacts table name
    private static final String TABLE_ORDERS = "orders";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_QTY = "item_qty";
    private static final String KEY_ITEM_TOTAL_PRICE = "item_total_price";


    public OrderDatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ITEM_ID + " INTEGER,"
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_ITEM_QTY + " INTEGER,"
                + KEY_ITEM_TOTAL_PRICE + " REAL" + " )";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    public void reCreateTable(SQLiteDatabase db) {

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ITEM_ID + " INTEGER,"
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_ITEM_QTY + " INTEGER,"
                + KEY_ITEM_TOTAL_PRICE + " REAL" + " )";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);

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

}