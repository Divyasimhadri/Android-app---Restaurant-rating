package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class filter extends AppCompatActivity {
    FirebaseFirestore db;
    FirestoreRecyclerAdapter<restaurant, rating.RestaurantViewHolder> adapter;
    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loc=getIntent().getStringExtra("LOC");


        LoadData();

    }

    private void LoadData() {
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("restaurant"). whereEqualTo("location",loc);

        //Log.i("result", String.valueOf(query));
        FirestoreRecyclerOptions<restaurant> options = new FirestoreRecyclerOptions.Builder<restaurant>()
                .setQuery(query, restaurant.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<restaurant, rating.RestaurantViewHolder>(options) {


            @NonNull
            @Override
            public rating.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_row, parent, false);
                return new rating.RestaurantViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final rating.RestaurantViewHolder holder, final int position, @NonNull restaurant model) {
                holder.setName(model.getName());
                holder.setDescription(model.getDescription());
                holder.setLocation(model.getLocation());
                holder.setrating(model.getRating());
                holder.setImage(model.getImage());


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                        //Toast.makeText(getApplicationContext(), "id:"+snapshot.getId(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), updatedelete.class);
                        intent.putExtra("id", snapshot.getId().toString());
                        startActivity(intent);
                    }
                });

            }


        };

        final RecyclerView mHouseList = (RecyclerView) findViewById(R.id.rlist);

        mHouseList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mHouseList.setAdapter(adapter);


    }


    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView resname = (TextView) mView.findViewById(R.id.res_name);
            resname.setText(name);
        }

        public void setLocation(String location) {
            TextView reslocation = mView.findViewById(R.id.res_location);
            reslocation.setText(location);
        }

        public void setDescription(String rent) {
            TextView resdesciption = mView.findViewById(R.id.res_description);
            resdesciption.setText(rent);
        }

        public void setrating(float rating) {
            RatingBar resrating = (RatingBar) mView.findViewById(R.id.res_rating);
            resrating.setRating(rating);
        }

        public void setImage(String image) {

            ImageView resimage = (ImageView) mView.findViewById(R.id.res_image);
            Picasso.get().load(image).into(resimage);

        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}