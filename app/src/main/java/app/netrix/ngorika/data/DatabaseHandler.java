package app.netrix.ngorika.data;

/**
 * Created by chrissie on 12/4/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import app.netrix.ngorika.pojo.DataBundle;
import app.netrix.ngorika.pojo.Farmer;
import app.netrix.ngorika.pojo.Grader;
import app.netrix.ngorika.pojo.Routes;
import app.netrix.ngorika.pojo.User;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ngorika";
    private static final String TABLE_ROUTES = "routes";
    private static final String TABLE_GRADERS = "graders";
    private static final String TABLE_FARMERS = "farmers";
    private static final String TABLE_INVENTORY = "inventory";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_UOM = "uom";
    private static final String KEY_SHIFT = "shift";
    private static final String KEY_CAN_NUMBER = "cannumber";
    private static final String KEY_ALCOHAL = "alcohal";
    private static final String KEY_PEROXIDE = "peroxide";
    private static final String KEY_DESC = "desc";
    private static final String KEY_VAT = "vat";
    private static final String KEY_INTERNAL_CODE = "internalCode";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ROUTEID = "rid";
    private static final String KEY_FARMER = "Fid";
    private static final String TABLE_DATA = "data";
    private static final String TABLE_USERS = "users";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_DATE = "date";
    private static final String KEY_USER= "user";
    private static final String KEY_FARMERID= "FID";
    private static final String KEY_FARMERNAME= "FARMERNAME";
    private static final String KEY_USERNAME= "USERNAME";
    private static final String KEY_USERPASS= "PASSWORD";
    private static final String KEY_USERID= "USERID";
    private static final String KEY_UPLOADSTATUS= "uploaded";
    public static final String yes="YES";
    public static final String no="NO";

    Context _context;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context=context;
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
  /*  @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if ( oldVersion<3){
           *//* database.execSQL(DBAdapter.UpgradeForm);
            database.execSQL(DBAdapter.UpgradeQuestion);*//*
}
        }*/
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_ROUTES_TABLE = "CREATE TABLE " + TABLE_ROUTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_ROUTEID+" INTEGER ," + KEY_NAME + " TEXT)";

        String CREATE_INVENTORY_TABLE = "CREATE TABLE "+ TABLE_INVENTORY + "(" +
                KEY_ID + " INTEGER PRIMARY KEY,"+KEY_CURRENCY+" TEXT,"+KEY_UOM+" TEXT," +KEY_DESC+ " TEXT,"+KEY_VAT+" FL0AT,"+KEY_INTERNAL_CODE+" TEXT)";

        String CREATE_FARMER_TABLE = "CREATE TABLE " + TABLE_FARMERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_FARMERID+" INTEGER ," + KEY_FARMERNAME + " TEXT,"+KEY_ROUTEID+" INTEGER)";

        String CREATE_GRADER_TABLE = "CREATE TABLE " + TABLE_GRADERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_ROUTEID+" INTEGER ," + KEY_NAME + " TEXT)";

        String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_ROUTEID+" INTEGER ," + KEY_WEIGHT + " TEXT ," +
                KEY_USER +" INTEGER,"+KEY_FARMER+" INTEGER,"+KEY_DATE+" long , "+KEY_UPLOADSTATUS+" TEXT , "+KEY_FARMERNAME+" TEXT , "+KEY_CAN_NUMBER+" INTEGER , "+KEY_SHIFT+" INTEGER , "+KEY_ALCOHAL+" INTEGER , "+KEY_PEROXIDE+" INTEGER )";

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"+KEY_USERNAME+" TEXT ," + KEY_USERPASS + " TEXT ,"+ KEY_USERID+" INTEGER )";


        db.execSQL(CREATE_ROUTES_TABLE);
        db.execSQL(CREATE_INVENTORY_TABLE);
        db.execSQL(CREATE_FARMER_TABLE);
        db.execSQL(CREATE_GRADER_TABLE);
        db.execSQL(CREATE_DATA_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new route
     * */
    public long insertRoute(String route,int routeID){
        long stat=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor=db.query(true,TABLE_ROUTES,new String[]{KEY_ROUTEID},
                KEY_ROUTEID+"=? AND "+KEY_NAME+"=?",new String[]{routeID+"",route},null,null,null,null);
        if(!cursor.moveToFirst())
        {
            values.put(KEY_NAME, route);
            values.put(KEY_ROUTEID, routeID);
            stat= db.insert(TABLE_ROUTES, null, values);

        }
        db.close();
        return stat;
    }
    /*
    new farmers
     */
    public long insertFarmer(String farmer,int farmerID,int route){
        long count=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    Cursor cursor=db.query(true,TABLE_FARMERS,new String[]{KEY_FARMERID},
            KEY_FARMERID+"=? AND "+KEY_FARMERNAME+"=?",new String[]{farmerID+"",farmer},null,null,null,null);
    if(!cursor.moveToFirst()){
        values.put(KEY_FARMERNAME, farmer);
        values.put(KEY_FARMERID, farmerID);
        values.put(KEY_ROUTEID, route);
        count= db.insert(TABLE_FARMERS, null, values);

    }

        db.close(); // Closing database connection
        return count;
    }
    public long insertBundle(int farmer,int routeID,int user,int cannumber, int shift, boolean alcohal, boolean peroxide, String weight,long milli,String farmerName){
        long count=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor=db.query(true,TABLE_DATA,new String[]{KEY_DATE},
                KEY_DATE+"=?  ",new String[]{milli+""},null,null,null,null);
        if(!cursor.moveToFirst()){
            values.put(KEY_USER, user);
            values.put(KEY_ROUTEID, routeID);
            values.put(KEY_WEIGHT, weight);
            values.put(KEY_DATE, milli);
            values.put(KEY_FARMER, farmer);
            values.put(KEY_FARMERNAME, farmerName);
            values.put(KEY_CAN_NUMBER,cannumber);
            values.put(KEY_SHIFT,shift);
            values.put(KEY_ALCOHAL,alcohal ? 1 : 0);
            values.put(KEY_PEROXIDE,peroxide ? 1 : 0);
            values.put(KEY_UPLOADSTATUS, no);
            count= db.insert(TABLE_DATA, null, values);
        }

        db.close(); // Closing database connection
        return count;
    }
    /*
new grader
 */
    public long insertgrader(String grader,int empID){
        long count=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor=db.query(true,TABLE_GRADERS,new String[]{KEY_ROUTEID},
                KEY_ROUTEID+"=? AND "+KEY_NAME+"=?",new String[]{empID+"",grader},null,null,null,null);
        if(!cursor.moveToFirst()){
            values.put(KEY_NAME, grader);
            values.put(KEY_ROUTEID, empID);
            count= db.insert(TABLE_GRADERS, null, values);
        }

        db.close(); // Closing database connection
        return count;
    }
    public long insertUser(String password,String username,int userid){
        long count=-1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor=db.query(true,TABLE_USERS,new String[]{KEY_USERNAME},
                KEY_USERNAME+"=? AND "+KEY_USERPASS+"=?",new String[]{username,password},null,null,null,null);
        if(!cursor.moveToFirst()){
            values.put(KEY_USERNAME, username);
            values.put(KEY_USERPASS, password);
            values.put(KEY_USERID, userid);
            count= db.insert(TABLE_USERS, null, values);
        }
//        db.close();
        return count;
    }
    /**
     * Getting all routes
     *
     * */
    public  ArrayList<Routes> getAllRoutes(){
        ArrayList<Routes> routes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_ROUTES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(KEY_NAME));
                int id=cursor.getInt(cursor.getColumnIndex(KEY_ROUTEID));
                Routes r=new Routes();
                r.setRoute(name);
                r.setId(id);
                routes.add(r);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return routes;
    }
    public  ArrayList<Farmer> getAllFarmers(int route){
        ArrayList<Farmer> routes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARMERS ;
        if (route > 0){
            selectQuery += " WHERE "+ KEY_ROUTEID+" = "+ route;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(KEY_FARMERNAME));
                int id=cursor.getInt(cursor.getColumnIndex(KEY_FARMERID));
                Farmer r=new Farmer();
                r.setName(name);
                r.setId(id);
                routes.add(r);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();


        return routes;
    }
    /* retrieve unsent records */
    public  ArrayList<DataBundle> getunsentRecords(){
        ArrayList<DataBundle> dataBundles = new ArrayList<>();

//        String selectQuery = "SELECT  * FROM " + TABLE_DATA+ " WHERE "+ KEY_UPLOADSTATUS+" = "+ no; /

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.query(true, TABLE_DATA, new String[]{KEY_FARMERNAME, KEY_ROUTEID, KEY_USER, KEY_FARMER, KEY_ID, KEY_WEIGHT, KEY_DATE, KEY_CAN_NUMBER, KEY_SHIFT, KEY_ALCOHAL, KEY_PEROXIDE},
                KEY_UPLOADSTATUS + "=? ", new String[]{no}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String farmer=cursor.getString(cursor.getColumnIndex(KEY_FARMERNAME));
                int routeId=cursor.getInt(cursor.getColumnIndex(KEY_ROUTEID));
                int farmerId=cursor.getInt(cursor.getColumnIndex(KEY_FARMER));
                int cannumber=cursor.getInt(cursor.getColumnIndex(KEY_CAN_NUMBER));
                int shift = cursor.getInt(cursor.getColumnIndex(KEY_SHIFT));
                boolean alcohal = cursor.getInt(cursor.getColumnIndex(KEY_ALCOHAL)) == 1 ? true : false;
                boolean peroxide = cursor.getInt(cursor.getColumnIndex(KEY_PEROXIDE)) == 1 ? true : false;
                int userId=cursor.getInt(cursor.getColumnIndex(KEY_USER));
                int recordID=cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String weight=cursor.getString(cursor.getColumnIndex(KEY_WEIGHT));
                long time=cursor.getLong(cursor.getColumnIndex(KEY_DATE));
                DataBundle r=new DataBundle();
                r.setFarmerID(farmerId);
                r.setCannumber(cannumber);
                r.setShift(shift);
                r.setAlcohal(alcohal);
                r.setPeroxide(peroxide);
                r.setFarmerName(farmer);
                r.setRouteId(routeId);
                r.setTime(time);
                r.setUserID(userId);
                r.setWeight(weight);
                r.setRecordId(recordID);
                dataBundles.add(r);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();


        return dataBundles;
    }
    public User getPassword(String username){
        String password;
        int userid;
        User user=new User();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.query(true, TABLE_USERS, new String[]{KEY_USERPASS, KEY_USERID},
                KEY_USERNAME + "=?", new String[]{username}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                password=cursor.getString(cursor.getColumnIndex(KEY_USERPASS));
                userid=cursor.getInt(cursor.getColumnIndex(KEY_USERID));
                user.setUserid(userid);
                user.setPassword(password);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();


        return user;
    }
    public  ArrayList<Grader> getAllGraders(){
        ArrayList<Grader> graders = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_GRADERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(KEY_NAME));
                int id=cursor.getInt(cursor.getColumnIndex(KEY_ROUTEID));
                Grader r=new Grader();
                r.setName(name);
                r.setId(id);
                graders.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return graders;
    }

    public int getFarmerCount(int route){
        String selectQuery = "SELECT  * FROM " + TABLE_FARMERS+ " WHERE "+ KEY_ROUTEID+" = "+ route;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount();
    }
    public void deleteFarmers(){
        SQLiteDatabase db = this.getReadableDatabase();
      String delete = "DELETE FROM "+TABLE_FARMERS;
        db.execSQL(delete);

    }
    public int updateUploadStat(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOADSTATUS, yes);
        int count=db.update(TABLE_DATA,values,KEY_ID+" = ?",new String[]{ "" + id} );
        return count;
    }
}