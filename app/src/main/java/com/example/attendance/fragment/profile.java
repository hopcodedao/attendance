package com.example.attendance.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance.R;
import com.example.attendance.beans.addteacherdatbase;
import com.example.attendance.beans.profiledatabase;
import com.example.attendance.global;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.time.Year;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.Y;

public class profile extends Fragment {
    private TextView full_names,showfacutly,Birthofdates,Year1, mssvs, showclasss, showclass, showmajorr, showmajor,Contacts,mails,addresss,mssv,showdegreet,Year,clazz,showdegree;
    private ImageButton profile;
    private ProgressBar ppb;
    private StorageReference mStorageRef;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.profile,container,false);
        full_names=layout.findViewById(R.id.full_name);
        showfacutly=layout.findViewById(R.id.showfacutly);
        Birthofdates=layout.findViewById(R.id.Birthofdate);
        Year=layout.findViewById(R.id.showyear);
        showclasss=layout.findViewById(R.id.showclasss);
        showclass=layout.findViewById(R.id.showclass);
        Year1=layout.findViewById(R.id.showyearr);
        showmajor = layout.findViewById(R.id.showmajor);
        showmajorr = layout.findViewById(R.id.showmajorr);
        Contacts=layout.findViewById(R.id.Contact);
        mails=layout.findViewById(R.id.mail);
        addresss=layout.findViewById(R.id.address);
        profile=layout.findViewById(R.id.profileimage);
        ppb=layout.findViewById(R.id.profilepb);
        mssv=layout.findViewById(R.id.mssv);
        mssvs=layout.findViewById(R.id.mssvs);
        clazz=layout.findViewById(R.id.showclass);
        showdegree=layout.findViewById(R.id.showdegree);
        showdegreet=layout.findViewById(R.id.showdegreet);
        showdegree.setVisibility(GONE);
        showdegreet.setVisibility(GONE);
        getActivity().setTitle("Thông tin cá nhân");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        return layout;
    }
    @Override
    public void onStart() {
        super.onStart();
        ppb.setVisibility(View.VISIBLE);
        String loginid=((global) requireActivity().getApplication()).getUid();
        DatabaseReference notice;
        notice = FirebaseDatabase.getInstance().getReference("Profile/"+loginid);
        notice.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profiledatabase addteacherdatbase= dataSnapshot.getValue(profiledatabase.class);
                assert addteacherdatbase != null;
                full_names.setText(addteacherdatbase.getFullName());
                mails.setText(addteacherdatbase.getEmail());

                if(addteacherdatbase.getDegree()!=null){
                    showdegree.setVisibility(View.VISIBLE);
                    showdegreet.setVisibility(View.VISIBLE);
                    showdegree.setText(addteacherdatbase.getDegree());
                }

                if(addteacherdatbase.getYear()==null){
                    Year.setVisibility(GONE);
                    Year1.setVisibility(GONE);
                }

                Year.setText(addteacherdatbase.getYear());

                showfacutly.setText(addteacherdatbase.getFaculty());

                if(addteacherdatbase.getMajors()==null){
                    showmajor.setVisibility(GONE);
                    showmajorr.setVisibility(GONE);
                }

                showmajor.setText(addteacherdatbase.getMajors());

                if(addteacherdatbase.getClazz()==null){
                    showclasss.setVisibility(GONE);
                    showclass.setVisibility(GONE);
                }

                showclass.setText(addteacherdatbase.getClazz());

                if(addteacherdatbase.getMSSV()==null){
                    mssv.setVisibility(GONE);
                    mssvs.setVisibility(GONE);
                }

                mssv.setText(addteacherdatbase.getMSSV());

                Birthofdates.setText(addteacherdatbase.getBrithofDate());
                Contacts.setText(addteacherdatbase.getNumber());
                addresss.setText(addteacherdatbase.getAddress());

                ppb.setVisibility(GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"error"+error,Toast.LENGTH_LONG).show();
                ppb.setVisibility(GONE);
            }
        });
        String pp=((global) requireActivity().getApplication()).getLocalprofilepic();
        if(pp!=null){
            File newfile=new File(pp);
            Bitmap bitmapdatabase2 = BitmapFactory.decodeFile(newfile.getAbsolutePath());
            profile.setImageBitmap(bitmapdatabase2);
        }
    }
}
