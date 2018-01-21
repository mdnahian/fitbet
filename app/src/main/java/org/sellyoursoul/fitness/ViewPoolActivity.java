package org.sellyoursoul.fitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by mdnah on 1/20/2018.
 */

public class ViewPoolActivity extends Activity {

    private TextView balance;
    private TextView name;
    private TextView joinPoolBtn;
    private ListView users;


    private UsersAdapter usersAdapter;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");
    private DatabaseReference poolsRef = database.getReference("pools");
    private DatabaseReference logsRef = database.getReference("logs");

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();


    private String poolId;
    private String userId;


    private ArrayList<Log> u;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool);

        balance = findViewById(R.id.balance);
        name = findViewById(R.id.name);
        joinPoolBtn = findViewById(R.id.joinPoolBtn);
        users = findViewById(R.id.users);

        u = new ArrayList<>();
        usersAdapter = new UsersAdapter();
        users.setAdapter(usersAdapter);


        poolId = getIntent().getStringExtra("poolId");
        userId = getIntent().getStringExtra("userId");


        joinPoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poolsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final Pool pool = dataSnapshot.getValue(Pool.class);

                        if(poolId.equals(pool.getId())){
                            usersRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if(user.getId().equals(userId)){
                                        if(user.getBalance() > pool.getEntryFee()){

                                            AlertDialog alertDialog = new AlertDialog.Builder(ViewPoolActivity.this).create();
                                            alertDialog.setTitle("Joining Pool");
                                            alertDialog.setMessage(""+formatter.format(pool.getEntryFee())+" will be removed from you account. You will not be able to leave the pool until the expiration date.");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                            usersRef.removeEventListener(this);

                                            poolsRef.child(pool.getId()).child("totalBalance").setValue(pool.getTotalBalance()+pool.getEntryFee());
                                            poolsRef.child(pool.getId()).child("users").push().setValue(user.getId());
                                            usersRef.child(userId).child("balance").setValue(user.getBalance()-pool.getEntryFee());
                                            usersRef.child(userId).child("currentPool").setValue(pool.getId());

                                            joinPoolBtn.setVisibility(View.GONE);
                                            users.setPaddingRelative(0, 0,0, -64);

                                        } else {
                                            AlertDialog alertDialog = new AlertDialog.Builder(ViewPoolActivity.this).create();
                                            alertDialog.setTitle("Insufficient Funds");
                                            alertDialog.setMessage("You only have "+formatter.format(user.getBalance())+" this pool requires "+formatter.format(pool.getEntryFee())+" to join.");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();

                                        }
                                        usersRef.removeEventListener(this);
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
                            poolsRef.removeEventListener(this);
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



        initializeListeners();
    }




    private void initializeListeners(){
        poolsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Pool pool = dataSnapshot.getValue(Pool.class);
                if(pool.getId().equals(poolId)){
                    balance.setText(formatter.format(pool.getTotalBalance()));
                    name.setText(pool.getName());

                    usersRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            User user = dataSnapshot.getValue(User.class);

                            if(user.getId().equals(userId)){
                                joinPoolBtn.setVisibility(View.GONE);
                            }

                            logsRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Log log = dataSnapshot.getValue(Log.class);

                                    if(log.getPoolId().equals(pool.getId())){
                                        u.add(log);
                                    }

                                    users.deferNotifyDataSetChanged();
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




    private class UsersAdapter extends ArrayAdapter<Log> {

        public UsersAdapter() {
            super(getApplicationContext(), R.layout.item_user, u);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final Log log = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            }


            final TextView name = convertView.findViewById(R.id.name);
            final ImageView verified = convertView.findViewById(R.id.verified);


            usersRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);

                    if(user.getId().equals(log.getUserId())){
                        name.setText(user.getName());

                        if (log.isChecked()) {
                            if (log.isVerified()) {
                                verified.setImageResource(R.drawable.check);
                            } else {
                                verified.setImageResource(R.drawable.cross);
                            }
                        }
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






            return convertView;
        }
    }




}
