package com.application.boltfax;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SignUpPage extends AppCompatActivity {
    private static String AES = "AES";
    CountryCodePicker cpp;
    CheckBox termAndCondition_status;
    EditText fullname_txt, mobilenumber_txt, password_txt, repassword_txt, otp_txt;
    TextView getotp_txt, otp_timmer_txt, signup, terms_and_conditions;
    Button signup_btn;
    String verifyPhoneNumber;
    String originalPhoneNumber;
    int seconds = 60;
    FirebaseAuth mAuth;

    FirebaseFirestore db;
    boolean authenctication_proceed = false;
    String otpid;
    com.application.boltfax.MyDataBase dataBase;
    ProgressDialog d;

    public static boolean isAllPresent(String str) {

        String regex = "^(?=.*[a-z])(?=."
                + "*[A-Z])(?=.*\\d)(?=."
                + "*[0-9])(?=.*\\d)"
                + "(?=.*[-+_!@#$%^&*., ?]).+$";

        Pattern p = Pattern.compile(regex);

        if (str == null) {

            return false;
        }

        Matcher m = p.matcher(str);

        if (m.matches() && str.length() >= 8) {
            return true;
        }

        return false;


    }

//    Timer function for OTP duration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

//        get id's of all the UI Components


        fullname_txt = findViewById(R.id.fullname);
        cpp = findViewById(R.id.ccp);
        mobilenumber_txt = findViewById(R.id.mobile_number);
        password_txt = findViewById(R.id.signup_password);
        repassword_txt = findViewById(R.id.signup_repassword);
        otp_txt = findViewById(R.id.otp);
        termAndCondition_status = findViewById(R.id.termsandcondition_checked);
        otp_timmer_txt = findViewById(R.id.otp_timmer);
        getotp_txt = findViewById(R.id.get_otp);
        terms_and_conditions = findViewById(R.id.accept_termsandcondition);
        signup_btn = findViewById(R.id.sign_up);
        signup = findViewById(R.id.signuptext);

        //        Firebase Authentication and Firestore objects
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dataBase = new com.application.boltfax.MyDataBase(SignUpPage.this);

//        attach CountryCodePicker With Mobile number
        cpp.registerCarrierNumberEditText(mobilenumber_txt);

        try {
            Intent restoration = getIntent();


            fullname_txt.setText(restoration.getStringExtra("name"));
            mobilenumber_txt.setText(restoration.getStringExtra("contactno"));


        } catch (Exception ignored) {

        }

        fullname_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String username = fullname_txt.getText().toString().trim();
                if (fullname_txt.isFocused() == false) {
                    if (username.length() > 0) {
                        if ((username.matches("[A-Za-z ]+")) == false) {
                            fullname_txt.setError("Invalid Name");
                        }
                    } else {
                        fullname_txt.setError("Required Feild");
                    }
                }
            }
        });
        terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tandc = new Intent(SignUpPage.this, PrivacyAndPolicy.class);

                tandc.putExtra("name", fullname_txt.getText().toString().trim());
                tandc.putExtra("contactno", mobilenumber_txt.getText().toString().trim());
                startActivity(tandc);
            }
        });

        mobilenumber_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String contactno = mobilenumber_txt.getText().toString().trim();
                if (mobilenumber_txt.isFocused() == false) {
                    if (contactno.length() == 11) {
                        if (contactno.contains(" ") && contactno.matches("[0-9 ]+")) {

                        } else {
                            mobilenumber_txt.setError("Invalid Mobile Number");
                        }
                    } else {
                        mobilenumber_txt.setError("Invalid length");
                    }


                }
            }
        });

        password_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String password = password_txt.getText().toString().trim();
                if (!isAllPresent(password)) {
                    password_txt.setError("Password Must contain atleast 1 Uppercase, Lowercase, Numeric & Special Character");
                }
            }
        });

        repassword_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (repassword_txt.isFocused() == false) {
                    String repassword = repassword_txt.getText().toString().trim();
                    if (!password_txt.getText().toString().trim().equals(repassword)) {
                        repassword_txt.setError("Password doesn't match");
                    }
                }
            }
        });


//        Get OTP
        getotp_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                verifyPhoneNumber = cpp.getFullNumberWithPlus().replace("", "");


                authenctication_proceed = validation();
                if (authenctication_proceed) {
                    if (connection()) {

                        existence(originalPhoneNumber);


                    } else {
                        Toast.makeText(SignUpPage.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

//       sign up button for moving to next Activity
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (otp_txt.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpPage.this, "required field", Toast.LENGTH_LONG).show();
                } else if (otp_txt.getText().toString().length() != 6) {
                    Toast.makeText(SignUpPage.this, "invalid otp", Toast.LENGTH_SHORT).show();
                } else {
                    authenctication_proceed = validation();
                    if (authenctication_proceed) {
                        if (!otp_txt.getText().toString().isEmpty() && otp_txt.getText().toString().length() == 6) {
                            d = new ProgressDialog(SignUpPage.this);
                            d.setMessage("Creating Account...");
                            d.setCancelable(false);
                            d.show();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp_txt.getText().toString());
                            signInWithPhoneAuthCredential(credential);
                        } else {
                            Toast.makeText(SignUpPage.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

            }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpPage.this, LoginPage.class));
                finish();
            }
        });
    }

    public void create_account() {
        String contactno = mobilenumber_txt.getText().toString().trim();

        if (contactno.contains(" ") && contactno.matches("[0-9 ]+")) {

            d = new ProgressDialog(SignUpPage.this);
            d.setMessage("Creating Account...");
            d.setCancelable(false);
            d.show();
            fullname_txt.setEnabled(false);
            mobilenumber_txt.setEnabled(false);
            password_txt.setEnabled(false);
            repassword_txt.setEnabled(false);
            cpp.setEnabled(false);
            initiateotp();
            otp_timmer_txt.setText("01:00");
            seconds = 60;
            Timerr();
            signup_btn.setEnabled(true);
            getotp_txt.setEnabled(false);
            otp_txt.setEnabled(true);
            getotp_txt.setTextColor(getResources().getColor(R.color.shadow_grey));


        } else {
            mobilenumber_txt.setError("Invalid Mobile Number");
        }
    }

