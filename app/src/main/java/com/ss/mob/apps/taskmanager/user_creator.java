package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class user_creator extends Activity {
    public TextInputEditText username;
    public TextInputEditText name;
    public TextInputEditText email;

    public TextInputEditText password1;
    public TextInputEditText password2;

    public Button sign_up;

    public Firebase myFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_creator);
        Firebase.setAndroidContext(this);

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
                String name_text = name.getText().toString();
                String username_text = username.getText().toString();
                String email_text = email.getText().toString();

                String password1_text = password1.getText().toString();
                String password2_text = password2.getText().toString();

                if(password1_text.equals(password2_text)) {
                    Firebase myFireChild = myFire.child(username_text);

                    Firebase name_child = myFireChild.child("name");
                    name_child.setValue(name_text);

                    Firebase email_child = myFireChild.child("email");
                    email_child.setValue(email_text);

                    Firebase password_child = myFireChild.child("password");
                    password_child.setValue(password1_text);
                }else{
                    Toast.makeText(user_creator.this,
                            "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
