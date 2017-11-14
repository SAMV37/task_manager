package com.ss.mob.apps.taskmanager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

public class main_screen extends Activity {

    ImageView profile_pic;
    String url = "https://scontent-frx5-1.xx.fbcdn.net/v/t1.0-1/p160x160/11892207_738044959674454_8710502169150256130_n.jpg?oh=bb7d874ea4d65f6ec7eb61eddf284b36&oe=5A6BF1C2";
    public int counter = 0;
    ScrollView scroll;
    ImageButton plus;
    ImageButton logout;
    public Firebase myFire;

    public TextView name_textView;

    public String name;

    public long children_count;

    public String note_name;
    public String note_text;

    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Picasso.with(this).setLoggingEnabled(true);
        Firebase.setAndroidContext(this);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        name_textView = (TextView) findViewById(R.id.name);

        username = getIntent().getStringExtra("nickname");

        final Firebase nickname_child = myFire.child(username);

        nickname_child.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
                name_textView.setText(name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        nickname_child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("notes")){
                    nickname_child.child("notes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            children_count = dataSnapshot.getChildrenCount();
                            for(int i = 1;i <= children_count;i++){
                                counter++;
                                layout_adder(i);
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        plus = (ImageButton) findViewById(R.id.plus_button);
        logout = (ImageButton) findViewById(R.id.logout_button);

        scroll = (ScrollView) findViewById(R.id.scroll);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("username", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("username", null);
                        editor.apply();

                        startActivity(new Intent(getApplicationContext(), start_screen.class));
                        logout.setBackgroundResource(R.drawable.logout_grey);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        logout.setBackgroundResource(R.drawable.logout_black);
                        break;
                }

                return false;
            }
        });

        plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(getBaseContext(), note_adder.class);
                        intent.putExtra("nickname", username);
                        intent.putExtra("counter", counter);
                        startActivity(intent);
                        plus.setBackgroundResource(R.drawable.note_adder_grey);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        plus.setBackgroundResource(R.drawable.note_adder_black);
                        break;
                }

                return false;
            }
        });

    }

    private void loadImageFromUrl(String url){
        Picasso.with(this).load(url).resize(30,30).into(profile_pic);
    }
    public void layout_adder(final int num) {
        final LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        final RelativeLayout layout = new RelativeLayout(this);
//        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setX((width - 650) / 2);
        layout.setAlpha(.7F);
        layout.setBackgroundResource(R.drawable.layout_background);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////on click function
            }
        });

        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.height = 300;
        layoutParam.width = 650;
        layoutParam.setMargins(0,30,0,0);

        layout.setLayoutParams(layoutParam);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        final Firebase nickname_child = myFire.child(username);

        nickname_child.child("notes").child("note" + num).child("note_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                note_name = dataSnapshot.getValue().toString();
                final TextView task_name = new TextView(main_screen.this);
                task_name.setText(note_name);
                task_name.setX(20);
                task_name.setTextSize(35);
                task_name.setTextColor(Color.BLACK);

                layout.addView(task_name);

                Log.d("Note " + num + " name", "" + note_name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        nickname_child.child("notes").child("note" + num).child("note_text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                note_text = dataSnapshot.getValue().toString();
                final TextView task_text = new TextView(main_screen.this);
                task_text.setText(note_text);
                task_text.setTextSize(25);
                task_text.setY(70);
                task_text.setX(20);
                task_text.setMaxWidth(620);
                task_text.setMaxHeight(200);
                task_text.setTextColor(Color.GRAY);
                layout.addView(task_text);

                Log.d("Note " + num + " text", "" + note_text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        TextView space = new TextView(this);
        space.setText("             ");
        space.setTextSize(20);


        main_layout.addView(space);
        main_layout.addView(layout);
    }
}
