package com.example.attendance.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.attendance.R;
import com.example.attendance.activity.selectrollnumber;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class attandance_tc extends Fragment {
    private String attancourses;
    private String attanyears;
    private String attansubjects;
    private String divisions;
    private String starttimes;
    private String endtimes;
    private FusedLocationProviderClient fusedLocationClient;
    private Spinner attancourse,attanyear,attansubject;
    private EditText division,starttime,endtime;
    private Button qrcode,rollnumber;
    ImageView qrCodeImageView;

    private static final int YOUR_PERMISSIONS_REQUEST_LOCATION = 1; // Giá trị thực tế có thể là bất kỳ số int nào duy nhất trong project

    int CAMERA_PIC_REQUEST=1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private List<String> FY_1BSCIT=new ArrayList<>(10);
    private List<String> FY_2BSCIT=new ArrayList<>(10);
    private List<String> SY_3BSCIT=new ArrayList<>(10);
    private List<String> SY_4BSCIT=new ArrayList<>(10);
    private List<String> TY_5BSCIT=new ArrayList<>(10);
    private List<String> TY_6BSCIT=new ArrayList<>(10);

    private int sub;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.attandance_tc,container,false);
        attancourse=layout.findViewById(R.id.spinner);
        attanyear=layout.findViewById(R.id.spinner2);
        attansubject=layout.findViewById(R.id.spinner3);
        division=layout.findViewById(R.id.divisionattndanc);
        starttime=layout.findViewById(R.id.starttime);
        endtime=layout.findViewById(R.id.endtime);

        qrcode=layout.findViewById(R.id.qr_code);
        qrCodeImageView = layout.findViewById(R.id.qrCodeImageView);
        rollnumber=layout.findViewById(R.id.rollnumber);
        getActivity().setTitle("Điểm danh");

        FY_1BSCIT.add("Communication Skill");
        FY_1BSCIT.add("Operating Systems");
        FY_1BSCIT.add("Digital Electronics");
        FY_1BSCIT.add("Imperative Programming");
        FY_1BSCIT.add("Discrete Mathematics");
        FY_1BSCIT.add("Imperative Programming Practical");
        FY_1BSCIT.add("Digital Electronics Practical");
        FY_1BSCIT.add("Operating Systems Practical");
        FY_1BSCIT.add("Discrete Mathematics Practical");
        FY_1BSCIT.add("Communication Skills Practical");

        FY_2BSCIT.add("Object oriented Programming");
        FY_2BSCIT.add("Microprocessor Architecture");
        FY_2BSCIT.add("Web Programming");
        FY_2BSCIT.add("Numerical and Statistical Methods");
        FY_2BSCIT.add("Green Computing");
        FY_2BSCIT.add("Object Oriented Programming Practical");
        FY_2BSCIT.add("Microprocessor Architecture Practical");
        FY_2BSCIT.add("Web Programming Practical");
        FY_2BSCIT.add("Numerical & Statistical Methods Practical");
        FY_2BSCIT.add("Green Computing Practical");

        SY_3BSCIT.add("Python Programming");
        SY_3BSCIT.add("Data Structures");
        SY_3BSCIT.add("Computer Networks");
        SY_3BSCIT.add("Database Management Systems");
        SY_3BSCIT.add("Applied Mathematics");
        SY_3BSCIT.add("Python Programming Practical");
        SY_3BSCIT.add("Data Structures Practical");
        SY_3BSCIT.add("Computer Networks Practical");
        SY_3BSCIT.add("Database Management Systems Practical");
        SY_3BSCIT.add("Mobile Programming Practical");

        SY_4BSCIT.add("Core Java");
        SY_4BSCIT.add("Introduction to Embedded Systems");
        SY_4BSCIT.add("Computer Oriented Statistical Techniques");
        SY_4BSCIT.add("Software Engineering");
        SY_4BSCIT.add("Computer Graphics and Animation");
        SY_4BSCIT.add("Core Java Practical");
        SY_4BSCIT.add("Introduction to ES Practical");
        SY_4BSCIT.add("COST Practical");
        SY_4BSCIT.add("Software Engineering Practical");
        SY_4BSCIT.add("Computer Graphics and Animation Practical");

        TY_5BSCIT.add("Software Project Management");
        TY_5BSCIT.add("Internet of Things");
        TY_5BSCIT.add("Advanced Web Programming");
        TY_5BSCIT.add("AI/Linux Admin.");
        TY_5BSCIT.add("E Java/NGT");
        TY_5BSCIT.add("Project Dissertation");
        TY_5BSCIT.add("Internet of Things Practical");
        TY_5BSCIT.add("Advanced Web Programming Practical");
        TY_5BSCIT.add("AI/Linux Admin. Practical");
        TY_5BSCIT.add("E Java/NGT Practical");

        TY_6BSCIT.add("Software Quality Assurance");
        TY_6BSCIT.add("Security in Computing");
        TY_6BSCIT.add("Business Intelligence");
        TY_6BSCIT.add("Principles of GIS/EN");
        TY_6BSCIT.add("IT Service Management/Cyber Laws");
        TY_6BSCIT.add("Project Implementation");
        TY_6BSCIT.add("Security in Computing Practical");
        TY_6BSCIT.add("Business Intelligence Practical");
        TY_6BSCIT.add("Principles of GIS/EN Practical");
        TY_6BSCIT.add("Advanced Mobile Programming");

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


        List<String> categories = new ArrayList<String>();
