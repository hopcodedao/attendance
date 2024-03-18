package com.example.attendance;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.activity.login;
import com.example.attendance.beans.addteacherdatbase;
import com.example.attendance.beans.profiledatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

public class setpassword extends AppCompatActivity {
    private TextView timer,phonenumber;
    private EditText otps,password1,password2;
    private FirebaseAuth mAuth;
    private DatabaseReference notice;
    private DatabaseReference notice1;
    private StorageReference mStorageRef;
    private Uri file;
    private ProgressBar pbset;
    String msg,sendotp1;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword);
        phonenumber = findViewById(R.id.phonenumber);
        timer = findViewById(R.id.otptimer);
        otps = findViewById(R.id.otp);
        password1 = findViewById(R.id.passsword1);
        password2 = findViewById(R.id.passsword2);
        final Button otpverify = findViewById(R.id.otpsubmit);
        final Button setpassword = findViewById(R.id.submit);

        final Button sendotp = findViewById(R.id.resend);
        pbset=findViewById(R.id.progressaddTeacer);
        pbset.setVisibility(View.GONE);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        notice1 = FirebaseDatabase.getInstance().getReference("Profile");
        ActionBar actionBar= getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0052c5"));
        actionBar.setBackgroundDrawable(colorDrawable);

        password1.setEnabled(false);
        password2.setEnabled(false);
        setpassword.setEnabled(false);
        otps.setEnabled(true);
        sendotp.setEnabled(true);
        otpverify.setEnabled(true);

        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        final String Fullname = intent.getStringExtra("fullname");
        final String degree = intent.getStringExtra("degree");
        final String birthofdate = intent.getStringExtra("birthofdate");
        final String numbers = intent.getStringExtra("number");
        final String addresss = intent.getStringExtra("address");
        final String facultys = intent.getStringExtra("faculty");
        String image = intent.getStringExtra("uri");

        if(image!= null) {
            file=Uri.parse(image);
        }else {
            Log.d("random","NUll");
        }
        Log.d("random","file uri setpassword"+image);

        phonenumber.setText(numbers);

        new CountDownTimer(5*60000, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                timer.setText(""+String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            @SuppressLint("SetTextI18n")
            public void onFinish() {
                timer.setText("00:00");
            }
        }.start();

        sendsms(numbers);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendsms(numbers);
            }
        });
        otpverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pppp","otp"+sendotp1+"----------"+otps.getText().toString().trim());
                pbset.setVisibility(View.VISIBLE);
                if (sendotp1.equals(otps.getText().toString().trim())) {
                    password1.setEnabled(true);
                    password2.setEnabled(true);
                    otps.setEnabled(false);
                    setpassword.setEnabled(true);
                    sendotp.setEnabled(false);
                    otpverify.setEnabled(false);
                    pbset.setVisibility(View.GONE);
                } else {
                    otps.setError("Vui lòng nhập OTP hợp lệ!");
                    otps.requestFocus();
                    password1.setEnabled(false);
                    password2.setEnabled(false);
                    setpassword.setEnabled(false);
                    otps.setEnabled(true);
                    sendotp.setEnabled(true);
                    otpverify.setEnabled(true);
                    pbset.setVisibility(View.GONE);
                }
            }
        });
        setpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbset.setVisibility(View.VISIBLE);
                if(password1.getText().length()<6){
                    password1.setError("Mật khẩu quá ngắn!");
                    password1.requestFocus();
                    pbset.setVisibility(View.GONE);
                }if (password1.getText().toString().equals(password2.getText().toString())) {
                    mAuth = FirebaseAuth.getInstance();
                    notice = FirebaseDatabase.getInstance().getReference("Teacher");
                    final String password = password2.getText().toString().trim();
                    assert email != null;
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(setpassword.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            String uid = user.getUid();

                                            StorageReference riversRef = mStorageRef.child("Teacher/"+ facultys + "/" + uid + ".jpg");
                                            StorageReference riversRef1 = mStorageRef.child("Profile/"+uid+".jpg");
                                            riversRef.putFile(file);
                                            riversRef1.putFile(file);

                                            profiledatabase profiledatabase=new profiledatabase(Fullname, null, addresss, email, birthofdate, null, null, facultys, numbers, uid, null, password, "Teacher", null, degree);
                                            notice1.child(uid).setValue(profiledatabase);
                                            addteacherdatbase addTeacherdatabase = new addteacherdatbase(Fullname, email, facultys, degree, birthofdate, numbers, uid, addresss);
                                            notice.child(uid).setValue(addTeacherdatabase);
                                            Toast.makeText(setpassword.this, "Thành công!", Toast.LENGTH_LONG).show();
                                            pbset.setVisibility(View.GONE);
                                            password2.setText("");
                                            password1.setText("");
                                            mAuth.signOut();
                                            Intent intent=new Intent(setpassword.this, login.class);
                                            intent.putExtra("action","autologin");
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(setpassword.this, "Error", Toast.LENGTH_LONG).show();
                                            pbset.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            });
                } else {
                    password2.setError("Mật khẩu không giống nhau!");
                    password2.requestFocus();
                    pbset.setVisibility(View.GONE);
                }
            }
        });
    }
    private void sendSMS(String numbers, String msg) {
        Sms4India sms4India = new Sms4India(numbers,msg);
        sms4India.doInBackground();
    }
    public void sendsms(String numbers){
        pbset.setVisibility(View.VISIBLE);
        int random_int = (int)(Math.random() * (9999 - 1000 + 1) + 1000);
        Log.d("random","random---"+random_int);
        sendotp1=Integer.toString(random_int);
        msg = "Attendance mobile OTP:"+Integer.toString(random_int);

        // Hiển thị OTP trên AlertDialog
        new AlertDialog.Builder(setpassword.this)
                .setTitle("Xác minh OTP")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        try {
            android.telephony.SmsManager mSmsManager = android.telephony.SmsManager.getDefault();
            mSmsManager.sendTextMessage(numbers, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "SMS đã gửi thành công!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS gửi không thành công!" + e, Toast.LENGTH_LONG).show();
        }

        pbset.setVisibility(View.GONE);
    }
}
