package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SanPhamAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<classSanPham> SanPhamArrayList;

    public SanPhamAdapter(Context context, int resource, List<classSanPham> sanPhamArrayList) {
        this.context = context;
        this.resource = resource;
        SanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public int getCount() {
        return SanPhamArrayList.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.rowsanpham, viewGroup, false);
            viewHolder = new SanPhamAdapter.ViewHolder();
            viewHolder.txtmasp=view.findViewById(R.id.txtmasp);
            viewHolder.txttensp=view.findViewById(R.id.txttensp);
            viewHolder.txtgia=view.findViewById(R.id.txtgiasp);

            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        classSanPham contact = SanPhamArrayList.get(i);

        viewHolder.txtmasp.setText(contact.getMasp());
        viewHolder.txttensp.setText(contact.getTensp());
        viewHolder.txtgia.setText(contact.getGia());

        return view;
    }

    public class ViewHolder {
        TextView txtmasp,txttensp,txtgia;
    }
}
