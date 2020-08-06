package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class signup extends AppCompatActivity {


    FirebaseAuth mAuth;
    public static final String MY_PREFERENCE="com.example.restaurant.user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
    }
    public class Data{
        public String email;
        public String name;
        public Data(String em,String nm)
        {
            this.email = em;
            this.name =nm;
        }
    }
    public void signup(View view)
    {

        TextView emailtextview = (TextView) findViewById(R.id.email);
        TextView pwdtextview = (TextView) findViewById(R.id.password);
        TextView nametextview =(TextView) findViewById(R.id.name);
        TextView confirmpwdtextview =(TextView) findViewById(R.id.confirmpassword);

        view.setClickable(false);
        final String email = emailtextview.getText().toString();
        String password = pwdtextview.getText().toString();
        String cnfpwd = confirmpwdtextview.getText().toString();
        final String name = nametextview.getText().toString();
        if(password.equals(cnfpwd) && password.length()>=6)
        {
            Toast.makeText(this,"Signup in progress, Please wait",Toast.LENGTH_LONG).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(),"Signup success",Toast.LENGTH_SHORT).show();
                                Data dt = new Data(email,name);
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                user.updateProfile(profileUpdates);
                                Intent intent = new Intent(getApplicationContext(), rating.class);

                                    startActivity(intent);
                                    finish();



                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("signup failure reason",task.getException().toString());
                                String failuremsg = task.getException().toString();
                                if(failuremsg.equals("com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]"))
                                    Toast.makeText(getApplicationContext(),"Signup failed: Password length should be atleast 6 characters long",Toast.LENGTH_SHORT).show();
                                else if(failuremsg.equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."))
                                    Toast.makeText(getApplicationContext(),"Signup failed: Email already in use",Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(),"Signup failed: Some error occured",Toast.LENGTH_LONG).show();
                                Button btn = (Button) findViewById(R.id.signup);
                                btn.setClickable(true);
                            }
                        }
                    });
        }
        else if(password.equals(cnfpwd) && password.length()<6)
        {
            Toast.makeText(this,"Password length should be greater than or equals to 6",Toast.LENGTH_LONG).show();
            view.setClickable(true);
        }
        else if(!password.equals(cnfpwd))
        {
            Toast.makeText(this,"Passwords don't match",Toast.LENGTH_SHORT).show();
            view.setClickable(true);
        }
        SharedPreferences.Editor editor=getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
        editor.putBoolean("user", false);
        editor.commit();

    }
}