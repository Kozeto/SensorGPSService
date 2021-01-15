package com.example.sensorgpsservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String SENSORGPS_TABLE = "SENSORGPS_TABLE";
    public static final String SENSORGPS_LATITUDE = "SENSORGPS_LATITUDE";
    public static final String SENSORGPS_LONGITUDE = "SENSORGPS_LONGITUDE";

    public static final String SENSORACCELEROMETER_TABLE = "SENSORACCELEROMETER_TABLE";
    public static final String ACCELEROMETER_X = "ACCELEROMETER_X";
    public static final String ACCELEROMETER_Y = "ACCELEROMETER_Y";
    public static final String ACCELEROMETER_Z = "ACCELEROMETER_Z";
    public static final String Time="Time";

    public static final String SENSORGYROSCOPE_TABLE = "SENSORGYROSCOPE_TABLE";
    public static final String GYROSCOPE_X = "GYROSCOPE_X";
    public static final String GYROSCOPE_Y = "GYROSCOPE_Y";
    public static final String GYROSCOPE_Z = "GYROSCOPE_Z";
    public static final String Time1="Time";

    public static final String SENSORGRAVITY_TABLE = "SENSORGRAVITY_TABLE";
    public static final String GRAVITY_X = "GRAVITY_X";
    public static final String GRAVITY_Y = "GRAVITY_Y";
    public static final String GRAVITY_Z = "GRAVITY_Z";
    public static final String Time2="Time";

    public static final String SENSORMAGNETIC_TABLE = "SENSORMAGNETIC_TABLE";
    public static final String MAGNETIC_X = "MAGNETIC_X";
    public static final String MAGNETIC_Y = "MAGNETIC_Y";
    public static final String MAGNETIC_Z = "MAGNETIC_Z";
    public static final String Time3="Time";

    public static final String SENSORORIENTATION_TABLE = "SENSORORIENTATION_TABLE";
    public static final String ORIENTATION_AZIMUTH = "ORIENTATION_AZIMUTH";
    public static final String ORIENTATION_PITCH = "ORIENTATION_PITCH";
    public static final String ORIENTATION_ROLL = "ORIENTATION_ROLL";

    public static final String SENSORLIGHT_TABLE = "SENSORLIGHT_TABLE";
    public static final String LIGHT_V = "LIGHT_V";
    public static String user;

    Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "sensor.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatementLight = "CREATE TABLE " + SENSORLIGHT_TABLE + " (" + LIGHT_V + " REAL)";
        String createTableStatementOrientation = "CREATE TABLE " + SENSORORIENTATION_TABLE + " (" + ORIENTATION_AZIMUTH + " REAL," + ORIENTATION_PITCH + " REAL," + ORIENTATION_ROLL + " REAL)";
        String createTableStatementAccelerometer = "CREATE TABLE " + SENSORACCELEROMETER_TABLE + " (" + Time + " REAL," + ACCELEROMETER_X + " REAL," + ACCELEROMETER_Y + " REAL ," + ACCELEROMETER_Z + " REAL )";
        String createTableStatementGyroscope = "CREATE TABLE " + SENSORGYROSCOPE_TABLE + " (" + Time1 + " REAL," + GYROSCOPE_X + " REAL," + GYROSCOPE_Y + " REAL ," + GYROSCOPE_Z + " REAL )";
        String createTableStatementGravity = "CREATE TABLE " + SENSORGRAVITY_TABLE + " (" + Time2 + " REAL," + GRAVITY_X + " REAL," + GRAVITY_Y + " REAL ," + GRAVITY_Z + " REAL )";
        String createTableStatementMagnetic = "CREATE TABLE " + SENSORMAGNETIC_TABLE + " (" + Time3 + " REAL," + MAGNETIC_X + " REAL," + MAGNETIC_Y + " REAL ," + MAGNETIC_Z + " REAL )";
        String createTableStatementGPS = "CREATE TABLE " + SENSORGPS_TABLE + " (" + SENSORGPS_LATITUDE + " REAL , " + SENSORGPS_LONGITUDE + " REAL )";

        db.execSQL(createTableStatementOrientation);
        db.execSQL(createTableStatementLight);
        db.execSQL(createTableStatementAccelerometer);
        db.execSQL(createTableStatementGyroscope);
        db.execSQL(createTableStatementGravity);
        db.execSQL(createTableStatementMagnetic);
        db.execSQL(createTableStatementGPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addGPS(){
        SQLiteDatabase dbGPS = this.getWritableDatabase();

        ContentValues contentValuesGPS = new ContentValues();

        contentValuesGPS.put(SENSORGPS_LATITUDE, Constants.latitude);
        contentValuesGPS.put(SENSORGPS_LONGITUDE, Constants.longitude);

        long insertGPS = dbGPS.insert(SENSORGPS_TABLE,null,contentValuesGPS);

        if (insertGPS == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addAccelerometer(){
        SQLiteDatabase dbAccelerometer = this.getWritableDatabase();

        ContentValues contentValuesAccelerometer = new ContentValues();

        contentValuesAccelerometer.put(ACCELEROMETER_X, Constants.acc_x);
        contentValuesAccelerometer.put(ACCELEROMETER_Y, Constants.acc_y);
        contentValuesAccelerometer.put(ACCELEROMETER_Z, Constants.acc_z);
        contentValuesAccelerometer.put(Time, Constants.time);

        long insertAccelerometer = dbAccelerometer.insert(SENSORACCELEROMETER_TABLE,null,contentValuesAccelerometer);

        if (insertAccelerometer == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addGyroscope(){
        SQLiteDatabase dbGyroscope = this.getWritableDatabase();

        ContentValues contentValuesGyroscope = new ContentValues();

        contentValuesGyroscope.put(GYROSCOPE_X, Constants.gyro_x);
        contentValuesGyroscope.put(GYROSCOPE_Y, Constants.gyro_y);
        contentValuesGyroscope.put(GYROSCOPE_Z, Constants.gyro_z);
        contentValuesGyroscope.put(Time1, Constants.time1);

        long insertGyroscope = dbGyroscope.insert(SENSORGYROSCOPE_TABLE,null,contentValuesGyroscope);

        if (insertGyroscope == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addGravity(){
        SQLiteDatabase dbGravity = this.getWritableDatabase();

        ContentValues contentValuesGravity = new ContentValues();

        contentValuesGravity.put(GRAVITY_X, Constants.grav_x);
        contentValuesGravity.put(GRAVITY_Y, Constants.grav_y);
        contentValuesGravity.put(GRAVITY_Z, Constants.grav_z);
        contentValuesGravity.put(Time2, Constants.time2);

        long insertGravity = dbGravity.insert(SENSORGRAVITY_TABLE,null,contentValuesGravity);

        if (insertGravity == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addMagnetic(){
        SQLiteDatabase dbMagnetic = this.getWritableDatabase();

        ContentValues contentValuesMagnetic = new ContentValues();

        contentValuesMagnetic.put(MAGNETIC_X, Constants.mag_x);
        contentValuesMagnetic.put(MAGNETIC_Y, Constants.mag_y);
        contentValuesMagnetic.put(MAGNETIC_Z, Constants.mag_z);
        contentValuesMagnetic.put(Time3, Constants.time3);

        long insertMagnetic = dbMagnetic.insert(SENSORMAGNETIC_TABLE,null,contentValuesMagnetic);

        if (insertMagnetic == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addOrientation(){
        SQLiteDatabase dbOrientation = this.getWritableDatabase();

        ContentValues contentValuesOrientation = new ContentValues();

        contentValuesOrientation.put(ORIENTATION_AZIMUTH, Constants.azimuth);
        contentValuesOrientation.put(ORIENTATION_PITCH, Constants.pitch);
        contentValuesOrientation.put(ORIENTATION_ROLL, Constants.roll);

        long insertOrientation = dbOrientation.insert(SENSORORIENTATION_TABLE,null,contentValuesOrientation);

        if (insertOrientation == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addLight(){
        SQLiteDatabase dbLight = this.getWritableDatabase();

        ContentValues contentValuesLight = new ContentValues();

        contentValuesLight.put(LIGHT_V, Constants.acc_x);

        long insertLight = dbLight.insert(SENSORLIGHT_TABLE,null,contentValuesLight);

        if (insertLight == -1){
            return false;
        } else {
            return true;
        }
    }
    class data{
       public String ACCELEROMETERTIME,ACCELEROMETER_X,ACCELEROMETER_Y,ACCELEROMETER_Z ;
       public data(String Time,String X,String Y,String Z )
       {
           this.ACCELEROMETERTIME=Time;
           this.ACCELEROMETER_X=X;
           this.ACCELEROMETER_Y=Y;
           this.ACCELEROMETER_Z=Z;
       }
    }
    class data1{
        public String GYROSCOPETIME,GYROSCOPE_X,GYROSCOPE_Y,GYROSCOPE_Z ;
        public data1(String Time,String X,String Y,String Z )
        {
            this.GYROSCOPETIME=Time;
            this.GYROSCOPE_X=X;
            this.GYROSCOPE_Y=Y;
            this.GYROSCOPE_Z=Z;
        }
    }
    class data2{
        public String GRAVITYTIME,GRAVITY_X,GRAVITY_Y,GRAVITY_Z ;
        public data2(String Time,String X,String Y,String Z )
        {
            this.GRAVITYTIME=Time;
            this.GRAVITY_X=X;
            this.GRAVITY_Y=Y;
            this.GRAVITY_Z=Z;
        }
    }
    class data3{
        public String MAGNETICTIME,MAGNETIC_X,MAGNETIC_Y,MAGNETIC_Z ;
        public data3(String Time,String X,String Y,String Z )
        {
            this.MAGNETICTIME=Time;
            this.MAGNETIC_X=X;
            this.MAGNETIC_Y=Y;
            this.MAGNETIC_Z=Z;
        }
    }
    class data4{
        public String SENSORGPS_LATITUDE,SENSORGPSLONGITUDE ;
        public data4(String LAT,String LONG )
        {
            this.SENSORGPS_LATITUDE=LAT;
            this.SENSORGPSLONGITUDE=LONG;
        }
    }
    class data5{
        public String ORIENTATION_AZIMUTH, ORIENTATION_PITCH, ORIENTATION_ROLL;
        public data5(String azim,String pitch,String roll )
        {
            this.ORIENTATION_AZIMUTH=azim;
            this.ORIENTATION_PITCH=pitch;
            this.ORIENTATION_ROLL=roll;
        }
    }
    class data6{
        public String LIGHT_V ;
        public data6(String V )
        {
            this.LIGHT_V=V;
        }
    }
    class sensorData{
       public ArrayList<data> ACCELEROMETER;
        public ArrayList<data1> GYROSCOPE ;
        public ArrayList<data2> GRAVITY;
        public ArrayList<data3> MAGNETIC ;
        public ArrayList<data4> GPS ;
        public ArrayList<data5> ORIENTATION ;
        public ArrayList<data6> LIGHT;


    }
    public sensorData getList()
    {
        SQLiteDatabase myDataBase = this.getReadableDatabase();

        String searchQuery = "SELECT  * FROM SENSORACCELEROMETER_TABLE";
        String searchQuery1 = "SELECT  * FROM SENSORGYROSCOPE_TABLE";
        String searchQuery2 = "SELECT  * FROM SENSORGRAVITY_TABLE";
        String searchQuery3 = "SELECT  * FROM SENSORMAGNETIC_TABLE";
        String searchQuery4 = "SELECT  * FROM SENSORGPS_TABLE";
        String searchQuery5 = "SELECT  * FROM SENSORORIENTATION_TABLE";
        String searchQuery6 = "SELECT  * FROM SENSORLIGHT_TABLE";

        Cursor cursor = myDataBase.rawQuery(searchQuery, null );
        Cursor cursor1 = myDataBase.rawQuery(searchQuery1, null );
        Cursor cursor2 = myDataBase.rawQuery(searchQuery2, null );
        Cursor cursor3 = myDataBase.rawQuery(searchQuery3, null );
        Cursor cursor4 = myDataBase.rawQuery(searchQuery4, null );
        Cursor cursor5 = myDataBase.rawQuery(searchQuery5, null );
        Cursor cursor6 = myDataBase.rawQuery(searchQuery6, null );
        ArrayList<data> list = new ArrayList<data>();
        ArrayList<data1> list1 = new ArrayList<data1>();
        ArrayList<data2> list2 = new ArrayList<data2>();
        ArrayList<data3> list3 = new ArrayList<data3>();
        ArrayList<data4> list4 = new ArrayList<data4>();
        ArrayList<data5> list5 = new ArrayList<data5>();
        ArrayList<data6> list6 = new ArrayList<data6>();
        sensorData sd= new sensorData();

        if (cursor.moveToFirst()) {
            do {
                list.add(new data(cursor.getString(cursor.getColumnIndex("Time")),
                        cursor.getString(cursor.getColumnIndex("ACCELEROMETER_X")),
                        cursor.getString(cursor.getColumnIndex("ACCELEROMETER_Y")),
                        cursor.getString(cursor.getColumnIndex("ACCELEROMETER_Z")) ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (cursor1.moveToFirst()) {
            do {
                list1.add(new data1(cursor1.getString(cursor1.getColumnIndex("Time")),
                        cursor1.getString(cursor1.getColumnIndex("GYROSCOPE_X")),
                        cursor1.getString(cursor1.getColumnIndex("GYROSCOPE_Y")),
                        cursor1.getString(cursor1.getColumnIndex("GYROSCOPE_Z")) ));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        if (cursor2.moveToFirst()) {
            do {
                list2.add(new data2(cursor2.getString(cursor2.getColumnIndex("Time")),
                        cursor2.getString(cursor2.getColumnIndex("GRAVITY_X")),
                        cursor2.getString(cursor2.getColumnIndex("GRAVITY_Y")),
                        cursor2.getString(cursor2.getColumnIndex("GRAVITY_Z")) ));
            } while (cursor2.moveToNext());
        }
        cursor2.close();

        if (cursor3.moveToFirst()) {
            do {
                list3.add(new data3(cursor3.getString(cursor3.getColumnIndex("Time")),
                        cursor3.getString(cursor3.getColumnIndex("MAGNETIC_X")),
                        cursor3.getString(cursor3.getColumnIndex("MAGNETIC_Y")),
                        cursor3.getString(cursor3.getColumnIndex("MAGNETIC_Z")) ));
            } while (cursor3.moveToNext());
        }
        cursor3.close();

        if (cursor4.moveToFirst()) {
            do {
                list4.add(new data4(cursor4.getString(cursor4.getColumnIndex("SENSORGPS_LATITUDE")),
                        cursor4.getString(cursor4.getColumnIndex("SENSORGPS_LONGITUDE")) ));
            } while (cursor4.moveToNext());
        }
        cursor4.close();

        if (cursor5.moveToFirst()) {
            do {
                list5.add(new data5(cursor5.getString(cursor5.getColumnIndex("ORIENTATION_AZIMUTH")),
                        cursor5.getString(cursor5.getColumnIndex("ORIENTATION_PITCH")),
                        cursor5.getString(cursor5.getColumnIndex("ORIENTATION_ROLL")) ));
            } while (cursor5.moveToNext());
        }
        cursor5.close();

        if (cursor6.moveToFirst()) {
            do {
                list6.add(new data6(cursor6.getString(cursor6.getColumnIndex("LIGHT_V")) ));
            } while (cursor6.moveToNext());
        }
        cursor6.close();

        sd.ACCELEROMETER=list;
        sd.GYROSCOPE=list1;
        sd.GRAVITY=list2;
        sd.MAGNETIC=list3;
        sd.GPS=list4;
        sd.ORIENTATION=list5;
        sd.LIGHT=list6;


        myDataBase.execSQL("DELETE FROM "+SENSORACCELEROMETER_TABLE);
        myDataBase.execSQL("DELETE FROM "+SENSORGYROSCOPE_TABLE);
        myDataBase.execSQL("DELETE FROM "+SENSORGRAVITY_TABLE);
        myDataBase.execSQL("DELETE FROM "+SENSORMAGNETIC_TABLE);
        myDataBase.execSQL("DELETE FROM "+SENSORGPS_TABLE);
        myDataBase.execSQL("DELETE FROM "+SENSORORIENTATION_TABLE);
        myDataBase.execSQL("DELETE FROM "+SENSORLIGHT_TABLE);

        return sd;
    }
}
