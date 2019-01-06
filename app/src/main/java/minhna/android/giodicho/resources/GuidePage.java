package minhna.android.giodicho.resources;

import java.io.IOException;
import java.io.InputStream;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GuidePage extends Fragment {

	private int pos;

	public static GuidePage newInstance(int pos) {
		GuidePage dummyFragment = new GuidePage();
		dummyFragment.pos = pos;
		return dummyFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView img = new ImageView(getActivity());
		String fileName = "guide"+(pos+1)+".png";
		InputStream ims=null;
		try {
			ims = getActivity().getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ims!=null){
		    Drawable d = Drawable.createFromStream(ims, fileName);
		    img.setScaleType(ScaleType.FIT_XY);
		    img.setImageDrawable(d);
		    try {
		    	d=null;
				ims.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.gc();
		return img;	
	}
}