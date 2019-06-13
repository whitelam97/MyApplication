package com.example.myapplication;

public class classSanPham {
    String masp;
    String tensp;
    String gia;

    public classSanPham(String masp, String tensp, String gia) {
        this.masp = masp;
        this.tensp = tensp;
        this.gia = gia;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }
}
