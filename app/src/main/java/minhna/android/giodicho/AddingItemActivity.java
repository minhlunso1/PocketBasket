package minhna.android.giodicho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import minhna.android.giodicho.dao.SQLiteDatasource;
import minhna.android.giodicho.pojo.Item;
import minhna.android.giodicho.resources.PocketBasketResource;

public class AddingItemActivity extends AppCompatActivity {
	private long listId;
	private String color;
	private int listQuantity;
	private SQLiteDatasource datasource;

	private Button addBtn;
	private Toolbar toolbar;
	private ActionBar actionBar;
	private ViewGroup actionBarLayout;
	private TextView listAddingQuantity;
	private EditText itemName;
	private EditText itemQuantity;
	private EditText itemDesc;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adding_item);
		
		actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.adding_item_action_bar,null);
		toolbar= (Toolbar) findViewById(R.id.toolbar);
		toolbar.setPopupTheme(R.style.Toolbar_Popup);
		setSupportActionBar(toolbar);
		actionBar=getSupportActionBar();
		listAddingQuantity = (TextView) actionBarLayout.findViewById(R.id.listQuantityAdding);
		
		datasource = new SQLiteDatasource(this);
		itemName = (EditText) findViewById(R.id.itemNameEdtTxt);
		itemQuantity = (EditText) findViewById(R.id.itemQuantityEdtTxt);
		itemDesc = (EditText) findViewById(R.id.tvItemDesc);
	}
	
	@Override
	protected void onStart() {
		addBtn = (Button) findViewById(R.id.addItemBtn);
		Intent intent = getIntent();
		listId = intent.getLongExtra("listId", 0);
		color = intent.getStringExtra("color");
		listQuantity = intent.getIntExtra("listQuantity", 0);
		int color_id = getResources().getIdentifier(color, "color", getPackageName());
		addBtn.setBackgroundResource(color_id);
		addBtn.setTextColor(Color.WHITE);
		listAddingQuantity.setText(String.valueOf(listQuantity));	
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		datasource.updateListQuantity(listId, listQuantity);
		datasource.close();
		finish();//for safe
		super.onStop();
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int color_id = getResources().getIdentifier(color, "color", getPackageName());
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (PocketBasketResource.api<21)
			actionBar.setIcon(getResources().getDrawable(R.drawable.ic_launcher_white));
		else
			actionBar.setIcon(getResources().getDrawable(R.drawable.ic_launcher_white, null));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		LayoutParams lpab = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.START | Gravity.CENTER_VERTICAL);
		actionBar.setCustomView(actionBarLayout, lpab);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(color_id)));
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id){
			case android.R.id.home:
				datasource.updateListQuantity(listId, listQuantity);
				finish();
		}
		return super.onOptionsItemSelected(item);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////	/////////////////////////////
	public void addItem(View v){
		AlphaAnimation pressedAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.btn_animation);
        addBtn.startAnimation(pressedAnimation);
        
		String itemNameStr = itemName.getText().toString().trim();
		String description = itemDesc.getText().toString().trim();
		if (("").equals(itemNameStr))
			itemNameStr = getResources().getString(R.string.itemNameHint);
		String itemQuantityStr = itemQuantity.getText().toString();
		if (itemQuantityStr.length()==0)
			itemQuantityStr="1";
		int quantity = Integer.parseInt(itemQuantityStr);
		Item tmp = new Item(listId, -1, itemNameStr, quantity, description, false, color);
		long id = datasource.insertItem(listId, tmp);
		if (id > 0) {
			AnimationSet anim_list_quantity_notification = (AnimationSet)AnimationUtils.loadAnimation(getApplicationContext(), R.anim.quantity_notification);
			listAddingQuantity.startAnimation(anim_list_quantity_notification);
			listAddingQuantity.setText(String.valueOf(++listQuantity));
			itemName.setText("");
			itemDesc.setText("");
			itemQuantity.setText("1");
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////	/////////////////////////////
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSize > Configuration.SCREENLAYOUT_SIZE_LARGE) {
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} else {
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
		super.onConfigurationChanged(newConfig);
	}
	
}
