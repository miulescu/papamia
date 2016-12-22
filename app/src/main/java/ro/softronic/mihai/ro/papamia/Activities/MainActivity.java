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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ro.softronic.mihai.ro.papamia.Adapters.FilterAdapter;
import ro.softronic.mihai.ro.papamia.Adapters.NavDrawerAdapter;
import ro.softronic.mihai.ro.papamia.Application.AppController;
import ro.softronic.mihai.ro.papamia.Fragments.ConfigAddressFragment;
import ro.softronic.mihai.ro.papamia.Fragments.OferteFragment;
import ro.softronic.mihai.ro.papamia.Fragments.RestaurantsFragment;
import ro.softronic.mihai.ro.papamia.POJOs.FilterItem;
import ro.softronic.mihai.ro.papamia.POJOs.NavItem;
import ro.softronic.mihai.ro.papamia.POJOs.Restaurant;
import ro.softronic.mihai.ro.papamia.R;
import ro.softronic.mihai.ro.papamia.Utils.TypefaceSpan;

import static ro.softronic.mihai.ro.papamia.R.id.list_header_title;
import static ro.softronic.mihai.ro.papamia.R.id.search;

public class MainActivity extends AppCompatActivity  implements android.widget.CompoundButton.OnCheckedChangeListener {

    private HashMap<String, List<String>> hmap;
    private Activity activity;
    private String orasul;

    private List<NavItem> mNavItemsList;
    private ListView mDrawerListLeft;
//    private ArrayAdapter<String> mAdapterDrawerLeft;
    private NavDrawerAdapter mAdapterDrawerLeft;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private DrawerLayout mDrawerLayout;
    private ListView drawerView;              //lista drawerului din dreapta
    private List<FilterItem> mFilterItemsList;//lista adaptorului la drawerul din dreapta
    private FilterAdapter fa;       // adaptorul checkboxurilor cu filtre in navbar
    private ArrayList<String> sb;           // string ce apare la filtre in headerul listei cu restaurante Ex: pasta, pizza, vegan
    private String joined;
    private CheckBox rgr;                   //  checkbox  cu fatza de radio care este selectat cu toate
    private int no_checked;                 //  contorizeaza numarul de checkuri din nav drawerul din dreapta daca-i 0, se selecteaza toate

    protected AppController mMyApp;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SearchView searchView;

    private static int RC_SIGN_IN = 0;
    private FirebaseAuth auth;

    private boolean bypass;

    @Override
    public void onBackPressed() {
        //nu se mai duce la spash
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }



