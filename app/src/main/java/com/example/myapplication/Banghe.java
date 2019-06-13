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

public class Banghe extends AppCompatActivity {

    Button btnthem,bancapnhat;
    EditText edtmaban;
    EditText edttenban;

    ArrayList<classBan> banArrayList;
    url url= new url();

    ListView listView;

    String urlthemban= url.getUrl()+"qlcf/themban.php";
    String urlxoaban= url.getUrl()+"qlcf/xoaban.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banghe);
        btnthem= findViewById(R.id.BtnThemban);
        bancapnhat= findViewById(R.id.btncapnhaban);
        edtmaban= findViewById(R.id.EdtMaban);
        edttenban= findViewById(R.id.EdtTenban);
        listView= findViewById(R.id.lvbanghe);

        loadListview();

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maban= edtmaban.getText().toString();
                String tenban= edttenban.getText().toString();
                if (!maban.isEmpty()||!tenban.isEmpty()){
                    themban(urlthemban,maban,tenban);
                }
                else Toast.makeText(Banghe.this, "Điển đủ thông tin trước khi thêm !", Toast.LENGTH_SHORT).show();
            }
        });
        bancapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadListview();

            }
        });

       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               String ma= banArrayList.get(i).getMaban();
               xoaban(urlxoaban,ma);
               return false;
           }
       });

    }

    public void loadListview(){
        banArrayList= new ArrayList<classBan>();
        final String  URLtkbtuan1= url.getUrl()+"qlcf/selectBan.php";
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
                    banArrayList.add(new classBan(
                            lh.getString("maban"),
                            lh.getString("tenban"),
                            lh.getString("tinhtrangban")
                    ));
                }
                if (banArrayList.size()==0){
                    banArrayList.add(new classBan("10","Không tìm thấy!","0"));
                }
                Toast.makeText(Banghe.this, banArrayList.size()+"", Toast.LENGTH_SHORT).show();

                banAdapter  adapter= new banAdapter(getApplicationContext(), R.layout.rowban,banArrayList);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                Toast.makeText(Banghe.this, e.toString()+"", Toast.LENGTH_SHORT).show();
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

    //them  ban
    private void themban(String urladdsv, final String getidsv, final String getidtkb){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(Banghe.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(Banghe.this, "Cập nhật loi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Banghe.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("maban",getidsv);
                param.put("tenban",getidtkb);
                param.put("tinhtrangban","0");
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    //xoa  ban
    private void xoaban(String urladdsv, final String ma){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(Banghe.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(Banghe.this, "Xóa lỗi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Banghe.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("maban",ma);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}
