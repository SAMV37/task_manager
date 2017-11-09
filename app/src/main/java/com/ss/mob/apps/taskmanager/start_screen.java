package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFire.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username.getText().toString())) {
                            //child exists
                            Firebase nickname = myFire.child(username.getText().toString());

                            nickname.child("password").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(password.getText().toString().equals(dataSnapshot.getValue().toString())){
                                        Toast.makeText(start_screen.this,
                                                "Logged in successful", Toast.LENGTH_LONG).show();
                                    }else{
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
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), user_creator.class));
            }
        });


    }
}
