package com.example.attendance.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance.R;
import com.example.attendance.beans.addteacherdatbase;
import com.example.attendance.teacherdetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class detailteacher extends Fragment {
    ListView listView;
    DatabaseReference notice;
    List<addteacherdatbase> teacherinfo;
    ProgressBar detailtpb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.detailteacher,container,false);
        listView=layout.findViewById(R.id.teacherinfo);
        detailtpb=layout.findViewById(R.id.detailTeacherpb);
        notice = FirebaseDatabase.getInstance().getReference("Teacher");
        teacherinfo=new ArrayList<>();
        getActivity().setTitle("Thông tin giảng viên");
        return layout;
    }
    @Override
    public void onStart() {
        super.onStart();
        detailtpb.setVisibility(View.VISIBLE);
        notice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot info:dataSnapshot.getChildren()){
                    addteacherdatbase showinfo=info.getValue(addteacherdatbase.class);
                    teacherinfo.add(showinfo);
                }
                teacherdetail adapter = new teacherdetail(getContext(),teacherinfo);
                listView.setAdapter(adapter);
                detailtpb.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(),"error"+error,Toast.LENGTH_LONG).show();
                detailtpb.setVisibility(View.GONE);
            }
        });
    }
}
