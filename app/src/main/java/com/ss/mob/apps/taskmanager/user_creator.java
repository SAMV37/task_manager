package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
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

    public String name_text;
    public String username_text;
    public String email_text;

    public Button sign_up;

    public Firebase myFire;

    public String password1_text;
    public String password2_text;

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
                name_text = name.getText().toString();
                username_text = username.getText().toString();
                email_text = email.getText().toString();

                password1_text = password1.getText().toString();
                password2_text = password2.getText().toString();

                myFire.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(username_text)){
                            if(error_checker()) {
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
                                visible_error();
                            }
                        }else{
                            visible_error();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        sign_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        sign_up.setAlpha(.7F);
                        break;
                    case MotionEvent.ACTION_UP:
                        sign_up.setAlpha(1);
                        break;
                }
                return false;
            }
        });
    }
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), start_screen.class));
    }
    public void visible_error(){
        final TextView error_1 = (TextView) findViewById(R.id.error_1);
        final TextView error_2 = (TextView) findViewById(R.id.error_2);


        error_1.setVisibility(View.VISIBLE);
        error_2.setVisibility(View.VISIBLE);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {}
            public void onFinish() {

                error_1.setVisibility(View.INVISIBLE);
                error_2.setVisibility(View.INVISIBLE);
            }

        }.start();
    }
    public boolean error_checker(){
        if(password1_text.equals(password2_text) && email_text != null && name_text != null && username_text != null){
            return true;
        }
        return false;
    }
}