    private void clearReferences() {
        Activity currActivity = mMyApp.getCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setCurrentActivity(null);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    private void addDrawerItems() {

        mNavItemsList = new ArrayList<NavItem>();
        mNavItemsList.add(new NavItem("Adresa", false, R.drawable.ic_place_black_24dp));
        mNavItemsList.add(new NavItem("Log In", false, R.drawable.ic_vpn_key_black_24dp));
        mNavItemsList.add(new NavItem("Restaurante", false,R.drawable.ic_list_black_24dp));
        mNavItemsList.add(new NavItem("Oferte Speciale", false, R.drawable.ic_add_alert_black_24dp));
        mNavItemsList.add(new NavItem("Despre", false, R.drawable.ic_remove_red_eye_black_24dp));

        String[] osArray = { "Adresa", "Log In", "Oferte speciale", "Despre" };
//        mAdapterDrawerLeft = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mAdapterDrawerLeft =  new NavDrawerAdapter(this, mNavItemsList);
        mDrawerListLeft.setAdapter(mAdapterDrawerLeft);

        mDrawerListLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapterDrawerLeft.setSelectedPosition(position);
                mAdapterDrawerLeft.notifyDataSetChanged();
                if (position == 0){
//                    Intent restaurants = new Intent(getApplicationContext(), ConfigAddressActivity.class);
//                    startActivity(restaurants);
                    getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
//                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
//                    if(fragment != null)
//                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();


                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
                    if (f instanceof RestaurantsFragment | f instanceof  OferteFragment){
                        // do something with f
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,
                                        android.R.anim.fade_out)
                                .remove(f)
                                .commitAllowingStateLoss();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .add(R.id.container, new ConfigAddressFragment(), "address_config_frag")
                            .commitAllowingStateLoss();

                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();

                }
                else if (position == 1){



//                    getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
//
//
//                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
//                    if (f instanceof RestaurantsFragment | f instanceof  OferteFragment){
//                        // do something with f
//                        getSupportFragmentManager().beginTransaction()
//                                .setCustomAnimations(android.R.anim.fade_in,
//                                        android.R.anim.fade_out)
//                                .remove(f)
//                                .commitAllowingStateLoss();
//                    }

//                    auth = FirebaseAuth.getInstance();
//                    if (auth.getCurrentUser() != null){
//                        //already signed id
//                    }else {
//                        startActivityForResult(AuthUI.getInstance()
//                                .createSignInIntentBuilder()
//                                .setProviders(
//                                        AuthUI.FACEBOOK_PROVIDER,
//                                        AuthUI.EMAIL_PROVIDER,
//                                        AuthUI.GOOGLE_PROVIDER
//                                ).build(), RC_SIGN_IN);
//                    }
                    startActivity(new Intent(getApplicationContext(), MultiLoginActivity.class));

//                    getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(android.R.anim.fade_in,
//                                    android.R.anim.fade_out)
//                            .add(R.id.container, new RegisterFragment(), "login_frag")
//                            .commitAllowingStateLoss();
//
//                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//                    mDrawerLayout.closeDrawers();

                }
                else if (position == 3){
                    getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
                    if (f instanceof RestaurantsFragment | f instanceof  ConfigAddressFragment){
                        // do something with f
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,
                                        android.R.anim.fade_out)
                                .remove(f)
                                .commitAllowingStateLoss();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .add(R.id.container, new OferteFragment(), "oferte_frag")
                            .commitAllowingStateLoss();
//                            .commit();

                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                }
                else if (position == 2){
                    getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

//                    Intent restaurants = new Intent(getApplicationContext(), ConfigAddressActivity.class);
//                    startActivity(restaurants);

                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
                    if (f instanceof ConfigAddressFragment | f instanceof  OferteFragment | f instanceof  RestaurantsFragment){
                        // do something with f
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,
                                        android.R.anim.fade_out)
                                .remove(f)
                                .commitAllowingStateLoss();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .add(R.id.container, new RestaurantsFragment(), "restaurants_frag")
                            .commitAllowingStateLoss();

                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();

                }

               // Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Configurari");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //************************************************************
        // fac asta pentru a putea ca contentproviderul sa aiba acces
        // la activitatea asta (lista de restaurante)
        Log.d("********","MainActivity.onCreate: " + FirebaseInstanceId.getInstance().getToken());
        bypass = true;

        mMyApp = (AppController) this.getApplicationContext();
        mMyApp.setCurrentActivity(this);

        preferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        setContentView(R.layout.activity_main);

        // nu bag fragmentul cu restaurante aici ci dupa ce apare dialogul cu selectarea orasului si zonei

        activity = this;

        mDrawerListLeft = (ListView)findViewById(R.id.drawerListLeft);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();


        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mActivityTitle = getTitle().toString();



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mFilterItemsList = new ArrayList<FilterItem>();
        mFilterItemsList.add(new FilterItem("Pizza", false));
        mFilterItemsList.add(new FilterItem("Pasta", false));
        mFilterItemsList.add(new FilterItem("Mic Dejun", false));
        mFilterItemsList.add(new FilterItem("Italian", false));
        mFilterItemsList.add(new FilterItem("Desert", false));
        mFilterItemsList.add(new FilterItem("Chinezesc", false));
        mFilterItemsList.add(new FilterItem("Reduceri", false));
        mFilterItemsList.add(new FilterItem("Vegan", false));
        mFilterItemsList.add(new FilterItem("Raw Vegan", false));
        mFilterItemsList.add(new FilterItem("Vegetarian", false));
        mFilterItemsList.add(new FilterItem("Peste", false));
        mFilterItemsList.add(new FilterItem("Burgeri", false));

        fa = new FilterAdapter(this, mFilterItemsList);

        drawerView = (ListView) findViewById(R.id.drawerList);
        drawerView.setAdapter(fa);

        no_checked = 0;

        sb = new ArrayList<String>();

        View header_drawer = (View) getLayoutInflater().inflate(R.layout.widget_check, null);
        final Spinner sp = (Spinner) header_drawer.findViewById(R.id.sorting_spinner);

        ArrayAdapter<CharSequence> adapter_sorting = ArrayAdapter.createFromResource(this,
                R.array.sortare_array, R.layout.intro_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_sorting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp.setAdapter(adapter_sorting);
        sp.setSelection(0, false);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                Log.d("position-------",hmap.get(Integer.toString(position)).get(0));
                final RestaurantsFragment fragment_obj = (RestaurantsFragment) getSupportFragmentManager().
                        findFragmentByTag("restaurants_frag");
                Log.d("position-------", Integer.toString(position));
                if (fragment_obj != null) fragment_obj.getAdapter().sortData(position - 1);


//              orasul = hmap.get(Integer.toString(position)).get(0);
                //aici vom sorta restaurantele
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        drawerView.addHeaderView(header_drawer, null, true);

        View header_drawer_radio = (View) getLayoutInflater().inflate(R.layout.header_drawer_radio, null);
        drawerView.addHeaderView(header_drawer_radio);

        rgr = (CheckBox) header_drawer_radio.findViewById(R.id.select_all);
        rgr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton c, boolean b) {
                CheckBox chk = (CheckBox) c;
                if (chk.isChecked()) {
                    Log.d("checked", "true");
                    final RestaurantsFragment fragment_obj = (RestaurantsFragment) getSupportFragmentManager().
                            findFragmentByTag("restaurants_frag");
                    fragment_obj.getAdapter().setOriginalData();
                    drawerView.setAdapter(fa);
//                  header.setVisibility(View.GONE);
                    findViewById(R.id.removeHeaderButton).setVisibility(View.GONE);
                    findViewById(R.id.list_header_title).setVisibility(View.GONE);
                    findViewById(R.id.header_layout).setVisibility(View.GONE);
                    no_checked = 0;
//                  sb.remove(filterString);
//                  chk.setChecked(true);
                } else {
                    Log.d("checked", "false");
//                    final RestaurantsFragment fragment_obj = (RestaurantsFragment) getSupportFragmentManager().
//                            findFragmentByTag("restaurants_frag");
//                    fragment_obj.getAdapter().setOriginalData();
                    if (no_checked == 0) {
                        chk.setChecked(true);
                    }
                }
            }

        });

//        View header2 = (View)getLayoutInflater().inflate(R.layout.widget_check,null);
//        drawerView.addHeaderView(header2);

//        String[] orase = {"Craiova","Slatina"};
        final String[] orase = new String[]{"Craiova", "Slatina"};
        hmap = new HashMap<String, List<String>>();
//        hmap.put("0", Arrays.asList(new ArrayList<String>(){ "Centru", "Craiovita" }));

