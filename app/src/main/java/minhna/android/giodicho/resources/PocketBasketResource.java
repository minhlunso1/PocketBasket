package minhna.android.giodicho.resources;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import minhna.android.giodicho.R;
import minhna.android.giodicho.pojo.DialogOption;

public class PocketBasketResource{
	private Resources resources;
	
	public static String assetImages[] = {"guide1.png","guide2.png","guide3.png","guide4.png","guide5.png",
		"guide6.png"};
	public static int api = Build.VERSION.SDK_INT;
	public String option_titles[];
	public String option_titles_master[];
	public String option_icons[] = {"ic_menu_edit","ic_menu_delete","ic_menu_info_details"};
	public String option_icons_master[] = {"ic_menu_edit","ic_menu_delete","ic_menu_recent_history"};

	public PocketBasketResource(Context context){
		setResources(context.getResources());
		option_titles = new String[] {getResources().getString(R.string.modify),getResources().getString(R.string.delete),
				getResources().getString(R.string.view_detail)};
		option_titles_master = new String[] {getResources().getString(R.string.modify),getResources().getString(R.string.delete),
				getResources().getString(R.string.Set_reminder)};
	}
	
	public ArrayList<DialogOption> getDialogOptionResourcesMaster(){
		ArrayList<DialogOption> list = new ArrayList<DialogOption>();
		for (int i=0;i<option_titles_master.length;i++){
			DialogOption tmp = new DialogOption(option_icons_master[i], option_titles_master[i]);
			list.add(tmp);
		}
		return list;
	}

	public ArrayList<DialogOption> getDialogOptionResourcesNoDetail(){
		ArrayList<DialogOption> list = new ArrayList<DialogOption>();
		for (int i=0;i<2;i++){
			DialogOption tmp = new DialogOption(option_icons[i], option_titles[i]);
			list.add(tmp);
		}
		return list;
	}
	
	public ArrayList<DialogOption> getDialogOptionResources(){
		ArrayList<DialogOption> list = new ArrayList<DialogOption>();
		for (int i=0;i<option_titles.length;i++){
			DialogOption tmp = new DialogOption(option_icons[i], option_titles[i]);
			list.add(tmp);
		}
		return list;
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}
}
