package com.example.bdentepreneuerzone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bdentepreneuerzone.Fragments.HomeFrag;
import com.example.bdentepreneuerzone.Fragments.LoanFrag;
import com.example.bdentepreneuerzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAct extends AppCompatActivity {
    BottomNavigationView BottomNav;
    private static Fragment selectedFragment = null;
    /*All Firebase obj*/
    FirebaseAuth FAuthObj; static FirebaseUser FUserObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FAuthObj = FirebaseAuth.getInstance();

        BottomNav = findViewById(R.id.BottomNav);
        BottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_cont_lay, new HomeFrag()).commit();
    }
    //Listener when navigationitem is clicked
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.home_bottom_menu:
                        selectedFragment = new HomeFrag();
                    break;
                    case R.id.loan_bottom_menu:
                        selectedFragment = new LoanFrag();
                        break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_cont_lay, selectedFragment).commit();
            return true;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        FUserObj = FAuthObj.getCurrentUser();
        if(FUserObj == null){
            startActivity(new Intent(HomeAct.this, LoginAct.class));
            if(FAuthObj.getCurrentUser() == null)
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}