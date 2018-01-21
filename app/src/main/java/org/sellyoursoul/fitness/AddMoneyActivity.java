package org.sellyoursoul.fitness;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.*;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

/**
 * Created by mdnah on 1/20/2018.
 */

public class AddMoneyActivity extends Activity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");
    private DatabaseReference poolsRef = database.getReference("pools");
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        final String userId = getIntent().getStringExtra("userId");

        final EditText money = findViewById(R.id.money);
        TextView addMoneyBtn = findViewById(R.id.addMoneyBtn);

        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                usersRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        final User user = (User) dataSnapshot.getValue(User.class);

                        if(user.getId() != null && user.getId().equals(userId)) {
                            usersRef.child(userId).child("balance").setValue(user.getBalance() + Float.parseFloat(money.getText().toString()));

                            if (user.getCurrentPool() != null) {

                                poolsRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        final Pool pool = dataSnapshot.getValue(Pool.class);

                                        if (user.getCurrentPool().equals(pool.getId())) {
                                            poolsRef.child(pool.getId()).child("totalBalance").setValue(pool.getTotalBalance() + Float.parseFloat(money.getText().toString()));

                                            Toast.makeText(AddMoneyActivity.this, "Added " + formatter.format(Float.parseFloat(money.getText().toString())), Toast.LENGTH_LONG).show();

                                        }

                                        finish();
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        android.util.Log.d("Crash", databaseError.getMessage());
                                        finish();
                                    }
                                });

                            } else{
                                finish();
                            }
                        } else {
                            Log.d("Crash", user.toString());
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });

    }

}
