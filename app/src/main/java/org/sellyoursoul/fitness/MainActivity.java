package org.sellyoursoul.fitness;

import android.app.ProgressDialog;
import android.location.Location;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends EasyLocationActivity {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");
    private DatabaseReference poolsRef = database.getReference("pools");
    private DatabaseReference logsRef = database.getReference("logs");
    public static char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private TextView balance;
    private LinearLayout addMoneyBtn;
    private LinearLayout joinPoolBtn;
    private LinearLayout createPoolBtn;
    private TextView checkInBtn;
    private LinearLayout chickenOutBtn;
    private LinearLayout viewPoolBtn;

    private LinearLayout progressBtn;

    private double longitude;
    private double latitude;

//    ProgressDialog progress;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // will be updated automatically
        balance = findViewById(R.id.balance);

        addMoneyBtn = findViewById(R.id.addMoneyBtn);
        joinPoolBtn = findViewById(R.id.joinPoolBtn);
        createPoolBtn = findViewById(R.id.createPoolBtn);
        checkInBtn = findViewById(R.id.checkinBtn);
        chickenOutBtn = findViewById(R.id.chickenOutBtn);
        //viewPoolBtn = findViewById(R.id.viewPoolBtn);

        progressBtn = findViewById(R.id.progressBtn);


        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMoneyActivity.class);
                intent.putExtra("userId", getUserId());
                startActivity(intent);
            }
        });


        // join pool button will be hidden if the user is already in a pool
        joinPoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinPoolActivity.class);
                intent.putExtra("userId", getUserId());
                startActivity(intent);
            }
        });


