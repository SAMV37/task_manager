package com.ss.mob.apps.taskmanager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    public TextView lonely_text;

    public ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);
        Picasso.with(this).setLoggingEnabled(true);
        Firebase.setAndroidContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progress = (ProgressBar) findViewById(R.id.progress_bar);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        name_textView = (TextView) findViewById(R.id.name);

        username = getIntent().getStringExtra("nickname");

        lonely_text = (TextView) findViewById(R.id.lonely_text);

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
                            if(children_count == 0){
                                lonely_text.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.INVISIBLE);
                            }else {
                                lonely_text.setVisibility(View.INVISIBLE);
                                for (int i = 0; i < children_count; i++) {
                                    counter++;
                                    layout_adder(i);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else{
                    lonely_text.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        plus = (ImageButton) findViewById(R.id.plus_button);
        logout = (ImageButton) findViewById(R.id.logout_button);

        scroll = (ScrollView) findViewById(R.id.scroll);

        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(getBaseContext(), account_screen.class);
                        intent.putExtra("nickname", username);
                        startActivity(intent);
                        logout.setAlpha(1);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        logout.setAlpha(.7F);
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
                        plus.setAlpha(1);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        plus.setAlpha(.7F);
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
        final String[] completed = new String[1];

        progress = (ProgressBar) findViewById(R.id.progress_bar);

        progress.setVisibility(View.INVISIBLE);

        final LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        final RelativeLayout layout = new RelativeLayout(this);
//        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        //layout.setX((width - 650) / 2);
        layout.setBackgroundResource(R.drawable.layout_background);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringNum = String.valueOf(num);
                Intent intent = new Intent(getBaseContext(), note_display.class);
                intent.putExtra("nickname", username);
                intent.putExtra("counter", stringNum);
                intent.putExtra("completed", completed[0]);
                startActivity(intent);
            }
        });

        final RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.height = dip(this, 80);
        layoutParam.width = width;

        final RelativeLayout layout2 = new RelativeLayout(this);
        layout2.setBackgroundResource(R.drawable.layout2_back);
        RelativeLayout.LayoutParams layoutParam2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParam2.height = dip(this, 40);
        layoutParam2.width = width;

        layout2.setLayoutParams(layoutParam2);

        myFire = new Firebase("https://task-manager-6ee5b.firebaseio.com/");

        final Firebase nickname_child = myFire.child(username);
        nickname_child.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("" + num)) {
                    nickname_child.child("notes").child("" + num).child("note_name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            note_name = dataSnapshot.getValue().toString();
                            final TextView task_name = new TextView(main_screen.this);
                            task_name.setText(note_name);
                            task_name.setX(dip(main_screen.this, 40));
                            task_name.setY(0);
                            task_name.setMaxHeight(dip(main_screen.this, 75));
                            task_name.setMaxWidth(width - dip(main_screen.this, 250));
                            task_name.setTextSize(30);
                            task_name.setTextColor(Color.BLACK);

                            layout2.addView(task_name);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

                    nickname_child.child("notes").child("" + num).child("note_creation_date").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            note_name = dataSnapshot.getValue().toString();
                            final TextView task_name = new TextView(main_screen.this);
                            task_name.setText(note_name);
                            task_name.setX(width - dip(main_screen.this, 100));
                            task_name.setY(dip(main_screen.this, 5));
                            task_name.setTextColor(Color.BLUE);
                            task_name.setMaxHeight(dip(main_screen.this, 50));
                            task_name.setMaxWidth(dip(main_screen.this, 190));
                            task_name.setTextSize(18);

                            layout2.addView(task_name);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

                    nickname_child.child("notes").child("" + num).child("note_text").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            note_text = dataSnapshot.getValue().toString();
                            final TextView task_text = new TextView(main_screen.this);
                            task_text.setText(note_text);
                            task_text.setTextSize(25);
                            task_text.setY(dip(main_screen.this, 40));
                            task_text.setX(dip(main_screen.this, 12));
                            task_text.setMaxHeight(dip(main_screen.this, 75));
                            task_text.setMaxWidth(width - dip(main_screen.this, 15));
                            task_text.setTextColor(Color.GRAY);
                            layout.addView(task_text);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    final ImageButton image = new ImageButton(main_screen.this);
                    LinearLayout.LayoutParams layoutParams  = new
                            LinearLayout.LayoutParams(dip(main_screen.this, 30), dip(main_screen.this, 30));
                    image.setLayoutParams(layoutParams);
                    image.setX(dip(main_screen.this, 5));
                    image.setY(dip(main_screen.this, 5));
                    nickname_child.child("notes").child("" + num).child("note_completed").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue().toString().equals("true")){
                            image.setBackgroundResource(R.drawable.checked_icon_normal);
                            completed[0] = "true";
                        }else if(dataSnapshot.getValue().toString().equals("false")){
                            image.setBackgroundResource(R.drawable.not_checked_icon_normal);
                            completed[0] = "false";
                        }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    layout2.addView(image);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        layout.setLayoutParams(layoutParam);

        layout.addView(layout2);




        TextView space = new TextView(this);
        space.setText("");
        space.setTextSize(3);


        main_layout.addView(space);
        main_layout.addView(layout);
    }
    public void onBackPressed(){}
    public static int dip(Context context, int pixels) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }
}
