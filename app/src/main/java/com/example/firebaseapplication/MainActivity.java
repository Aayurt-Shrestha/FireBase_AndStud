package com.example.firebaseapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = findViewById(R.id.eIuser);
        Password = findViewById(R.id.eIpass);
        Info = findViewById(R.id.eattempt);
        Login = findViewById(R.id.elogin);
        userRegistration = findViewById(R.id.tvreg);

        firebaseAuth = FirebaseAuth.getInstance();//takes reads data
        progressDialog = new ProgressDialog(this);
        //to check login if koi logged in xa ki xaina bhanera


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

    }

    private void validate(String userName, String userPassword) {

        progressDialog.setMessage("+++++Aayurt+++++");
        progressDialog.show();

//        String aname = Name.getText().toString();
//        String apassword = Password.getText().toString();

        if (userName.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Blank credentials", Toast.LENGTH_SHORT).show();
            userName = "aayurtshrestha@gmail.com";
            userPassword = "123456";
        }


        Log.d("mytag",userName);
            firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();//dismissed as the succesful message needs to take place

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MainActivity.this, SecondActivity.class));

                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        counter--;
                        Info.setText("No. of attempts remaining:" + counter);
                        progressDialog.dismiss();//dismissed as the succesful message needs to take place

                        if (counter == 0) {

                            Login.setEnabled(false);
                        }

                    }
                }
            });

    }


}

