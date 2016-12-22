package ro.softronic.mihai.ro.papamia.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ro.softronic.mihai.ro.papamia.Fragments.AddItemFragment;
import ro.softronic.mihai.ro.papamia.R;

public class AddItemActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddItemFragment())
                    .commit();
        }

        SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("item_nume"));

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






}
