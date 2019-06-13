package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class BartenderActivity extends AppCompatActivity {

    ListView lvphache;
    ArrayList<classSanPham> phacheArrayList;
    url url= new url();
    String urlxoasp= url.getUrl()+"qlcf/Xoaphache.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bartender);

        TextView txtload = findViewById(R.id.txtloadphache);
        lvphache=findViewById(R.id.lvphache);

         Loadlv();

        lvphache.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ma= phacheArrayList.get(i).getIdpc();
                xoaspphache(urlxoasp,ma);
                return false;
            }
        });

        txtload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loadlv();
            }
        });

    }
    public void Loadlv(){
        phacheArrayList= new ArrayList<classSanPham>();
        final String  URLtkbtuan= url.getUrl()+"qlcf/selectPhaChe.php";
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
                    phacheArrayList.add(new classSanPham(
                            lh.getString("maban"),
                            lh.getString("tensp"),
                            lh.getString("giasp"),
                            lh.getString("idpc")
                            ));
                }
                SanPhamAdapter adapter= new SanPhamAdapter(getApplicationContext(), R.layout.rowsanpham, phacheArrayList);
                lvphache.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(BartenderActivity.this, e.toString()+"", Toast.LENGTH_SHORT).show();
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
    //xoa
    private void xoaspphache(String urladdsv, final String ma){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST,urladdsv,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(BartenderActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(BartenderActivity.this, "Xóa lỗi", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BartenderActivity.this, "Lỗi sever"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("idpc",ma);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


}
