package simple.list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class List extends ListActivity
{
	public boolean checked=false;
    private static final String TAG = "myDebug";
    public static String tableName="Items",itemKey="item",DataBase_Name="MyList";
    public static String query = String.format("select %s from %s;",itemKey,tableName);
    private ListView lv; //DO NOT MAKE STATIC, CAUSES MEMORY LEAK!
    public static ArrayAdapter<Item> saa;
    DatabaseHelper helper;
    SQLiteDatabase db;
    public static ArrayList<Item> items = new ArrayList<>();
    ContentValues cv = new ContentValues();

    AdapterView.OnItemClickListener modelListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
          
            boolean checked = items.get(i).check();
			items.get(i).setChecked(!checked);
			lv.setItemChecked(i,!checked);
            Log.println(Log.DEBUG,TAG,i+items.get(i).name+" -Checked- "+!checked);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        helper = new DatabaseHelper(this.getApplicationContext());
        db = helper.getReadableDatabase();
        lv = this.getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(modelListener);
        LoadList();
		//updateListAdapter();

    }//onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        populateMenu(menu);
        return true;
    }

	/*
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);

		Log.d("Focus debug", "Focus changed !");
		if(!hasFocus)
		{
			Log.d("Focus debug", "Lost focus !");
		}
	}
	*/


    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        super.onListItemClick(parent, v, position, id);
        Log.println(Log.INFO, TAG, Integer.toString(position));
		boolean checked = items.get(position).isChecked;
		items.get(position).setChecked(!checked);
		lv.setItemChecked(position,items.get(position).isChecked);
		updateListAdapter();
    }

    //List Functions
    private void LoadList()
    {
        //Read From File?
        Log.println(Log.DEBUG, TAG, "Read From File");
        items.clear();
        Log.println(Log.DEBUG, TAG, "List Cleared!");
        Log.println(Log.INFO, TAG, "Enter Try/Catch");
        try
        {
            Log.println(Log.DEBUG, TAG, "Executing SQL...");
            Cursor res = db.rawQuery(query,null);
            Log.println(Log.DEBUG, TAG, "Executed The SQL Statement");
            if(res.getCount()==0)
            {
                Toast.makeText(this, "Nothing In List", Toast.LENGTH_SHORT).show();
            }
            else
            {
                items.clear();
                res.moveToFirst();
                String Ittem = res.getString(0);
                items.add(new Item(Ittem));//add first item
                while(res.moveToNext())
                {
                    try
                    {
                        Ittem = res.getString(0);
                        Log.println(Log.INFO, TAG, Ittem);
                        items.add(new Item(Ittem));
                    }
                    catch (Exception e)
                    {
                        Log.println(Log.ERROR,TAG,e.toString());
                    }
                }
                res.close();
                updateListAdapter();
            }
        }
        catch(Error e)
        {
            Log.println(Log.ERROR, TAG, e.getMessage());
        }
    }
    private void SaveList()
    {
        //Write To File?
        Log.println(Log.DEBUG, TAG, "Write To File");
        String sql = String.format( "delete from %s;",tableName);
		
        db.execSQL(sql);
        for(int i=0;i < items.size();i++)
        {
            String it = items.get(i).name;	
            cv.put(itemKey,it);
            db.insert(tableName, null, cv);
        }
        Toast.makeText(this, "List Saved!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPause()
    {
        super.onPause();  // Always call the superclass method first
        SaveList();
    }

    @Override
    public void onResume()
    {
        super.onResume();  // Always call the superclass method first
        LoadList();
    }

	@Override
	protected void onStop()
	{
		SaveList();
		super.onStop();
		finish();
	}
	
	
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int nItem = item.getItemId();
        String sItem = item.toString();
        Log.println(Log.DEBUG, TAG, nItem + "-" + sItem);
        Toast.makeText(this, item.toString(),Toast.LENGTH_SHORT).show();
        if(nItem == 1)
        {
            SaveList();
            return true;
        }
        if(nItem == 2)
        {
            LoadList();
            return true;
        }
        if(nItem == 3)
        {
            //Add Item
            Log.println(Log.DEBUG, TAG, "Add Item");
            addItemDialog();
            return true;
        }
        if(nItem == 4)
        {
            //Remove All From List
            items.clear();
            updateListAdapter();
            return true;
        }
        if(nItem == 5)
        {
            //UnCheck All CheckBoxes

            int size = items.size();
            for(int i=0; i < size;i++)
            {
                lv.setItemChecked(i, false);
				items.get(i).setChecked(false);
            }
			updateListAdapter();
            return true;
        }
        if(nItem == 6)
        {
            //remove checked
            for (int i = 0 , itemsSize=items.size() ;  i < itemsSize; i++)
            {
                Item it = items.get(i);
                Log.println(Log.INFO, "myDebug", String.format("Size: %s", itemsSize));
			
                if (it.isChecked)
                {
                    Log.println(Log.INFO, TAG, String.format("%s | Checked: %s ", it.name, it.isChecked));
                    items.remove(i);
					items.trimToSize();
                  
                }
            }
			
            updateListAdapter();
            return true;
        }
        return false;
    }

    private void updateListAdapter()
    {
		
		//Reset Adapter
        saa = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice, items);
   
        //Check The Items Should Have A Check Mark Already...
		
        for(int i =0;i<items.size();i++)
        {
            try
            {
				boolean checked = items.get(i).isChecked;
				lv.setItemChecked(i,checked);
				items.get(i).setChecked(checked);
            }
            catch(Exception e)
            {
                Log.println(Log.ERROR,TAG,"Error: "+e.getMessage());
            }
        }
		
		setListAdapter(saa);
		
    }

    private void populateMenu(Menu menu)
    {
        menu.add(Menu.NONE, 3, Menu.NONE, "Add Item");
        menu.add(Menu.NONE, 4, Menu.NONE, "Remove All Items");
        menu.add(Menu.NONE, 6, Menu.NONE, "Remove Checked!");
        menu.add(Menu.NONE, 5, Menu.NONE, "Clear All CheckMarks");
        menu.add(Menu.NONE, 1, Menu.NONE, "Save List");
        menu.add(Menu.NONE, 2, Menu.NONE, "Open List");
        Log.println(Log.DEBUG, TAG, "Menu Has Been Populated :)");
    }

    private void showKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
        {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_FORCED);
        }
    }

    //ToDo: Fix This Method...
    private void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
        {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,InputMethodManager.RESULT_HIDDEN);
            inputMethodManager.hideSoftInputFromWindow(getListView().getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void addItemDialog()
    {
        showKeyboard();
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        @SuppressLint("InflateParams")
        final View addView = inflater.inflate(R.layout.add, null);
        showKeyboard();
      //  addView.setRotation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        AlertDialog add = new AlertDialog.Builder(this)
                .setTitle("Add Item!")
                .setView(addView)
                .setPositiveButton("Add Item!",
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                EditText txtItem = addView.findViewById(R.id.txtNewItem);
                                
                                String sItem = txtItem.getText().toString().trim();
                                Log.println(Log.DEBUG, TAG, "sItem='"+sItem+"'");
                                //Add Item To List
                                if(sItem.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Add Nothing To List?", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
									
									//ToDo: redundancy check
									
									
                                    items.add(new Item(sItem));
									
                                    updateListAdapter();
                                    hideKeyboard();
                                }
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //cancel - Do Nothing!
                        hideKeyboard();
                    }
                }).create();
				add.show();
        addView.setRotation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
