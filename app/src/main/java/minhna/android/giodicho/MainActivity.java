package minhna.android.giodicho;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import minhna.android.giodicho.iview.IAnimationEvent;
import minhna.android.giodicho.resources.Constant;
import minhna.android.giodicho.resources.PocketBasketResource;
import minhna.android.giodicho.utils.DimenUtils;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager manager;
    private Toolbar toolbar;
    private View drawerLeft;
    private SlidingRootNav slidingRootNav;
    private CardView cardMain;
    private IAnimationEvent drawerAnimationEvent;
    private Dialog aboutDialog;

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    private DragStateListener dragDrawerLeftListener = new DragStateListener() {
        @Override
        public void onDragStart() {
            if (drawerLeft.getVisibility() == View.VISIBLE)
                drawerLeft.setVisibility(View.GONE);
        }

        @Override
        public void onDragEnd(boolean isMenuOpened) {
            if (isMenuOpened)
                drawerLeft.setVisibility(View.VISIBLE);
            drawerAnimationEvent.onAnimationEnd();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.Toolbar_Popup);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawler_left)
                .addDragStateListener(dragDrawerLeftListener)
                .inject();
        drawerLeft = findViewById(R.id.drawer_left);
        drawerLeft.setVisibility(View.GONE);

        cardMain = findViewById(R.id.card_main);

        start();
    }

    //prevent rotating
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    }

    public void start() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("version", "1.0").compareTo(getString(R.string.app_version)) != 0) {
            editor.putString("version", getString(R.string.app_version));
            editor.apply();
            startGuide();
        } else {
            startUsing();
        }
    }

    public void startUsing() {
        MasterFragment fragment = new MasterFragment();
        drawerAnimationEvent = fragment;
        manager.beginTransaction().replace(R.id.contain, fragment, "first")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        toolbar.setVisibility(View.VISIBLE);
    }

    public void startGuide() {
        toolbar.setVisibility(View.GONE);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.contain, new GuideFragment(), "guide");
        transaction.commit();
    }

    public void toggleDrawerLeft() {
        if (slidingRootNav.isMenuOpened()) {
            slidingRootNav.closeMenu(true);
            cardMain.setRadius(0);
        } else {
            slidingRootNav.openMenu(true);
            cardMain.setRadius(DimenUtils.dptopx(this, 2));
        }
    }

    public SlidingRootNav getSlidingRootNav() {
        return slidingRootNav;
    }

    public void onMenuItemClick(View v) {
        switch (v.getId()) {
            case R.id.action_support:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getString(R.string.packet_name))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getString(R.string.packet_name))));
                }
                break;
            case R.id.action_get_more:
                Toast.makeText(this, getString(R.string.not_available), Toast.LENGTH_LONG).show();
                break;
            case R.id.action_about:
                callAboutDialog();
                break;
            case R.id.action_tip:
                toggleDrawerLeft();
                startGuide();
                break;
            default:
                break;
        }
    }

    public void callAboutDialog(){
        if (aboutDialog==null){
            View view = View.inflate(this, R.layout.dialog_about, null);
            if (PocketBasketResource.api<11){
                TextView tvAuthorName = (TextView) view.findViewById(R.id.tvAuthorName);
                tvAuthorName.setTextColor(getResources().getColor(android.R.color.white));
            }
            aboutDialog = new Dialog(this, R.style.FullHeightDialog);
            aboutDialog.setContentView(view);
            aboutDialog.setTitle(getString(R.string.original_app_name) + " " + getString(R.string.app_version));
        }
        aboutDialog.show();
    }
}