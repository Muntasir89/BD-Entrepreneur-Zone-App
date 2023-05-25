package com.example.bdentepreneuerzone.Model;

public class LoanModel {
    String LoanName, LoanDes, Priority;
    public LoanModel(String loanName, String loanDes, String priority) {
        LoanName = loanName;
        LoanDes = loanDes;
        Priority = priority;
    }
    public String getLoanName() {
        return LoanName;
    }

    public String getLoanDes() {
        return LoanDes;
    }

    public String getPriority() {return Priority;}
}
