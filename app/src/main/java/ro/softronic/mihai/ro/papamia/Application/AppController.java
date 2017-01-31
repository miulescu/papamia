package ro.softronic.mihai.ro.papamia.Application;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.Utils.LruBitmapCache;


public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private static String DATABASE_NAME  = "ordersManager";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static AppController mInstance;
    public static boolean isFabVisible = false;
    public static int fabQty = 0;
    private static OrderDatabaseHandler oDbHelper;

    private static Activity mCurrentActivity ;
    public static Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //-----Recreez tabela de la 0---///
        oDbHelper = new OrderDatabaseHandler(getApplicationContext());
        oDbHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + "orders");
        oDbHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + "offers");
        oDbHelper.reCreateTable(oDbHelper.getWritableDatabase());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static OrderDatabaseHandler getOrdersDatabaseHelper() {
        return oDbHelper;
    }

    @Override
    public void onTerminate() {
        // Close the internal db
        super.onTerminate();
        oDbHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + "orders");
        oDbHelper.close();
        oDbHelper = null;
//        getApplicationContext().deleteDatabase(DATABASE_NAME);
    }

}