        hmap.put("0", new ArrayList<String>(Arrays.asList("Centru", "Craiovita", "Lapus", "1 Mai", "Baiera", "Brazda lui Novac")));
        hmap.put("1", new ArrayList<String>(Arrays.asList("Centru")));
        SpannableString s = new SpannableString("Restaurante");
        s.setSpan(new TypefaceSpan(this, "Ubuntu-R.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        //        ActionBar actionBar = getActionBar();
        setTitle(s);
//        setTitle("Craiova - Restaurante");
        SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        String oras = settings.getString("Oras", "");

//        setTitle(Html.fromHtml("<small>YOUR TITLE</small>"));

        if (oras.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("Vezi Restaurante langa tine", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    View view = (View) LayoutInflater.from(getApplicationContext()).inflate(R.layout.intro_app, null);
                    LinearLayout l = (LinearLayout) view.findViewById(R.id.intro_layout);
                    String[] parametri = new String[l.getChildCount()];

                    LinearLayout l1 = (LinearLayout) l.getChildAt(1);
                    LinearLayout l2 = (LinearLayout) l.getChildAt(2);

                    Spinner s_orase = (Spinner) view.findViewById(R.id.orase_spinner);
//                    Spinner s_zona = (Spinner)view.findViewById(R.id.zona_spinner);

//                    String orasul = s_orase.getSelectedItem().toString();
//                    String zona = s_zona.getSelectedItem().toString() ;
                    Log.d("spinners-----", orasul);

//
//                    int i;
//
//                    for(i=0;i<l.getChildCount();i++) {
//                        if(l.getChildAt(i) instanceof Spinner) {
//                            Spinner temp=(Spinner)l.getChildAt(i);
//                            parametri[i]=temp.getSelectedItem().toString();
//                        }
//                    }
//                    Log.d("spinners",""+parametri[0]+"\n"+parametri[1]);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new RestaurantsFragment(), "restaurants_frag")
                            .commit();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null);


                }
            });



            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.intro_app, null);
            final Spinner spinner = (Spinner) dialogLayout.findViewById(R.id.orase_spinner);
            spinner.setSelection(0);

// Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.orase_array, R.layout.intro_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner.setSelection(0);

            setColorSpinner(spinner);

            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    Log.d("position", hmap.get(Integer.toString(position)).get(0));
//                        orasul = hmap.get(Integer.toString(position)).get(0);
                    orasul = orase[position];

                    Spinner spin2 = (Spinner) dialogLayout.findViewById(R.id.zona_spinner);
                    setColorSpinner(spin2);
                    ArrayAdapter<String> bb = new ArrayAdapter<String>(activity, R.layout.intro_spinner_item,
                            hmap.get(Integer.toString(position)));
                    bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(bb);
                    spin2.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

            dialog.setView(dialogLayout);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.spicy);
                    float imageWidthInPX = (float) image.getWidth();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                            Math.round(imageWidthInPX * (float) icon.getHeight() / (float) icon.getWidth()));
                    image.setLayoutParams(layoutParams);

                }
            });

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void setColorSpinner(Spinner spinner) {
        Drawable spinnerDrawable = spinner.getBackground().getConstantState().newDrawable();

        spinnerDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spinner.setBackground(spinnerDrawable);
        } else {
            spinner.setBackgroundDrawable(spinnerDrawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        SearchView.SearchAutoComplete autoCompleteTextView = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        if (autoCompleteTextView != null) {
            autoCompleteTextView.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.abc_popup_background_mtrl_mult));
        }
