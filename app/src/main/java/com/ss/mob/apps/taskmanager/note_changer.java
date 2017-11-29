package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class note_changer extends Activity {
    public ImageButton save_button;
    public TextInputEditText note_name;
    public TextInputEditText note_text;

    public Firebase myFire;

    public String username;
    public String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_changer);
        Firebase.setAndroidContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        username = getIntent().getStringExtra("nickname");
        count = getIntent().getStringExtra("counter");

        save_button = (ImageButton) findViewById(R.id.save_button);
        note_name = (TextInputEditText) findViewById(R.id.etnote_name);
        note_text = (TextInputEditText) findViewById(R.id.etnote_text);

        final Firebase nickname_child = myFire.child(username);

        nickname_child.child("notes").child(count).child("note_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = (String) dataSnapshot.getValue();
                note_name.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        nickname_child.child("notes").child(count).child("note_text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = (String) dataSnapshot.getValue();
                note_text.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        save_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_UP:
                        Firebase notes = nickname_child.child("notes");

                        Firebase note_num = notes.child(count);

                        Firebase name_child = note_num.child("note_name");
                        name_child.setValue(note_name.getText().toString());

                        Firebase text_child = note_num.child("note_text");
                        text_child.setValue(note_text.getText().toString());

                        Firebase com_child = note_num.child("note_completed");
                        com_child.setValue("false");

                        Time today = new Time(Time.getCurrentTimezone());
                        today.setToNow();

                        Firebase date_child = note_num.child("note_creation_date");
                        date_child.setValue(today.monthDay + "/" + (today.month+1) + "/" + today.year);

                        Intent intent = new Intent(getBaseContext(), note_display.class);
                        intent.putExtra("nickname", username);
                        intent.putExtra("counter", count);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

    }
    public void onBackPressed(){}
}
