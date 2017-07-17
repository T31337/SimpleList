package simple.list;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static simple.list.List.DataBase_Name;
import static simple.list.List.itemKey;
import static simple.list.List.items;
import static simple.list.List.saa;
import static simple.list.List.tableName;

class  DatabaseHelper extends SQLiteOpenHelper
{

    DatabaseHelper(Context context)
    {
        super(context,DataBase_Name,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //create empty Database
        String create = String.format("create table %s (id integer primary key autoincrement,%s text);",tableName,itemKey);
        //String sqlCreate = "create table Items(id integer primary key autoincrement,Item text);";
        db.execSQL(create);
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
        for(int i=0;i < items.size();i++)
        {
            String it = items.get(i).name;
            cv.put(itemKey,it);
            db.insert(tableName, null, cv);
        }
    }
}//DatabaseHelper
