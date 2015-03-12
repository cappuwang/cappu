package dz.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecordDao {
	private DBHelper helper;  
    private SQLiteDatabase db;  
    
    public RecordDao(Context context) {  
        helper = new DBHelper(context);  
        db = helper.getWritableDatabase();  
    }  
    
    public void add(Record record) {  
        db.beginTransaction();  //开始事务  
        try {  
            db.execSQL("INSERT INTO "+DBHelper.TABLE_NAME + " (src_path, create_time, deadline) VALUES(?, ?, ?)", new Object[]{record.getSrc_path(), record.getCreateDate(), record.getDeadline()});  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    
    public ArrayList<Record> query() {  
        ArrayList<Record> records = new ArrayList<Record>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
        	Record record = new Record();  
            record.setId(""+c.getInt(c.getColumnIndex("id")));  
            record.setSrc_path(c.getString(c.getColumnIndex("src_path")));  
            record.setCreateDate(c.getString(c.getColumnIndex("create_time")));  
            record.setDeadline(c.getString(c.getColumnIndex("deadline")));  
            records.add(record);  
        }  
        c.close();  
        return records;  
    }  
      
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.TABLE_NAME, null);  
        return c;  
    }  
      
    public void closeDB() {  
        db.close();  
    } 
      
}
