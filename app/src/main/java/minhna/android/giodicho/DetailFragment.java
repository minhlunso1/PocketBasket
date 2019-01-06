package minhna.android.giodicho;

import java.lang.reflect.Field;
import java.util.ArrayList;

import minhna.android.giodicho.dao.SQLiteDatasource;
import minhna.android.giodicho.pojo.DialogOption;
import minhna.android.giodicho.adapter.ItemListAdapter;
import minhna.android.giodicho.adapter.OptionAdapter;
import minhna.android.giodicho.pojo.Item;
import minhna.android.giodicho.resources.PocketBasketResource;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, OnItemClickListener,
 OnItemLongClickListener, OnClickListener{
	private long idList;
	private String listName;
	private String color;
	private int listMeetQuantity;
	private int listQuantity;
	private boolean isEditListMode;
	private ArrayList<Item> list;
	private EditText editTxtListName;
	private TextView tvGuide;
	private TextView tvDetail;
	private FloatingActionButton addBtn;
	
	private Bundle bundle;
	private SQLiteDatasource datasource;
	private InputMethodManager imm;
	private PocketBasketResource myResources;
	private Resources resources;
	
	private CoordinatorLayout detail_contain;
	private ItemListAdapter adapter;
	private ListView itemLV;
	private ListView optionLV;
	private MainActivity mainActivity;
	private ActionBar actionBar;
	private PopupMenu popup;
	private View actionModeView;
	public static ActionMode actionMode;
	private Dialog optionDialog;
	private int optionDialogClass;
	private Dialog detailDialog;
	private Snackbar snackbar;
	
/////////////////////////////////////////////////////////////////////////////
	public ActionMode.Callback callback = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.action_mode, menu);
			actionBar.hide();
			addBtn.setVisibility(View.INVISIBLE);
			detail_contain.setVisibility(View.INVISIBLE);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
				case R.id.colorChoosing:
					//can get view of menu through activity
					View containPopup = getActivity().findViewById(R.id.colorChoosing);
					popup = new PopupMenu(getActivity(), containPopup);
				    MenuInflater inflater = popup.getMenuInflater();
				    inflater.inflate(R.menu.color_popup, popup.getMenu());
				    //force show icon
				    Field field=null;
					try {
						field = popup.getClass().getDeclaredField("mPopup");
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    field.setAccessible(true);
				    MenuPopupHelper menuPopupHelper=null;
					try {
						menuPopupHelper = (MenuPopupHelper) field.get(popup);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    menuPopupHelper.setForceShowIcon(true);
				    
				    popup.show();
				    callPopupListener();
					return true;
			}
			
			
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			if (isEditListMode==false){
				updateColorComponents();
				
				listName=editTxtListName.getText().toString().trim();
				if (listName.compareTo("")==0)
					listName=resources.getString(R.string.listName);
				idList = datasource.initializeList(listName, color);
				if (idList==-1){
					getActivity().finish();
				} else {
					actionBar.setTitle(listName);
				}
			} else {
				listName=editTxtListName.getText().toString().trim();
				if (listName.compareTo("")==0)
					listName=resources.getString(R.string.listName);
				datasource.update_title_color(idList, listName, color);
				if (bundle!=null)
					bundle.putBoolean("isEditListMode", false);
				actionBar.setTitle(listName);
				displayItemListForAdapter(idList, color);
			}
			imm.hideSoftInputFromWindow(editTxtListName.getWindowToken(), 0);
			detail_contain.setVisibility(View.VISIBLE);
			actionBar.show();
			addBtn.setVisibility(View.VISIBLE);
		}
	};
/////////////////////////////////////////////////////////////////////////////////////////////////
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		resources = getResources();
		mainActivity=(MainActivity)getActivity();
		actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		View rootView = inflater.inflate(R.layout.detail_blank_layout, container,false);

		addBtn = (FloatingActionButton) rootView.findViewById(R.id.fabAddItem);
		tvGuide = (TextView)rootView.findViewById(R.id.tvItemGuide);
		itemLV = (ListView)rootView.findViewById(android.R.id.list);
		detail_contain = (CoordinatorLayout) rootView.findViewById(R.id.detail_coordinator_root);
		
		color="yellow";
		bundle = this.getArguments();
		if (bundle==null){
			int color_id = resources.getIdentifier(color, "color", getActivity().getPackageName());
			tvGuide.setTextColor(resources.getColor(color_id));
		}
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setHasOptionsMenu(true);
		
		imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		datasource = new SQLiteDatasource(getActivity());
		myResources = new PocketBasketResource(mainActivity);
	
		if (bundle!=null) {
			isEditListMode = bundle.getBoolean("isEditListMode",false);
			if (isEditListMode==true){
				//edit list mode
				getThingsFromMaster(bundle);
				updateColorComponents();

				callActionStage();
				displayItemListForAdapter(idList, color);
			} else {
				//normal mode
				//call out items
				getThingsFromMaster(bundle);

				updateColorComponents();
				displayItemListForAdapter(idList, color);
			}
		} else {
			//first mode
			callActionMode();
		}
		
		addBtn.setOnClickListener(this);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		datasource.updateListQuanSta(idList, listMeetQuantity, listQuantity);
		
		if (bundle==null)
			bundle = new Bundle();
		bundle.putLong("idList", idList);
		bundle.putString("listTitle", listName);
		bundle.putString("color", color);
		bundle.putInt("listMeetQuantity", listMeetQuantity);
			
		datasource.close();
	}