//    Function for insertion in FireStore

    //    Function where OTP generate and authenticate the Phone number
    private void initiateotp() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(verifyPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                d.dismiss();
                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                d.dismiss();

                                if (e.toString().contains("com.google.firebase.FirebaseTooManyRequestsException")) {
                                    Toast.makeText(SignUpPage.this, "You request many OTP at a time that's why Firebase block your device temporarily. Wait for few hour or you may try next day", Toast.LENGTH_LONG).show();
                                    //com.google.firebase.FirebaseTooManyRequestsException: We have blocked all requests from this device due to unusual activity. Try again later.
                                } else {
                                    Toast.makeText(SignUpPage.this, "error" + e, Toast.LENGTH_LONG).show();
                                }


                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (seconds == 0) {
                                mAuth.signOut();
                                Toast.makeText(SignUpPage.this, "Session Timed Out", Toast.LENGTH_SHORT).show();
                            } else {
                                if (connection()) {
                                    insert_data();
                                } else {
                                    Toast.makeText(SignUpPage.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                                }


                            }
                        } else {
                            d.dismiss();

                            Toast.makeText(SignUpPage.this, "error" + task.getException(), Toast.LENGTH_LONG).show();


                        }

                    }
                });
    }

    public void Timerr() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (seconds > 0) {
                    seconds--;
                    if (seconds < 10 && seconds >= 1) {
                        otp_timmer_txt.setText("00:0" + String.valueOf(seconds));
                    } else if (seconds == 0) {
                        otp_timmer_txt.setText(" ");
                        signup_btn.setEnabled(false);
                        fullname_txt.setEnabled(true);
                        cpp.setEnabled(true);
                        mobilenumber_txt.setEnabled(true);
                        password_txt.setEnabled(true);
                        repassword_txt.setEnabled(true);
                        otp_txt.setEnabled(false);
                    } else {
                        otp_timmer_txt.setText("00:" + String.valueOf(seconds));
                    }

                    handler.postDelayed(this, 1000);
                } else {
                    getotp_txt.setEnabled(true);
                    getotp_txt.setTextColor(getResources().getColor(R.color.blue));
                }
            }
        });
    }

    public void insert_data() {


        Map<String, Object> city = new HashMap<>();
        city.put("countrycode", "+" + cpp.getSelectedCountryCode().trim());

        try {
            String encrypted_password = encrypt(keyval(fullname_txt.getText().toString(), originalPhoneNumber), password_txt.getText().toString());
            city.put("password", encrypted_password);
        } catch (Exception e) {
            city.put("password", password_txt.getText().toString());
        }
        city.put("fullname", fullname_txt.getText().toString());

        db.collection("users").document(originalPhoneNumber)
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataBase.add_data(fullname_txt.getText().toString(), verifyPhoneNumber, repassword_txt.getText().toString());
                        d.dismiss();
                        startActivity(new Intent(SignUpPage.this, com.application.boltfax.MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        return;
                    }
                });

    }

    public boolean validation() {


        String username = fullname_txt.getText().toString().trim();
        String contactno = mobilenumber_txt.getText().toString().trim();
        String password = password_txt.getText().toString().trim();
        String repassword = repassword_txt.getText().toString().trim();

        if (username.matches("[A-Za-z ]+") && username.length() > 0) {
            if (contactno.length() == 10 || contactno.length() == 11 && contactno.contains(" ") && contactno.matches("[0-9 ]+")) {
                if (isAllPresent(password)) {
                    if (password.equals(repassword)) {
                        if (termAndCondition_status.isChecked() == true) {
                            originalPhoneNumber = contactno;
                            return true;
                        }

                    }
                }

            }

        }
        return false;
    }

    public String keyval(String name, String mobile) {
        String key = "";


        for (int i = 0; i < name.length(); i++) {
            try {
                key = key + name.charAt(i) + mobile.charAt(i);
            } catch (Exception e) {

            }

        }
        return key;
    }

    //    name+mobilenumber = keyvalue,,  password=by user
//    n+m+a+o+m+b+e+i+l+e
    private String encrypt(String keyvalue, String password) throws Exception {

        SecretKeySpec key = generateKey(keyvalue);

        Cipher c = Cipher.getInstance(AES);

        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encVal = c.doFinal(password.getBytes());

        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;

    }


    private SecretKeySpec generateKey(String keydata) throws Exception {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] bytes = keydata.getBytes("UTF-8");

        digest.update(bytes, 0, bytes.length);

        byte[] key = digest.digest();

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        return secretKeySpec;
    }

    public boolean connection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }

    public void existence(String number) {
        d = new ProgressDialog(SignUpPage.this);
        d.setMessage("Check Existence...");
        d.setCancelable(false);
        d.show();

        DocumentReference docRef = db.collection("users").document(number);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    d.dismiss();

                    Toast.makeText(SignUpPage.this, "User Already Exists", Toast.LENGTH_SHORT).show();


                } else {
                    d.dismiss();
                    create_account();
                }

            }
        });

    }

}


