package com.mujadidiainc.youtubelikebottomnavigation;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private Stack<String> fragmentHistory=new Stack();


    final Fragment feed = new feed();
    final Fragment ramadan = new MyRamadan();
    final Fragment Quran = new Quran();
    final Fragment Mahasba = new mahasba();
    final Fragment report = new report();
    final FragmentManager fm = getSupportFragmentManager();


    Fragment active = feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        removeDefaultAnimationOFBottom();
        setUpFragments();
        pushRootFragment();
        bottomNavigationListener();
    }

    private void bottomNavigationListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.feed_id:
                        fm.beginTransaction().addToBackStack("1").hide(active).show(feed).commit();
                        active = feed;
                        MaintainFragmentStack("1");
                        //   setPage("1",feed);
                        return true;
                    case R.id.my_ramadan_id:
                       fm.beginTransaction().addToBackStack("2").hide(active).show(ramadan).commit();
                        active = ramadan;
                      //  setPage("2",ramadan);
                        MaintainFragmentStack("2");
                        return true;
                    case R.id.quran_id:
                        fm.beginTransaction().addToBackStack("3").hide(active).show(Quran).commit();
                        active = Quran;
                       // setPage("3",Quran);
                        MaintainFragmentStack("3");

                        return true;
                    case R.id.muhasba_id:

                        fm.beginTransaction().addToBackStack("4").hide(active).show(Mahasba).commit();
                        active = Mahasba;

                        MaintainFragmentStack("4");
                     //   setPage("4",Mahasba);

                        return true;
                    case R.id.report_id:

                        fm.beginTransaction().addToBackStack("5").hide(active).show(report).commit();
                        active = report;

                       // setPage("5",report);
                        MaintainFragmentStack("5");
                        return true;
                }
                return true;
            }

        });
    }

    private void pushRootFragment() {
        fragmentHistory.push("1");
    }

    private void setUpFragments() {
        fm.beginTransaction().add(R.id.main_frame, ramadan, "2").hide(ramadan).commit();
        fm.beginTransaction().add(R.id.main_frame, Quran, "3").hide(Quran).commit();
        fm.beginTransaction().add(R.id.main_frame, Mahasba, "4").hide(Mahasba).commit();
        fm.beginTransaction().add(R.id.main_frame, report, "5").hide(report).commit();
        fm.beginTransaction().add(R.id.main_frame,feed, "1").addToBackStack("1").commit();
    }

    private void MaintainFragmentStack(String tag) {
        if (fragmentHistory.contains(tag))
        {
            fragmentHistory.remove(tag);
            fragmentHistory.push(tag);
        }
        else
        {
            fragmentHistory.push(tag);
        }
    }

    private void initViews() {
        bottomNavigation=findViewById(R.id.bottom_navigation_id);

    }
    private void removeDefaultAnimationOFBottom() {
        removeShiftMode(bottomNavigation);
    }

    @SuppressLint("RestrictedApi")
    public void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }

        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }


    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        for (String index:fragmentHistory)
        {
            Log.e("fragment stack", "index: "+index);
        }
//


        if (fragmentHistory.size()==1)

        {
            if (!fragmentHistory.pop().equalsIgnoreCase("1"))
                fragmentHistory.push("1");
        }
        else
        {
            fragmentHistory.pop();
        }

        if (fragmentHistory.size()<=0)
        {
            finish();

        }
        else
        {
            Log.e("pop", "index: "+ fragmentHistory.peek());
            setSelectedItem(fragmentHistory.peek());
        }

    }

    private void setSelectedItem(String tag) {
       bottomNavigation.setSelectedItemId( getItemID(tag));
    }

    private Integer getItemID(String tag) {
        if (tag.equalsIgnoreCase("1"))
            return R.id.feed_id;
        if (tag.equalsIgnoreCase("2"))
            return R.id.my_ramadan_id;
        if (tag.equalsIgnoreCase("3"))
            return R.id.quran_id;
        if (tag.equalsIgnoreCase("4"))
            return R.id.muhasba_id;
        if (tag.equalsIgnoreCase("5"))
            return R.id.report_id;
        return null;
    }
}
