package com.ss.mob.apps.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

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
        setContentView(R.layout.activity_note_adder);
        Firebase.setAndroidContext(this);

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

                Firebase note_num = notes.child("note" + (counter + 1));

                Firebase name_child = note_num.child("note_name");
                name_child.setValue(text_name);

                Firebase text_child = note_num.child("note_text");
                text_child.setValue(text_text);

                Intent intent = new Intent(getBaseContext(), main_screen.class);
                intent.putExtra("nickname", username);
                startActivity(intent);
            }
        });

    }
}
