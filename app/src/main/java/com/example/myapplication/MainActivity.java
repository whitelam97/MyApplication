package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private EditText EdtTaikhoan,EdtMatkhau;
    private Button BtnDangnhap;
    private FirebaseAuth Datalogin;
    private ProgressBar LoadLogin;

    private Intent StoreActivity;

    private ImageView TestBartender;

    url url= new url();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            EdtTaikhoan = (EditText)findViewById(R.id.EdtTaikhoan);
            EdtMatkhau = (EditText)findViewById(R.id.EdtMatkhau);
            BtnDangnhap = (Button)findViewById(R.id.BtnDangnhap);
            LoadLogin = (ProgressBar)findViewById(R.id.LoadLogin);
             TestBartender = (ImageView)findViewById(R.id.IconLogin);

             EdtTaikhoan.setText("thuytien");
            EdtMatkhau.setText("thuytien");



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
                        login(mail,password);
                    }

                }

                private  void  Dangnhap(final String mail, final String password)
                {

                    Datalogin.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                LoadLogin.setVisibility(View.VISIBLE);
                                BtnDangnhap.setVisibility(View.INVISIBLE);

                                StoreActivity = new Intent(MainActivity.this, StoreActivity.class );
                                startActivity(StoreActivity);
                                finish();
                            }
                            else
                            {
//                                 showMassage(task.getException().getMessage());

                            }
                        }
                    });

                }
                private void showMassage(String text) {
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                }
            });

    }

    //đăng nhập
    public void login(final String user, final String pass){
       String urllogin = url.getUrl()+"login.php?tk="+user+"&mk="+pass+"";
        Log.i("Hiteshurl",""+urllogin);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urllogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String manv = jsonObject1.getString("manv");
                    String tennv = jsonObject1.getString("tennv");
                    String tk = jsonObject1.getString("tk");
                    String mk = jsonObject1.getString("mk");
                    String congviec = jsonObject1.getString("congviec");



                    SharedPreferences shared = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("manv",manv);
                    editor.putString("tennv",tennv);
                    editor.putString("tk",tk);
                    editor.putString("mk",mk);
                    editor.putString("congviec",congviec);

                    editor.commit();

                    // kiểm tra id xem là giáo viên hay thanh tra để chuyển activity
                    switch (congviec){
                        case "admin":  {
                            LoadLogin.setVisibility(View.VISIBLE);
                            BtnDangnhap.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(MainActivity.this,StoreActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case "Pha chế":  {
                            LoadLogin.setVisibility(View.VISIBLE);
                            BtnDangnhap.setVisibility(View.INVISIBLE);
                            Intent intent1 = new Intent(MainActivity.this,BartenderActivity.class);
                            startActivity(intent1);
                            break;
                        }
                        case "order":  {
                            LoadLogin.setVisibility(View.VISIBLE);
                            BtnDangnhap.setVisibility(View.INVISIBLE);
                            Intent intent1 = new Intent(MainActivity.this,OderActivity.class);
                            startActivity(intent1);
                            break;
                        }
                        default:
                            Toast.makeText(MainActivity.this, "Bạn Không có quyền truy cập !", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
//                    Toast.makeText(MainActivity.this, "Tài khoản không đúng!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });
        requestQueue.add(stringRequest);
    }


}
