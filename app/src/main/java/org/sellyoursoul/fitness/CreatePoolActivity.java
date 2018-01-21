package org.sellyoursoul.fitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mdnah on 1/20/2018.
 */

public class CreatePoolActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");
    private DatabaseReference poolsRef = database.getReference("pools");
    private DatabaseReference logsRef = database.getReference("logs");

    private TextView date;
    private EditText name;
    private EditText entryFee;
    private EditText days;
    private RadioButton group;
    private RadioButton commercial;

    private TextView createPoolBtn;

    private String userId;
    private double longitude;
    private double latitude;

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        userId = getIntent().getStringExtra("userId");
        longitude = getIntent().getDoubleExtra("longitude", 0);
        latitude = getIntent().getDoubleExtra("latitude", 0);

        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        entryFee = findViewById(R.id.entryFee);
        days = findViewById(R.id.days);
        group = findViewById(R.id.group);
        commercial = findViewById(R.id.commercial);

        createPoolBtn = findViewById(R.id.createPoolBtn);

        Date d = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        date.setText(""+(cal.get(Calendar.MONTH) + 1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR));

        TextView datePickerBtn = findViewById(R.id.datePickerBtn);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(CreatePoolActivity.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, cal.get(Calendar.DAY_OF_MONTH))
                        .setDateRange(new MonthAdapter.CalendarDay(), null)
                        .setDoneText("Save")
                        .setCancelText("Cancel")
                        .setThemeCustom(R.style.MyCustomBetterPickersDialogs);
                cdp.show(getSupportFragmentManager(), "fragment_date_picker_name");
            }
        });

        createPoolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                usersRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final User user = dataSnapshot.getValue(User.class);

                        if(user.getId().equals(userId)) {
                            if (user.getBalance() > Float.parseFloat(entryFee.getText().toString())) {

                                DateFormat df = new SimpleDateFormat("M/dd/yyyy kk:mm", Locale.ENGLISH);
                                try {
                                    Date timeEnd = df.parse(date.getText().toString() + " 23:23");

                                    final String poolId = MainActivity.randomString(MainActivity.CHARSET_AZ_09, 10);

                                    ArrayList<String> daysList = new ArrayList<>();
                                    Collections.addAll(daysList, days.getText().toString().split(","));

                                    final Pool pool = new Pool(poolId,
                                            name.getText().toString(),
                                            0,
                                            0,
                                            group.isChecked(),
                                            Float.parseFloat(entryFee.getText().toString()),
                                            daysList,
                                            timeEnd,
                                            Float.parseFloat(entryFee.getText().toString()));

                                    poolsRef.child(poolId).setValue(pool).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            poolsRef.child(poolId).child("users").push().setValue(userId);
                                            usersRef.child(userId).child("currentPool").setValue(poolId);

                                            usersRef.child(userId).child("balance").setValue(user.getBalance()-pool.getEntryFee());
                                            poolsRef.child(poolId).child("currentBalance").setValue(pool.getTotalBalance()+pool.getEntryFee());
                                            String logId = MainActivity.randomString(MainActivity.CHARSET_AZ_09, 10);
                                            org.sellyoursoul.fitness.Log log = new org.sellyoursoul.fitness.Log(userId, poolId, logId, new Date(), longitude, latitude, false, false);
                                            //usersRef.child(userId).child("log").push().setValue(log);

                                            logsRef.child(logId).setValue(log, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    setLastDate(new Date());
                                                }
                                            });


                                            finish();
                                        }
                                    });

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CreatePoolActivity.this).create();
                                alertDialog.setTitle("Insufficient Funds");
                                alertDialog.setMessage("You only have " + formatter.format(user.getBalance()) + " this pool requires " + formatter.format(Float.parseFloat(entryFee.getText().toString())) + " to create.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
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
        });
    }


    private void setLastDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SharedPreferences sp = getSharedPreferences("SavedUser", 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("date", simpleDateFormat.format(date));
        ed.apply();
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        date.setText(""+monthOfYear+"/"+dayOfMonth+"/"+year);
    }
}
