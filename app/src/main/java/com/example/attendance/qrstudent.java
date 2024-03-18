package com.example.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.attendance.beans.QRCodeResultModel;
import com.example.attendance.fragment.notification;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrstudent extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private Result lastScanResult;


    public static final int YOUR_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qrstudent, container, false);
        scannerView = view.findViewById(R.id.zxing_scanner);
        getActivity().setTitle("QR Code");
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }
    private void attemptAttendance(String path, QRCodeResultModel model) {
        // Get a reference to our posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // if data exists, return or make a toast
                    Toast.makeText(getActivity(), "Bạn đã điểm danh lớp này rồi!", Toast.LENGTH_SHORT).show();
                    switchToNotificationFragment();
                    return;
                }

                // Write your data to the database if it's a new data
                ref.child("start_time").setValue(model.getStartTime());
                ref.child("end_time").setValue(model.getEndTime());
                Toast.makeText(getActivity(), "Điểm danh thành công", Toast.LENGTH_SHORT).show();
                switchToNotificationFragment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleResult(Result rawResult) {
        lastScanResult = rawResult;

        String jsonResult = null;

        try {
            // Giả sử rằng rawResult.getText() đã trả về dữ liệu JSON mà bạn cần
            jsonResult = rawResult.getText();

            checkPermissionsAndHandleLocation(jsonResult);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), "Lỗi khi giải đọc chuỗi QR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return; // Return from the function if there's an error
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, YOUR_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            getLocationAndAttemptAttendance(jsonResult);
        }
        QRCodeResultModel model = new Gson().fromJson(jsonResult, QRCodeResultModel.class);
        // Get rollnumber
        String mssv = ((global) getActivity().getApplication()).getRollnumber();
        // Get current datetime string
        String date = new SimpleDateFormat("yyyyMMddHH:mm").format(new Date());

        // Generate the path
        String path = "Attandance/" + model.getCourse() + "/" + model.getYear() + "/"
                + model.getDivision() + "/" + mssv + "/" + model.getSubject() + "/" + date;

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double currentLatitude = location.getLatitude();
                        double currentLongitude = location.getLongitude();

                        double qrLatitude = model.getLatitude();
                        double qrLongitude = model.getLongitude();

                        float [] results = new float[1];
                        Location.distanceBetween(currentLatitude, currentLongitude, qrLatitude, qrLongitude, results);
                        float distanceInMeters = results[0];

                        if (distanceInMeters > 1) {
                            Toast.makeText(getActivity(), "Bạn quá xa vị trí điểm danh!", Toast.LENGTH_SHORT).show();
                            switchToNotificationFragment();
                        } else {
                            // Call attemptAttendance() if within 20 meters
                            attemptAttendance(path, model);
                        }
                    } else {
                        // Handle the case where location is null
                        Toast.makeText(getActivity(), "Không thể lấy vị trí hiện tại của bạn. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the case where location can't be retrieved
                    Toast.makeText(getActivity(), "Đã xảy ra lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkPermissionsAndHandleLocation(String jsonResult) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, YOUR_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            getLocationAndAttemptAttendance(jsonResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == YOUR_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Bắt đầu lấy vị trí khi đã có quyền
                // Sử dụng lastScanResult lấy từ cuộc gọi cuối cùng của handleResult
                if (lastScanResult != null) {
                    String base64EncodedString = lastScanResult.getText();
                    try {
                        // Giải mã từ Base64 về mảng byte.
                        byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
                        // Tạo chuỗi từ mảng byte bằng cách sử dụng bảng mã UTF-8
                        String decodedString = new String(decodedBytes, "UTF-8");
                        // Xử lý chuỗi JSON đã giải mã
                        getLocationAndAttemptAttendance(decodedString);
                    } catch (IllegalArgumentException | UnsupportedEncodingException e) {
                        // Xử lý lỗi giải mã Base64 hoặc lỗi không hỗ trợ mã hóa chuỗi
                        Toast.makeText(getActivity(), "Lỗi khi giải mã chuỗi QR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } else {
                // Permission denied
                Toast.makeText(getActivity(), "Permission denied to access your location", Toast.LENGTH_SHORT).show();
            }
        }
        // Handle other permission requests if there are any
    }
    private void getLocationAndAttemptAttendance(String jsonResult) {
        // Xử lý chuỗi JSON để lấy thông tin để điểm danh.
        // Parse the JSON to QRCodeResultModel using Gson or another JSON library.
        QRCodeResultModel model = new Gson().fromJson(jsonResult, QRCodeResultModel.class);
        // Và sau đó là code để lấy vị trí và tiếp tục với logic điểm danh...
        // Note that you should also move the relevant parts from handleResult to here.
    }

    private void switchToNotificationFragment() {
        notification notificationFragment = new notification();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace the current Fragment with the new one
        transaction.replace(R.id.zxing_scanner, notificationFragment);

        // Add transaction to the back stack and commit
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
