package com.example.bdentepreneuerzone.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bdentepreneuerzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileAct extends AppCompatActivity implements View.OnClickListener {
    TextView UserNameTV, EmailTV, OccupationTV, MobileNoTV;
    EditText UserNameET, MobileNoET;
    Button ChangeInfoBtn, LogoutBtn, SubmitBtn;
    AutoCompleteTextView OccupationACTV;
    ProgressBar Progressbar;
    Toolbar toolbar;
    String UserID;

    Intent intent;

    //Firebase
    FirebaseAuth FAuthObj;
    FirebaseFirestore FFStoreObj;
    DocumentReference DRObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.Toolbar);   //initialization of ActionBar
        setSupportActionBar(toolbar); // Step 1 -> toolbar

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); //Setting Title in Toolbar
        }

        //TextView
        UserNameTV = findViewById(R.id.UserNameTV);
        EmailTV = findViewById(R.id.EmailTV);
        OccupationTV = findViewById(R.id.OccupationTV);
        MobileNoTV = findViewById(R.id.MobileNoTV);
        //Button
        ChangeInfoBtn = findViewById(R.id.ChangeInfoBtn);
        LogoutBtn = findViewById(R.id.LogoutBtn);

        //Firebase
        FAuthObj = FirebaseAuth.getInstance();
        FFStoreObj = FirebaseFirestore.getInstance();
        UserID = FAuthObj.getCurrentUser().getUid();
        DRObj = FFStoreObj.collection("Users").document(UserID);

        //ClickListener
        ChangeInfoBtn.setOnClickListener(this);
        LogoutBtn.setOnClickListener(this);

        LoadUserData();
    }

    private void LoadUserData() {
        DRObj.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                UserNameTV.setText("User name: "+snapshot.getString("UserName"));
                EmailTV.setText("Email: "+snapshot.getString("Email"));
                if(snapshot.getString("Occu")!= "")
                    OccupationTV.setText("Occupation: "+snapshot.getString("Occu"));
                else
                    OccupationTV.setText("Occupation: None");

                if(snapshot.getString("MobileNo")!= "")
                    MobileNoTV.setText("Mobile No: "+snapshot.getString("MobileNo"));
                else
                    MobileNoTV.setText("Mobile No: None");
            }
        });
    }
    public void ChangeInformation(){
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }else{
            alert = new AlertDialog.Builder(this);
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_change_info, null);

        UserNameET = view.findViewById(R.id.UserNameET);
        MobileNoET = view.findViewById(R.id.MobileNoET);
        OccupationACTV = view.findViewById(R.id.OccupationACTV);
        Progressbar = view.findViewById(R.id.Progressbar);
        SubmitBtn = view.findViewById(R.id.SubmitBtn);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change();
                dialog.dismiss();
            }
        });
    }

    private void Change() {
        String userName = UserNameET.getText().toString().trim();
        String Occu = OccupationACTV.getText().toString();
        String mobileNo = MobileNoET.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
            UserNameET.setError("User name is empty");
        }else if(TextUtils.isEmpty(Occu)) {
            OccupationACTV.setError("Occupation is empty");
        }else if(TextUtils.isEmpty(mobileNo)){
            MobileNoET.setError("Mobile no is empty");
        }else{
            DRObj.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                }
            });
        }
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.ChangeInfoBtn){
            ChangeInformation();
        }else if(v.getId() == R.id.LogoutBtn){
            FAuthObj.signOut();
            intent = new Intent(this, LoginAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
    }
}