//        viewPoolBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                usersRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        final User user =  dataSnapshot.getValue(User.class);
//                        if(user.getId().equals(getUserId())){
//
//                            poolsRef.addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                    Pool pool = dataSnapshot.getValue(Pool.class);
//                                    if(pool.getId().equals(user.getCurrentPool())){
//
//                                        Intent intent = new Intent(MainActivity.this, ViewPoolActivity.class);
//                                        intent.putExtra("userId", getUserId());
//                                        intent.putExtra("poolId", pool.getId());
//                                        startActivity(intent);
//
//                                        poolsRef.removeEventListener(this);
//                                    }
//                                }
//
//                                @Override
//                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//
//                            usersRef.removeEventListener(this);
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });


        progressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                intent.putExtra("userId", getUserId());
                startActivity(intent);
            }
        });


        // hidden if already in a pool
        createPoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CreatePoolActivity.class);
                intent.putExtra("userId", getUserId());
                startActivity(intent);
            }
        });


        // check in button will be changed to view pool if the user has already checked in
        // will be hidden if user is not in a pool
        checkInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poolsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final Pool pool = dataSnapshot.getValue(Pool.class);
                        usersRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                User user = dataSnapshot.getValue(User.class);

                                if(user.getId().equals(getUserId())) {
                                    if(user.getCurrentPool() != null){
                                        if (user.getCurrentPool().equals(pool.getId())) {
                                            String logId = MainActivity.randomString(MainActivity.CHARSET_AZ_09, 10);
                                            org.sellyoursoul.fitness.Log log = new org.sellyoursoul.fitness.Log(getUserId(),
                                                    pool.getId(),
                                                    logId,
                                                    new Date(),
                                                    longitude,
                                                    latitude,
                                                    false,
                                                    false);
                                            setLastDate(new Date());
                                            logsRef.child(logId).setValue(log, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    onCheckedInSuccessfully();
                                                }
                                            });
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


        // be visible if part of pool
        chickenOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final User user = dataSnapshot.getValue(User.class);
                        if (user.getId().equals(getUserId())) {
                            poolsRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Pool pool = dataSnapshot.getValue(Pool.class);
                                    if (pool.getId().equals(user.getCurrentPool())) {
                                        usersRef.child(getUserId()).child("currentPool").removeValue();
                                        poolsRef.child(pool.getId()).child("totalBalance").setValue(pool.getTotalBalance() + user.getBalance());
                                        usersRef.child(getUserId()).child("balance").setValue(0);

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


        id = getUserId();

        if (id == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("What is your name?");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    id = randomString(CHARSET_AZ_09, 10);
                    setUserId(id);
                    User user = new User(id,
                            input.getText().toString(),
                            0,
                            null,
                            null,
                            null);
                    usersRef.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                            showLoading();
                            initializeListener();
                            logUserLocation();
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
//            showLoading();
            initializeListener();
            logUserLocation();
        }

    }


    private void initializeListener() {
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateUserInfo(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateUserInfo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateUserInfo(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                updateUserInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



//    private void showLoading(){
//        progress = new ProgressDialog(this);
//        progress.setTitle("Loading");
//        progress.setMessage("Checking your location...");
//        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();
//    }


    private void logUserLocation(){
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);

        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();

        requestSingleLocationFix(easyLocationRequest);
    }




    private void updateUserInfo(DataSnapshot dataSnapshot){
        final User user = dataSnapshot.getValue(User.class);

        if(user.getId().equals(getUserId())) {

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            balance.setText(formatter.format(user.getBalance()));

            if (user.getBalance() > 0) {
                if (user.getCurrentPool() == null) {
//                    checkInBtn.setVisibility(View.GONE);
//                    viewPoolBtn.setVisibility(View.GONE);
//                    joinPoolBtn.setVisibility(View.VISIBLE);
//                    createPoolBtn.setVisibility(View.VISIBLE);
//                    addMoneyBtn.setVisibility(View.VISIBLE);
//                    chickenOutBtn.setVisibility(View.GONE);

                } else {
//                    joinPoolBtn.setVisibility(View.GONE);
//                    createPoolBtn.setVisibility(View.GONE);
//                    viewPoolBtn.setVisibility(View.VISIBLE);
//                    addMoneyBtn.setVisibility(View.GONE);
//                    chickenOutBtn.setVisibility(View.VISIBLE);
//                    checkInBtn.setVisibility(View.VISIBLE);

                        poolsRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Pool pool = dataSnapshot.getValue(Pool.class);

                                if (pool.getId().equals(user.getCurrentPool())) {
                                    if (userIsCheckedIn(pool)) {
//                                        checkInBtn.setVisibility(View.GONE);
//                                        progress.dismiss();
                                    }

//                                    progress.dismiss();
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
            } else {
//                joinPoolBtn.setVisibility(View.GONE);
//                createPoolBtn.setVisibility(View.GONE);
//                addMoneyBtn.setVisibility(View.VISIBLE);
//                chickenOutBtn.setVisibility(View.GONE);
//                checkInBtn.setVisibility(View.GONE);
//                viewPoolBtn.setVisibility(View.GONE);



//                if(user.getCurrentPool() != null){
//                    poolsRef.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            Pool pool = dataSnapshot.getValue(Pool.class);
//
//                            if(pool.getId().equals(user.getCurrentPool())) {
//
//                                if (userIsCheckedIn(user, pool)) {
//                                    checkInBtn.setVisibility(View.VISIBLE);
//                                }
//                                viewPoolBtn.setVisibility(View.VISIBLE);
//
//                                poolsRef.removeEventListener(this);
//                            }
//
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }


//                progress.dismiss();
            }
        }
    }



    private boolean userIsCheckedIn(Pool pool) {

        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            android.util.Log.d("Crash", sdf.format(date));
            android.util.Log.d("Crash", sdf.format(getLastDate()));

            if (sdf.format(date).equals(sdf.format(getLastDate()))) {
                return true;
            } else {

                SimpleDateFormat s = new SimpleDateFormat("HH:mm");

                android.util.Log.d("Crash", s.format(date));
                android.util.Log.d("Crash", s.format(pool.getTimeEnd()));


                try {
                    if (s.parse(s.format(date)).after(s.parse(s.format(pool.getTimeEnd())))) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    android.util.Log.d("Crash", e.getMessage());
                }


            }
        } catch (Exception e){
            android.util.Log.d("Crash", e.getMessage());
        }

        return false;

    }



    private String getUserId(){
        SharedPreferences sp = this.getSharedPreferences("SavedUser", 0);
        return sp.getString("id", null);
    }


    private void setUserId(String id){
        SharedPreferences sp = getSharedPreferences("SavedUser", 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("id", id);
        ed.apply();
    }


    private Date getLastDate() throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SharedPreferences sp = this.getSharedPreferences("SavedUser", 0);
        return simpleDateFormat.parse(sp.getString("date", null));
    }


    private String getLastDateStirng() throws ParseException {
        SharedPreferences sp = this.getSharedPreferences("SavedUser", 0);
        return sp.getString("date", null);
    }

    private void setLastDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SharedPreferences sp = getSharedPreferences("SavedUser", 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("date", simpleDateFormat.format(date));
        ed.apply();
    }


    public static String randomString(char[] characterSet, int length) {
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }


    private void onCheckedInSuccessfully(){
        checkInBtn.setVisibility(View.GONE);
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final User user = dataSnapshot.getValue(User.class);

                if(user.getId().equals(getUserId())) {
                    poolsRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Pool pool = dataSnapshot.getValue(Pool.class);
                            if(user.getCurrentPool().equals(pool.getId())){
                                Intent intent = new Intent(MainActivity.this, ViewPoolActivity.class);
                                intent.putExtra("userId", getUserId());
                                intent.putExtra("poolId", pool.getId());
                                startActivity(intent);

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
    public void onLocationPermissionGranted() {

    }

    @Override
    public void onLocationPermissionDenied() {

    }

    @Override
    public void onLocationReceived(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onLocationProviderEnabled() {

    }

    @Override
    public void onLocationProviderDisabled() {

    }
}
