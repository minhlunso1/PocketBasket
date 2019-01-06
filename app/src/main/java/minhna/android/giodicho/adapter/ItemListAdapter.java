package minhna.android.giodicho.adapter;

import java.util.ArrayList;

import minhna.android.giodicho.R;
import minhna.android.giodicho.pojo.Item;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<Item> {
	private Context context;
	private int resId;
	private ArrayList<Item> items;

	public ItemListAdapter(Context context, int resId,
			ArrayList<Item> items) {
		super(context, resId, items);
		this.context = context;
		this.resId = resId;
		this.items = items;
	}
	
	private class ViewHolder {
		LinearLayout colorLine;
		TextView itemTitle, itemQuantity;
		ImageView infoDetail;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = View.inflate(context, resId, null);

			holder = new ViewHolder();

			holder.colorLine = (LinearLayout) convertView.findViewById(R.id.colorLine);
			holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
			holder.itemQuantity = (TextView) convertView.findViewById(R.id.itemQuantity);
			holder.infoDetail = (ImageView) convertView.findViewById(R.id.infoDetail);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		 
		Item tmp = items.get(position);

		if(tmp != null){
			String uri = tmp.getColor();
            int color_id = context.getResources().getIdentifier(uri, "color", context.getPackageName());
            holder.colorLine.setBackgroundColor(context.getResources().getColor(color_id));
			holder.itemTitle.setText(tmp.getTitle());
			holder.itemQuantity.setText(tmp.getQuantity()+"");
			if (tmp.getDescription().trim().compareTo("")==0)
				holder.infoDetail.setVisibility(View.INVISIBLE);
			else
				holder.infoDetail.setVisibility(View.VISIBLE);
		}
		
		if (tmp.isDone()){
			AlphaAnimation alpha = new AlphaAnimation(1f,0.45f);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			convertView.startAnimation(alpha);
			convertView.setBackgroundResource(R.drawable.strikeline);
		} else {
			AlphaAnimation alpha = new AlphaAnimation(0.45f,1f);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			convertView.startAnimation(alpha);
			convertView.setBackgroundResource(0);
		}
		
		return convertView;
	}
}
