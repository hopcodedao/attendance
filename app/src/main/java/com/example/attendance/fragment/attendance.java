package com.example.attendance.fragment;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance.Common.Common;
import com.example.attendance.MatchFace;
import com.example.attendance.R;
import com.example.attendance.activity.selectrollnumber;
import com.example.attendance.beans.Attandance;
import com.example.attendance.beans.profiledatabase;
import com.example.attendance.global;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class attendance extends Fragment {

    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private Spinner attansubject;
    private TextView attanfacutly, attanyear, division, starttime, endtime;
    private Button camera;
    boolean isIn = false;

    final List<Integer> rollNoList = new ArrayList<>();

    private DatabaseReference notice2;
    private DatabaseReference notice3;
    private String[] subjects = {
            "Software Quality Assurance",
            "Security in Computing",
            "Business Intelligence",
            "Principles of GIS/EN",
            "T Service Management/Cyber Laws",
            "Project Implementation",
            "Security in Computing Practical",
            "Business Intelligence Practical",
            "Principles of GIS/EN Practical",
            "Advanced Mobile Programming"
    };

    private int sub;
    private StorageReference mStorageRef;
    private Attandance Attandance;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.attandance, container, false);
        attanfacutly = layout.findViewById(R.id.spinner);
        attanyear = layout.findViewById(R.id.spinner1);

        attansubject = layout.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attansubject.setAdapter(adapter);

        division = layout.findViewById(R.id.spinner3);
        starttime = layout.findViewById(R.id.starttime);
        endtime = layout.findViewById(R.id.endtime);

        camera = layout.findViewById(R.id.camera);
        getActivity().setTitle("Attendance");

        mStorageRef = FirebaseStorage.getInstance().getReference();

        starttime.setInputType(InputType.TYPE_NULL);
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                TimePickerDialog picker1;
                picker1 = new TimePickerDialog(getContext(),R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        starttime.setText(sHour + ":" + sMinute);
                    }
                }, hour, minutes, false);
                picker1.getWindow().setBackgroundDrawableResource(R.color.grey);
                picker1.show();
            }
        });

        endtime.setInputType(InputType.TYPE_NULL);
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                TimePickerDialog picker1;
                picker1 = new TimePickerDialog(getContext(),R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        endtime.setText(sHour + ":" + sMinute);
                    }
                }, hour, minutes, false);
                picker1.getWindow().setBackgroundDrawableResource(R.color.grey);
                picker1.show();
            }
        });

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                            if (Attandance == null) {
                                Attandance = new Attandance("1","2");
                            }
                            markAttendance();
                            Toast.makeText(getActivity(), "Okay", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String attancourses=attanfacutly.getText().toString().trim();
                String attanyears=attanyear.getText().toString().trim();
                String attansubjects=attansubject.getSelectedItem().toString().trim();
                String divisions=division.getText().toString().trim();
                String starttimes=starttime.getText().toString().trim();
                String endtimes=endtime.getText().toString().trim();
                if(divisions.isEmpty()){
                    division.setError("Please enter division");
                    division.requestFocus();
                }else if(starttimes.isEmpty()){
                    starttime.setError("Please enter division");
                    starttime.requestFocus();
                }else if(endtimes.isEmpty()){
                    endtime.setError("Please enter division");
                    endtime.requestFocus();
                }else if(starttimes.length()<4){
                    starttime.setError("Please enter valid format");
                    starttime.requestFocus();
                }else if(endtimes.length()<4){
                    endtime.setError("Please enter valid format");
                    endtime.requestFocus();
                }else{
                    isIn = true;
                    matchFace();
                }
            }
        });
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        String loginid=((global) requireActivity().getApplication()).getUid();
        DatabaseReference notice;
        notice = FirebaseDatabase.getInstance().getReference("Profile/"+loginid);
        notice.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profiledatabase addstudentdatabase = dataSnapshot.getValue(profiledatabase.class);
                assert addstudentdatabase != null;
                attanfacutly.setText(addstudentdatabase.getFaculty());
                attanyear.setText(addstudentdatabase.getYear());
                division.setText(addstudentdatabase.getMSSV());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"error"+error,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void markAttendance() {

        String attancourses=attanfacutly.getText().toString().trim();
        String attanyears=attanyear.getText().toString().trim();
        String attansubjects=attansubject.getSelectedItem().toString().trim();
        String divisions=division.getText().toString().trim();
        String starttimes=starttime.getText().toString().trim();
        String endtimes=endtime.getText().toString().trim();

        Date dNow = new Date( );
        @SuppressLint("SimpleDateFormat") SimpleDateFormat ft1 = new SimpleDateFormat ("yyyyMMddHH:mm");
        final String date=ft1.format(dNow);
        notice2 = FirebaseDatabase.getInstance().getReference("Attandance");

        Attandance attandance=new Attandance(starttimes,endtimes);

        notice2.child(attancourses+"/"+attanyears+"/"+divisions+"/"+attansubjects+"/"+date).setValue(attandance).addOnSuccessListener(unused -> {
            Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failure!", Toast.LENGTH_SHORT).show());
    }

    private void startMatchFaceActivity() {
        Intent intent = new Intent(getActivity(), MatchFace.class);
        someActivityResultLauncher.launch(intent);
    }
    private void matchFace() {
        startMatchFaceActivity();
    }

}