//////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressLint("NewApi")
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

		inflater.inflate(R.menu.action_bar_detail, menu);
		actionBar.setTitle(listName);
		if (PocketBasketResource.api<21)
			actionBar.setIcon(resources.getDrawable(R.drawable.ic_launcher_white));
		else
			actionBar.setIcon(resources.getDrawable(R.drawable.ic_launcher_white,null));
		//change color backgroud of actionBar in onStart()
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().onBackPressed();
			return true;
		case R.id.action_support:
			try {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + resources.getString(R.string.packet_name))));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + resources.getString(R.string.packet_name))));
			}
			return true;
		case R.id.action_delete_all_item:
			if (list==null) {
				Toast.makeText(mainActivity, R.string.emptyList, Toast.LENGTH_SHORT).show();
			} else {
				new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle2)
			    .setTitle(R.string.alert_do_this)
			    .setMessage(R.string.alert_do_this_desc_item)
			    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	clearList();
			        }
			     })
			    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
			return true;
		case R.id.action_delete_choice:
			if (list==null) {
				Toast.makeText(mainActivity, R.string.emptyList, Toast.LENGTH_SHORT).show();
			} else {
				for (int i=0;i<list.size();i++){
					if (list.get(i).isDone()){
						if (datasource.deleteItem(list.get(i).get_id())>0){
							listMeetQuantity--;
							list.remove(i);
							i--;
						}
					}
				}
				listQuantity=list.size();
				updateState();
				adapter.notifyDataSetChanged();
			}
			return true;
		case R.id.action_modify:
			isEditListMode=true;
			if (bundle!=null)
				bundle.putBoolean("isEditListMode", true);//old action mode 
			callActionStage();
			return true;
		case R.id.action_share:
			callActionShare();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void callPopupListener(){
		popup.setOnMenuItemClickListener(this);
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Menu actionMenu= actionMode.getMenu();
		MenuItem colorChoosing = actionMenu.getItem(0);
		//change color attr
		switch (item.getItemId()){
			case R.id.yellowPicker:
				colorChoosing.setIcon(R.drawable.yellow_circle);
				color="yellow";
				break;
			case R.id.greenPicker:
				colorChoosing.setIcon(R.drawable.green_circle);
				color="green";
				break;
			case R.id.redPicker:
				colorChoosing.setIcon(R.drawable.red_circle);
				color="red";
				break;
			case R.id.cyanPicker:
				colorChoosing.setIcon(R.drawable.cyan_circle);
				color="cyan";
				break;
			case R.id.purplePicker:
				colorChoosing.setIcon(R.drawable.purple_circle);
				color="purple";
				break;
			case R.id.orangePicker:
				colorChoosing.setIcon(R.drawable.orange_circle);
				color="deepOrange";
				break;
			case R.id.limePicker:
				colorChoosing.setIcon(R.drawable.lime_circle);
				color="lime";
				break;
			case R.id.bluePicker:
				colorChoosing.setIcon(R.drawable.blue_circle);
				color="blue";
				break;
			default:
				break;
		}
		//update color components in view
		updateColorComponents();
		
		return true;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void getThingsFromMaster(Bundle bundle){
		idList = bundle.getLong("idList");
		listName=bundle.getString("listTitle");
		color = bundle.getString("color");
		listMeetQuantity = bundle.getInt("listMeetQuantity");
	}
	
	public void updateState(){
		if (list.isEmpty()){
			tvGuide.setVisibility(View.VISIBLE);
			list=null;
		}
	}
	
	public void updateTitleActionMode(){
		editTxtListName.setText(listName);
		editTxtListName.setSelection(listName.length());
	}
	
	public void updateColorComponents(){
		int color_id = resources.getIdentifier(color, "color", getActivity().getPackageName());
		tvGuide.setTextColor(resources.getColor(color_id));
		actionBar.setBackgroundDrawable(new ColorDrawable(resources.getColor(color_id)));
		addBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(color_id)));
	}
	
	private void callActionShare(){
		if (list!=null){
	     Intent i = new Intent(Intent.ACTION_SEND);
	     i.setType("text/plain");
	     i.putExtra(Intent.EXTRA_SUBJECT, listName);
	     
	     StringBuilder body = new StringBuilder();
	     
		     for (Item tmp: list){
		    	 if (tmp.isDone()){
		    		 body.append("- "+tmp.getTitle() + " " + "\u2713");
		    		 body.append(tmp.getDescription().equals("") ? "\n":"\n"+tmp.getDescription()+"\n");
		    	 }
		    	 else {
		    		 body.append("- "+tmp.getTitle() + " (" + tmp.getQuantity()+")");
		    		 body.append(tmp.getDescription().equals("") ? "\n":"\n"+tmp.getDescription()+"\n");
		    	 }
		    	 body.append("\n");
		     }
		     i.putExtra(Intent.EXTRA_TEXT, body.toString());
	     startActivity(Intent.createChooser(i, resources.getString(R.string.chooseAction)));
		} else 
			Toast.makeText(mainActivity, R.string.emptyList, Toast.LENGTH_SHORT).show();
	}
	
	public void callListDoneAction(){
		if (listMeetQuantity==listQuantity){
			snackbar = Snackbar.make(detail_contain, R.string.done, Snackbar.LENGTH_LONG).setAction(R.string.clear_list, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearList();
                }
            });
			ViewGroup group = (ViewGroup) snackbar.getView();
			group.setBackgroundColor(resources.getColor(R.color.grey_bar));
			snackbar.show();
		}
	}
	
	public void changeIconColorChoosing(String color){
		Menu actionMenu= actionMode.getMenu();
		MenuItem colorChoosing = actionMenu.getItem(0);
		//change color attr
		switch (color){
			case "yellow":
				colorChoosing.setIcon(R.drawable.yellow_circle);
				break;
			case "green":
				colorChoosing.setIcon(R.drawable.green_circle);
				break;
			case "red":
				colorChoosing.setIcon(R.drawable.red_circle);
				break;
			case "cyan":
				colorChoosing.setIcon(R.drawable.cyan_circle);
				break;
			case "purple":
				colorChoosing.setIcon(R.drawable.purple_circle);
				break;
			case "deepOrange":
				colorChoosing.setIcon(R.drawable.orange_circle);
				break;
			case "lime":
				colorChoosing.setIcon(R.drawable.lime_circle);
				break;
			case "blue":
				colorChoosing.setIcon(R.drawable.blue_circle);
				break;
			default:
				break;
		}
	}
	
	public void displayItemListForAdapter(long idList, String color){
		list = datasource.getItemListById(idList,color);
		if (list!=null){
			listQuantity=list.size();
			tvGuide.setVisibility(View.INVISIBLE);
			adapter = new ItemListAdapter(getActivity(),R.layout.item_layout,list);
			itemLV.setAdapter(adapter);
			itemLV.setOnItemClickListener(this);
			itemLV.setOnItemLongClickListener(this);
		}
	}

	private void clearList(){
		if (datasource.deleteItemByList(idList)>0) {
			list.clear();
			listMeetQuantity=0;
			listQuantity=list.size();
			updateState();
			adapter.notifyDataSetChanged();
			list=null;
		}
	}
	
	public void callActionStage(){
		callActionMode();
		updateTitleActionMode();
		changeIconColorChoosing(color);
	}
	
	public void callActionMode(){
		LayoutInflater inflater = getLayoutInflater(null);
		AppCompatActivity actionBarActivity = (AppCompatActivity)getActivity();
		actionMode = actionBarActivity.startSupportActionMode(callback);
		actionMode.setCustomView(inflater.inflate(R.layout.action_mode, null));
		
		configEnterBtn();
	}
	
	public void configEnterBtn(){
		actionModeView = actionMode.getCustomView();
		editTxtListName = (EditText)actionModeView.findViewById(R.id.inputListName);
		imm.showSoftInput(editTxtListName, 0);
		editTxtListName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				actionMode.finish();
				return true;
			}
		});
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Item item = list.get(position);
		long itemId = item.get_id();
		if (item.isDone()){
			if (datasource.updateItemState(itemId, false)>0){
				item.setDone(false);
				listMeetQuantity--;
				
				AlphaAnimation alpha = new AlphaAnimation(0.45f,1f);
				alpha.setDuration(0);
				alpha.setFillAfter(true);
				view.startAnimation(alpha);
				view.setBackgroundResource(0);
			}
		} else {
			if (datasource.updateItemState(itemId, true)>0){
				item.setDone(true);
				listMeetQuantity++;
				
				AlphaAnimation alpha = new AlphaAnimation(1f,0.45f);
				alpha.setDuration(0);
				alpha.setFillAfter(true); //Tell it to persist after the animation ends
				view.startAnimation(alpha);
				view.setBackgroundResource(R.drawable.strikeline);
			}
		}
		callListDoneAction();
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		callOptionDialog(id);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fabAddItem:
				Intent intent = new Intent(getActivity(),AddingItemActivity.class);
				intent.putExtra("color", color);
				intent.putExtra("listId", idList);
				intent.putExtra("listQuantity", listQuantity);
				startActivity(intent);
		}
	}
	
	public void callEditDialog(int passPosition){
		View editView = getLayoutInflater(null).inflate(R.layout.dialog_edit, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
		alertDialogBuilder.setView(editView);

		final EditText title = (EditText) editView.findViewById(R.id.itemTitle_dialog);
		final EditText quantity = (EditText) editView.findViewById(R.id.itemQuantity_dialog);
		final EditText description = (EditText) editView.findViewById(R.id.itemDescription_dialog);
		final int position = passPosition;
		final long idItem = list.get(position).get_id();
		title.setText(list.get(position).getTitle());
		title.setSelection(title.getText().toString().length());
		quantity.setText(String.valueOf(list.get(position).getQuantity()));
		description.setText(list.get(position).getDescription());

		// set dialog message
		alertDialogBuilder
			.setCancelable(true)
			.setPositiveButton(R.string.save,
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    	String titleStr = title.getText().toString().trim().equals("") ? resources.getString(R.string.itemNameHint):title.getText().toString().trim();
			    	int quantityInt = quantity.getText().toString().equals("") ? 0:Integer.parseInt(quantity.getText().toString());
			    	String desStr = description.getText().toString().trim();
			    	if (datasource.updateItem(idItem, titleStr, quantityInt, desStr)>0){
			    		list.get(position).setTitle(titleStr);
			    		list.get(position).setQuantity(quantityInt>=0 ? quantityInt:0);
			    		list.get(position).setDescription(desStr);
			    		adapter.notifyDataSetChanged();
			    	}
			    }
			  })
			.setNegativeButton(R.string.cancel,
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		AlertDialog editDialog = alertDialogBuilder.create();// create dialog
		editDialog.show();// show it
	}
	
	public void callOptionDialog(int passPosition){
		final int position=passPosition;
		ArrayList<DialogOption> optionList;
		OptionAdapter optionAdapter;
		if (list.get(position).getDescription().trim().equals("")){
			if (optionDialog==null){
				View view = View.inflate(mainActivity, R.layout.dialog_option, null);
				optionLV = (ListView) view.findViewById(R.id.list_options);
				optionList = myResources.getDialogOptionResourcesNoDetail();
				optionAdapter = new OptionAdapter(mainActivity, R.layout.option_dialog_layout, optionList);
				optionLV.setAdapter(optionAdapter);
				optionDialog = new Dialog(mainActivity, R.style.AppCompatAlertDialogStyle);
				optionDialog.setContentView(view);
			} else if (optionDialogClass==2){
				optionList = myResources.getDialogOptionResourcesNoDetail();
				optionAdapter = new OptionAdapter(mainActivity, R.layout.option_dialog_layout, optionList);
				optionLV.setAdapter(optionAdapter);
			}
			optionDialogClass=1;
			optionDialog.show();
		} else {
			if (optionDialog==null){
				View view = View.inflate(mainActivity, R.layout.dialog_option, null);
				optionLV = (ListView) view.findViewById(R.id.list_options);
				optionList = myResources.getDialogOptionResources();
				optionAdapter = new OptionAdapter(mainActivity, R.layout.option_dialog_layout, optionList);
				optionLV.setAdapter(optionAdapter);
				optionDialog = new Dialog(mainActivity, R.style.AppCompatAlertDialogStyle);
				optionDialog.setContentView(view);
			} else if (optionDialogClass==1){
				optionList = myResources.getDialogOptionResources();
				optionAdapter = new OptionAdapter(mainActivity, R.layout.option_dialog_layout, optionList);
				optionLV.setAdapter(optionAdapter);
			}
			optionDialogClass=2;
			optionDialog.show();
		}
		
		optionLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
				if (id==0){//edit
					callEditDialog(position);
					optionDialog.cancel();
				} else if (id==1){//remove
					int rows = datasource.deleteItem(list.get(position).get_id());
					if (list.get(position).isDone())
						listMeetQuantity--;
					list.remove(position);
					listQuantity=list.size();
					adapter.notifyDataSetChanged();
					if (rows>0)
						updateState();
					optionDialog.cancel();
				} else if (id==2) {//view detail
					optionDialog.cancel();
					if (detailDialog==null){
						detailDialog = new Dialog(mainActivity, R.style.AppCompatAlertDialogStyle);
						tvDetail = new TextView(mainActivity);
						tvDetail.setPadding(20, 20, 20, 20);
						detailDialog.setContentView(tvDetail);
					}
					tvDetail.setText(list.get(position).getDescription());
					detailDialog.show();
				}
			}
		});
	}
	
}
//////////////////////////////////////////////////////////////////////////////////////////////////