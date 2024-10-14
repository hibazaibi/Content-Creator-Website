package com.app.cc.auth;

public class EmployeeCount {
    private String etat;
    private Long number;

    public EmployeeCount(String etat, Long number) {
        this.etat = etat;
        this.number = number;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
