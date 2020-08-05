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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class add extends AppCompatActivity {

    private FirebaseFirestore mStore;
    ProgressBar mProgress;
    private StorageReference mStorage;
    RatingBar res_rating;
    Button btnsubmit;
    ImageButton image;
    private Uri uri = null;
    private static final int GALLERY_INTENT = 2;
    EditText name, location, description,phone;
    UploadTask uploadTask;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.get().load(uri).into(image);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        res_rating = (RatingBar) findViewById(R.id.res_rating);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        name = (EditText) findViewById(R.id.name);
        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        image = (ImageButton) findViewById(R.id.image);
        phone =(EditText) findViewById(R.id.phone);

        mStorage= FirebaseStorage.getInstance().getReference();
        mStore=FirebaseFirestore.getInstance();

        image.setVisibility(View.VISIBLE);
        mProgress = new ProgressBar(this);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/w");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();
            }
        });

    }

    private void startposting() {

        mProgress.setProgress(0);
        mProgress.setVisibility(View.VISIBLE);

        final String name_value = name.getText().toString().trim();
        final String desc_value = description.getText().toString().trim();
        final String loc_value = location.getText().toString().trim();
        final String phone_val=phone.getText().toString().trim();
       // final float res_value =res_rating.getRating();
        if (!TextUtils.isEmpty(name_value) && !TextUtils.isEmpty(desc_value) && !TextUtils.isEmpty(loc_value) && uri != null) {
            final StorageReference filepath = mStorage.child(uri.getLastPathSegment());
            ;
            //adding a picture to storage

            uploadTask=filepath.putFile(uri);
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    restaurant r = new restaurant(uri.toString(),desc_value,loc_value,name_value,phone_val,res_rating.getRating());
                    mStore.collection("restaurant").document().set(r);

                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), rating.class);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }
    }

}
