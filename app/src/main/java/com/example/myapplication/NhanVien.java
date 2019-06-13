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

public class NhanVien extends AppCompatActivity {
    ListView listViewnv;
    ArrayList<classnhanvien> nhanvienArrayList;
    url url= new url();
    EditText edtten,edtmanv,edttk,edtmk,edtcongviec;

    String urlthemnv= url.getUrl()+"ThemNhanVien.php";
    String urlxoanv= url.getUrl()+"XoaNhanVien.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_vien);

        Button btnthem=findViewById(R.id.BtnThemNhanvien);
        Button btncn=findViewById(R.id.btncapnhatnv);

         edtten=findViewById(R.id.edttennv);
         edtmanv=findViewById(R.id.edtmanv);
         edttk=findViewById(R.id.edttknv);
         edtmk=findViewById(R.id.edtmknv);
         edtcongviec=findViewById(R.id.edtcvnv);

        listViewnv = findViewById(R.id.lvnhanvien);
        loadListview();

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String manv= edtmanv.getText().toString();
                    String tennv= edtten.getText().toString();
                    String tknv= edttk.getText().toString();
                    String mk= edtmk.getText().toString();
                    String congviec= edtcongviec.getText().toString();
//                    Toast.makeText(NhanVien.this, manv+tennv+tknv+mk+congviec, Toast.LENGTH_SHORT).show();
                    if (!manv.isEmpty()||!tennv.isEmpty()||!tknv.isEmpty()||!mk.isEmpty()||!congviec.isEmpty()){
                        themnv(urlthemnv,manv,tennv,tknv,mk,congviec);
                    }
                    else Toast.makeText(NhanVien.this, "Điển đủ thông tin trước khi thêm !", Toast.LENGTH_SHORT).show();

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

        listViewnv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ma= nhanvienArrayList.get(i).getManv();
                xoanv(urlxoanv,ma);
                return false;
            }
        });




    }
    public void loadListview(){
        nhanvienArrayList= new ArrayList<classnhanvien>();
        final String  URLtkbtuan1= url.getUrl()+"selectnhanvien.php";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute(URLtkbtuan1);
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
                    nhanvienArrayList.add(new classnhanvien(
                            lh.getString("manv"),
                            lh.getString("tennv"),
                            lh.getString("tk"),
                            lh.getString("mk"),
                            lh.getString("congviec")
                    ));
                }
                NhanVienAdapter  adapter= new NhanVienAdapter(getApplicationContext(), R.layout.rownhanvien,nhanvienArrayList);
                listViewnv.setAdapter(adapter);
            } catch (JSONException e) {
                Toast.makeText(NhanVien.this, e.toString()+"", Toast.LENGTH_SHORT).show();
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


    //them  nv
    private void themnv(String urladdsv, final String manv, final String tennv,final String tk,final String mk,final String congviec){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(NhanVien.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(NhanVien.this, "Cập nhật loi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NhanVien.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("manv",manv);
                param.put("tennv",tennv);
                param.put("tk",tk);
                param.put("mk",mk);
                param.put("congviec",congviec);

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
                            Toast.makeText(NhanVien.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(NhanVien.this, "Xóa lỗi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NhanVien.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("manv",ma);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
