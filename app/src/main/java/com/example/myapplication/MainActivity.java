package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private EditText EdtTaikhoan,EdtMatkhau;
    private Button BtnDangnhap;
    private FirebaseAuth Datalogin;
    private ProgressBar LoadLogin;

    private Intent StoreActivity;

    private ImageView TestBartender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            EdtTaikhoan = (EditText)findViewById(R.id.EdtTaikhoan);
            EdtMatkhau = (EditText)findViewById(R.id.EdtMatkhau);
            BtnDangnhap = (Button)findViewById(R.id.BtnDangnhap);
            LoadLogin = (ProgressBar)findViewById(R.id.LoadLogin);
             TestBartender = (ImageView)findViewById(R.id.IconLogin);



            StoreActivity = new Intent(this, com.example.myapplication.StoreActivity.class );


            TestBartender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Bartender = new Intent(getApplicationContext(),BartenderActivity.class);
                    startActivity(Bartender);
                }
            });


            Datalogin = FirebaseAuth.getInstance();
            LoadLogin.setVisibility(View.INVISIBLE);

            BtnDangnhap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String mail = EdtTaikhoan.getText().toString();
                    final String password = EdtMatkhau.getText().toString();

                    if(mail.isEmpty() || password.isEmpty())
                    {
                            Intent Oder = new Intent(getApplicationContext(),OderActivity.class);
                            startActivity(Oder);
                        //showMassage("Vui lòng nhập đầy đủ thông tin");
                    }
                    else
                    {
                            Dangnhap(mail, password);
                    }
                }

                private  void  Dangnhap(String mail, String password)
                {
                    Datalogin.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                LoadLogin.setVisibility(View.VISIBLE);
                                BtnDangnhap.setVisibility(View.INVISIBLE);
                                updateUI();
                                Intent Store = new Intent(getApplicationContext(),StoreActivity.class);
                            }
                            else
                            {
                                 showMassage(task.getException().getMessage());
                            }
                        }
                    });
                }
                private void showMassage(String text) {
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void updateUI() {
        startActivity(StoreActivity);
        finish();
    }
}
