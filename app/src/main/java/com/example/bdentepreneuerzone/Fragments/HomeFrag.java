package com.example.bdentepreneuerzone.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdentepreneuerzone.Activity.ProfileAct;
import com.example.bdentepreneuerzone.Model.LoanModel;
import com.example.bdentepreneuerzone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFrag extends Fragment implements View.OnClickListener {
    Button CloseBtn;
    TextView LoanDesT, LoanTitleT;
    ImageView ProfileImg;
    CardView StartupCard, SMECard, PrantonariCard, ShaktiCard, ShikhaCard, UthsahoCard;
    FirebaseFirestore objectFrestre = FirebaseFirestore.getInstance();
    CollectionReference LoanDesCol = objectFrestre.collection("LoanDescripList");
    List<LoanModel>  LoanList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_frag, container, false);

        StartupCard = view.findViewById(R.id.StartupCard);
        SMECard = view.findViewById(R.id.SMECard);
        PrantonariCard = view.findViewById(R.id.PrantonariCard);
        ShaktiCard = view.findViewById(R.id.ShaktiCard);
        ShikhaCard = view.findViewById(R.id.ShikhaCard);
        UthsahoCard = view.findViewById(R.id.UthsahoCard);
        ProfileImg = view.findViewById(R.id.ProfileImg);

        StartupCard.setOnClickListener(this);
        SMECard.setOnClickListener(this);
        PrantonariCard.setOnClickListener(this);
        ShaktiCard.setOnClickListener(this);
        ShikhaCard.setOnClickListener(this);
        UthsahoCard.setOnClickListener(this);

        ProfileImg.setOnClickListener(this);

        loadLoanList();

        return view;
    }

    private void loadLoanList() {
        LoanDesCol.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    LoanModel model = new LoanModel(d.get("Loan_Name").toString(), d.get("Loan_Description").toString(), d.get("Priority").toString());
                    LoanList.add(model);
                }
                Collections.sort(LoanList, new Comparator<LoanModel>(){
                    @Override
                    public int compare(LoanModel obj1, LoanModel obj2){
                        return obj1.getPriority().compareTo(obj2.getPriority());
                    }
                });
                //loader.setVisibility(View.GONE); //while data is loaded
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){
                Toast.makeText(getContext(), "Failed to load data"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String title, String des){
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        }else{
            alert = new AlertDialog.Builder(getContext());
        }

        View view = getLayoutInflater().inflate(R.layout.dialog_home, null);

        LoanDesT = view.findViewById(R.id.LoanDesT);
        CloseBtn = view.findViewById(R.id.CloseBtn);
        LoanTitleT = view.findViewById(R.id.LoanTitleT);

        //Setting data
        LoanTitleT.setText(title);
        LoanDesT.setText(des);

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    //handling onclickListener in the cardView
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.StartupCard){
            showDialog(LoanList.get(0).getLoanName(), LoanList.get(0).getLoanDes());
        }else if (v.getId() == R.id.SMECard){
            showDialog(LoanList.get(1).getLoanName(), LoanList.get(1).getLoanDes());
        }else if (v.getId() == R.id.PrantonariCard){
            showDialog(LoanList.get(2).getLoanName(), LoanList.get(2).getLoanDes());
        }else if (v.getId() == R.id.ShaktiCard){
            showDialog(LoanList.get(3).getLoanName(), LoanList.get(3).getLoanDes());
        }else if (v.getId() == R.id.ShikhaCard){
            showDialog(LoanList.get(4).getLoanName(), LoanList.get(4).getLoanDes());
        }else if (v.getId() == R.id.UthsahoCard){
            showDialog(LoanList.get(5).getLoanName(), LoanList.get(5).getLoanDes());
        }else if(v.getId() == R.id.ProfileImg){
            startActivity(new Intent(getContext(), ProfileAct.class));
        }
    }
}
