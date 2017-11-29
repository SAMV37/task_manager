package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class account_screen extends Activity {

    public TextView note_count_text;
    public TextView age_text;
    public TextView username_text;
    public Button sign_out_button;
    public ImageView logo;

    public String username;

    public Firebase myFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_screen);
        Firebase.setAndroidContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        note_count_text = (TextView) findViewById(R.id.note_count_text);
        age_text = (TextView) findViewById(R.id.age_text);
        sign_out_button = (Button) findViewById(R.id.sign_out_button);
        logo = (ImageView) findViewById(R.id.logo);
        username_text = (TextView) findViewById(R.id.username_text);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        username = getIntent().getStringExtra("nickname");

        myFire.child(username).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username_text.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myFire.child(username).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username_text.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final int[] children_count = new int[1];

        myFire.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("notes")){
                    myFire.child(username).child("notes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            children_count[0] = (int) dataSnapshot.getChildrenCount();
                            note_count_text.setText("" + children_count[0]);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else{
                    note_count_text.setText("0");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        myFire.child(username).child("regi_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = String.valueOf(dataSnapshot.getValue());
                int month = Integer.valueOf(date.charAt(0) + "" + date.charAt(1));
                int day = Integer.valueOf(date.charAt(3) + "" + date.charAt(4));
                int year = Integer.valueOf(date.charAt(6) + "" + date.charAt(7) + date.charAt(8) + date.charAt(9));

                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();

                int now_day = today.monthDay;
                int now_month = today.month + 1;
                int now_year = today.year;

                if(now_year == year){
                    if(now_month == month){
                        if((now_day - day) == 1) {
                            age_text.setText((now_day - day) + " day");
                        }else{
                            age_text.setText((now_day - day) + " days");
                        }
                    }else{
                        Log.d("now_month","" + now_month);
                        Log.d("month","" + month);
                        age_text.setText((now_month - month) + " month");
                    }
                }else{
                    age_text.setText((now_year - year) + " year");
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        sign_out_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("username", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("username", null);
                        editor.apply();

                        startActivity(new Intent(getApplicationContext(), start_screen.class));

                        sign_out_button.setAlpha(.7F);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        sign_out_button.setAlpha(.5F);
                        break;
                }

                return false;
            }
        });

    }
}
