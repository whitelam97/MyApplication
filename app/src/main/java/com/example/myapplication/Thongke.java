package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Thongke extends AppCompatActivity {
    ListView lvthongke;
    ArrayList<classSanPham> thongkeArrayList;
    url url= new url();
    TextView txttongtien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);

        lvthongke= findViewById(R.id.lvthongke);
        txttongtien=findViewById(R.id.txttongtienthongke);
        thongkeArrayList= new ArrayList<classSanPham>();
        final String  URLtkbtuan= url.getUrl()+"qlcf/selectThongKe.php";
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
                    thongkeArrayList.add(new classSanPham(
                            lh.getString("manv"),
                            lh.getString("tensp"),
                            lh.getString("giasp"),
                            ""
                    ));
                }
                float tongtien=0;
                for (int i=0;i<thongkeArrayList.size();i++){
                    float gia =Float.parseFloat(thongkeArrayList.get(i).getGia());
                    tongtien=tongtien+gia;
                }
                txttongtien.setText(tongtien+"");
                SanPhamAdapter adapter= new SanPhamAdapter(getApplicationContext(), R.layout.rowsanpham, thongkeArrayList);
                lvthongke.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(Thongke.this, e.toString()+"", Toast.LENGTH_SHORT).show();
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

}
