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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by mdnah on 1/20/2018.
 */

public class JoinPoolActivity extends Activity {

    private ListView pools;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("users");
    private DatabaseReference poolsRef = database.getReference("pools");

    private String userId;
    private ArrayList<Pool> p;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        userId = getIntent().getStringExtra("userId");
        pools = findViewById(R.id.pools);
        p = new ArrayList<>();
        final PoolsAdapter poolArrayAdapter = new PoolsAdapter();
        pools.setAdapter(poolArrayAdapter);


        poolsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                p.add(dataSnapshot.getValue(Pool.class));
                poolArrayAdapter.notifyDataSetChanged();
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




    private class PoolsAdapter extends ArrayAdapter<Pool> {

         public PoolsAdapter() {
            super(getApplicationContext(), R.layout.item_pool, p);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final Pool pool = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pool, parent, false);
            }

            TextView name = convertView.findViewById(R.id.name);
            TextView totalBalance = convertView.findViewById(R.id.totalBalance);
            TextView entryFee = convertView.findViewById(R.id.entryFee);

            name.setText(pool.getName());
            totalBalance.setText(formatter.format(pool.getTotalBalance()));
            entryFee.setText(formatter.format(pool.getEntryFee()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent intent = new Intent(JoinPoolActivity.this, ViewPoolActivity.class);
//                    intent.putExtra("poolId", pool.getId());
//                    intent.putExtra("userId", userId);
//                    startActivity(intent);
//                    finish();







                }
            });

            return convertView;
        }
    }





}
