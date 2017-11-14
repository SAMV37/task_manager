package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class start_screen extends Activity{

    public Firebase myFire;
    public TextInputEditText username;
    public Button login_button;
    public TextInputEditText password;
    public Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_screen);
        Firebase.setAndroidContext(this);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        username = (TextInputEditText) findViewById(R.id.etUsername);
        password = (TextInputEditText) findViewById(R.id.etPassword);
        login_button = (Button) findViewById(R.id.login_button);
        signup_button = (Button) findViewById(R.id.signup_button);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("username", 0);
        String user_name = settings.getString("username", "null");


        if(user_name == "null") {
            login_button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_UP:
                            login_button.setAlpha(.5F);
                            break;
                        case MotionEvent.ACTION_DOWN:
                            login_button.setAlpha(.7F);
                            if (username.getText().toString().matches("") && password.getText().toString().matches("")) {
                                Toast.makeText(start_screen.this,
                                        "Please fill in Username and Password fields", Toast.LENGTH_LONG).show();
                            } else {
                                myFire.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(username.getText().toString())) {
                                            //child exists
                                            final Firebase nickname = myFire.child(username.getText().toString());

                                            nickname.child("password").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (password.getText().toString().equals(dataSnapshot.getValue().toString())) {
                                                        SharedPreferences settings = getApplicationContext().getSharedPreferences("username", 0);
                                                        SharedPreferences.Editor editor = settings.edit();
                                                        editor.putString("username", username.getText().toString());

                                                        editor.apply();

                                                        Intent intent = new Intent(getBaseContext(), main_screen.class);
                                                        intent.putExtra("nickname", username.getText().toString());
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(start_screen.this,
                                                                "Password is incorrect", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {

                                                }
                                            });
                                        } else {
                                            Toast.makeText(start_screen.this,
                                                    "Sorry no user registered with that username", Toast.LENGTH_LONG).show();
                                            Log.d("This was found", "----------->" + username.getText().toString() + "," + password.getText().toString() + "<-----------------");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }
                            break;
                    }

                    return true;
                }
            });
        }else{
            Intent intent = new Intent(getBaseContext(), main_screen.class);
            intent.putExtra("nickname", user_name);
            startActivity(intent);
        }
        signup_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        signup_button.setAlpha(.7F);
                        break;
                    case MotionEvent.ACTION_UP:
                        startActivity(new Intent(getApplicationContext(), user_creator.class));
                        signup_button.setAlpha(.5F);
                        break;
                }

                return true;
            }
        });

    }
}
