package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import com.google.firebase.auth.FirebaseAuth;

public class rating extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    SearchView searchView,sv;

    FirestoreRecyclerAdapter <restaurant,RestaurantViewHolder>adapter;
    public static final String MY_PREFERENCE="com.example.restaurant.user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        searchView = (SearchView) findViewById(R.id.searchview);
        sv = (SearchView) findViewById(R.id.sv);

        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(getApplicationContext());
        LoadData();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent =new Intent(getApplicationContext(),filter.class);
                intent.putExtra("LOC",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent =new Intent(getApplicationContext(),search.class);
                intent.putExtra("LOC",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void LoadData() {
        db= FirebaseFirestore.getInstance();

        Query query=db.collection("restaurant");

        //Log.i("result", String.valueOf(query));
        FirestoreRecyclerOptions<restaurant> options=new FirestoreRecyclerOptions.Builder<restaurant>()
                .setQuery(query,restaurant.class)
                .build();

        adapter=new FirestoreRecyclerAdapter<restaurant,RestaurantViewHolder>(options) {


            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.block_row,parent,false);
                return new RestaurantViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RestaurantViewHolder holder, final int position, @NonNull restaurant model) {
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
                        intent.putExtra("id",snapshot.getId().toString());
                        startActivity(intent);
                    }
                });

            }


        };

        final RecyclerView mHouseList = (RecyclerView) findViewById(R.id.rest_list);

        mHouseList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mHouseList.setAdapter(adapter);


    }


    public static class RestaurantViewHolder extends RecyclerView.ViewHolder
    {
        View mView;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name)
        {
            TextView resname =(TextView) mView.findViewById(R.id.res_name);
            resname.setText(name);
        }
        public void setLocation(String location)
        {
            TextView reslocation = mView.findViewById(R.id.res_location);
            reslocation.setText(location);
        }
        public void setDescription(String rent)
        {
            TextView resdesciption =mView.findViewById(R.id.res_description);
            resdesciption.setText(rent);
        }
        public void setrating(float rating)
        {
            RatingBar resrating =(RatingBar) mView.findViewById(R.id.res_rating);
            resrating.setRating(rating);
        }
        public void setImage(String image)
        {

            ImageView resimage=(ImageView) mView.findViewById(R.id.res_image);
            Picasso.get().load(image).into(resimage);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.account:
                Intent intent = new Intent(getApplicationContext(), profile.class);
                startActivity(intent);
                return true;
            case R.id.add:
                Intent intent1 = new Intent(getApplicationContext(), add.class);
                startActivity(intent1);
                return true;
            case R.id.logout:
                 mAuth.signOut();
                SharedPreferences.Editor editor=getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
                editor.putBoolean("user", false);
                editor.commit();

                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                 intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);

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