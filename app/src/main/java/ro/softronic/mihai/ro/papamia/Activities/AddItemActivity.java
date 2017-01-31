package ro.softronic.mihai.ro.papamia.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import ro.softronic.mihai.ro.papamia.Adapters.TabsPagerAdapter;
import ro.softronic.mihai.ro.papamia.Fragments.AddItemFragment;
import ro.softronic.mihai.ro.papamia.Fragments.ItemExtraFragment1;
import ro.softronic.mihai.ro.papamia.R;

public class AddItemActivity extends AppCompatActivity {
    PagerAdapter adapter;
    public ViewPager viewPager;
    public TabLayout tabLayout;

    private String mItemName;
    private int mItemQty;
    private double mItemSumaTotala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("item_nume"));
        boolean item_has_extras = extras.getBoolean("item_has_extras");
        //daca trebuie sa fac tabbed View:
        if(item_has_extras){
            Toast.makeText(this, "Vom trimite un SMS cu recomandarea DVS.", Toast.LENGTH_LONG).show();
            setContentView(R.layout.fragment_add_item_extra_tabview);
            adapter = new TabsPagerAdapter(getSupportFragmentManager(), AddItemActivity.this);

//        adapter = new MyPagerAdapter(getSupportFragmentManager());
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);

            // Give the TabLayout the ViewPager
            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);

        }else{
            setContentView(R.layout.add_item);
            if (savedInstanceState == null) {
                AddItemFragment ai = new AddItemFragment();

                Bundle args1 = new Bundle();
                args1.putBoolean("has_extras", false);
                ItemExtraFragment1 ief = new ItemExtraFragment1();
                ai.setArguments(args1);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, ai)
                        .commit();
            }
            SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    super.onBackPressed();
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

    public String getItemID(){
        Bundle extras = getIntent().getExtras();
        String value = null;
        if (extras != null) {
            value = extras.getString("itemID");
        }
//        return Integer.parseInt(value);
        return value;
    }

    public  void setmItemName(String name){
        mItemName = name;
    }

    public void setmItemQty (int itemQty){
        mItemQty = itemQty;
    }

    public void setmItemSumaTotala(double sumaTotala){
        mItemSumaTotala = sumaTotala;
    }

    public String getmItemName(){
        return mItemName;
    }

    public  int getmItemQty(){
        return mItemQty;
    }

    public double getmItemSumaTotala(){
        return mItemSumaTotala;
    }







}
