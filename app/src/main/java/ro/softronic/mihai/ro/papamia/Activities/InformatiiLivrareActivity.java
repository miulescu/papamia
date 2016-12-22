package ro.softronic.mihai.ro.papamia.Activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ro.softronic.mihai.ro.papamia.Fragments.InformatiiLivrareFragment;
import ro.softronic.mihai.ro.papamia.R;

public class InformatiiLivrareActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        String suma_totala = extra.getString("suma_totala", "0");

        setContentView(R.layout.activity_order_info);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new InformatiiLivrareFragment())
                    .commit();
        }

        SharedPreferences settings = getSharedPreferences(getString(R.string.preference_file_key), 0);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.action_bar_info_livrare_layout);

        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_info_livrare_layout, null);
        View v = getSupportActionBar().getCustomView();
        TextView action_bar_title = (TextView) v.findViewById(R.id.title);
        action_bar_title.setText("Informatii Livrare");
        TextView priceviewTitle = (TextView) v.findViewById(R.id.order_price);
        priceviewTitle.setText("RON " + suma_totala);
        priceviewTitle.setTypeface(null, Typeface.BOLD);
        // din bundle

        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
//        ab.setTitle("This is Title");
//        ab.setSubtitle("This is Subtitle");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

}
