package minhna.android.giodicho;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import minhna.android.giodicho.adapter.BasketAdapter;
import minhna.android.giodicho.adapter.OptionAdapter;
import minhna.android.giodicho.dao.SQLiteDatasource;
import minhna.android.giodicho.iview.IAnimationEvent;
import minhna.android.giodicho.pojo.DialogOption;
import minhna.android.giodicho.pojo.List;
import minhna.android.giodicho.resources.PocketBasketResource;

@SuppressLint("NewApi")
public class MasterFragment extends ListFragment implements OnClickListener, OnItemLongClickListener,
    IAnimationEvent{
	private MainActivity mainActivity;
	private FragmentManager manager;
	private SQLiteDatasource datasource;
	private Resources resources;
	private PocketBasketResource myResources;

	private Dialog optionDialog;

	private ListView lv;
	private ListView optionLV;
	private ImageView centerLogo;
	private ArrayList<List> data;
	private BasketAdapter adapter;
    private Parcelable state;
    private boolean canBeSaved = true;

	private ActionBar actionBar;
	private MenuItem menuAddItem;
/////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity=(MainActivity)getActivity();
		manager = mainActivity.manager;
		resources=getResources();
		myResources = new PocketBasketResource(mainActivity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.master_blank_layout, container, false);
		lv = (ListView) v.findViewById(android.R.id.list);
		centerLogo = (ImageView) v.findViewById(R.id.ivLogo);
		centerLogo.setVisibility(View.GONE);
		return v;
	}

    //just have this, no onCreateView ok but no addition (bar: Master Fragment)
	@Override
	public void onStart() {
		super.onStart();
		this.setHasOptionsMenu(true);
		actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
		actionBar.setTitle(R.string.app_name);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setHomeUpIcon();
		if (PocketBasketResource.api<21){
			actionBar.setIcon(resources.getDrawable(R.drawable.ic_launcher_white));
			actionBar.setBackgroundDrawable(resources.getDrawable(R.color.grey_bar));
		} else {
			actionBar.setIcon(resources.getDrawable(R.drawable.ic_launcher_white,null));
			actionBar.setBackgroundDrawable(resources.getDrawable(R.color.grey_bar,null));
		}
		actionBar.setDisplayShowHomeEnabled(false);//show PocketBasket icon
		
		datasource = new SQLiteDatasource(getActivity());
		data = datasource.getAllList();
		updateLogoVisibility();

		lv.setOnItemLongClickListener(this);
		if (PocketBasketResource.api==18) {
			lv.setOnScrollListener(new AbsListView.OnScrollListener() {//use this to prevent lv items animation leaking
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if (scrollState == SCROLL_STATE_IDLE)
						menuAddItem.setEnabled(true);
					else
						menuAddItem.setEnabled(false);
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					//do nothing
				}
			});
		}
	}

	private void setHomeUpIcon() {
		if (mainActivity.getSlidingRootNav().isMenuOpened())
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
		else
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (canBeSaved)
		    state = getListView().onSaveInstanceState();
		else {
		    state = null;
		    canBeSaved = true;
        }
	}

	@Override
	public void onStop() {
		centerLogo.clearAnimation();
		datasource.close();
		super.onStop();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		menuInflater.inflate(R.menu.action_bar_master, menu);
		menuAddItem = menu.getItem(0);
		notifyListview(1);
        if(state != null)
            getListView().onRestoreInstanceState(state);
		super.onCreateOptionsMenu(menu, menuInflater);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_delete_complete:
			int count = 0;
			for (int i=0;i<data.size();i++){
				if (data.get(i).isDone()){
					datasource.deleteList(data.get(i).getId());
					data.remove(i);
					i--;
					count++;
				}
			}
			notifyListview(2);
			Toast.makeText(mainActivity, resources.getString(R.string.deleted)+ " ("+count+")", Toast.LENGTH_SHORT).show();
			updateLogoVisibility();
			return true;
		case R.id.action_delete_all:
			new AlertDialog.Builder(mainActivity,R.style.AppCompatAlertDialogStyle2)
		    .setTitle(resources.getString(R.string.alert_do_this))
		    .setMessage(resources.getString(R.string.alert_do_this_desc_list))
		    .setPositiveButton(resources.getString(R.string.yes), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	if (datasource.deleteAllList()>0){
		        		data.clear();
		        		adapter.notifyDataSetChanged();
		        		updateLogoVisibility();
		        	}	
		        }
		     })
		    .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
			return true;
		case R.id.action_sort_reverse:
			Collections.reverse(data);
			notifyListview(2);
			return true;
		case R.id.action_addList:
			callAddingList();
			return true;
        case android.R.id.home:
            mainActivity.toggleDrawerLeft();
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void callAddingList(){
	    canBeSaved = false;
		manager.beginTransaction()
				.replace(R.id.contain, new DetailFragment(), "new second")
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null)
				.commit();
	}
	
	public void updateLogoVisibility(){
		if (data.size()==0){
			centerLogo.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			centerLogo.setClickable(true); 
			centerLogo.setOnClickListener(this);
			AnimationSet logoAnimation = (AnimationSet) AnimationUtils.loadAnimation(mainActivity, R.anim.logo_notification);
	        centerLogo.startAnimation(logoAnimation);
		}
	}
	
	public void callOptionDialog(int passPosition){
		final int position=passPosition;
		if (optionDialog==null){
			View view = View.inflate(mainActivity, R.layout.dialog_option, null);
			optionLV = (ListView) view.findViewById(R.id.list_options);
			ArrayList<DialogOption> optionList = myResources.getDialogOptionResourcesMaster();
			OptionAdapter optionAdapter = new OptionAdapter(mainActivity, R.layout.option_dialog_layout, optionList);
			optionLV.setAdapter(optionAdapter);
			optionDialog = new Dialog(mainActivity, R.style.AppCompatAlertDialogStyle);
			optionDialog.setContentView(view);
		}
		optionDialog.show();
		
		optionLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
				if (id == 0) {//edit
					Bundle bundle = new Bundle();
					bundle.putBoolean("isEditListMode", true);
					bundle.putLong("idList", data.get(position).getId());
					bundle.putString("listTitle", data.get(position).getTitle());
					bundle.putString("color", data.get(position).getColor());
					bundle.putInt("listMeetQuantity", data.get(position).getMeetQuantity());
					bundle.putInt("listQuantity", data.get(position).getQuantity());

					DetailFragment f = new DetailFragment();
					f.setArguments(bundle);

					FragmentTransaction transaction = manager.beginTransaction();
					transaction.replace(R.id.contain, f, "second");
					transaction.setCustomAnimations(FragmentTransaction.TRANSIT_FRAGMENT_FADE, FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					transaction.addToBackStack(null);
					transaction.commit();
					optionDialog.cancel();
				} else if (id == 1) {//remove
					if (datasource.deleteList(data.get(position).getId()) > 0) {
						data.remove(position);
						notifyListview(0);
						updateLogoVisibility();
					}
					optionDialog.cancel();
				} else if (id == 2) {//reminder
					optionDialog.dismiss();
					optionDialog=null;
					DatePickerFragment.newInstance(data.get(position).getTitle()).show(mainActivity.getSupportFragmentManager(), "datePicker");
				}
			}
		});
	}

	public void notifyListview(int animationCase){
		if (PocketBasketResource.api==18)
			menuAddItem.setVisible(false);
		adapter = new BasketAdapter(mainActivity,R.layout.basket_adapter_layout,data,animationCase);
		lv.setAdapter(adapter);
		if (PocketBasketResource.api==18) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (data.size() > 7)
							Thread.sleep(1000);
						else
							Thread.sleep(500);
						mainActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								menuAddItem.setVisible(true);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		FragmentTransaction transaction = manager.beginTransaction();

		Bundle bundle = new Bundle();
		bundle.putLong("idList", data.get(position).getId());
		bundle.putString("listTitle", data.get(position).getTitle());
		bundle.putString("color", data.get(position).getColor());
		bundle.putInt("listMeetQuantity", data.get(position).getMeetQuantity());
		bundle.putInt("listQuantity", data.get(position).getQuantity());

		DetailFragment f = new DetailFragment();
		f.setArguments(bundle);
		transaction.replace(R.id.contain, f, "second");
		transaction.setCustomAnimations(FragmentTransaction.TRANSIT_FRAGMENT_FADE, FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
			case R.id.ivLogo:
				callAddingList();
				break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		callOptionDialog(position);
		return true;
	}

    @Override
    public void onAnimationEnd() {
        setHomeUpIcon();
    }
}


