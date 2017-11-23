package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

public class note_display extends Activity{

    public TextView note_name;
    public ImageButton back;
    public TextView note_text;
    public ImageButton edit;
    public ImageButton complete;

    public String username;
    public String count;

    public Firebase myFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_note_display);
        Firebase.setAndroidContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        note_name = (TextView) findViewById(R.id.note_name);
        note_text = (TextView) findViewById(R.id.note_text);
        back = (ImageButton) findViewById(R.id.back_button);
        edit = (ImageButton) findViewById(R.id.edit_button);
        complete = (ImageButton) findViewById(R.id.complete_button);

        username = getIntent().getStringExtra("nickname");
        count = getIntent().getStringExtra("counter");

        Log.d("Number", count + "");

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        final Firebase nickname_child = myFire.child(username);

        nickname_child.child("notes").child("" + count).child("note_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue().toString();
                note_name.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        nickname_child.child("notes").child("" + count).child("note_text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue().toString();
                note_text.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        final boolean[] completed = new boolean[1];
        nickname_child.child("notes").child("" + count).child("note_completed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch(dataSnapshot.getValue().toString()){
                    case "true":
                        completed[0] = true;
                        complete.setBackgroundResource(R.drawable.checked_icon_normal);
                        break;
                    case "false":
                        completed[0] = false;
                        complete.setBackgroundResource(R.drawable.not_checked_icon_normal);
                        break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(completed[0] == true){
                    Log.d("Darav", "false");
                    nickname_child.child("notes").child("" + count).child("note_completed").setValue("false");
                    complete.setBackgroundResource(R.drawable.not_checked_icon_normal);
                }else{
                    Log.d("Darav", "true");
                    nickname_child.child("notes").child("" + count).child("note_completed").setValue("true");
                    complete.setBackgroundResource(R.drawable.checked_icon_normal);
                }
            }
        });

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(getBaseContext(), main_screen.class);
                        intent.putExtra("nickname", username);
                        startActivity(intent);
                        back.setBackgroundResource(R.drawable.back);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        back.setBackgroundResource(R.drawable.back_grey);
                        break;
                }
                return false;
            }
        });

        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(getBaseContext(), note_changer.class);
                        intent.putExtra("nickname", username);
                        intent.putExtra("counter", count);
                        startActivity(intent);
                        break;
                    case MotionEvent.ACTION_DOWN:

                        break;
                }
                return false;
            }
        });




    }
}
