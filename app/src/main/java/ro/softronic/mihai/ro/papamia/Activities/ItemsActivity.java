package ro.softronic.mihai.ro.papamia.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.Fragments.RestaurantItemsFragment;
import ro.softronic.mihai.ro.papamia.POJOs.Item;
import ro.softronic.mihai.ro.papamia.R;

import static ro.softronic.mihai.ro.papamia.Fragments.RestaurantItemsFragment.ADD_ITEMS;
import static ro.softronic.mihai.ro.papamia.R.id.fab;
import static ro.softronic.mihai.ro.papamia.R.id.search;

public class ItemsActivity extends AppCompatActivity {

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

        setContentView(R.layout.items);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        setTitle(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        if (savedInstanceState == null) {
            int commit = getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RestaurantItemsFragment(), "frag_items_tag")
//                    .addToBackStack(null)
                    .commit();
        }

        Bundle extras = getIntent().getExtras();
        String categorie = extras.getString("cat_nume");
        this.setTitle(categorie);


//        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

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
                finish();
//                super.onBackPressed();
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

    public String getRestID(){
        Bundle extras = getIntent().getExtras();
        String value = null;
        if (extras != null) {
            value = extras.getString("restID");
        }
//        return Integer.parseInt(value);
        return value;
    }

    public String getCatID(){
        Bundle extras = getIntent().getExtras();
        String value = null;
        if (extras != null) {
            value = extras.getString("catID");
        }
//        return Integer.parseInt(value);
        return value;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == ADD_ITEMS) {
            String itemsQty = data.getStringExtra("orderItemsQty");
//            Toast.makeText(this, itemsQty, Toast.LENGTH_SHORT).show();

            RestaurantItemsFragment fragment_obj = (RestaurantItemsFragment) getSupportFragmentManager().
                    findFragmentByTag("frag_items_tag");
            if (AppController.getInstance().fabQty > 0)
            fragment_obj.getView().findViewById(fab).setVisibility(View.VISIBLE);

            TextView cartQTY = (TextView)fragment_obj.getView().findViewById(R.id.cartQTY);
            int fabQty = AppController.getInstance().fabQty;
            cartQTY.setText(Integer.toString(fabQty));
            fragment_obj.getView().findViewById(R.id.cartQTY).setVisibility(View.VISIBLE);

            //----check if 0 --------//
            FloatingActionButton fab = (FloatingActionButton)fragment_obj.getView().findViewById(R.id.fab);
            fab.clearAnimation();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Your FAB click action here...
                    Intent intent = new Intent(v.getContext(), OrderViewActivity.class);
                    startActivity(intent);
                    Toast.makeText(getBaseContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            if (fabQty != 0)
            {
                Animation scaleAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
                fab.startAnimation(scaleAnimation);
            }
            else
            {
                fab.setVisibility(View.GONE);
            }



            // code to handle cancelled state


        }


//        else if (requestCode == CAMERA_REQUEST) {
//            // code to handle data from CAMERA_REQUEST
//        }
//        else if (requestCode == CONTACT_VIEW) {
//            // code to handle data from CONTACT_VIEW
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMyApp = (AppController) this.getApplicationContext();
        mMyApp.setCurrentActivity(this);// Always call the superclass method first
        RestaurantItemsFragment fragment_obj = (RestaurantItemsFragment) getSupportFragmentManager().
                findFragmentByTag("frag_items_tag");

        TextView cartQTY = (TextView)fragment_obj.getView().findViewById(R.id.cartQTY);
        int fabQty = AppController.getInstance().fabQty;
        cartQTY.setText(Integer.toString(fabQty));

        FloatingActionButton fab = (FloatingActionButton)fragment_obj.getView().findViewById(R.id.fab);
        fab.clearAnimation();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            final RestaurantItemsFragment fragment_obj = (RestaurantItemsFragment) getSupportFragmentManager().
                    findFragmentByTag("frag_items_tag");
            List<Item> items = fragment_obj.getItemsList();

            int index=uri.lastIndexOf('/');
            String item_id  = uri.substring(index +1 ,uri.length());
            Toast.makeText(this, "Suggestion: "+ item_id, Toast.LENGTH_SHORT).show();

            for (Item item : items){
                if (item.getItemID().equals(item_id)){
                    Bundle extras = new Bundle();
                    extras.putString("itemID", item.getItemID());
                    extras.putString("item_nume", item.getName());
                    Intent intent_categorii = new Intent(this, AddItemActivity.class).putExtras(extras);
                    startActivity(intent_categorii);
                }
            }
//            Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
        }
    }

}
