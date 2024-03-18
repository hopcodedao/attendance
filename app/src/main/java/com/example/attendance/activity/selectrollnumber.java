package com.example.attendance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.R;
import com.example.attendance.beans.Attandance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class selectrollnumber extends AppCompatActivity {
    private DatabaseReference notice;
    private DatabaseReference notice1;
    private DatabaseReference notice2;
    private ProgressBar pbs;
    ArrayList<Integer> list=new ArrayList<Integer>();
    long k=0;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectrollnumber);
        ActionBar actionBar= getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0052c5"));
        actionBar.setBackgroundDrawable(colorDrawable);
        setTitle("MSSV");
        pbs=findViewById(R.id.setectpb);
        pbs.setVisibility(View.VISIBLE);
        final Intent intent=getIntent();
        final String course =intent.getStringExtra("course");
        final String year=intent.getStringExtra("year");
        final String division=intent.getStringExtra("division");
        final String subject=intent.getStringExtra("subject");
        final String starttime=intent.getStringExtra("starttime");
        final String endtime=intent.getStringExtra("endtime");
        notice = FirebaseDatabase.getInstance().getReference("Attandance");
        notice = FirebaseDatabase.getInstance().getReference("Student/"+course+"/"+year+"/"+division);
        notice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    pbs.setVisibility(View.GONE);

                    TableLayout tableLayout = findViewById(R.id.mainLayout);
                    tableLayout.removeAllViews(); // Xóa các view trước đó nếu có

                    // Tạo một danh sách lưu trữ mã số sinh viên
                    List<String> studentRollNumbers = new ArrayList<>();

                    // Lặp qua mỗi child trong dataSnapshot
                    for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                        String studentRollNumber = studentSnapshot.getKey();
                        if (studentRollNumber != null) {
                            studentRollNumbers.add(studentRollNumber);
                        }
                    }

                    // Tiếp tục phần tạo CheckBox và Button như trước nhưng thay 'k' bằng studentRollNumbers.size()
                    // Phần code dưới đây giả sử bạn muốn 3 CheckBox trên mỗi hàng
                    int numberOfStudents = studentRollNumbers.size();
                    TableRow tableRow = null;
                    CheckBox checkBox;

                    for(int i = 0; i < numberOfStudents; i++){
                        if(i % 3 == 0) {
                            tableRow = new TableRow(getApplicationContext());
                            tableLayout.addView(tableRow);
                        }

                        // Tạo CheckBox với ID là mã số sinh viên từ Firebase
                        checkBox = new CheckBox(getApplicationContext());
                        checkBox.setText("MSSV: " + studentRollNumbers.get(i));
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setId(Integer.parseInt(studentRollNumbers.get(i))); // Đặt ID là MSSV
                        checkBox.setTextSize(12);
                        checkBox.setPadding(50, 30, 50, 30);
                        tableRow.addView(checkBox);
                    }

                    Button btn=new Button(getApplicationContext());
                    btn.setText("Submit");
                    btn.setTextColor(Color.WHITE);
                    btn.setId(1000);
                    btn.setBackgroundColor(Color.parseColor("#1134af"));

                    tableLayout.addView(btn);

                    final List<String> rollNoList = new ArrayList<>();

                    for (String rollNo : studentRollNumbers) {
                        // Chúng ta có thể khai báo lại biến trong scope của vòng lặp
                        CheckBox checkBoxx = findViewById(Integer.parseInt(rollNo));
                        checkBoxx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                Log.d("tag", "RollNo.: " + rollNo + " checked: " + isChecked);
                                if (isChecked) {
                                    // Thêm mã số sinh viên vào danh sách nếu được chọn
                                    rollNoList.add(rollNo);
                                } else {
                                    // Xóa mã số sinh viên khỏi danh sách nếu không được chọn
                                    rollNoList.remove(rollNo);
                                }
                                Log.d("tag", "rollNoList - " + Arrays.toString(rollNoList.toArray()));
                            }
                        });
                    }

                    notice1=FirebaseDatabase.getInstance().getReference("Attandance");

                    btn=findViewById(1000);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Date dNow = new Date( );
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHH:mm");
                            final String date=ft.format(dNow);

                            pbs.setVisibility(View.VISIBLE);
                            for(int roll=0;roll<rollNoList.size();roll++){
                                Attandance attandance=new Attandance(starttime,endtime);
                                notice1.child(course+"/"+year+"/"+division+"/"+ rollNoList.get(roll)+"/"+subject+"/"+date).setValue(attandance);
                                Log.d("tag","submit");
                            }
                            Date AdNow = new Date( );
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat ft1 = new SimpleDateFormat ("yyyyMMddHH:mm");
                            final String Adate=ft1.format(AdNow);

                            notice2 = FirebaseDatabase.getInstance().getReference("Attandancedetail");
                            notice2.child(course+"/"+year+"/"+division+"/"+subject+"/"+Adate).setValue("date");
                            pbs.setVisibility(View.GONE);
                            Toast.makeText(selectrollnumber.this,"Submit", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }else {
                    Log.d("tag","you don't have child");
                    Toast.makeText(selectrollnumber.this,"First Enter a Student",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error","Error"+error);
                Toast.makeText(selectrollnumber.this,"Error--"+error,Toast.LENGTH_LONG).show();
            }
        });
    }
}
