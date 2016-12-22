package ro.softronic.mihai.ro.papamia.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ro.softronic.mihai.ro.papamia.Activities.LoginActivity;
import ro.softronic.mihai.ro.papamia.Activities.MainActivity;
import ro.softronic.mihai.ro.papamia.Activities.ProfileActivity;
import ro.softronic.mihai.ro.papamia.R;
//import ro.softronic.mihi.ro.papamia.Adapters.ListViewAdapter;


/**
 * Created by mihai on 21.09.16.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{
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

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignup;
    TextView textViewSignin;
    private FirebaseAuth firebaseAuth;



    public RegisterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        buttonSignup = (Button) rootView.findViewById(R.id.buttonSignup);
        textViewSignin = (TextView)rootView.findViewById(R.id.textViewSignin);

        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        pDialog = new ProgressDialog(getActivity());


        return rootView;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
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

    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        pDialog.setMessage("Registering Please Wait...");
        pDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here

                            startActivity(new Intent(getActivity().getApplicationContext(), ProfileActivity.class));
                            Toast.makeText(getActivity(),"Successfully registered",Toast.LENGTH_LONG).show();
                        }else{
                            //display some message here
                            Toast.makeText(getActivity(),"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        pDialog.dismiss();
                    }
                });

    }
}