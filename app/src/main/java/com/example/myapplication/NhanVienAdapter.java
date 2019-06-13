package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.List;

public class NhanVienAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<classnhanvien> NhanvienArrayList;

    public NhanVienAdapter(Context context, int resource, List<classnhanvien> nhanvienArrayList) {
        this.context = context;
        this.resource = resource;
        NhanvienArrayList = nhanvienArrayList;
    }

    @Override
    public int getCount() {
        return NhanvienArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.rownhanvien, viewGroup, false);
            viewHolder = new NhanVienAdapter.ViewHolder();
            viewHolder.txtmanv=view.findViewById(R.id.txtmanv);
            viewHolder.txttennv=view.findViewById(R.id.txttennv);
            viewHolder.txttk=view.findViewById(R.id.txttk);
            viewHolder.txtmk=view.findViewById(R.id.txtmk);
            viewHolder.txtcongviec=view.findViewById(R.id.txtcongviec);
            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        classnhanvien contact = NhanvienArrayList.get(i);
        viewHolder.txtmanv.setText(contact.getManv());
        viewHolder.txttennv.setText(contact.getTennv());
        viewHolder.txttk.setText(contact.getTk());
        viewHolder.txtmk.setText(contact.getMk());
        viewHolder.txtcongviec.setText(contact.getCongviec());

        return view;
    }

    public class ViewHolder {
        TextView txtmanv,txttennv,txttk,txtmk,txtcongviec;
    }
}
