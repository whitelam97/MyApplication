package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

public class StoreActivity extends AppCompatActivity {

    private BottomNavigationView NavMenu;
    private FrameLayout FrameStore;
    private TextView Tv1,Tv2;

    private FragmentBanghe FraBangghe;
    private FragmentSanpham FraSanpham;
    private FragmentNhanvien FraNhanvien;
    private FragmentThongke FraThongke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        FrameStore = (FrameLayout)findViewById(R.id.FrameStore);
        NavMenu = (BottomNavigationView)findViewById(R.id.NavMenu);
        Tv1 = (TextView)findViewById(R.id.TextView1);
        Tv2 = (TextView)findViewById(R.id.TextView2);


        FraBangghe = new FragmentBanghe();
        FraSanpham = new FragmentSanpham();
        FraNhanvien = new FragmentNhanvien();
        FraThongke = new FragmentThongke();

        NavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                Tv1.setText("");
                Tv2.setText("");
                switch(Item.getItemId())
                {
                    case R.id.Nav_Banghe:
                        setFragment(FraBangghe);
                        return true;
                    case R.id.Nav_Sanpham:
                        setFragment(FraSanpham);
                        return true;
                    case R.id.Nav_Nhanvien:
                        setFragment(FraNhanvien);
                        return true;
                    case R.id.Nav_Thongke:
                        setFragment(FraThongke);
                        return true;
                        default: return false;
                }
            }

            private void setFragment(Fragment frag) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.FrameStore, frag);
                fragmentTransaction.commit();
            }
        });
    }
}
