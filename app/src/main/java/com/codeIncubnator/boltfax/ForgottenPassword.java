package com.codeIncubnator.boltfax;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
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

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ForgottenPassword extends AppCompatActivity {

    private static final String AES = "AES";
    EditText contactnumber_txt, newpassword_txt, confirmnewpassword_txt, otp_txt;
    public TextView timerAndError_txt, getotp_txt, BackToLogin_txt, CreateANewAccount_txt;
    Button verifyChange;
    String originalNumber;
    String originalNumberWithSpace;
    String fullname;
    String verifyPhonenumber;
    String otpid = "";
    String otpverified = "no";
    String countrycode;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference docRef;
    int seconds = 60;
    com.codeIncubnator.boltfax.MyDataBase dataBase;
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

        return m.matches();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        contactnumber_txt = findViewById(R.id.forget_contactnumber);
        otp_txt = findViewById(R.id.forget_otp);
        getotp_txt = findViewById(R.id.forget_getotp);
        timerAndError_txt = findViewById(R.id.forget_otptimerAndError);
        newpassword_txt = findViewById(R.id.forget_newpassword);
        confirmnewpassword_txt = findViewById(R.id.forget_repassword);
        verifyChange = findViewById(R.id.forget_VerifyChange);
        BackToLogin_txt = findViewById(R.id.forgot_BackToLogin);
        CreateANewAccount_txt = findViewById(R.id.forgot_createnewaccount);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dataBase = new com.codeIncubnator.boltfax.MyDataBase(ForgottenPassword.this);

        contactnumber_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (contactnumber_txt.getText().toString().length() != 10) {
                    contactnumber_txt.setError("Invalid Number");
                }
            }
        });

        newpassword_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!isAllPresent(newpassword_txt.getText().toString())) {
                    newpassword_txt.setError("Password Must contain atleast 1 Uppercase, Lowercase, Numeric & Special Character");
                }
            }
        });

        confirmnewpassword_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!newpassword_txt.getText().toString().equals(confirmnewpassword_txt.getText().toString())) {
                    confirmnewpassword_txt.setError("password doesn't match");
                }
            }
        });

        getotp_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection()) {
                    d = new ProgressDialog(ForgottenPassword.this);
                    d.setMessage("Authenticating...");
                    d.setCancelable(false);
                    d.show();
                    contactnumber_txt.setEnabled(false);
                    otp_txt.setEnabled(true);
                    seconds = 60;
                    Timerr();
                    retrieve_data("yes");
                } else {
                    Toast.makeText(ForgottenPassword.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                }


            }
        });


        verifyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (otpverified.equals("no")) {
                    d = new ProgressDialog(ForgottenPassword.this);
                    d.setMessage("Verifying...");
                    d.setCancelable(false);
                    d.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp_txt.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                } else {
                    if (connection()) {
                        d = new ProgressDialog(ForgottenPassword.this);
                        d.setMessage("Changing...");
                        d.setCancelable(false);
                        d.show();
                        changePassword();
                    } else {
                        Toast.makeText(ForgottenPassword.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        BackToLogin_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgottenPassword.this, com.codeIncubnator.boltfax.LoginPage.class));
                finish();
            }
        });
        CreateANewAccount_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgottenPassword.this, com.codeIncubnator.boltfax.SignUpPage.class));
                finish();
            }
        });
    }

    public void Timerr() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (seconds > 0) {
                    seconds--;

                    if (seconds < 10 && seconds >= 1) {
                        timerAndError_txt.setText("00:0" + seconds);
                    } else if (seconds == 0) {
                        timerAndError_txt.setText(" ");
                    } else {
                        timerAndError_txt.setText("00:" + seconds);
                    }

                    handler.postDelayed(this, 1000);
                } else {
                    getotp_txt.setEnabled(true);
                    getotp_txt.setTextColor(getResources().getColor(R.color.blue));
                }
            }
        });
    }

    private void retrieve_data(String otp) {
        originalNumber = contactnumber_txt.getText().toString();

        originalNumberWithSpace = (originalNumber.substring(0, 5) + " " + originalNumber.substring(5));

        DocumentReference docRef = db.collection("users").document(originalNumberWithSpace);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                              @SuppressLint("SetTextI18n")
                                              @Override
                                              public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                                  if (documentSnapshot.exists()) {
                                                      d.dismiss();

                                                      fullname = documentSnapshot.getString("fullname");
                                                      countrycode = documentSnapshot.getString("countrycode");
                                                      verifyPhonenumber = countrycode + originalNumber;
                                                      verifyChange.setEnabled(true);

                                                      d = new ProgressDialog(ForgottenPassword.this);
                                                      d.setMessage("Generating OTP...");
                                                      d.setCancelable(false);
                                                      d.show();
                                                      initiateotp();

                                                  } else {
                                                      contactnumber_txt.setEnabled(true);
                                                      otp_txt.setEnabled(false);
                                                      seconds = 0;
                                                      timerAndError_txt.setText("Account doesn't Exist");
                                                  }

                                              }
                                          }

        ).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Exception e) {
                seconds = 0;
                timerAndError_txt.setText("unable to Retrieve Data");
            }
        });

    }

    private void initiateotp() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(verifyPhonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                otpid = s;
                                d.dismiss();
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(ForgottenPassword.this, "error" + e, Toast.LENGTH_LONG).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mAuth.signOut();
                            if (seconds == 0) {
                                Toast.makeText(ForgottenPassword.this, "Session Timed Out", Toast.LENGTH_SHORT).show();
                            } else {
                                d.dismiss();
                                seconds = 0;
                                timerAndError_txt.setText("OTP Verified");
                                contactnumber_txt.setEnabled(false);
                                otp_txt.setEnabled(false);
                                getotp_txt.setEnabled(false);
                                verifyChange.setText("Change Password");
                                newpassword_txt.setEnabled(true);
                                confirmnewpassword_txt.setEnabled(true);
                                otpverified = "yes";

                            }
                        } else {
                            seconds = 0;
                            timerAndError_txt.setText("OTP Doesn't Match");
                        }

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    public void changePassword() {
        d.dismiss();
        if (isAllPresent(newpassword_txt.getText().toString())) {
            if (newpassword_txt.getText().toString().equals(confirmnewpassword_txt.getText().toString())) {


                docRef = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(originalNumberWithSpace);

                Map<String, Object> city = new HashMap<>();

                try {
                    city.put("password", encrypt(keyval(fullname, originalNumberWithSpace), newpassword_txt.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                docRef.update(city)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                dataBase.update_record(verifyPhonenumber, newpassword_txt.getText().toString());
                                startActivity(new Intent(ForgottenPassword.this, com.codeIncubnator.boltfax.LoginPage.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                timerAndError_txt.setText("Unable to change data");
                            }
                        });

            } else {
                seconds = 0;
                timerAndError_txt.setText("password doesn't match");
            }
        } else {
            seconds = 0;
            timerAndError_txt.setText("Password Must contain atleast 1 Uppercase, Lowercase, Numeric & Special Character");
        }


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

    public String keyval(String name, String mobile) {
        String key = "";

        for (int i = 0; i < name.length(); i++) {
            try {
                key = key + name.charAt(i) + mobile.charAt(i);
            } catch (Exception ignored) {

            }

        }
        return key;
    }

    private String encrypt(String keyvalue, String password) throws Exception {

        SecretKeySpec key = generateKey(keyvalue);

        Cipher c = Cipher.getInstance(AES);

        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encVal = c.doFinal(password.getBytes());

        return Base64.encodeToString(encVal, Base64.DEFAULT);

    }


    private SecretKeySpec generateKey(String keydata) throws Exception {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] bytes = keydata.getBytes("UTF-8");

        digest.update(bytes, 0, bytes.length);

        byte[] key = digest.digest();

        return new SecretKeySpec(key, "AES");
    }


}