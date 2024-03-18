package com.example.attendance.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance.R;
import com.example.attendance.setpassword;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class addteacher extends Fragment {

    private ImageView profile;
    private EditText fullname,degree,birthofdate,number,email,address;
    private Spinner faculty;
    private static final int CAMERA_PIC_REQUEST = 1;
    Uri imagefileteacher;

    private Uri imagefile;
    private FaceDetector faceDetector; // Thêm FaceDetector
    DatePickerDialog picker;

    private static final int GALLERY_REQUEST_CODE = 2; // Define as class member
    public addteacher() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.addteacher,container,false);

        profile=layout.findViewById(R.id.profile);

        getActivity().setTitle("Thêm giảng viên");

        fullname=layout.findViewById(R.id.fullname);
        degree=layout.findViewById(R.id.degree);
        birthofdate=layout.findViewById(R.id.birtofdate);
        number=layout.findViewById(R.id.number);
        email=layout.findViewById(R.id.email);
        address=layout.findViewById(R.id.addresss);
        faculty = layout.findViewById(R.id.faculty);
        Button next = layout.findViewById(R.id.submit);



        birthofdate.setInputType(InputType.TYPE_NULL);
        birthofdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthofdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
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
        faculty.setAdapter(dataAdapter);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[]{"Camera", "Gallery"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // User chose 'Camera', launch the camera intent
                                        openCamera();
                                        break;
                                    case 1:
                                        // User chose 'Gallery', launch the gallery intent
                                        openGallery();
                                        break;
                                }
                            }
                        });
                builder.show();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullnames = fullname.getText().toString();
                final String emai = email.getText().toString();
                final String degre = degree.getText().toString();
                final String birthofdates=birthofdate.getText().toString();
                final String numbers=number.getText().toString();
                final String addresss=address.getText().toString();
                final String facultys =faculty.getSelectedItem().toString();

                if(imagefileteacher == null) {
                    Toast.makeText(getContext(),"First enter face",Toast.LENGTH_LONG).show();
                }
                if(emai.isEmpty()){
                    email.setError("Please enter email id");
                    email.requestFocus();
                } else if(fullnames.isEmpty()){
                    fullname.setError("Please enter FullName id");
                    fullname.requestFocus();
                } else if(degre.isEmpty()){
                    degree.setError("Please enter FullName id");
                    degree.requestFocus();
                } else if(birthofdates.isEmpty()){
                    birthofdate.setError("Please enter email id");
                    birthofdate.requestFocus();
                } else if(numbers.isEmpty()){
                    number.setError("Please enter email id");
                    number.requestFocus();
                } else if(addresss.isEmpty()){
                    address.setError("Please enter email id");
                    address.requestFocus();
                } else if(emai.length()<10){
                    email.setError("Please enter proper email id");
                    email.requestFocus();
                } else if(fullnames.length()<9){
                    fullname.setError("Please enter Full Name");
                    fullname.requestFocus();
                } else if(numbers.length()<10){
                    number.setError("number lenght will be 10");
                    number.requestFocus();
                } else if(addresss.length()<7){
                    address.setError("Please enter proper address");
                    address.requestFocus();
                } else if(!isValid(emai)){
                    email.setError("Email not valid");
                    email.requestFocus();
                    Toast.makeText(getContext(),"Email are not valid",Toast.LENGTH_LONG).show();
                } else  if(!(emai.isEmpty() && fullnames.isEmpty()&&birthofdates.isEmpty()&&numbers.isEmpty()&&addresss.isEmpty())){
                    Intent intent=new Intent(getContext(), setpassword.class);
                    intent.putExtra("fullname",fullnames);
                    intent.putExtra("email",emai);
                    intent.putExtra("degree",degre);
                    intent.putExtra("birthofdate",birthofdates);
                    intent.putExtra("number",numbers);
                    intent.putExtra("address",addresss);
                    intent.putExtra("faculty",facultys);
                    intent.putExtra("uri",imagefileteacher.toString());
                    startActivity(intent);
                    fullname.setText("");
                    degree.setText("");
                    birthofdate.setText("");
                    number.setText("");
                    email.setText("");
                    address.setText("");
                } else {
                    Toast.makeText(getContext(),"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ProgressDialog detectionProgressDialog = new ProgressDialog(getContext());
        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            // Create a file to save the image
            imagefileteacher = saveImageToExternalStorage(image); // save image and get uri
            // Update ImageView with the Bitmap
            profile.setImageBitmap(image);
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Update the profile ImageView with the chosen image
            profile.setImageURI(selectedImageUri);
            // Save the gallery image URI
            imagefileteacher = selectedImageUri;
        }
    }

    private Uri saveImageToExternalStorage(Bitmap image) {
        // Create a file in the Pictures directory
        String imageName = "teacher_profile_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
        try {
            if (storageDir.createNewFile()) {
                FileOutputStream outputStream = new FileOutputStream(storageDir);
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                // Notify gallery about the new file
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(storageDir);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
                return contentUri;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
}
