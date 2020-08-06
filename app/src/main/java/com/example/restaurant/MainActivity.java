package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



     private FirebaseAuth mAuth;
    public static final String MY_PREFERENCE="com.example.restaurant.user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences u =getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        if(u.getBoolean("user",false))
        {
            Intent intent3 =new Intent(getApplicationContext(), rating.class);
            startActivity(intent3);
        }
    }
    public void login(View view) {
        TextView emailtextview = (TextView) findViewById(R.id.email);
        TextView pwdtextview = (TextView) findViewById(R.id.password);

        String email = emailtextview.getText().toString();
        String password = pwdtextview.getText().toString();

        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), rating.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Email/Password is invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if(email.length()>0 && password.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Password shouldn't be empty",Toast.LENGTH_SHORT).show();
        }
        else if(email.length()==0 && password.length()>0)
            Toast.makeText(getApplicationContext(),"email shouldn't be empty",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Email and password shouldn't be empty",Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor=getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
        editor.putBoolean("user", true);
        editor.commit();


    }

    public void signup(View view)
    {
        Intent intent = new Intent(getApplicationContext(), signup.class);
        startActivity(intent);
    }
}