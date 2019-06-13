package com.example.myapplication;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class banAdapter extends BaseAdapter {
   private Context context;
   private int resource;
  private   List<classBan> arrBanList;

    public banAdapter(Context context, int resource, List<classBan> arrBanList) {
        this.context = context;
        this.resource = resource;
        this.arrBanList = arrBanList;
    }

    @Override
    public int getCount() {
        return arrBanList.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.rowban, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtmaban=view.findViewById(R.id.txtmaban);
            viewHolder.txttenban=view.findViewById(R.id.txttenban);
            viewHolder.constraintLayout=view.findViewById(R.id.layoutban);
            viewHolder.listView=view.findViewById(R.id.lvbanghe);
            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
            classBan contact = arrBanList.get(i);

            viewHolder.txtmaban.setText(contact.getMaban());

            viewHolder.txttenban.setText(contact.getTenban());


            String string = contact.getTinhtrangban();

            if (string.equals("1")) {
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.rowban1);

            }
            if (string.equals("0")) {
                viewHolder.constraintLayout.setBackgroundResource(R.drawable.rowban0);
            }

        return view;
    }

    public class ViewHolder {
        TextView txtmaban,txttenban;
        FrameLayout constraintLayout;
        ListView listView;

    }
}
