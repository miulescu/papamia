package ro.softronic.mihai.ro.papamia.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ro.softronic.mihai.ro.papamia.R;


public class MultiLoginActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views


    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private static int RC_SIGN_IN = 0;

    //progress dialog
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor ;

    private Button continuaButton;
    private Button logoutButton;
    private TextView salutText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences= getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();
        setContentView(R.layout.activity_multilogin);

        salutText = (TextView)findViewById(R.id.salut);



        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //user is loged in
            //arata pagina normala adica cea cu profilul
            salutText.setText("Salut, " + firebaseAuth.getCurrentUser().getEmail().toString());
            Log.d("AUTH", firebaseAuth.getCurrentUser().getEmail().toString());

        }else{
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.GreenTheme)
                    .setLogo(R.drawable.logo)
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.EMAIL_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER
                    ).build(),RC_SIGN_IN);
        }

        continuaButton = (Button)findViewById(R.id.buttonLogout);
        continuaButton.setOnClickListener(this);
        logoutButton = (Button)findViewById(R.id.continua);
        logoutButton.setOnClickListener(this);




        //if the objects getcurrentuser method is not null
        //means user is already logged in
//        if(firebaseAuth.getCurrentUser() != null){
//            //close this activity
//            finish();
//            //opening profile activity
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonLogout){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("Auth", "USER LOGOUT");
                            finish();
                        }
                    });
        }else  if (view.getId() == R.id.continua) {
                    finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                //user loged in
                salutText.setText("Bine ai venit, " + firebaseAuth.getCurrentUser().getEmail().toString());
                Toast.makeText(this, "Salut," + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                Log.d("Auth", firebaseAuth.getCurrentUser().getEmail());


//                int comanda = preferences.getInt("loginOrder", -1);
//                if (comanda == 1){
//                    findViewById(R.id.con).setVisibility(View.VISIBLE);
//                }


//                finish();

            }
            else{
                Log.d("Auth", "Not authenticated");
            }

        }
    }
}