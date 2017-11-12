package com.ss.mob.apps.taskmanager;

import android.app.Activity;
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
    public Firebase myFire;

    public TextView name_textView;

    public String name;

    public long children_count;

    public String note_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Picasso.with(this).setLoggingEnabled(true);
        Firebase.setAndroidContext(this);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        name_textView = (TextView) findViewById(R.id.name);

        String username = getIntent().getStringExtra("nickname");

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
                    children_count = dataSnapshot.getChildrenCount();
                    Log.d("has:", children_count + " notes");
                    for(int i = 1;i<= children_count;i++){
                        nickname_child.child("notes").child("note" + i).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                note_text = dataSnapshot.getValue().toString();
                                layout_adder(counter, note_text);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        counter++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        loadImageFromUrl(url);

        plus = (ImageButton) findViewById(R.id.plus_button);

        scroll = (ScrollView) findViewById(R.id.scroll);

        plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        plus.setBackgroundDrawable( getResources().getDrawable(R.drawable.rounded_add_button_clicked));
                        break;
                    case MotionEvent.ACTION_UP:
                        plus.setBackgroundDrawable( getResources().getDrawable(R.drawable.rounded_add_button_not_clicked));
                        layout_adder(counter, "");
                        counter++;
                        scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        break;
                }
                return true;
            }
        });


    }



    private void loadImageFromUrl(String url){
        Picasso.with(this).load(url).resize(30,30).into(profile_pic);
    }

    public void layout_adder(final int num, String text) {
        final TextView task_name = new TextView(this);
        //task_name.setText("Task number " + (num + 1));
        task_name.setText(text);
        task_name.setTextSize(30);
        task_name.setTextColor(Color.BLACK);


        final LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        RelativeLayout layout = new RelativeLayout(this);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setX((width - 650) / 2);
        layout.setAlpha(.8F);
        layout.setBackgroundResource(R.drawable.layout_background);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(main_screen.this, "Number: " + (num + 1),
                        Toast.LENGTH_LONG).show();
            }
        });

        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.height = 300;
        layoutParam.width = 650;
        layoutParam.setMargins(0,30,0,0);

        layout.setLayoutParams(layoutParam);



        layout.addView(task_name);

        TextView space = new TextView(this);
        space.setText("             ");
        space.setTextSize(20);


        main_layout.addView(space);
        main_layout.addView(layout);
    }
}
