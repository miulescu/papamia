package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.R;

import static ro.softronic.mihai.ro.papamia.R.id.locatia;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class ConfigAddressFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private String URLItem = "";
    private ProgressDialog dialog = null;

//    private ArrayList<Restaurant> restaurantsList;

    private ProgressDialog pDialog;
    private TextView textQTY, textSumaTotala;
    private String price;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    // private ProgressBar mProgressBar;
    private Dialog pd;

    MenuItem mi1;
    MenuItem mi2;



    public ConfigAddressFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_config_address, container, false);
        final EditText edtx_orasul = (EditText)rootView.findViewById(R.id.email_address);
        final EditText edtx_locatia = (EditText)rootView.findViewById(locatia);

        LinearLayout linclick = (LinearLayout)rootView.findViewById(R.id.restaurante_btn);
        linclick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //---pun in baza de date de pe telefon order items

                Intent data = new Intent();

                String orasul = edtx_orasul.getText().toString();
                String locatia = edtx_locatia.getText().toString();
                getActivity().getFragmentManager().popBackStack();
                //--- fragmentul cu restaurante se va construi cu datele de mai sus  ---
                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                if (f instanceof ConfigAddressFragment | f instanceof  OferteFragment){
                    // do something with f
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .remove(f)
                            .commitAllowingStateLoss();
                }
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,  new RestaurantsFragment(),"restaurants_frag")
//                        .add(R.id.container, new RestaurantsFragment(), "restaurants_frag")
                        .commit();

//                data.putExtra("orasul",orasul);
            }
        });

        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        mi1 = menu.getItem(0);
        mi2 = menu.getItem(1);


        mi1.setVisible(false);
        mi2.setVisible(false);
    }
}






