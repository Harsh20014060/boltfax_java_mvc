package com.codeIncubnator.boltfax;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.appcompat.app.AppCompatDelegate;

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
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginPage extends AppCompatActivity {
    private static String AES = "AES";
    EditText contactnumber_txt, password_txt, otp_txt;
    TextView forgotten_txt, timer_txt, getotp_txt, error_txt, signup_txt;
    Button sign_btn;
    String originalNumber;
    String originalNumberWithSpace;
    String verifyPhoneNumber;
    String otpid = "";
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    int seconds = 60;
    com.codeIncubnator.boltfax.MyDataBase dataBase;
    String retrieved_pass;
    String username;

    StringBuilder b;
    ProgressDialog d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin_page);

        contactnumber_txt = findViewById(R.id.signin_number);
        password_txt = findViewById(R.id.signin_password);
        forgotten_txt = findViewById(R.id.signin_forgottenpassword);
        otp_txt = findViewById(R.id.signin_otp);
        getotp_txt = findViewById(R.id.signin_getotp);
        timer_txt = findViewById(R.id.signin_timer);
        sign_btn = findViewById(R.id.signin_button);
        error_txt = findViewById(R.id.login_error);
        signup_txt = findViewById(R.id.signin_to_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dataBase = new com.codeIncubnator.boltfax.MyDataBase(LoginPage.this);


        contactnumber_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!contactnumber_txt.isFocused()) {
                    if (contactnumber_txt.getText().toString().length() != 10) {
                        contactnumber_txt.setError("Invalid Number");
                    }
                }

            }
        });

        forgotten_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, com.codeIncubnator.boltfax.ForgottenPassword.class));
                finish();
            }
        });

        getotp_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contactnumber_txt.getText().toString().length() == 10) {
                    if (connection()) {
                        timer_txt.setText("01:00");
                        password_txt.setEnabled(false);
                        contactnumber_txt.setEnabled(false);
                        getotp_txt.setEnabled(false);
                        otp_txt.setEnabled(true);
                        getotp_txt.setTextColor(getResources().getColor(R.color.shadow_grey));
                        seconds = 60;
                        Timerr();
                        retrieve_data("yes");
                        d = new ProgressDialog(LoginPage.this);
                        d.setMessage("Authenticating...");
                        d.setCancelable(false);
                        d.show();
                    } else {
                        Toast.makeText(LoginPage.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    contactnumber_txt.setError("Invalid Number");
                }

            }
        });

        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, com.codeIncubnator.boltfax.SignUpPage.class));
                finish();
            }
        });


        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                d = new ProgressDialog(LoginPage.this);
                d.setMessage("Authenticating...");
                d.setCancelable(false);
                d.show();
                error_txt.setText("");
                if (otpid.isEmpty()) {
                    if (!password_txt.getText().toString().isEmpty() && contactnumber_txt.getText().toString().length() == 10) {
                        retrieve_data("no");
                    } else {
                        error_txt.setText("Required Field");
                        d.dismiss();
                    }

                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp_txt.getText().toString());
                    signInWithPhoneAuthCredential(credential);
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
                        timer_txt.setText("00:0" + String.valueOf(seconds));
                    } else if (seconds == 0) {
                        timer_txt.setText(" ");
                    } else {
                        timer_txt.setText("00:" + String.valueOf(seconds));
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
                                              @Override
                                              public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                                  if (documentSnapshot.exists()) {

                                                      retrieved_pass = documentSnapshot.getString("password");
                                                      username = documentSnapshot.getString("fullname");


                                                      if (otp.equals("yes")) {
                                                          String countrycode = documentSnapshot.getString("countrycode");
                                                          verifyPhoneNumber = countrycode + originalNumber;
                                                          initiateotp();
                                                      } else {
                                                          String pass = "";

                                                          try {
                                                              pass = decrypt(keyval(username, originalNumberWithSpace), retrieved_pass);

                                                          } catch (Exception e) {
                                                              d = new ProgressDialog(LoginPage.this);
                                                              d.dismiss();
                                                              Toast.makeText(LoginPage.this, "Contact to BoltFax Developer Team", Toast.LENGTH_SHORT).show();
                                                          }

                                                          if (password_txt.getText().toString().equals(pass)) {

                                                              dataBase.add_data(username, documentSnapshot.getId().toString(), retrieved_pass);
                                                              d.dismiss();
                                                              startActivity(new Intent(LoginPage.this, com.codeIncubnator.boltfax.MainActivity.class));
                                                              finish();
                                                          } else {
                                                              if (connection()) {
                                                                  d.dismiss();
                                                                  error_txt.setText("Password doesn't match");
                                                              } else {
                                                                  d.dismiss();
                                                                  Toast.makeText(LoginPage.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                                                              }


                                                          }

                                                      }


                                                  } else {

                                                      d.dismiss();
                                                      otp_txt.setEnabled(false);
                                                      timer_txt.setText("");
                                                      seconds = 0;
                                                      contactnumber_txt.setEnabled(true);
                                                      password_txt.setEnabled(true);
                                                      error_txt.setText("Account doesn't Exist");
                                                  }

                                              }
                                          }

        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                error_txt.setText("unable to Retrieve Data");
            }
        });

    }

    private void initiateotp() {
        d.dismiss();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(verifyPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(LoginPage.this, "error" + e, Toast.LENGTH_LONG).show();
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
                            mAuth.signOut();
                            if (seconds == 0) {
                                contactnumber_txt.setEnabled(true);
                                password_txt.setEnabled(true);
                                d.dismiss();
                                Toast.makeText(LoginPage.this, "Session Timed Out", Toast.LENGTH_SHORT).show();
                            } else {

                                Cursor cursor = dataBase.getAllInfo();

                                if (cursor.getCount() == 0) {

                                    dataBase.add_data(username, verifyPhoneNumber, retrieved_pass);
                                }
                                d.dismiss();
                                startActivity(new Intent(LoginPage.this, com.codeIncubnator.boltfax.MainActivity.class));
                                finish();


                            }
                        } else {

                            error_txt.setText("OTP Doesn't Match");
                        }

                    }
                });
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
            } catch (Exception e) {

            }

        }
        return key;
    }

    private String decrypt(String keyvalue, String encryptedpass) throws Exception {

        SecretKeySpec key = generateKey(keyvalue);

        Cipher c = Cipher.getInstance(AES);

        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedvalue = Base64.decode(encryptedpass, Base64.DEFAULT);
        byte[] decval = c.doFinal(decodedvalue);

        String decryptedValue = new String(decval);
        return decryptedValue;

    }


    private SecretKeySpec generateKey(String keydata) throws Exception {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] bytes = keydata.getBytes("UTF-8");

        digest.update(bytes, 0, bytes.length);

        byte[] key = digest.digest();

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        return secretKeySpec;
    }


}
