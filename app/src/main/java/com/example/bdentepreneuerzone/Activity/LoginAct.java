package com.example.bdentepreneuerzone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdentepreneuerzone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {
    EditText EmailET, PassET;
    TextView ForgetPassT;
    Button LoginBtn, NewAccT;
    ProgressBar Progressbar;
    static Intent intent;
    //Firebase Objects
    FirebaseAuth FAuthObj;
    FirebaseUser FUserObj;

    private long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailET = findViewById(R.id.EmailET);
        PassET = findViewById(R.id.PassET);
        LoginBtn = findViewById(R.id.LoginBtn);
        ForgetPassT = findViewById(R.id.ForgetPassT);
        NewAccT = findViewById(R.id.NewAccT);
        Progressbar = findViewById(R.id.Progressbar);

        FAuthObj = FirebaseAuth.getInstance();

        intent = new Intent(getApplicationContext(), SignupAct.class);

        //Setting onClickListener
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        NewAccT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
    public void LoginUser(){
        String email = EmailET.getText().toString().trim();
        String password = PassET.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            EmailET.setError("Email cannot be empty");
            EmailET.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            PassET.setError("Password cannot be empty");
            PassET.requestFocus();
        }else{
            Progressbar.setVisibility(View.VISIBLE);
            FAuthObj.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginAct.this, "Log-in successful", Toast.LENGTH_SHORT).show();
                        Progressbar.setVisibility(View.GONE);
                        //Creating new Intent without coming back
                        intent = new Intent(LoginAct.this, HomeAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginAct.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    Progressbar.setVisibility(View.GONE);
                    EmailET.setText("");
                    PassET.setText("");
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}