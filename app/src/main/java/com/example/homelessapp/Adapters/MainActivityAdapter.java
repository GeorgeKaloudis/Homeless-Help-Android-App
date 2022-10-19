package com.example.homelessapp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.homelessapp.LoginTabFragment;
import com.example.homelessapp.SignupTabFragment;

public class MainActivityAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;


    public MainActivityAdapter(@NonNull FragmentManager fm,Context context ,int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return null;


        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
