package com.example.attendance.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance.R;
import com.example.attendance.beans.addstudentdatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class detailstudent extends Fragment {
    private EditText detailrollnumber,detaildivison;
    private TextView detailfull_name,detailshowcource,detailBirthofdate,detailroll,detailContact,detailmail,detailaddress,divisionstudent,uidstudent;
    private ImageView detailprofile;
    private Button detailshow;
    private Spinner detailcource,detailyear;
    private DatabaseReference notice;
    private DatabaseReference notice1;
    private ProgressBar detailstudentpb;
    private StorageReference mStorageRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.detailstudent,container,false);
        detailrollnumber=layout.findViewById(R.id.detailrollnumber);
        detaildivison=layout.findViewById(R.id.detaildivison);

        detailfull_name=layout.findViewById(R.id.detailfull_name);
        detailshowcource=layout.findViewById(R.id.detailshowcource);
        detailBirthofdate=layout.findViewById(R.id.detailBirthofdate);
        detailroll=layout.findViewById(R.id.detailroll);
        detailContact=layout.findViewById(R.id.detailContact);
        detailmail=layout.findViewById(R.id.detailmail);
        detailaddress=layout.findViewById(R.id.detailaddress);
        uidstudent=layout.findViewById(R.id.uidstudent);
        divisionstudent=layout.findViewById(R.id.divisionstudent);

        detailprofile=layout.findViewById(R.id.detailprofile);
        detailshow=layout.findViewById(R.id.detailshow);

        detailcource=layout.findViewById(R.id.detailcource);
        detailyear=layout.findViewById(R.id.detailyear);

        detailstudentpb=layout.findViewById(R.id.progressdetail);
        detailstudentpb.setVisibility(View.GONE);
        getActivity().setTitle("Sinh viên");

        List<String> categories = new ArrayList<String>();
        categories.add("Công nghệ thông tin");
        categories.add("Điện – Điện tử – Viễn thông");
        categories.add("Kỹ thuật Cơ khí");
        categories.add("Kỹ thuật Xây dựng");
        categories.add("Kinh tế - Quản lý công nghiệp");
        categories.add("Khoa Công sinh học - Công nghệ hóa học - Công nghệ thực phẩm");
        categories.add("Khoa học Xã hội");
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_item);
        detailcource.setAdapter(dataAdapter);

        List<String> categories1 = new ArrayList<String>();
        categories1.add("Khoa học máy tính");
        categories1.add("Khoa học dữ liệu");
        categories1.add("Hệ thống thông tin");
        categories1.add("Công nghệ thông tin");
        categories1.add("Kỹ thuật phần mềm");
        categories1.add("Kỹ thuật hệ thống công nghiệp");
        categories1.add("Quản lý công nghiệp");
        categories1.add("Logistics và Quản lý chuỗi cung ứng");
        categories1.add("Quản lý xây dựng");
        categories1.add("Công nghệ kỹ thuật công trình xây dựng");
        categories1.add("Công nghệ kỹ thuật cơ điện tử");
        categories1.add("Công nghệ kỹ thuật điều khiển và tự động hóa");
        categories1.add("Công nghệ kỹ thuật điện, điện tử");
        categories1.add("Công nghệ thực phẩm");
        categories1.add("Công nghệ sinh học");
        categories1.add("Công nghệ kỹ thuật năng lượng");
        categories1.add("Công nghệ kỹ thuật hóa học");
        categories1.add("Quản trị kinh doanh");
        categories1.add("Tài chính - Ngân hàng");
        categories1.add("Kế toán");
        categories1.add("Luật");
        categories1.add("Ngôn ngữ Anh");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, categories1);
        dataAdapter1.setDropDownViewResource(R.layout.spinner_drop_item);
        detailyear.setAdapter(dataAdapter1);

        detailshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailstudentpb.setVisibility(View.VISIBLE);
                String rollnumberlocal=detailrollnumber.getText().toString().trim();
                final String divisionlocal=detaildivison.getText().toString().trim();
                final String coureseslocal =detailcource.getSelectedItem().toString();
                final String yearslocal = detailyear.getSelectedItem().toString();
                if(!(rollnumberlocal.isEmpty())){
                    if(divisionlocal.isEmpty()){
                        detaildivison.setError("Vui lòng nhập lớp!");
                        detaildivison.requestFocus();
                        detailstudentpb.setVisibility(View.GONE);
                    }else {
                        notice = FirebaseDatabase.getInstance().getReference("Student/"+coureseslocal+"/"+yearslocal+"/"+divisionlocal+"/"+rollnumberlocal);
                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        notice.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null){
                                    addstudentdatabase showinfo=dataSnapshot.getValue(addstudentdatabase.class);
                                    assert showinfo != null;

                                    if(showinfo!=null){
                                        detailfull_name.setText(showinfo.getFullName());
                                        detailshowcource.setText(showinfo.getFaculty());
                                        detailBirthofdate.setText(showinfo.getBrithofDate());
                                        detailContact.setText(showinfo.getNumber());
                                        detailmail.setText(showinfo.getEmail());
                                        detailaddress.setText(showinfo.getAddress());
                                        detailroll.setText(showinfo.getMSSV());
                                        uidstudent.setText(showinfo.getMajors());
                                        String uids=showinfo.getUid();
                                        divisionstudent.setText(showinfo.getClazz());
                                        if(uids!=null){
                                            StorageReference riversRef = mStorageRef.child("Profile/"+uids+".jpg");
                                            try {
                                                final File localFile = File.createTempFile("images", "jpg");
                                                riversRef.getFile(localFile)
                                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                Bitmap bitmapdatabase2 = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                                detailprofile.setImageBitmap(bitmapdatabase2);
                                                                detailstudentpb.setVisibility(View.GONE);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        Toast.makeText(getContext(),"Error"+exception,Toast.LENGTH_LONG).show();
                                                        detailstudentpb.setVisibility(View.GONE);
                                                    }
                                                });
                                            } catch (IOException e) {
                                                Toast.makeText(getContext(),"Error"+e,Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                                detailstudentpb.setVisibility(View.GONE);
                                            }
                                        }else {detailstudentpb.setVisibility(View.GONE);}
                                    }else {
                                        Toast.makeText(getContext(), "Information not found", Toast.LENGTH_LONG).show();
                                        // Set the views to default state or hide them
                                        detailstudentpb.setVisibility(View.GONE);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(getContext(),"Error"+error,Toast.LENGTH_LONG).show();
                                detailstudentpb.setVisibility(View.GONE);
                            }
                        });
                    }
                }else {
                    Toast.makeText(getContext(),"Vui lòng điền các ô!",Toast.LENGTH_LONG).show();
                    detailrollnumber.requestFocus();
                    detailstudentpb.setVisibility(View.GONE);
                }
            }
        });
        return layout;
    }
}