//        categories.add("BSCIT");
        categories.add("Công nghệ thông tin");
        categories.add("Điện – Điện tử – Viễn thông");
        categories.add("Kỹ thuật Cơ khí");
        categories.add("Kỹ thuật Xây dựng");
        categories.add("Kinh tế - Quản lý công nghiệp");
        categories.add("Khoa Công sinh học - Công nghệ hóa học - Công nghệ thực phẩm");
        categories.add("Khoa học Xã hội");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_item);
        attancourse.setAdapter(dataAdapter);
        attancourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"Error--"+parent,Toast.LENGTH_LONG).show();
            }
        });

        List<String> categories1 = new ArrayList<String>();
//        categories1.add("FY_1");
//        categories1.add("FY_2");
//        categories1.add("SY_3");
//        categories1.add("SY_4");
//        categories1.add("TY_5");
//        categories1.add("TY_6");
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
        attanyear.setAdapter(dataAdapter1);

        attanyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(sub == 0 && position == 0){
                    ArrayAdapter<String> dataAdapter2=new ArrayAdapter<>(getContext(), R.layout.spinner_item, FY_1BSCIT);
                    dataAdapter2.setDropDownViewResource(R.layout.spinner_drop_item);
                    attansubject.setAdapter(dataAdapter2);
                }else if(sub == 0 && position == 1){
                    ArrayAdapter<String> dataAdapter2=new ArrayAdapter<>(getContext(), R.layout.spinner_item, FY_2BSCIT);
                    dataAdapter2.setDropDownViewResource(R.layout.spinner_drop_item);
                    attansubject.setAdapter(dataAdapter2);
                }else if(sub == 0 && position == 2) {
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getContext(), R.layout.spinner_item, SY_3BSCIT);
                    dataAdapter2.setDropDownViewResource(R.layout.spinner_drop_item);
                    attansubject.setAdapter(dataAdapter2);
                }else if(sub == 0 && position == 3) {
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getContext(), R.layout.spinner_item, SY_4BSCIT);
                    dataAdapter2.setDropDownViewResource(R.layout.spinner_drop_item);
                    attansubject.setAdapter(dataAdapter2);
                }else if(sub == 0 && position == 4) {
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getContext(), R.layout.spinner_item, TY_5BSCIT);
                    dataAdapter2.setDropDownViewResource(R.layout.spinner_drop_item);
                    attansubject.setAdapter(dataAdapter2);
                }else if(sub == 0 && position == 5) {
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getContext(), R.layout.spinner_item, TY_6BSCIT);
                    dataAdapter2.setDropDownViewResource(R.layout.spinner_drop_item);
                    attansubject.setAdapter(dataAdapter2);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(),"Error"+parent,Toast.LENGTH_LONG).show();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attancourses = attancourse.getSelectedItem().toString().trim();
                attanyears = attanyear.getSelectedItem().toString().trim();
                attansubjects = attansubject.getSelectedItem().toString().trim();
                divisions = division.getText().toString().trim();
                starttimes = starttime.getText().toString().trim();
                endtimes = endtime.getText().toString().trim();

                if (divisions.isEmpty() || starttimes.isEmpty() || endtimes.isEmpty() || starttimes.length() < 4 || endtimes.length() < 4) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, YOUR_PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        getLocationAndCreateQR();
                    }
                }
            }
        });

        rollnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attancourses = attancourse.getSelectedItem().toString().trim();
                attanyears = attanyear.getSelectedItem().toString().trim();
                attansubjects = attansubject.getSelectedItem().toString().trim();
                divisions = division.getText().toString().trim();
                starttimes = starttime.getText().toString().trim();
                endtimes = endtime.getText().toString().trim();


                if(divisions.isEmpty() || starttimes.isEmpty() || endtimes.isEmpty() || starttimes.length() < 4 || endtimes.length() < 4) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent=new Intent(getContext(), selectrollnumber.class);
                    intent.putExtra("course",attancourses);
                    intent.putExtra("year",attanyears);
                    intent.putExtra("division",divisions);
                    intent.putExtra("subject",attansubjects);
                    intent.putExtra("starttime",starttimes);
                    intent.putExtra("endtime",endtimes);
                    startActivity(intent);
                }
            }
        });
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Kiểm tra kết quả từ cửa sổ dialog settings
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    getLocationAndCreateQR();
                } else {
                    Toast.makeText(getContext(), "Bạn cần bật GPS để sử dụng chức năng này.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getLocationAndCreateQR() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        client.checkLocationSettings(builder.build())
                .addOnSuccessListener(locationSettingsResponse -> {
                    // Tất cả cài đặt vị trí đã hợp lệ, và chúng tôi có thể bắt đầu yêu cầu cập nhật vị trí ở đây
                    requestLocationUpdates(locationRequest);
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        // Vị trí cài đặt không hợp lệ, nhưng chúng tôi có thể sửa nó bằng cách hiển thị người dùng một dialog
                        try {
                            // Hiển thị dialog bằng cách gọi startResolutionForResult(),
                            // và kiểm tra kết quả trong phương thức onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(getActivity(),
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Bỏ qua lỗi ở đây
                        }
                    }
                });

    }

    private void requestLocationUpdates(LocationRequest locationRequest) {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                JSONObject attandanceData = new JSONObject();
                try {
                    attandanceData.put("course", attancourses);
                    attandanceData.put("year", attanyears);
                    attandanceData.put("division", divisions);
                    attandanceData.put("subject", attansubjects);
                    attandanceData.put("start_time", starttimes);
                    attandanceData.put("end_time", endtimes);
                    attandanceData.put("latitude", location.getLatitude());
                    attandanceData.put("longitude", location.getLongitude());

                    if (Charset.forName("UTF-8").newEncoder().canEncode(attandanceData.toString())) {

                        TaoMaQR(attandanceData.toString());
                    } else {
                        Toast.makeText(getContext(), "Chuỗi đầu vào chứa ký tự không hợp lệ!", Toast.LENGTH_LONG).show();
                    }
                    fusedLocationClient.removeLocationUpdates(this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error creating QR Code", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == YOUR_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndCreateQR();
            } else {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void TaoMaQR(String maHD) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            // Encode chuỗi JSON với UTF-8
            byte[] utf8Bytes = maHD.getBytes(StandardCharsets.UTF_8);
            String utf8String = new String(utf8Bytes, StandardCharsets.UTF_8);

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(utf8String, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error while creating QR code: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
