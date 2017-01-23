package com.intellisysla.rmsscanner.UI.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.intellisysla.rmsscanner.BLL.ActivityCommunicator;
import com.intellisysla.rmsscanner.BLL.Utils;
import com.intellisysla.rmsscanner.DAL.Department;
import com.intellisysla.rmsscanner.DAL.Item;
import com.intellisysla.rmsscanner.DAL.Store;
import com.intellisysla.rmsscanner.R;
import com.intellisysla.rmsscanner.UI.View.Fragment.AliasFragment;
import com.intellisysla.rmsscanner.UI.View.Fragment.DepartmentFragment;
import com.intellisysla.rmsscanner.UI.View.Fragment.PriceFragment;
import com.intellisysla.rmsscanner.UI.View.Fragment.SearchFragment;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
        PriceFragment.OnFragmentInteractionListener,
        AliasFragment.OnFragmentInteractionListener,
        ActivityCommunicator
{

    public static final String TAG = "SearchActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> registeredFragments;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void passDataToActivity(Item item) {
        DepartmentFragment departmentFragment = (DepartmentFragment) registeredFragments.get(1);
        departmentFragment.SetItem(item);
        PriceFragment priceFragment = (PriceFragment) registeredFragments.get(2);
        priceFragment.SetItem(item);
        AliasFragment aliasFragment = (AliasFragment) registeredFragments.get(3);
        aliasFragment.SetItem(item);
    }

    @Override
    public void Clear() {
        DepartmentFragment departmentFragment = (DepartmentFragment) registeredFragments.get(1);
        departmentFragment.Clear();
        PriceFragment priceFragment = (PriceFragment) registeredFragments.get(2);
        priceFragment.Clear();
        AliasFragment aliasFragment = (AliasFragment) registeredFragments.get(3);
        aliasFragment.Clear();
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
            Intent intent = getIntent();
            Item foundItem = (Item) intent.getSerializableExtra("item");

            registeredFragments = new ArrayList<>();
            registeredFragments.add(SearchFragment.newInstance(foundItem));
            registeredFragments.add(DepartmentFragment.newInstance());
            registeredFragments.add(PriceFragment.newInstance());
            registeredFragments.add(AliasFragment.newInstance());
        }


        @Override
        public Fragment getItem(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public int getCount() {
            return registeredFragments.size();
        }
    }
}
