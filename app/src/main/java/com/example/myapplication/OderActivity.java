package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OderActivity extends AppCompatActivity {

    TextView txtcapnhat;
    Spinner spnban, spnsp;
    url url=new url();
    ArrayList<String> banArrayList;
    ArrayList<String> spArrayList;
    TextView txtmanv,tongtien;
    ListView listViewoder;
    ArrayList<classSanPham> SanPhamOderArrayList;
    String urlthemspoer= url.getUrl()+"qlcf/ThemspOder.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder);

        txtcapnhat =findViewById(R.id.txtcn);
        Button btnthem= findViewById(R.id.btnthemspoder);
        listViewoder = findViewById(R.id.lvspoder);
        txtmanv =findViewById(R.id.txtmanv);
        spnban= findViewById(R.id.spnban);
        spnsp=findViewById(R.id.spnsanpham);
        tongtien=findViewById(R.id.txttongtien);

        banArrayList= new ArrayList<>();
        String urlban =url.getUrl()+"qlcf/loadspinnerBan.php";
        loadSpinnerban(urlban);


        spArrayList= new ArrayList<>();
        String urlsp =url.getUrl()+"qlcf/loadSpinnerSanPham.php";
        loadSpinnersp(urlsp);

        SharedPreferences shared= getSharedPreferences("login", Context.MODE_PRIVATE);
        final String manv = shared.getString("manv", "");
        txtmanv.setText(manv);


        spnban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ma =banArrayList.get(i);
                loadListview(ma);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
               loadListview("00");
            }
        });

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maban= spnban.getSelectedItem().toString();
                String sp= spnsp.getSelectedItem().toString();
                String[] outpu = sp.split(" ");
                String masp =outpu[0];
                Toast.makeText(OderActivity.this, maban+masp, Toast.LENGTH_SHORT).show();

                themspoer(urlthemspoer,manv,masp,maban,"0");
            }
        });

        txtcapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maban= spnban.getSelectedItem().toString();
                loadListview(maban);
            }
        });


    }
    public void loadListview(final String maban){
        SanPhamOderArrayList= new ArrayList<classSanPham>();
        final String  URLtkbtuan= url.getUrl()+"qlcf/selctBanOser.php?maban="+maban+"";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(URLtkbtuan);
            }
        });


    }

    class docJson extends AsyncTask<String,Integer,String> {
        //doinbackgroufd dung doc du lieu tren mang
        @Override
        protected String doInBackground(String... strings) {
            return docnoidungtuURL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray mangjson = new JSONArray(s);
                for(int i=0;i<mangjson.length();i++) {
                    JSONObject lh = mangjson.getJSONObject(i);
                    SanPhamOderArrayList.add(new classSanPham(
                            lh.getString("masp"),
                            lh.getString("tensp"),
                            lh.getString("giasp")
                    ));
                }
                SanPhamAdapter adapter= new SanPhamAdapter(getApplicationContext(), R.layout.rowsanpham,SanPhamOderArrayList);
                listViewoder.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(OderActivity.this, e.toString()+"", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private static String docnoidungtuURL(String theUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //tạo một biến url
            URL url = new URL(theUrl);
            //tạo một biến ket noi ủrl
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            //Doc urlconnet tư buttfetreader
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");
            }
            bufferedReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    public void loadSpinnerban(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String maban=jsonObject1.getString("maban");
                        banArrayList.add(maban);
                    }
                    spnban.setAdapter(new ArrayAdapter<String>(OderActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, banArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OderActivity.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    public void loadSpinnersp(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String masp=jsonObject1.getString("masp");
                        String tensp=jsonObject1.getString("tensp");
                        spArrayList.add(masp+" - "+tensp);
                    }
                    spnsp.setAdapter(new ArrayAdapter<String>(OderActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, spArrayList));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OderActivity.this, "KHông tim thay", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    //them sp
    private void themspoer(String urladdsv, final String manv, final String masp,final String maban,final String tinhtrang){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(OderActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(OderActivity.this, "Cập nhật loi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OderActivity.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("manv",manv);
                param.put("masp",masp);
                param.put("maban",maban);
                param.put("tinhtrang","0");

                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

}
