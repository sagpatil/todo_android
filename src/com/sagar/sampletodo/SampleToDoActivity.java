package com.sagar.sampletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;




public class SampleToDoActivity extends ActionBarActivity {


	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	int index;
	EditText etNewItem;
	private final int REQUEST_CODE = 20;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_to_do);
		lvItems = (ListView) findViewById(R.id.lvItems);
		etNewItem = (EditText) 
				findViewById(R.id.etNewItem);
		readItems();

		itemsAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);

		registerForContextMenu(lvItems);

//		etNewItem.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				etNewItem.setHint("");
//			}
//		});
		
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.lvItems) {
			ListView lv = (ListView) v;
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			String obj = (String) lv.getItemAtPosition(acmi.position);
			System.out.println("acmi position"+ acmi.position);
			menu.setHeaderTitle("Choose");
			menu.add(0,acmi.position, 0, "Delete");
			menu.add(0,acmi.position, 0, "Edit");

		}
	}
	private void readItems()
	{
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir,"todo.txt");
		try{
			items= new ArrayList<String>(FileUtils.readLines(todoFile));

		}catch(IOException e)
		{
			items = new ArrayList<String>();
		}
	}


	private void writeItems()
	{
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir,"todo.txt");
		try{
			FileUtils.writeLines(todoFile, items);

		}catch(IOException e)
		{
			items = new ArrayList<String>();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getTitle() == "Delete") {
			System.out.println("Clicked delete");
			String oldItem= items.get(item.getItemId());
			System.out.println(oldItem );
			deleteItem(item.getItemId());
		} else if (item.getTitle() == "Edit")  {
			editItem(item.getItemId());
		}
		else {
			return false;
		}
		return true;

	}

	private void editItem(int pos) {

		String oldItem= items.get(pos);
		index = pos; // save global variable for index of item to be edirted

		Intent i = new Intent(this, EditItemActivity.class);
		i.putExtra("index", pos);
		i.putExtra("itemName", oldItem);
		startActivityForResult(i, REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// REQUEST_CODE is defined above
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// Extract name value from result extras
			String itemName = data.getExtras().getString("itemName");
			// Toast the name to display temporarily on screen
			Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();

			items.remove(index);
			items.add(index, itemName);
			itemsAdapter.notifyDataSetInvalidated();

		}
	} 

	private void deleteItem(int pos) {
		items.remove(pos);
		itemsAdapter.notifyDataSetInvalidated();
		writeItems();

	}
	
	public void addTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		Button btn = (Button) findViewById(R.id.btnAddItem);
		String itemName = etNewItem.getText().toString();
		itemName = itemName.trim();
		if(!itemName.equals("") )
		{	
			items.add(etNewItem.getText().toString());
			itemsAdapter.notifyDataSetInvalidated();
			etNewItem.setText("");
			writeItems();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample_to_do, menu);
		return true;
	}


}



