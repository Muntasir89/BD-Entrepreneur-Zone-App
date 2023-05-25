package com.example.bdentepreneuerzone.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bdentepreneuerzone.Activity.BankAct;
import com.example.bdentepreneuerzone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LoanFrag extends Fragment {
    String[] YesNoList = {"Yes", "No"};
    String[] GenderList = {"Male", "Female"};
    String[] HousingList = {"Own", "Rent"};
    String[] EducationList = {"Not Graduate", "Graduate", "Certified Entrepreneur"};
    String[] PropertyAreaList = {"Semi-urban", "Rural", "Urban"};
    //String[] DistrictListAr = {"Dhaka", "Mymensingh", "Gazipur"};
    String[] LoanTypeListAr = {"High credit", "Long term"};

    FirebaseFirestore objectFrestre = FirebaseFirestore.getInstance();
    CollectionReference BankCol = objectFrestre.collection("Bank");
    //For LoanResult Dialog
    ArrayList<String> DistrictListAr = new ArrayList<>();
    ArrayList<String> RegionListAr = new ArrayList<>();
    Set<String> DistrictListSet = new HashSet<>();
    Set<String> RegionListSet = new HashSet<>();
    Map<String, Object> BankInfo = new HashMap<String, Object>();

    TextView DiaCongraTV;

    TextInputEditText AgeTIET, DependentsTIET, IncomeTIET, LoanAmountTIET, DurationTIET, BusiExpTIET;

    AutoCompleteTextView GenderACTV, MarriedACTV, HousingACTV, EducationACTV, PropertyAreaACTV, BusiIdeaTypeACTV, UnpaidLoanACTV;
    ArrayAdapter<String> GenderACTVAda, MarriedACTVAda, HousingACTVAda, EducationACTVAda, PropertyAreaACTVAda, BusiIdeaTypeACTVAda, UnpaidLoanACTVAda;

    String GenderACTVStr, MarriedACTVStr, HousingACTVStr, EducationACTVStr, PropertyAreaACTVStr, BusiIdeaTypeACTVStr, UnpaidLoanACTVStr
            ,AgeTIETStr, DependentsTIETStr, IncomeTIETStr, LoanAmountTIETStr, DurationTIETStr, BusiExpTIETStr;
    String url = "https://bd-entrepreneur-zone.herokuapp.com/predict";

    Button SubmitBtn;
    static boolean bankExist = false;

    //For LoanResultDialog
    static AutoCompleteTextView DiaRegionACTV, DiaDistrictACTV, DiaLoanTypeACTV;
    static ArrayAdapter<String> DiaRegionACTVAda, DiaDistrictACTVAda, DiaLoanTypeACTVAda;
    static String DiaRegionACTVStr, DiaDistrictACTVStr, DiaLoanTypeACTVStr;
    ProgressBar Progressbar;
    TextInputLayout DiaRegionTIL;

    static Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.loan_frag, container, false);

        //initializing ACTV
        GenderACTV = view.findViewById(R.id.GenderACTV);
        MarriedACTV = view.findViewById(R.id.MarriedACTV);
        HousingACTV = view.findViewById(R.id.HousingACTV);
        EducationACTV = view.findViewById(R.id.EducationACTV);
        PropertyAreaACTV = view.findViewById(R.id.PropertyAreaACTV);
        BusiIdeaTypeACTV = view.findViewById(R.id.BusiIdeaTypeACTV);
        UnpaidLoanACTV = view.findViewById(R.id.UnpaidLoanACTV);

        //TIET
        AgeTIET = view.findViewById(R.id.AgeTIET);
        DependentsTIET = view.findViewById(R.id.DependentsTIET);
        IncomeTIET = view.findViewById(R.id.IncomeTIET);
        LoanAmountTIET = view.findViewById(R.id.LoanAmountTIET);
        DurationTIET = view.findViewById(R.id.DurationTIET);
        BusiExpTIET = view.findViewById(R.id.BusiExpTIET);

        //Button
        SubmitBtn = view.findViewById(R.id.SubmitBtn);

        //Initializing ACTV adapters
        GenderACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, GenderList);
        MarriedACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, YesNoList);
        HousingACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, HousingList);
        EducationACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, EducationList);
        PropertyAreaACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, PropertyAreaList);
        BusiIdeaTypeACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, YesNoList);
        UnpaidLoanACTVAda = new ArrayAdapter<> (getContext(), R.layout.auto_comp_item, YesNoList);

        //Setting ACTV adapters
        GenderACTV.setAdapter(GenderACTVAda);
        MarriedACTV.setAdapter(MarriedACTVAda);
        HousingACTV.setAdapter(HousingACTVAda);
        EducationACTV.setAdapter(EducationACTVAda);
        PropertyAreaACTV.setAdapter(PropertyAreaACTVAda);
        BusiIdeaTypeACTV.setAdapter(BusiIdeaTypeACTVAda);
        UnpaidLoanACTV.setAdapter(UnpaidLoanACTVAda);

        //ClickListener
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessLoan();
            }
        });
        //Intent for BankAct
        intent = new Intent(getContext(), BankAct.class);

        //For Loan District List
        LoadDistrictList();

        return view;
    }

    private void ProcessLoan() {
        LoadAllString();

        if(TextUtils.isEmpty(AgeTIETStr)  || TextUtils.isEmpty(GenderACTVStr) || TextUtils.isEmpty(HousingACTVStr)
                || TextUtils.isEmpty(MarriedACTVStr) || TextUtils.isEmpty(EducationACTVStr)
                || TextUtils.isEmpty(PropertyAreaACTVStr) || TextUtils.isEmpty(BusiIdeaTypeACTVStr)
                || TextUtils.isEmpty(UnpaidLoanACTVStr) || TextUtils.isEmpty(DependentsTIETStr)
                || TextUtils.isEmpty(DurationTIETStr) || TextUtils.isEmpty(IncomeTIETStr)
                || TextUtils.isEmpty(LoanAmountTIETStr) || TextUtils.isEmpty(BusiExpTIETStr)){
            Toast.makeText(getContext(), "Some field are empty"+GenderACTVStr+MarriedACTVStr+HousingACTVStr+EducationACTVStr+PropertyAreaACTVStr+BusiIdeaTypeACTVStr+UnpaidLoanACTVStr +AgeTIETStr+DependentsTIETStr+IncomeTIETStr+LoanAmountTIETStr+DurationTIETStr+BusiExpTIETStr, Toast.LENGTH_SHORT).show();
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //System.out.print();
                        JSONObject jsonObject = new JSONObject(response);
                        String data = jsonObject.getString("result");
                        if(data.equals("1")){
                            LoanResultYesDialog("Congratulations! You are eligible");
                        }else{
                            LoanResultNoDialog("Sorry! You are not eligible");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("MainActivity", error.getMessage().toString());
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Age", AgeTIETStr);
                    params.put("Gender", GenderACTVStr);
                    params.put("Married", MarriedACTVStr);
                    params.put("Housing", HousingACTVStr);
                    params.put("Dependents", DependentsTIETStr);
                    params.put("Education", EducationACTVStr);
                    params.put("Income", IncomeTIETStr);
                    params.put("LoanAmount", LoanAmountTIETStr);
                    params.put("LoanDuration", DurationTIETStr);
                    params.put("PropertyArea", PropertyAreaACTVStr);
                    params.put("BusinessIdea", BusiIdeaTypeACTVStr);
                    params.put("UnpaidLoan", UnpaidLoanACTVStr);
                    params.put("BusinessExp", BusiExpTIETStr);

                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(stringRequest);
        }
    }
    private void LoadAllString(){
        //For Gender
        GenderACTVStr = GenderACTV.getText().toString();
        if(GenderACTVStr.equals("Male"))
            GenderACTVStr = "1";
        else if(GenderACTVStr.equals("Female"))
            GenderACTVStr = "0";
        //For Married
        MarriedACTVStr = MarriedACTV.getText().toString();
        if(MarriedACTVStr.equals("Yes"))
            MarriedACTVStr = "1";
        else if(MarriedACTVStr.equals("No"))
            MarriedACTVStr = "0";
        //For Housing
        HousingACTVStr = HousingACTV.getText().toString();
        if(HousingACTVStr.equals("Own"))
            HousingACTVStr = "0";
        else if(HousingACTVStr.equals("Rent"))
            HousingACTVStr = "1";
        //For Education
        EducationACTVStr = EducationACTV.getText().toString();
        if(EducationACTVStr.equals("Not Graduate"))
            EducationACTVStr = "2";
        else if(EducationACTVStr.equals("Graduate"))
            EducationACTVStr = "1";
        else if(EducationACTVStr.equals("Certified Entrepreneur"))
            EducationACTVStr = "0";
        //For PropertyArea
        PropertyAreaACTVStr = PropertyAreaACTV.getText().toString();
        if(PropertyAreaACTVStr.equals("Rural"))
            PropertyAreaACTVStr = "0";
        else if(PropertyAreaACTVStr.equals("Semi-urban"))
            PropertyAreaACTVStr = "1";
        else if(PropertyAreaACTVStr.equals("Urban"))
            PropertyAreaACTVStr = "2";
        //For Business Type
        BusiIdeaTypeACTVStr = BusiIdeaTypeACTV.getText().toString();
        if(BusiIdeaTypeACTVStr.equals("Yes"))
            BusiIdeaTypeACTVStr = "1";
        else if(BusiIdeaTypeACTVStr.equals("No"))
            BusiIdeaTypeACTVStr = "0";
        //For Unpaid Loan
        UnpaidLoanACTVStr = UnpaidLoanACTV.getText().toString();
        if(UnpaidLoanACTVStr.equals("Yes"))
            UnpaidLoanACTVStr = "1";
        else if(UnpaidLoanACTVStr.equals("No"))
            UnpaidLoanACTVStr = "0";

        AgeTIETStr = AgeTIET.getText().toString().trim();
        DependentsTIETStr = DependentsTIET.getText().toString().trim();
        IncomeTIETStr = IncomeTIET.getText().toString().trim();
        LoanAmountTIETStr = LoanAmountTIET.getText().toString().trim();
        DurationTIETStr = DurationTIET.getText().toString().trim();
        BusiExpTIETStr = BusiExpTIET.getText().toString().trim();
        //Temporary data
        /*AgeTIETStr = "32";
        GenderACTVStr = "0";
        MarriedACTVStr = "0";
        HousingACTVStr = "0";
        DependentsTIETStr = "1";
        EducationACTVStr = "0";
        IncomeTIETStr = "0";
        LoanAmountTIETStr = "160";
        DurationTIETStr = "4";
        PropertyAreaACTVStr = "0";
        BusiIdeaTypeACTVStr = "0";
        UnpaidLoanACTVStr = "0";
        BusiExpTIETStr = "0";*/
    }
    public void LoanResultYesDialog(String msg){
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        }else{
            alert = new AlertDialog.Builder(getContext());
        }
        View view = getLayoutInflater().inflate(R.layout.dialog_bank_recomnd, null);

        DiaCongraTV = view.findViewById(R.id.CongraTV);
        DiaDistrictACTV = view.findViewById(R.id.DistrictACTV);
        DiaRegionACTV = view.findViewById(R.id.RegionACTV);
        DiaLoanTypeACTV = view.findViewById(R.id.LoanTypeACTV);
        Progressbar = view.findViewById(R.id.Progressbar);

        DiaRegionTIL = view.findViewById(R.id.DistrictTIL);

        DiaCongraTV.setText(msg);

        //load 3 adapter for 3 ACTV
        DiaDistrictACTVAda = new ArrayAdapter<>(getContext(), R.layout.auto_comp_item, DistrictListAr);
        DiaLoanTypeACTVAda = new ArrayAdapter<>(getContext(), R.layout.auto_comp_item, LoanTypeListAr);
        //Setting Adapter
        DiaDistrictACTV.setAdapter(DiaDistrictACTVAda);
        DiaLoanTypeACTV.setAdapter(DiaLoanTypeACTVAda);

        SubmitBtn = view.findViewById(R.id.SubmitBtn);

        //Setting Listener in ACTV
        DiaDistrictACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiaDistrictACTVStr = DiaDistrictACTV.getText().toString();
                LoadRegionList();
                //setting ArrayAdapter for DistrictACTV
                DiaRegionACTVAda = new ArrayAdapter<>(getContext(), R.layout.auto_comp_item, RegionListAr);
                DiaRegionACTV.setAdapter(DiaRegionACTVAda);
            }
        });

        alert.setView(view);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Submit Action for Bank Recommendation
                LoadBankData();
                dialog.dismiss();
                ResetLoanInput();
            }
        });
    }

    private void LoadBankData() {
        bankExist = false;
        Progressbar.setVisibility(View.VISIBLE);

        DiaDistrictACTVStr = DiaDistrictACTV.getText().toString();
        DiaRegionACTVStr = DiaRegionACTV.getText().toString();
        DiaLoanTypeACTVStr = DiaLoanTypeACTV.getText().toString();

        if(DiaRegionACTVStr.isEmpty()){
            DiaRegionACTV.requestFocus();
            return;
        }
        if(DiaLoanTypeACTVStr.isEmpty()){
            DiaLoanTypeACTV.requestFocus();
            return;
        }
        BankCol.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document: task.getResult()){
                        if(document.get("District").toString().equals(DiaDistrictACTVStr)
                                && document.get("Region").toString().equals(DiaRegionACTVStr)
                                && document.get("LoanType").toString().equals(DiaLoanTypeACTVStr)){
                            intent.putExtra("District", document.get("District").toString());
                            intent.putExtra("Region", document.get("Region").toString());
                            intent.putExtra("LoanType", document.get("LoanType").toString());
                            intent.putExtra("Bank", document.get("Bank").toString());
                            intent.putExtra("Address", document.get("Address").toString());
                            bankExist = true;
                            break;
                        }
                    }
                    Progressbar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getContext(), "Here"+bankExist, Toast.LENGTH_SHORT).show();
                    if(bankExist) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Something wrong about data or no such bank", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public void LoanResultNoDialog(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Result");
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ResetLoanInput();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ResetLoanInput() {
        AgeTIET.setText("");
        GenderACTV.setText("");
        MarriedACTV.setText("");
        HousingACTV.setText("");
        DependentsTIET.setText("");
        EducationACTV.setText("");
        IncomeTIET.setText("");
        LoanAmountTIET.setText("");
        DurationTIET.setText("");
        PropertyAreaACTV.setText("");
        BusiIdeaTypeACTV.setText("");
        UnpaidLoanACTV.setText("");
        BusiExpTIET.setText("");
    }

    private void LoadDistrictList() {
        DistrictListAr.clear();
        BankCol.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document: task.getResult()){
                        DistrictListSet.add(document.get("District").toString());
                    }
                    for (String district:DistrictListSet){
                        DistrictListAr.add(district);
                    }
                    DistrictListSet.clear();
                }
            }
        });
    }

    private void LoadRegionList(){
        Progressbar.setVisibility(View.VISIBLE);
        RegionListAr.clear();
        RegionListSet.clear();
        BankCol.whereEqualTo("District",DiaDistrictACTVStr).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document:task.getResult())
                        RegionListSet.add(document.get("Region").toString());

                    for(String region:RegionListSet)
                        RegionListAr.add(region);

                    RegionListSet.clear();
                }
                Progressbar.setVisibility(View.INVISIBLE);
                DiaRegionTIL.setEnabled(true);
            }
        });
    }
}