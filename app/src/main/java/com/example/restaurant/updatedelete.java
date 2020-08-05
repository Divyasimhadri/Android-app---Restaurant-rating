package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class updatedelete extends AppCompatActivity {
    FirebaseFirestore db;
    ImageButton u_image;
    TextView u_name,u_description,u_location,u_phone;
    RatingBar res_rating;
    FirebaseFirestore mStore;
    StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;

    Button updatebtn,deletebtn;
    Button callbtn;
    DocumentReference docRef;
    private Uri uri = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.get().load(uri).into(u_image);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedelete);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        u_image = (ImageButton) findViewById(R.id.u_image);
        u_name = (TextView) findViewById(R.id.u_name);
        u_description = (TextView) findViewById(R.id.u_description);
        u_location = (TextView) findViewById(R.id.u_location);
        u_phone =(TextView) findViewById(R.id.u_phone);

        res_rating = (RatingBar) findViewById(R.id.res_rating);
        mStorage = FirebaseStorage.getInstance().getReference();
        mStore = FirebaseFirestore.getInstance();
        u_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/w");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        updatebtn = (Button) findViewById(R.id.updatebtn);
        deletebtn = (Button) findViewById(R.id.delbtn);
        callbtn =(Button) findViewById(R.id.callbtn);

        db = FirebaseFirestore.getInstance();

        final String dp = getIntent().getStringExtra("id");


        docRef = db.collection("restaurant").document(dp);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    u_name.setText(documentSnapshot.getString("name"));
                    u_description.setText(documentSnapshot.getString("description"));
                    u_location.setText(documentSnapshot.getString("location"));
                    Picasso.get().load(documentSnapshot.getString("image")).into(u_image);
                    res_rating.setRating(documentSnapshot.getLong("rating"));
                    u_phone.setText(documentSnapshot.getString("pno"));


                } else {
                    Toast.makeText(getApplicationContext(), "Document doesnt exist", Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("restaurant").document(dp)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Deleted succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), rating.class);
                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error deleting record", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), rating.class);
                                startActivity(intent);
                            }
                        });
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_value = u_name.getText().toString().trim();
                final String desc_value = u_description.getText().toString().trim();
                final String loc_value = u_location.getText().toString().trim();
            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+u_phone.getText()));
               startActivity(intent);
            }
        });
    }
}