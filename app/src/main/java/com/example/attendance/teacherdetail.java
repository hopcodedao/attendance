package com.example.attendance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.attendance.beans.addteacherdatbase;

import java.util.List;

public class teacherdetail extends ArrayAdapter<addteacherdatbase> {
    private TextView teachername,teachercource,teacherbirthdate,teacheremail,teacheraddress;
    private Activity context;
    private List<addteacherdatbase> teacherinfo;
    public teacherdetail(Context context, List<addteacherdatbase> teacherinfo) {
        super(context,R.layout.teacherlist,teacherinfo);
        this.context= (Activity) context;
        this.teacherinfo=teacherinfo;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.teacherlist,null,true);
        teachername=listViewItem.findViewById(R.id.teachername);
        teachercource=listViewItem.findViewById(R.id.teacherfaculty);
        teacherbirthdate=listViewItem.findViewById(R.id.degree);
        teacheremail=listViewItem.findViewById(R.id.teacheremail);
        addteacherdatbase addteacherdatbase=teacherinfo.get(position);
        teachername.setText(addteacherdatbase.getFullname());
        teachercource.setText(addteacherdatbase.getFaculty());
        teacherbirthdate.setText(addteacherdatbase.getDegree());
        teacheremail.setText(addteacherdatbase.getEmail());
        return listViewItem;
    }
}
