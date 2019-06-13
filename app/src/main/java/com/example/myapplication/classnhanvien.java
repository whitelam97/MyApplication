package com.example.myapplication;

public class classnhanvien {
        String manv;
        String tennv;
        String tk;
        String mk;
        String congviec;

    public classnhanvien(String manv, String tennv, String tk, String mk, String congviec) {
        this.manv = manv;
        this.tennv = tennv;
        this.tk = tk;
        this.mk = mk;
        this.congviec = congviec;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }

    public String getCongviec() {
        return congviec;
    }

    public void setCongviec(String congviec) {
        this.congviec = congviec;
    }
}
