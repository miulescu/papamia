package ro.softronic.mihai.ro.papamia.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ro.softronic.mihai.ro.papamia.Activities.InformatiiLivrareActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Adapters.OrdersCursorAdapter;
import ro.softronic.mihai.ro.papamia.POJOs.Order;
import ro.softronic.mihai.ro.papamia.R;

public class InformatiiLivrareFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog dialog = null;
    private static  String URL = "https://cryptic-harbor-22680.herokuapp.com/things";
    private ProgressDialog pDialog;
    private List<Order> orderList =  new ArrayList<Order>();
    private ListView listView;
    private OrdersCursorAdapter odc;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor ;
    private InformatiiLivrareActivity i;

    private DatabaseReference mDatabase;

    private static final int PERMISSION_SEND_SMS = 1;


    //pentru locatie
    private LocationListener locationListener;
    private LocationManager lm;
    private ImageButton btn_getLocation;

    private TextView txt_locatia;

    private EditText edtOrasul, edtLocatia;
    private TextView txtOrasul;

    public static final int USE_ADDRESS_LOCATION = 2;
    int fetchType = USE_ADDRESS_LOCATION;


    public InformatiiLivrareFragment(){
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        }
        else
        {
           // Toast.makeText(getActivity(), "No permissions.", Toast.LENGTH_LONG).show();
        }

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener(){

            @Override
            public void onLocationChanged(Location location) {
                new GeocodeAsyncTask().execute(new LatLng(location.getLatitude(),location.getLongitude()));
                edtLocatia.setText("\n " + location.getLongitude() + " " + location.getLatitude());
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                lm.removeUpdates(locationListener);
                txt_locatia.clearAnimation();
//                turnGPSOff();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        Bundle extras = getActivity().getIntent().getExtras();
        final String email_body = extras.getString("str_email_de_trimis", "");


        View rootView = inflater.inflate(R.layout.fragment_informatii_livrare, container, false);

        preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        i = (InformatiiLivrareActivity)getActivity();
        editor = preferences.edit();

        final CheckBox chk = (CheckBox)rootView.findViewById(R.id.chk_livrare);
        chk.setOnClickListener(new CompoundButton.OnClickListener(){

            @Override
            public void onClick(View v ) {
                chk.setChecked(true);
            }
        });

        ImageButton btn_email  = (ImageButton) rootView.findViewById(R.id.email);
        ImageButton btn_sms  = (ImageButton) rootView.findViewById(R.id.sms);
        ImageButton btn_telegram  = (ImageButton) rootView.findViewById(R.id.telegram);
        edtOrasul = (EditText)rootView.findViewById(R.id.edtOrasul);
        edtLocatia = (EditText)rootView.findViewById(R.id.edtLocatia);
        txtOrasul = (TextView)rootView.findViewById(R.id.txtOrasul);
        btn_getLocation = (ImageButton) rootView.findViewById(R.id.btn_getLocation);
        txt_locatia = (TextView) rootView.findViewById(R.id.txtLocatia);

        btn_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email_restaurant   = preferences.getString("restaurant_email","");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                emailIntent.setType("text/plain");
                emailIntent.setData(Uri.parse("mailto:"));
//                emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email_restaurant});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Comanda ");
//                emailIntent.putExtra(Intent.EXTRA_TEXT,email_body);
                emailIntent.putExtra(Intent.EXTRA_TEXT,"www");
                if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(emailIntent);
                }


//                startActivity(emailIntent);

//                Toast.makeText(getActivity(), email_restaurant, Toast.LENGTH_LONG).show();


            }
        });
        btn_sms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                    //************* Validate EditBoxes **********************//
                    // daca locatia este in coordonate atunci ia din edittextul de mai sus
                    // daca nu atunci ia adresa Orasul + locatia
                    StringBuilder strConstructorSMS = new StringBuilder(email_body);

                    String locatie_raw = edtLocatia.getText().toString();
                    String[] parts_locatie_raw = locatie_raw.split(" ");
                    Double latitude = 0.0, longitude = 0.0;
                    String adresa;String orasul;

                    if (parts_locatie_raw[1].indexOf('.') != -1)
                    {
                        latitude = Double.parseDouble(parts_locatie_raw[1]);
                        longitude = Double.parseDouble(parts_locatie_raw[2]);
                        adresa  = edtOrasul.getText().toString();
                        strConstructorSMS.append("\n Adresa : " + adresa);
                        strConstructorSMS.append("\n Latitudine: " + latitude);
                        strConstructorSMS.append("\n Longitudine " + longitude);
                    }
                    else{
                        adresa = edtLocatia.getText().toString();
                        orasul = edtOrasul.getText().toString();
                        strConstructorSMS.append("\n Adresa : " + orasul + " " + adresa);
                    }


                try {


                    //****************** Send SMS ***********************//
                    SmsManager smsManager = SmsManager.getDefault();
//                    StringBuilder strConstructor = new StringBuilder(email_body);
                    ArrayList<String> detrimis  = smsManager.divideMessage(strConstructorSMS.toString());

                    smsManager.sendMultipartTextMessage("+40746048564", null, detrimis, null, null);
                    Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();

                    //*************send notification (insert in Firebase Database) **************

                    //first construct de message
                    sendNotificationToRestaurantAndDriver(email_body, "Temple Bars");


                }
                catch (Exception e) {
                    Toast.makeText(getActivity(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
//        btn_getLocation.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v)
//            {
//                //****************** Locatie *****************************//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                            getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(new String[]{
//                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.INTERNET},10);
//                        });
//                        return ;
//                    }else {
//                    configureButton();
//                }
//
//            }
//        });
        configureButton();
        return rootView;
    }

    private void configureButton(){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                lm.requestLocationUpdates("gps", 0, 0, locationListener);


                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                txt_locatia.startAnimation(anim);
            }
        });
    }

    private void sendNotificationToRestaurantAndDriver( String detrimis, String s) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Map notification = new HashMap();
        notification.put("Restaurant", s);
        notification.put("Order", detrimis);
        mDatabase.child("notificationsRequest").push().setValue(notification);
