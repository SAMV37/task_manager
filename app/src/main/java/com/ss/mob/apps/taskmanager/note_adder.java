package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class note_adder extends Activity {
    public Firebase myFire;
    public String username;
    public long counter;

    public Button create;

    public TextInputEditText note_name;
    public TextInputEditText note_text;

    public String text_name;
    public String text_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_adder);
        Firebase.setAndroidContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        username = getIntent().getStringExtra("nickname");

        final Firebase usernameF = myFire.child(username);

        usernameF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("notes")){
                    usernameF.child("notes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            counter = dataSnapshot.getChildrenCount();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else{
                    counter = 0;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        note_name = (TextInputEditText) findViewById(R.id.etnote_name);
        note_text = (TextInputEditText) findViewById(R.id.etnote_text);

        note_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        create = (Button) findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_name = note_name.getText().toString();
                text_text = note_text.getText().toString();

                Firebase usernameF = myFire.child(username);

                usernameF.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        counter = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                Firebase notes = usernameF.child("notes");

                Firebase note_num = notes.child("" + counter);

                Firebase name_child = note_num.child("note_name");
                name_child.setValue(text_name);

                Firebase text_child = note_num.child("note_text");
                text_child.setValue(text_text);

                Firebase com_child = note_num.child("note_completed");
                com_child.setValue("false");

                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();

                Firebase date_child = note_num.child("note_creation_date");
                date_child.setValue(today.monthDay + "/" + (today.month+1) + "/" + today.year);




                Intent intent = new Intent(getBaseContext(), main_screen.class);
                intent.putExtra("nickname", username);
                startActivity(intent);
            }
        });

    }
}
