package simple.list;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static simple.list.List.saa;
import static simple.list.List.tableName;

public class  DatabaseHelper extends SQLiteOpenHelper
{
    public DatabaseHelper(Context context)
    {
        super(context,"Items.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //create empty Database
        String crate = String.format("create table %s(id integer primary key autoincrement,Item text);",tableName);
        //String sqlCreate = "create table Items(id integer primary key autoincrement,Item text);";
        db.execSQL(crate);
			/*
				//add record
				cv.put("Item", "Programming Skills");
				db.insert("Items",null, cv);
			 */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2)
    {
        ContentValues cv = new ContentValues();
        for(int i=0;i < saa.getCount();i++)
        {
            String it =  saa.getItem(i).name;
            cv.put("Item",it);
            db.insert("Items", null, cv);
        }
    }
}//DatabaseHelper
