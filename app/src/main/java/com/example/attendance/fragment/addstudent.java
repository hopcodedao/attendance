package com.example.attendance.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance.CaptureFace;
import com.example.attendance.Common.Common;
import com.example.attendance.R;
import com.example.attendance.activity.login;
import com.example.attendance.beans.addstudentdatabase;
import com.example.attendance.beans.profiledatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class addstudent extends Fragment {
    private EditText studentfullname, studenmssv,studentnumber,studentemail,studentmajors,studentclass,studentbirtofdate,studentaddresss;
    private ImageView studentimage;
    private Spinner studentfaculty,studentyear;
    private DatabaseReference notice;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth1;
    private DatabaseReference notice1;
    private ProgressBar pbadd;
    private Button studentsubmit;
    DatePickerDialog picker;
    String candidateBase64Photo = "", candidateFaceArray = "";

    boolean isImageSelected = false;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            candidateFaceArray = data.getStringExtra("candidateFaceArray");
            candidateBase64Photo = "data:image/jpeg;base64," + Common.imageBase64;
            byte[] decodedString = Base64.decode(Common.imageBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            studentimage.setImageBitmap(decodedByte);
        }
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout= inflater.inflate(R.layout.addstudent,container,false);

        studentfullname=layout.findViewById(R.id.studentfullname);
        studenmssv=layout.findViewById(R.id.studentmssv);
        studentnumber=layout.findViewById(R.id.studentnumber);
        studentemail=layout.findViewById(R.id.studentemail);
        studentclass=layout.findViewById(R.id.studentclass);;
        studentbirtofdate=layout.findViewById(R.id.studentbirtofdate);
        studentaddresss=layout.findViewById(R.id.studentaddresss);
        studentfaculty=layout.findViewById(R.id.studentfaculty);
        studentyear=layout.findViewById(R.id.studentyear);
        studentmajors = layout.findViewById(R.id.studentmajors);

        studentsubmit = layout.findViewById(R.id.studentsubmit);
        studentimage = layout.findViewById(R.id.studentimage);
        pbadd=layout.findViewById(R.id.progressaddstudent);
        pbadd.setVisibility(View.GONE);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        notice = FirebaseDatabase.getInstance().getReference("Student");
        notice1 = FirebaseDatabase.getInstance().getReference("Profile");

        requireActivity().setTitle("Thêm sinh viên");

        studentbirtofdate.setInputType(InputType.TYPE_NULL);
        studentbirtofdate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> studentbirtofdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Công nghệ thông tin");
        categories.add("Điện – Điện tử – Viễn thông");
        categories.add("Kỹ thuật Cơ khí");
        categories.add("Kỹ thuật Xây dựng");
        categories.add("Kinh tế - Quản lý công nghiệp");
        categories.add("Khoa Công sinh học - Công nghệ hóa học - Công nghệ thực phẩm");
        categories.add("Khoa học Xã hội");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_item);
        studentfaculty.setAdapter(dataAdapter);

        List<String> categories1 = new ArrayList<String>();
        categories1.add("2020");
        categories1.add("2021");
        categories1.add("2022");
        categories1.add("2023");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, categories1);
        dataAdapter1.setDropDownViewResource(R.layout.spinner_drop_item);
        studentyear.setAdapter(dataAdapter1);

        studentimage.setOnClickListener(view -> startActivityForResult(new Intent(getActivity(), CaptureFace.class).putExtra("captureFace", true), 201));

        studentsubmit.setOnClickListener(v -> {
            pbadd.setVisibility(View.VISIBLE);
            setfalse();
            final String emai = studentemail.getText().toString().trim();
            final String fullnames = studentfullname.getText().toString().trim();
            final String mssvs = studenmssv.getText().toString().trim();
            final String birthofdates = studentbirtofdate.getText().toString().trim();
            final String numbers = studentnumber.getText().toString().trim();
            final String classs = studentclass.getText().toString().trim();
            final String addresss = studentaddresss.getText().toString().trim();
            final String facultys = studentfaculty.getSelectedItem().toString();
            final String years = studentyear.getSelectedItem().toString();
            final String majors = studentmajors.getText().toString();

            if (Objects.equals(candidateFaceArray, "")) {
                Log.d("student", "file null");
                Toast.makeText(getActivity(), "Vui lòng chọn ảnh của bạn", Toast.LENGTH_LONG).show();
            } else {
                isImageSelected = true;
            }
            if (!isImageSelected) {
                Toast.makeText(getActivity(), "Vui lòng chọn ảnh của bạn", Toast.LENGTH_LONG).show();
            } else if (emai.isEmpty()) {
                studentemail.setError("Vui lòng nhập email");
                studentemail.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (fullnames.isEmpty()) {
                studentfullname.setError("Vui lòng nhập họ và tên");
                studentfullname.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (mssvs.isEmpty()) {
                studenmssv.setError("Vui lòng nhập mã số sinh viên");
                studenmssv.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (birthofdates.isEmpty()) {
                studentbirtofdate.setError("Vui lòng nhập ngày sinh");
                studentbirtofdate.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (numbers.isEmpty()) {
                studentnumber.setError("Vui lòng nhập số điện thoại");
                studentnumber.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (classs.isEmpty()) {
                studentclass.setError("Vui lòng nhập lớp");
                studentclass.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (addresss.isEmpty()) {
                studentaddresss.setError("Vui lòng nhập địa chỉ");
                studentaddresss.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (emai.length() < 10) {
                studentemail.setError("Vui lòng nhập đúng định dạng email");
                studentemail.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (fullnames.length() < 9) {
                studentfullname.setError("Vui lòng nhập họ và tên đầy đủ");
                studentfullname.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (numbers.length() < 10) {
                studentnumber.setError("Vui lòng nhập số điện thoại hợp lệ");
                studentnumber.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (addresss.length() < 7) {
                studentaddresss.setError("Vui lòng nhập địa chỉ đầy đủ");
                studentaddresss.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (!isValid(emai)) {
                studentemail.setError("Email không hợp lệ");
                studentemail.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (majors.isEmpty()) {
                studentaddresss.setError("Vui lòng nhập ngành");
                studentaddresss.requestFocus();
                pbadd.setVisibility(View.GONE);
                settrue();
            } else if (emai.isEmpty() && fullnames.isEmpty() && mssvs.isEmpty() && birthofdates.isEmpty() && numbers.isEmpty() && classs.isEmpty() && addresss.isEmpty() && majors.isEmpty()) {
                Toast.makeText(getContext(), "Các trường đều trống!", Toast.LENGTH_SHORT).show();
                pbadd.setVisibility(View.GONE);
                settrue();
            }
            mAuth1 = FirebaseAuth.getInstance();
            mAuth1.createUserWithEmailAndPassword(emai, numbers).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        StorageReference riversRef = mStorageRef.child("Student/" + facultys + "/" + majors + "/" + classs + "/" + mssvs + ".jpg");
                        StorageReference riversRef1 = mStorageRef.child("Profile/"+uid+".jpg");
                        riversRef.putBytes(Base64.decode(Common.imageBase64, Base64.DEFAULT));
                        riversRef1.putBytes(Base64.decode(Common.imageBase64, Base64.DEFAULT));
                        profiledatabase profiledatabase = new profiledatabase(fullnames, mssvs, addresss, emai, birthofdates, classs, majors, facultys, numbers, uid, candidateFaceArray, numbers, "Student", years, null);
                        notice1.child(uid).setValue(profiledatabase);

                        addstudentdatabase addstudentdatabase = new addstudentdatabase(fullnames, mssvs, addresss, emai, birthofdates, classs, majors, facultys, numbers, uid);
                        notice.child(facultys + "/" + majors + "/" + classs + "/" + mssvs).setValue(addstudentdatabase);

                        studentfullname.setText("");
                        studentaddresss.setText("");
                        studentbirtofdate.setText("");
                        studentemail.setText("");
                        studenmssv.setText("");
                        studentnumber.setText("");
                        studentclass.setText("");
                        studentmajors.setText("");

                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                        pbadd.setVisibility(View.GONE);
                        settrue();
                        mAuth1.signOut();
                        Intent intent = new Intent(getContext(), login.class);
                        intent.putExtra("action", "autologin");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        pbadd.setVisibility(View.GONE);
                        settrue();
                    }
                } else {
                    Toast.makeText(getContext(), "Thêm không thành công!", Toast.LENGTH_LONG).show();
                    pbadd.setVisibility(View.GONE);
                    settrue();
                }
            });
        });
        return layout;
    }

    private void setfalse(){
        studentemail.setEnabled(false);
        studentfullname.setEnabled(false);
        studenmssv.setEnabled(false);
        studentbirtofdate.setEnabled(false);
        studentnumber.setEnabled(false);
        studentclass.setEnabled(false);
        studentaddresss.setEnabled(false);
        studentfaculty.setEnabled(false);
        studentyear.setEnabled(false);
        studentsubmit.setEnabled(false);
        studentmajors.setEnabled(false);
    }
    private void settrue(){
        studentemail.setEnabled(true);
        studentfullname.setEnabled(true);
        studenmssv.setEnabled(true);
        studentbirtofdate.setEnabled(true);
        studentnumber.setEnabled(true);
        studentclass.setEnabled(true);
        studentaddresss.setEnabled(true);
        studentfaculty.setEnabled(true);
        studentyear.setEnabled(true);
        studentsubmit.setEnabled(true);
        studentmajors.setEnabled(true);
    }
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
