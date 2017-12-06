package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class user_creator extends Activity {
    public TextInputEditText username;
    public TextInputEditText name;
    public TextInputEditText email;

    public TextInputEditText password1;
    public TextInputEditText password2;

    public Button sign_up;

    public Firebase myFire;

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_creator);
        Firebase.setAndroidContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        name = (TextInputEditText) findViewById(R.id.etName);
        username = (TextInputEditText) findViewById(R.id.etUsername);
        email = (TextInputEditText) findViewById(R.id.etEmail);

        password1 = (TextInputEditText) findViewById(R.id.etPassword1);
        password2 = (TextInputEditText) findViewById(R.id.etPassword2);

        sign_up = (Button) findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_text = name.getText().toString();
                final String username_text = username.getText().toString();
                final String email_text = email.getText().toString();

                final String password1_text = password1.getText().toString();
                final String password2_text = password2.getText().toString();

                myFire.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(username_text)){
                            if(password1_text.equals(password2_text)) {
                                Time today = new Time(Time.getCurrentTimezone());
                                today.setToNow();

                                int now_day = today.monthDay;
                                int now_month = today.month + 1;
                                int now_year = today.year;

                                Log.d("Day", "" + now_day);

                                if(now_month < 10){
                                    if(now_day < 10) {
                                        date = "0" + now_month + "/0" + now_day + "/" + now_year;
                                        Log.d("Date", date + "");
                                    }else{
                                        date = "0" + now_month + "/" + now_day + "/" + now_year;
                                    }
                                }else{
                                    if(now_day < 10) {
                                        date = now_month + "/0" + now_day + "/" + now_year;
                                    }else {
                                        date = now_month + "/" + now_day + "/" + now_year;
                                    }
                                }



                                Firebase myFireChild = myFire.child(username_text);

                                Firebase name_child = myFireChild.child("name");
                                name_child.setValue(name_text);

                                Firebase email_child = myFireChild.child("email");
                                email_child.setValue(email_text);

                                Firebase password_child = myFireChild.child("password");
                                password_child.setValue(password1_text);

                                Firebase regi_date_child = myFireChild.child("regi_date");
                                regi_date_child.setValue(date);

                                Intent intent = new Intent(getBaseContext(), main_screen.class);
                                intent.putExtra("nickname", username_text);
                                startActivity(intent);

                            }else{
                                Toast.makeText(user_creator.this,
                                        "Passwords do not match", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            //User already exists ))
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
    public void onBackPressed(){}
}
