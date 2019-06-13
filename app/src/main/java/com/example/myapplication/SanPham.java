package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

public class SanPham extends AppCompatActivity {

    ListView lvsanpham;
    ArrayList<classSanPham> SanPhamArrayList;
    url url= new url();
    EditText edtmasp,edttensp,edtgia;

    String urlthemsp= url.getUrl()+"qlcf/ThemSanPham.php";
    String urlxoasp= url.getUrl()+"qlcf/XoaSanPham.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);

        edtmasp= findViewById(R.id.edtmasp);
        edttensp= findViewById(R.id.edttensp);
        edtgia= findViewById(R.id.edtgiasp);

        Button btnthem= findViewById(R.id.btnthemsp);
        Button btncn= findViewById(R.id.btncnsp);

        lvsanpham = findViewById(R.id.lvsanphamp);

        loadListview();

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String masp= edtmasp.getText().toString();
                    String tensp= edttensp.getText().toString();
                    String gia= edtgia.getText().toString();

//                    Toast.makeText(NhanVien.this, manv+tennv+tknv+mk+congviec, Toast.LENGTH_SHORT).show();
                    if (!masp.isEmpty()||!tensp.isEmpty()||!gia.isEmpty()){
                        themnv(urlthemsp,masp,tensp,gia);
                    }
                    else Toast.makeText(SanPham.this, "Điển đủ thông tin trước khi thêm !", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){

                }

            }
        });
        btncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadListview();
            }
        });

        lvsanpham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ma= SanPhamArrayList.get(i).getMasp();
                xoanv(urlxoasp,ma);
                return false;
            }
        });
    }

    public void loadListview(){
        SanPhamArrayList= new ArrayList<classSanPham>();
        final String  URLtkbtuan= url.getUrl()+"qlcf/SelectSanPham.php";
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
                    SanPhamArrayList.add(new classSanPham(
                            lh.getString("masp"),
                            lh.getString("tensp"),
                            lh.getString("giasp")
                    ));
                }
                SanPhamAdapter adapter= new SanPhamAdapter(getApplicationContext(), R.layout.rowsanpham,SanPhamArrayList);
                lvsanpham.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(SanPham.this, e.toString()+"", Toast.LENGTH_SHORT).show();
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

    //them sp
    private void themnv(String urladdsv, final String masp, final String tensp,final String gia){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(SanPham.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                          Toast.makeText(SanPham.this, "Cập nhật loi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SanPham.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("masp",masp);
                param.put("tensp",tensp);
                param.put("giasp",gia);


                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //xoa  nv
    private void xoanv(String urladdsv, final String ma){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(SanPham.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SanPham.this, "Xóa lỗi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SanPham.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("masp",ma);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
