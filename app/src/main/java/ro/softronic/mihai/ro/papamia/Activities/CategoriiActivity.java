package ro.softronic.mihai.ro.papamia.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.DB.OrderDatabaseHandler;
import ro.softronic.mihai.ro.papamia.Fragments.RestaurantCategoriiFragment;
import ro.softronic.mihai.ro.papamia.Fragments.RestaurantInfoFragment;
import ro.softronic.mihai.ro.papamia.Fragments.RestaurantInfoMapFragment;
import ro.softronic.mihai.ro.papamia.POJOs.Categorie;
import ro.softronic.mihai.ro.papamia.POJOs.RestaurantInfo;
import ro.softronic.mihai.ro.papamia.R;

import static ro.softronic.mihai.ro.papamia.R.id.search;

public class CategoriiActivity extends AppCompatActivity {
    private boolean flag_map;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private LatLng latlng;
    private RestaurantInfo restaurant_info;
    private boolean dialog_flag = true;
    private Menu menu ;
    protected AppController mMyApp;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SearchView searchView;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }
    private void clearReferences(){
        Activity currActivity = mMyApp.getCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setCurrentActivity(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (AppController) this.getApplicationContext();
        mMyApp.setCurrentActivity(this);

        setContentView(R.layout.restaurant_categorii);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        int flag_open_notification = getIntent().getIntExtra("flag", 0);
        Bundle extras = getIntent().getExtras();


        int flag_not_open = extras.getInt("flag_open");
//        LinearLayout linlaHeaderProgress = (LinearLayout) this.findViewById(R.id.linlaHeaderProgress);
//        linlaHeaderProgress.setVisibility(View.VISIBLE);

//        setTitle(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        if (savedInstanceState == null) {
            //il ia din bundle pe dialog_flag

                int commit = getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_restaurant, new RestaurantInfoFragment(), "frag_restaurant")
                        .add(R.id.container_categorii, new RestaurantCategoriiFragment(), "frag_categorii")
//                    .addToBackStack(null)
                        .commit();


//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Restaurantul este momentan inchis");
//                builder.setMessage("Continuati?");
//                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//                         getSupportFragmentManager().beginTransaction()
//                                .add(R.id.container_categorii, new RestaurantInfoMapFragment(), "frag_map")
////                    .addToBackStack(null)
//                                .commit();
//
//
//                    }
//                });
//                builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing
//                        finish();
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();

            if (flag_not_open == 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
               // builder.setCancelable(false);

                builder.setPositiveButton("Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         getSupportFragmentManager().beginTransaction()
                                .add(R.id.container_categorii, new RestaurantInfoMapFragment(), "frag_map")
//                    .addToBackStack(null)
                                .commit();
//                        menu.findItem(R.id.search).setVisible(false);



                    }
                }).setNegativeButton("Inapoi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_closed_restaurant, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();


                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.spicy);
                        float imageWidthInPX = (float)image.getWidth();

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                                Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                        image.setLayoutParams(layoutParams);

                    }
                });
            }

        }



        flag_map = false;
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), 0);
        int nrItems = prefs.getInt("noItemsInBag", 0);

//        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyApp = (AppController) this.getApplicationContext();
        mMyApp.setCurrentActivity(this);
        RestaurantCategoriiFragment fragment_obj = (RestaurantCategoriiFragment) getSupportFragmentManager().
                findFragmentByTag("frag_categorii");
//        fragment_obj.getView().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        TextView cartQTY = (TextView)fragment_obj.getView().findViewById(R.id.cartQTY);
        int fabQty = AppController.getInstance().fabQty;
        cartQTY.setText(Integer.toString(fabQty));

        FloatingActionButton fab = (FloatingActionButton)fragment_obj.getView().findViewById(R.id.fab);
        fab.clearAnimation();
//        fab.setImageResource(R.drawable.shopping_bag128);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your FAB click action here...
                Intent intent = new Intent(v.getContext(), OrderViewActivity.class);
                startActivity(intent);
//                Toast.makeText(getBaseContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        if (fabQty != 0)
        {
            fab.setVisibility(View.VISIBLE);
            cartQTY.setVisibility(View.VISIBLE);
            Animation scaleAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
            fab.startAnimation(scaleAnimation);
        }
        else
        {
            fab.setVisibility(View.GONE);
            cartQTY.setVisibility(View.GONE);
        }

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.detail, menu);
//        return true;
//    }
//

        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            mSearchAction = menu.findItem(R.id.search);
            return super.onPrepareOptionsMenu(menu);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.restaurantfragment, menu);

            MenuItem item = menu.findItem(search);
            searchView = (SearchView) MenuItemCompat.getActionView(item);
            SearchView.SearchAutoComplete autoCompleteTextView = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
            if (autoCompleteTextView != null) {
                autoCompleteTextView.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));
            }
