package com.example.sensorgpsservice;

import  androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    Handler handler= new Handler();
    Runnable runnable;
    DataBaseHelper getdata = new DataBaseHelper(MainActivity.this);
    int t = 0;
    private EditText mEmail , mPass;
    private TextView STextView,LTextView,Forget;
    private Button loginBtn,stop,signupBtn,send;
    private FirebaseAuth mAuth;
    private int dt=3000;//

    private void init (){
        LocationResult locationResult = null;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            startLocationService();
        }

        startAccelerometerService();
        startOrientationService();
        startLightService();

        DataBaseHelper sensor = new DataBaseHelper(MainActivity.this);


        runnable = new Runnable() {
            @Override
            public void run() {


                sensor.addOrientation();
                sensor.addLight();
                sensor.addAccelerometer();
                sensor.addGPS();
                sensor.addGravity();
                sensor.addGyroscope();
                sensor.addMagnetic();


                Toast.makeText(MainActivity.this, "Executing from Runner and Data inserted", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this,2000);
            }
        };
        handler.postDelayed(runnable,4000);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference delaytime = FirebaseDatabase.getInstance().getReference().child("time");
        delaytime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time=snapshot.getValue().toString();
                //System.out.println(time);
                dt=Integer.parseInt(time);
                // System.out.println(delay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        mEmail = findViewById(R.id.loginEmailAddress);
        mPass = findViewById(R.id.loginPassword2);
        loginBtn = findViewById(R.id.login);
        signupBtn=findViewById(R.id.Signup);
        stop=findViewById(R.id.buttonStopLocationsUpdates);
        STextView = findViewById(R.id.reg);
        LTextView = findViewById(R.id.log);
        Forget=findViewById(R.id.forget);
        send=findViewById(R.id.send);
        mAuth = FirebaseAuth.getInstance();
        Forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail.setText("");
                mPass.setText("");
                mPass.setVisibility(View.GONE);
                loginBtn.setVisibility(View.GONE);
                STextView .setVisibility(View.VISIBLE);
                signupBtn.setVisibility(View.GONE);
                LTextView .setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                Forget.setVisibility(View.GONE);

            }
        });
        LTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail.setText("");

                mPass.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.VISIBLE);
                STextView .setVisibility(View.VISIBLE);
                signupBtn.setVisibility(View.GONE);
                LTextView .setVisibility(View.GONE);
                send.setVisibility(View.GONE);

            }
        });
        STextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail.setText("");
                mPass.setText("");
                mPass.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
                loginBtn.setVisibility(View.GONE);
                STextView .setVisibility(View.GONE);
                signupBtn.setVisibility(View.VISIBLE);
                LTextView .setVisibility(View.VISIBLE);

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPass();
            }
        });



        findViewById(R.id.buttonStopLocationsUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopLocationService();
              /*  Intent intent = new Intent(getApplicationContext(), LocationService.class);
                intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
                stopService(intent);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                handler.removeCallbacks(runnable);*/
                mAuth.signOut();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        if(t!=0){

            upload();}

    }

    public void upload(){
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DatabaseReference delaytime = FirebaseDatabase.getInstance().getReference().child("time");
                delaytime.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String time=snapshot.getValue().toString();
                        //System.out.println(time);
                        //dt=Integer.parseInt(time);
                        // System.out.println(delay);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference fireBase = FirebaseDatabase.getInstance().getReference().child("User");
                DataBaseHelper.sensorData data=getdata.getList();
                String season=getdata.user;
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+6:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
                date.setTimeZone(TimeZone.getTimeZone("GMT+6:00"));

                String localTime = date.format(currentLocalTime);

                fireBase.child(season).child(localTime).child("Sensor Data").setValue(data);


                System.out.println(dt);
            }

        },0,dt);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else {
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null){
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())){
                    if (service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    private void startLocationService(){
        if (!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location Service Started", Toast.LENGTH_SHORT).show();


        }

    }
    private void startAccelerometerService(){

        Intent intent = new Intent(getApplicationContext(), AccelerometerService.class);
        //intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(intent);
        Toast.makeText(this, "Accelerometer Service Started", Toast.LENGTH_SHORT).show();
    }

    private void startOrientationService(){

        Intent intent = new Intent(getApplicationContext(), OrientationService.class);
        //intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(intent);
        Toast.makeText(this, "Orientation Service Started", Toast.LENGTH_SHORT).show();
    }

    private void startLightService(){

        Intent intent = new Intent(getApplicationContext(), LightService.class);
        //intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(intent);
        Toast.makeText(this, "Light Service Started", Toast.LENGTH_SHORT).show();
    }

    private void stopLocationService(){
        if (!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationServices.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location Service Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    private void loginUser(){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty()){
                mAuth.signInWithEmailAndPassword(email , pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(MainActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(SignInActivity.this , MainActivity.class));
                                t=1;
                                upload();
                                FirebaseUser loguser=mAuth.getCurrentUser();
                                getdata.user=loguser.getUid();
                                if(t!=0){stop.setVisibility(View.VISIBLE);
                                    mEmail .setVisibility(View.GONE);
                                    mPass.setVisibility(View.GONE);
                                    loginBtn.setVisibility(View.GONE);
                                    STextView .setVisibility(View.GONE);
                                    send.setVisibility(View.GONE);
                                    Forget.setVisibility(View.GONE);
                                }
                                // finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Login Failed !!", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mPass.setError("Empty Fields Are not Allowed");
            }
        }else if(email.isEmpty()){
            mEmail.setError("Empty Fields Are not Allowed");
        }else{
            mEmail.setError("Pleas Enter Correct Email");
        }
    }
    private void createUser(){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty()){
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(MainActivity.this, "Registered Successfully !!", Toast.LENGTH_SHORT).show();
                                // startActivity(new Intent(MainActivity.this , SignInActivity.class));
                                //finish();
                                loginBtn.setVisibility(View.VISIBLE);
                                STextView .setVisibility(View.VISIBLE);
                                signupBtn.setVisibility(View.GONE);
                                LTextView .setVisibility(View.GONE);
                                mEmail.setText("");
                                mPass.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Registration Error !!", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mPass.setError("Empty Fields Are not Allowed");
            }
        }else if(email.isEmpty()){
            mEmail.setError("Empty Fields Are not Allowed");
        }else{
            mEmail.setError("Pleas Enter Correct Email");
        }
    }



    private void ForgetPass(){
        String email=mEmail.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if(task.getResult().getSignInMethods().isEmpty()){ Toast.makeText(MainActivity.this, "This is not an registered email, you can create new account", Toast.LENGTH_SHORT).show();}
                    else{mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "An email to reset password has been sent ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Network Error !!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    }}
            });
        }
        else if(email.isEmpty()){
            mEmail.setError("Empty Fields Are not Allowed");
        }else{
            mEmail.setError("Pleas Enter Correct Email");
        }
    }
    private void check(){
        String email=mEmail.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.getResult().getSignInMethods().isEmpty()) {
                        createUser();
                    }else {

                        Toast.makeText(MainActivity.this, "An email is already exist", Toast.LENGTH_SHORT).show();}


                }

            });}


        else if(email.isEmpty()){
            mEmail.setError("Empty Fields Are not Allowed");
        }else{
            mEmail.setError("Pleas Enter Correct Email");
        }
    }

}