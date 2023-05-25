package com.example.bdentepreneuerzone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bdentepreneuerzone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupAct extends AppCompatActivity {
    String[] OccupationList = {"Business", "Teaching", "Service"};
    EditText UserNameET, EmailET, MobileNoET, PassET, ConfirmPassET;
    Button SignupBtn;
    AutoCompleteTextView OccupationACTV;
    ProgressBar Progressbar;
    ArrayAdapter<String> OccupationACTVAda;
    static Intent intent;
    //Declaration for Firebase
    FirebaseAuth FAuthObj;
    FirebaseFirestore FFStoreObj;
    DocumentReference DRObj;
    FirebaseUser FUserObj;

    String userName, email,  mobileNo, occupation, password, confirmPassword, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initializing
        OccupationACTV = findViewById(R.id.OccupationACTV);
        UserNameET = findViewById(R.id.UserNameET);

        EmailET = findViewById(R.id.EmailET);
        MobileNoET = findViewById(R.id.MobileNoET);
        PassET = findViewById(R.id.PassET);
        ConfirmPassET = findViewById(R.id.ConfirmPassET);
        SignupBtn = findViewById(R.id.SignupBtn);
        Progressbar = findViewById(R.id.Progressbar); //ProgessBar

        // ACTV adapters
        OccupationACTVAda = new ArrayAdapter<> (this, R.layout.occu_auto_comp_item, OccupationList);

        //Firebase
        FAuthObj = FirebaseAuth.getInstance();
        FFStoreObj = FirebaseFirestore.getInstance();

        //Setting ACTV adapters
        OccupationACTV.setAdapter(OccupationACTVAda);
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //dialog.show();

                userName = UserNameET.getText().toString().trim();
                email = EmailET.getText().toString().trim();
                mobileNo = MobileNoET.getText().toString().trim();
                occupation = OccupationACTV.getText().toString();
                password = PassET.getText().toString();
                confirmPassword = ConfirmPassET.getText().toString();

                if(TextUtils.isEmpty(userName)){
                    UserNameET.setError("This field is required");
                    return;
                }else if(TextUtils.isEmpty(email)){
                    EmailET.setError("Email is required");
                    return;
                }else if(TextUtils.isEmpty(password)){
                    PassET.setError("Password is required");
                    return;
                }else if(TextUtils.isEmpty(confirmPassword)){
                    ConfirmPassET.setError("This field is required");
                    return;
                }else{
                    if(password.equals(confirmPassword)){
                        Progressbar.setVisibility(View.VISIBLE);
                        FAuthObj.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignupAct.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                    userID = FAuthObj.getCurrentUser().getUid();
                                    DRObj = FFStoreObj.collection("Users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("UserName", userName);
                                    user.put("Email", email);
                                    user.put("MobileNo", mobileNo);
                                    user.put("Occu", occupation);
                                    DRObj.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(SignupAct.this, "User profile updated", Toast.LENGTH_SHORT).show();
                                            Progressbar.setVisibility(View.INVISIBLE);
                                            //Creating new Intent without coming back
                                            intent = new Intent(SignupAct.this, HomeAct.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("EXIT", true);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e){
                                            Toast.makeText(SignupAct.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                            Progressbar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(SignupAct.this, HomeAct.class));
                                        }
                                    });
                                    //Return Login Activity
                                }else{
                                    Toast.makeText(SignupAct.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Progressbar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignupAct.this, "Password and Confirm Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}