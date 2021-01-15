package com.example.sensorgpsservice;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class AccelerometerService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mySensor, mySensor1, mySensor2, mySensor3;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void setContentView(int activity_main) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mySensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mySensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mySensor1, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mySensor2, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mySensor3, SensorManager.SENSOR_DELAY_NORMAL);

        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Constants.acc_x = event.values[0];
            Constants.acc_y = event.values[1];
            Constants.acc_z = event.values[2];
            Constants.time=event.timestamp;

            // Log.d("AccelerometerData",Constants.acc_x +" "+ Constants.acc_y +" "+ Constants.acc_z);
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Constants.gyro_x = event.values[0];
            Constants.gyro_y = event.values[1];
            Constants.gyro_z = event.values[2];
            Constants.time1=event.timestamp;

            // Log.d("GyroscopeData",Constants.gyro_x +" "+ Constants.gyro_y +" "+ Constants.gyro_z);
        }

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            Constants.grav_x = event.values[0];
            Constants.grav_y = event.values[1];
            Constants.grav_z = event.values[2];
            Constants.time2=event.timestamp;

            //Log.d("GravityData",Constants.grav_x +" "+ Constants.grav_y +" "+ Constants.grav_z);
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Constants.mag_x = event.values[0];
            Constants.mag_y = event.values[1];
            Constants.mag_z = event.values[2];
            Constants.time3=event.timestamp;

            // Log.d("MagneticFieldData",Constants.mag_x +" "+ Constants.mag_y +" "+ Constants.mag_z);
        }
    }
}
