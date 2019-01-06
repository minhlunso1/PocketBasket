package minhna.android.giodicho.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import minhna.android.giodicho.R;
import minhna.android.giodicho.pojo.DialogOption;

public class OptionAdapter extends ArrayAdapter<DialogOption> {
	private Context context;
	private int resId;
	private ArrayList<DialogOption> items;

	public OptionAdapter(Context context, int resId,
			ArrayList<DialogOption> items) {
		super(context, resId, items);
		this.context = context;
		this.resId = resId;
		this.items = items;
	}
	
	private class ViewHolder {
		private ImageView icon;
		private TextView itemTitle;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = View.inflate(context, resId, null);

			holder = new ViewHolder();

			holder.icon = (ImageView) convertView.findViewById(R.id.option_icon);
			holder.itemTitle = (TextView) convertView.findViewById(R.id.option_title);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		 
		DialogOption tmp = items.get(position);

		if(tmp != null){
			String uri = tmp.getIcon();
            context.getResources();
			int id = Resources.getSystem().getIdentifier(tmp.getIcon(), "drawable", "android");  
            holder.icon.setImageResource(id);
			holder.itemTitle.setText(tmp.getTitle());
		}
		
		return convertView;
	}
}
