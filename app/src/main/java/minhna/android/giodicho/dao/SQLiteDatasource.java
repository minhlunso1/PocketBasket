package minhna.android.giodicho.dao;

import java.util.ArrayList;

import minhna.android.giodicho.pojo.Item;
import minhna.android.giodicho.pojo.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDatasource {

	private MyDBHelper helper;

	private SQLiteDatabase database;

	public SQLiteDatasource(Context context) {
		helper = new MyDBHelper(context);
		database = helper.getWritableDatabase();
	}
	
	public ArrayList<List> getAllList(){
		ArrayList<List> list = new ArrayList<List>();
	
		String query = "SELECT * FROM " + MyDBHelper.TABLE_NAME1;
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
            do {
                List tmp = new List();
                tmp.setId(cursor.getInt(cursor.getColumnIndex(helper.COL1_IdList)));
                tmp.setTitle(cursor.getString(cursor.getColumnIndex(helper.COL1_Title)));
                tmp.setMeetQuantity(cursor.getInt(cursor.getColumnIndex(helper.COL1_MeetQuantity)));
                tmp.setQuantity(cursor.getInt(cursor.getColumnIndex(helper.COL1_Quantity)));
				tmp.setDone(cursor.getInt(cursor.getColumnIndex(helper.COL1_State))==1 ? true:false);
                tmp.setColor(cursor.getString(cursor.getColumnIndex(helper.COL1_Color)));
                // Adding contact to list
                list.add(tmp);
            } while (cursor.moveToNext());
        }
		cursor.close();
		return list;
	}
	
	public ArrayList<Item> getAll(){
		ArrayList<Item> list = new ArrayList<Item>();
	
		String query = "SELECT  * FROM " + MyDBHelper.TABLE_NAME2;
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
            do {
                Item tmp = new Item();
                tmp.set_id(cursor.getLong(cursor.getColumnIndex(helper.COL2_IdItem)));
                tmp.setTitle(cursor.getString(cursor.getColumnIndex(helper.COL2_Title)));
                tmp.setQuantity(cursor.getInt(cursor.getColumnIndex(helper.COL2_Quantity)));
                tmp.setDescription(cursor.getString(cursor.getColumnIndex(helper.COL2_Description)));
                tmp.setColor(cursor.getString(cursor.getColumnIndex(helper.COL2_Color)));
                int done = cursor.getInt(cursor.getColumnIndex(helper.COL2_State));
                tmp.setDone(done==1 ? true:false);
                
                list.add(tmp);
            } while (cursor.moveToNext());
        }
		cursor.close();
		return list;
	}
	
	public ArrayList<Item> getItemListById(long id, String color){
		ArrayList<Item> list = null;
	
		String query = "SELECT  * FROM " + MyDBHelper.TABLE_NAME2
						+ " WHERE " + MyDBHelper.COL2_IdList + " = " + id;
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			list = new ArrayList<Item>();
            do {
                Item tmp = new Item();
                tmp.set_id(cursor.getLong(cursor.getColumnIndex(helper.COL2_IdItem)));
                tmp.setTitle(cursor.getString(cursor.getColumnIndex(helper.COL2_Title)));
                tmp.setQuantity(cursor.getInt(cursor.getColumnIndex(helper.COL2_Quantity)));
                tmp.setDescription(cursor.getString(cursor.getColumnIndex(helper.COL2_Description)));
                tmp.setColor(color);
                int done = cursor.getInt(cursor.getColumnIndex(helper.COL2_State));
                tmp.setDone(done==1 ? true:false);
 
                list.add(tmp);
            } while (cursor.moveToNext());
        }
		cursor.close();
		return list;
	}

	public long initializeList(String listTitle, String color) {
		ContentValues values = new ContentValues();
		values.putNull(MyDBHelper.COL1_IdList);
		values.put(MyDBHelper.COL1_Title, listTitle);
		values.put(MyDBHelper.COL1_Color, color);

		long newRowId = database.insert(MyDBHelper.TABLE_NAME1, null, values);

		return newRowId;
	}

	public int updateTitleColor(long id, String title, String color) {//update two tables
		ContentValues values = new ContentValues();
		values.put(MyDBHelper.COL1_Title, title);
		values.put(MyDBHelper.COL1_Color, color);
		String whereClause = MyDBHelper.COL1_IdList + "=" + id;
		int result = database.update(MyDBHelper.TABLE_NAME1, values,
				whereClause, null);
		
		values.clear();
		values.put(MyDBHelper.COL2_Color, color);
		whereClause = MyDBHelper.COL2_IdList + "=" + id;
		result = database.update(MyDBHelper.TABLE_NAME2, values,
				whereClause, null);
		return result;
	}
	
	public int deleteList(long id) {
		String whereClause = MyDBHelper.COL1_IdList + "=" + id;
		return database.delete(MyDBHelper.TABLE_NAME1, whereClause, null);
	}
	
	public int deleteAllList() {
		return database.delete(MyDBHelper.TABLE_NAME1, null, null);
	}
	
	public long insertItem(long idList, Item item){
		ContentValues values = new ContentValues();
		values.put(MyDBHelper.COL2_IdList, item.getListId());
		values.put(MyDBHelper.COL2_Title, item.getTitle());
		values.put(MyDBHelper.COL2_Quantity, item.getQuantity());
		values.put(MyDBHelper.COL2_Description, item.getDescription());
		values.put(MyDBHelper.COL2_State, item.isDone() ? 1:0);
		values.put(MyDBHelper.COL2_Color, item.getColor());
		long result = database.insert(MyDBHelper.TABLE_NAME2, null, values);
		
		return result; 
	}
	
	public int updateListQuantity(long id, int quantity) {
		ContentValues values = new ContentValues();
		values.put(MyDBHelper.COL1_Quantity, quantity);
		String whereClause = MyDBHelper.COL1_IdList + "=" + id;
		int result = database.update(MyDBHelper.TABLE_NAME1, values,
				whereClause, null);
		return result;
	}
	
	public int deleteItem(long id) {
		String whereClause = MyDBHelper.COL2_IdItem + "=" + id;
		return database.delete(MyDBHelper.TABLE_NAME2, whereClause, null);
	}
	
	public int deleteItemByList(long idList) {
		String whereClause = MyDBHelper.COL2_IdList + "=" + idList;
		return database.delete(MyDBHelper.TABLE_NAME2, whereClause, null);
	}

	public int updateItem(long id, String title, int quantity, String description) {
		ContentValues values = new ContentValues();
		values.put(MyDBHelper.COL2_Title, title);
		values.put(MyDBHelper.COL2_Quantity, quantity);
		values.put(MyDBHelper.COL2_Description, description);
		String whereClause = MyDBHelper.COL2_IdItem + "=" + id;
		int result = database.update(MyDBHelper.TABLE_NAME2, values,
				whereClause, null);
		return result;
	}
	
	public int updateItemState(long id, boolean isDone) {
		ContentValues values = new ContentValues();
		values.put(MyDBHelper.COL2_State, isDone ? 1:0);
		String whereClause = MyDBHelper.COL2_IdItem + "=" + id;
		int result = database.update(MyDBHelper.TABLE_NAME2, values,
				whereClause, null);
		return result;
	}
	
	//list, meet quantity & state
	public int updateListQuanSta(long id, int listMeetQuantity, int listQuantity) {
		ContentValues values = new ContentValues();
		values.put(MyDBHelper.COL1_MeetQuantity, listMeetQuantity);
		values.put(MyDBHelper.COL1_Quantity, listQuantity);
		values.put(MyDBHelper.COL1_State, listMeetQuantity==listQuantity ? 1:0);
		String whereClause = MyDBHelper.COL1_IdList + "=" + id;
		int result = database.update(MyDBHelper.TABLE_NAME1, values,
				whereClause, null);
		return result;
	}
	
	public void close(){
		database.close();
		helper.close();
	}
}