////        ((EditText)((SearchView)findViewById(R.id.searchView)).findViewById(((SearchView)findViewById(R.id.searchView)).getContext().getResources().getIdentifier("android:id/search_src_text", null, null))).setHintTextColor(Color.LTGRAY);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:

            case search:
                Toast.makeText(this, "yahoo", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_info:

                // child drawer view

                if (!mDrawerLayout.isDrawerOpen(drawerView)) {
                    mDrawerLayout.openDrawer(drawerView);
                } else if (mDrawerLayout.isDrawerOpen(drawerView)) {
                    mDrawerLayout.closeDrawer(drawerView);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        final RestaurantsFragment fragment_obj = (RestaurantsFragment) getSupportFragmentManager().
                findFragmentByTag("restaurants_frag");

        final ListView restaurantsListView = fragment_obj.getListView();
        final View header = getLayoutInflater().inflate(R.layout.header_lisview_restaurants, null, false);
        boolean cleared = true;

        int pos = drawerView.getPositionForView(compoundButton) - 1;

        CheckBox chk = (CheckBox) compoundButton;

        FilterItem fi = (FilterItem) chk.getTag();
        fi.setSelected(compoundButton.isChecked());

        String filterString = mFilterItemsList.get(pos).getName();
        if (chk.isChecked()) {
            Log.d("checked", "true");
            no_checked++;
            sb.add(filterString);
            rgr.setChecked(false);

        } else {
            Log.d("checked", "false");
            sb.remove(filterString);
            no_checked--;
            // verifica daca mai e vreounnul
            if (no_checked == 0) rgr.setChecked(true);
        }

        Log.d("Elemente checked----->", String.valueOf(drawerView.getCheckedItemPosition()));

        //aici o sa construiesc filterstring din mai multe
        //este un array global unde pun la click si scot la unclick
        //il fac empty la select all (radio button)

        Log.d("filterString---------->", filterString);
        joined = TextUtils.join(",", sb);
        if (joined.length() != 0) {

            fragment_obj.getAdapter().getFilter().filter(joined);
            if (restaurantsListView.getHeaderViewsCount() != 0) {
                findViewById(R.id.removeHeaderButton).setVisibility(View.VISIBLE);
                findViewById(R.id.list_header_title).setVisibility(View.VISIBLE);
                findViewById(R.id.header_layout).setVisibility(View.VISIBLE);
            }
        } else {
            fragment_obj.getAdapter().setOriginalData();
//          header.setVisibility(View.GONE);
            findViewById(R.id.removeHeaderButton).setVisibility(View.GONE);
            findViewById(R.id.list_header_title).setVisibility(View.GONE);
            findViewById(R.id.header_layout).setVisibility(View.GONE);

//          restaurantsListView.removeHeaderView(header);
        }
        //fragment_obj.getAdapter().getFilter().filter(filterString);
        if (restaurantsListView.getHeaderViewsCount() == 0) {
            ImageButton btn = (ImageButton) header.findViewById(R.id.removeHeaderButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restaurantsListView.removeHeaderView(header);
                    fragment_obj.getAdapter().setOriginalData();
                    SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
                    settings.edit().putBoolean("Filtered", false).commit();

                    rgr = (CheckBox) findViewById(R.id.select_all);
                    rgr.setChecked(true);
                    // TODO : listview unselect all checkboxes!!
                    drawerView.setAdapter(fa);
                    sb.clear();
                }
            });

            TextView tx_header = (TextView) header.findViewById(list_header_title);
//          tx_header.setText("Filtre:  " + filterString);
            tx_header.setText("Filtre:  " + joined);
            restaurantsListView.addHeaderView(header, null, false);
            SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
            settings.edit().putBoolean("Filtered", true).commit();
            //ultimele sunt pentru non click
        } else {
//            ((TextView)restaurantsListView.findViewById(R.id.list_header_title)).setText("Filtre: " + filterString);
            ((TextView) restaurantsListView.findViewById(list_header_title)).setText("Filtre: " + joined);
        }
        Log.d("pozitia", String.valueOf(pos));
    }

    public void onRadioButtonClicked(View v) {
        CheckBox rb = (CheckBox) v;
        if (rb.isChecked() == true) rb.setChecked(false);
        else rb.setChecked(true);

        final RestaurantsFragment fragment_obj = (RestaurantsFragment) getSupportFragmentManager().
                findFragmentByTag("restaurants_frag");
        fragment_obj.getAdapter().setOriginalData();
//            header.setVisibility(View.GONE);
//        findViewById(R.id.removeHeaderButton).setVisibility(View.GONE);
//        findViewById(R.id.list_header_title).setVisibility(View.GONE);
//        findViewById(R.id.header_layout).setVisibility(View.GONE);

        drawerView.setAdapter(fa);

        sb.clear();
    }

    //face parte din searchview  -- atunci cand este selectata o sugestie
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();
            searchView.setQuery(query, true);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            final RestaurantsFragment fragment_obj = (RestaurantsFragment) getSupportFragmentManager().
                    findFragmentByTag("restaurants_frag");
            List<Restaurant> restaurants = fragment_obj.getAdapter().getList();

            int index = uri.lastIndexOf('/');
            String rest_id = uri.substring(index + 1, uri.length());
            Toast.makeText(this, "Suggestion: " + rest_id, Toast.LENGTH_SHORT).show();

            for (Restaurant restaurant : restaurants) {
                if (restaurant.getRestID().equals(rest_id)) {
                    Bundle extras = new Bundle();
                    extras.putString("restID", restaurant.getRestID());
                    extras.putDouble("rest_pretTransport", restaurant.getPret_transport());

                    editor.putString("restaurant_id", restaurant.getRestID());
                    editor.putFloat("restaurant_transport", (float) restaurant.getPret_transport());
                    Intent intent_categorii = new Intent(this, CategoriiActivity.class).putExtras(extras);
                    startActivity(intent_categorii);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyApp = (AppController) this.getApplicationContext();
        mMyApp.setCurrentActivity(this);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN){
//            if (resultCode == RESULT_OK){
//                //user loged in
//                Log.d("Auth", auth.getCurrentUser().getEmail());
//            }
//            else{
//                Log.d("Auth", "Not authenticated");
//            }
//
//        }
//    }
}


