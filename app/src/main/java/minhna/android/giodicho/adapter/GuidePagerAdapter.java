package minhna.android.giodicho.adapter;

import minhna.android.giodicho.resources.GuidePage;
import minhna.android.giodicho.resources.PocketBasketResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GuidePagerAdapter extends FragmentPagerAdapter{

	public GuidePagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int pos) {
		return GuidePage.newInstance(pos);
	}

	@Override
	public int getCount() {
		return PocketBasketResource.assetImages.length;
	}
}
