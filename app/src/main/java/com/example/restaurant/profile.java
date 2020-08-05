package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profile extends AppCompatActivity {

    TextView name ,email;

    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=(TextView) findViewById(R.id.name);
        email=(TextView) findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());




    }
}