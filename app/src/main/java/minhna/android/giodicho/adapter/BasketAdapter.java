package minhna.android.giodicho.adapter;

import java.util.ArrayList;

import minhna.android.giodicho.R;
import minhna.android.giodicho.pojo.List;
import minhna.android.giodicho.resources.PocketBasketResource;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BasketAdapter extends ArrayAdapter<List> {

	private Context context;
	private int resId;
	private ArrayList<List> lists;
	private PocketBasketResource pbResources;
	private int animationCase;

	public BasketAdapter(Context context, int resId,
			ArrayList<List> lists, int animationCase) {
		super(context, resId, lists);
		this.context = context;
		this.resId = resId;
		this.lists = lists;
		this.animationCase = animationCase;
		this.pbResources = new PocketBasketResource(context);
	}

	private class ViewHolder {
		LinearLayout colorLine;
		TextView listTitle, listUnit;
		int animationCase;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			// Khoi tao View ngay lan chay dau tien
			convertView = View.inflate(context, resId, null);

			holder = new ViewHolder();
			holder.animationCase = animationCase;
			holder.colorLine = (LinearLayout) convertView.findViewById(R.id.colorLine);
			holder.listTitle = (TextView) convertView.findViewById(R.id.listTitle);
			holder.listUnit = (TextView) convertView.findViewById(R.id.itemLabel);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//Lay du lieu tu trong mang
		List l = lists.get(position);

		if(l != null){
			//Gan du lieu vao cac view thich hop
			String uri = l.getColor().toLowerCase() + "_shape";
            //int color_id = context.getResources().getIdentifier(uri, "color", context.getPackageName()); color
			int color_id = context.getResources().getIdentifier(uri, "drawable", context.getPackageName());
            holder.colorLine.setBackgroundResource(color_id);
			holder.listTitle.setText(l.getTitle());
			String unit;
			unit=l.getMeetQuantity()+"   /   "+l.getQuantity()+"\t\t";
			if (l.getQuantity()>1)
				holder.listUnit.setText(unit+pbResources.getResources().getString(R.string.items));
			else
				holder.listUnit.setText(unit+pbResources.getResources().getString(R.string.item));
		}

		Animation animation = null;
		switch (holder.animationCase) {
			case 1:
				animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
				convertView.startAnimation(animation);
				break;
			case 2:
				animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_top);
				convertView.startAnimation(animation);
				holder.animationCase = 1;
				break;
			default:
				holder.animationCase = 1;
				break;
		}

		//Tra ve view tao tu file resid
		return convertView;
	}
}