////        ((EditText)((SearchView)findViewById(R.id.searchView)).findViewById(((SearchView)findViewById(R.id.searchView)).getContext().getResources().getIdentifier("android:id/search_src_text", null, null))).setHintTextColor(Color.LTGRAY);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

                @Override
                public boolean onQueryTextSubmit(String query) {

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(
//                new ComponentName(this, MainActivity.class)));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
            searchView.setIconified(true);

            return super.onCreateOptionsMenu(menu);
//            return true;




}

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case android.R.id.home:
                    if (!flag_map){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                            odb.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + "offers");
                            odb.reCreateTable(odb.getReadableDatabase());
                            //TODO:Clear shared Preferences!!!!
                            finish();
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
                    }else{
                        int commit = getSupportFragmentManager().beginTransaction()
                                .add(R.id.container_categorii, new RestaurantCategoriiFragment(), "frag_map")
//                    .addToBackStack(null)
                                .commit();
                        flag_map = false;
                        setTitle("Restaurant");

                    }
                return  true;

                case R.id.search:


//                    ActionBar action = getSupportActionBar();
//                    if(isSearchOpened){ //test if the search is open
//
//                        action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
//                        action.setDisplayShowTitleEnabled(true); //show the title in the action bar
//
//                        //hides the keyboard
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
//
//                        //add the search icon in the action bar
//                        mSearchAction.setIcon(getResources().getDrawable(R.drawable.abc_ic_search_api_mtrl_alpha));
//
//                        isSearchOpened = false;
//                    } else { //open the search entry
//
//                        action.setDisplayShowCustomEnabled(true); //enable it to display a
//                        // custom view in the action bar.
//                        action.setCustomView(R.layout.search_bar);//add the custom view
//                        action.setDisplayShowTitleEnabled(false); //hide the title
//
//                        edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor
//
//                        //this is a listener to do a search when the user clicks on search button
//                        edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                            @Override
//                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                                    doSearch();
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });
//
//
//                        edtSeach.requestFocus();
//
//                        //open the keyboard focused in the edtSearch
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
//
//
//                        //add the close icon
//                        mSearchAction.setIcon(getResources().getDrawable(R.drawable.cancel_close_cross_delete_exit_remove_icon));
//
//                        isSearchOpened = true;
//                    }
                    return  true;



                case R.id.action_info:
                    flag_map = true;
                    setTitle("Info");
                    int commit = getSupportFragmentManager().beginTransaction()
                            .add(R.id.container_categorii, new RestaurantInfoMapFragment(), "frag_map")
//                    .addToBackStack(null)
                            .commit();
//                    mSearchAction.setIcon(getResources().getDrawable(R.drawable.abc_btn_radio_material));

                default:
                    return super.onOptionsItemSelected(item);
            }
        }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                odb.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + "offers");
                odb.reCreateTable(odb.getReadableDatabase());
                //TODO:Clear shared Preferences!!!!
                finish();
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


//        builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing but close the dialog
//
//                //TODO:Clear Preferences!!!!!
//
//                AppController.getInstance().isFabVisible = false;
//                AppController.getInstance().fabQty = 0;
//                finish();
//            }
//        });
//        builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing
//                dialog.dismiss();
//            }
//        });
        AlertDialog alert = builder.create();
        alert.show();




    }




    public String getRestID(){
        Bundle extras = getIntent().getExtras();
        String value = null;
        if (extras != null) {
            value = extras.getString("restID");
        }
//        return Integer.parseInt(value);
        return value;
    }

    public int getFlagOpen(){
        Bundle extras = getIntent().getExtras();
        int value = 0;
        if (extras != null) {
            value = extras.getInt("flag_open");
        }
//        return Integer.parseInt(value);
        return value;
    }

    public LatLng getLatLng(){
        return  this.latlng;
    }

    public void setLatLng(LatLng latlng){
        this.latlng = latlng;
    }


    public RestaurantInfo getRestaurantInfo() {
        return restaurant_info;
    }
    public void setRestaurant_info(RestaurantInfo r_info){
        this.restaurant_info = r_info;
    }
    private void doSearch(){
        return;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            final RestaurantCategoriiFragment  fragment_obj = (RestaurantCategoriiFragment) getSupportFragmentManager().
                    findFragmentByTag("frag_categorii");
            List<Categorie> categories = fragment_obj.getList();

            int index=uri.lastIndexOf('/');
            String item_id  = uri.substring(index +1 ,uri.length());
            Toast.makeText(this, "Suggestion: "+ item_id, Toast.LENGTH_SHORT).show();

            for (Categorie categorie : categories){
                if (Integer.toString(categorie.getCatID()).equals(item_id)){
                    Bundle extras = new Bundle();
                    SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
                    String restaurant_id = settings.getString("restaurant_id", "");
                    extras.putString("restID", restaurant_id);
                    extras.putString("catID", Integer.toString(categorie.getCatID()));
                    Intent intent_items = new Intent(this, ItemsActivity.class).putExtras(extras);
                    startActivity(intent_items);
                }
            }
//            Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
        }
    }






}
