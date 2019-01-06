package minhna.android.giodicho;

import me.relex.circleindicator.CircleIndicator;
import minhna.android.giodicho.adapter.GuidePagerAdapter;
import minhna.android.giodicho.resources.PocketBasketResource;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;

public class GuideFragment extends Fragment implements OnClickListener, OnPageChangeListener {	
	private MainActivity mainActivity;	
	private View guideView;
	private ViewPager guideVP;
	private AppCompatButton backGuide;
	private AppCompatButton nextGuide;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		guideView = inflater.inflate(R.layout.guide_layout, container, false);
		return guideView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mainActivity=(MainActivity)getActivity();
		AppCompatButton exitGuide = (AppCompatButton) guideView.findViewById(R.id.exitGuide);
		backGuide = (AppCompatButton) guideView.findViewById(R.id.backGuide);
		backGuide.setVisibility(View.GONE);
		nextGuide = (AppCompatButton) guideView.findViewById(R.id.nextGuide);

		CircleIndicator indicator = (CircleIndicator) guideView.findViewById(R.id.vp_indicator);
		guideVP = (ViewPager) guideView.findViewById(R.id.vpImages);
		GuidePagerAdapter adapter = new GuidePagerAdapter(getChildFragmentManager());
		guideVP.setAdapter(adapter);
		guideVP.setOffscreenPageLimit(1);
		indicator.setViewPager(guideVP);
		guideVP.addOnPageChangeListener(this);
			
		exitGuide.setOnClickListener(this);
		backGuide.setOnClickListener(this);
		nextGuide.setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.exitGuide:
				mainActivity.startUsing();
				break;
			case R.id.nextGuide:
				guideVP.setCurrentItem(guideVP.getCurrentItem()+1);
				break;
			case R.id.backGuide:
				guideVP.setCurrentItem(guideVP.getCurrentItem()-1);
				break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int page) {
		//not used for only two pages
		if (page>0){
			backGuide.setVisibility(View.VISIBLE);
			if (page<PocketBasketResource.assetImages.length-1)
				nextGuide.setVisibility(View.VISIBLE);
			else 
				nextGuide.setVisibility(View.GONE);
		} else 
			backGuide.setVisibility(View.GONE);
	}

}
