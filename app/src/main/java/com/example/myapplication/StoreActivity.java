package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StoreActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Button btnnv=findViewById(R.id.btnnv);
        Button btnbanghe =findViewById(R.id.btnbanghe);
        Button btnsp = findViewById(R.id.btnsanpham);
        Button btnds = findViewById(R.id.btndoanhso);


        btnbanghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(StoreActivity.this, Banghe.class);
                startActivity(intent);
            }
        });

        btnnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(StoreActivity.this, NhanVien.class);
                startActivity(intent);
            }
        });

        btnsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(StoreActivity.this, SanPham.class);
                startActivity(intent);
            }
        });

        btnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(StoreActivity.this, Thongke.class);
                startActivity(intent);
            }
        });



    }
}