//        String locationProvider = LocationManager.NETWORK_PROVIDER;
//        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case PERMISSION_SEND_SMS:
                if(grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //do send or read sms
                }
            case 10:
                configureButton();
                break;
        }
    }

    private LatLng getLastKnownLocation(boolean isMoveMarker) {


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }
//        Location loc = lm.getLastKnownLocation(provider);
//        if (loc != null) {
//            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
//            return latLng;
//        }
        return null;
    }

    class GeocodeAsyncTask extends AsyncTask<LatLng, Void, Address> {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Address doInBackground(LatLng...  latlngs) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = null;

//            if(fetchType == USE_ADDRESS_NAME) {
//                String name = addressEdit.getText().toString();
//                try {
//                    addresses = geocoder.getFromLocationName(name, 1);
//                } catch (IOException e) {
//                    errorMessage = "Service not available";
//                    Log.e(TAG, errorMessage, e);
//                }
//            }
            if(fetchType == USE_ADDRESS_LOCATION) {
                LatLng loc = latlngs[0];
                double latitude = loc.latitude;
                double longitude = loc.longitude;

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException ioException) {
                    errorMessage = "Service Not Available";
                    Log.e(TAG, errorMessage, ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    errorMessage = "Invalid Latitude or Longitude Used";
                    Log.e(TAG, errorMessage + ". " +
                            "Latitude = " + latitude + ", Longitude = " +
                            longitude, illegalArgumentException);
                }
            }
            else {
                errorMessage = "Unknown Type";
                Log.e(TAG, errorMessage);
            }

            if(addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if(address == null) {
                // scrie fara adresa
                edtOrasul.setText("Latitude: " + address.getLatitude() + "\n" +
                        "Longitude: " + address.getLongitude() + "\n" +
                        "Address: " + "nu am gasit-o");
            }
            else {
                String addressName = "";
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressName +=  " " + address.getAddressLine(i);
                }
                edtOrasul.setText(addressName);
                txtOrasul.setText("Adresa");

            }
        }
    }

    private void turnGPSOff(){
        String provider = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getActivity().sendBroadcast(poke);
        }
    }

}