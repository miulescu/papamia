package ro.softronic.mihai.ro.papamia.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ro.softronic.mihai.ro.papamia.Fragments.ConfigAddressFragment;
import ro.softronic.mihai.ro.papamia.R;

public class ConfigAddressActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ConfigAddressFragment())
                    .commit();
        }

//        SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
//        Bundle extras = getIntent().getExtras();
//        setTitle(extras.getString("item_nume"));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

}
