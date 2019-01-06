package minhna.android.giodicho.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "BasketDB";

	// dinh nghia ten cot
	public static final String TABLE_NAME1 = "List";
	public static final String TABLE_NAME2 = "Item";
	
	public static final String COL1_IdList = "_id";
	public static final String COL1_Title = "Title";
	public static final String COL1_MeetQuantity = "MeetQuantity";
	public static final String COL1_Quantity = "Quantity";
	public static final String COL1_State = "State";
	public static final String COL1_Color = "Color";
	
	public static final String COL2_IdList = "IdList";
	public static final String COL2_IdItem = "_id";
	public static final String COL2_Title = "Title";
	public static final String COL2_Quantity = "Quantity";
	public static final String COL2_Description = "Description";
	public static final String COL2_State = "State";
	public static final String COL2_Color = "Color";

	/*
	 * Tao Table Dung cau lenh CREATE TABLE
	 * 
	 * CREATE TABLE <Table_Name> (<Column_Name> INTEGER PRIMARY KEY
	 * AUTOINCREMENT, <Column_Name> TEXT NOT NULL, <Column_Name> TEXT)
	 */
	private String createTableStatement1 = "CREATE TABLE " + TABLE_NAME1 + " ("
			+ COL1_IdList + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL1_Title + " TEXT NOT NULL, " 
			+ COL1_MeetQuantity + " INTEGER DEFAULT 0, "
			+ COL1_Quantity + " INTEGER DEFAULT 0, "
			+ COL1_State + " INTEGER, "
			+ COL1_Color + " INTEGER)";
	
	private String createTableStatement2 = "CREATE TABLE " + TABLE_NAME2 + " ("
			+ COL2_IdList +" INTEGER, "
			+ COL2_IdItem + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL2_Title + " TEXT NOT NULL, " 
			+ COL2_Quantity + " INTEGER DEFAULT 1, "
			+ COL2_Description + " TEXT, " 
			+ COL2_State + " INTEGER, "
			+ COL2_Color + " INTEGER, "
			+ "FOREIGN KEY ("+COL2_IdList+") REFERENCES "+TABLE_NAME1+" ("+COL1_IdList+") ON DELETE CASCADE )";

	public MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		// Chi mo ket noi den database
		// Neu version trung nhau thi khong tao moi database ma chi mo ket noi	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Tao Table
		db.execSQL(createTableStatement1);
		db.execSQL(createTableStatement2);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys=ON");
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
