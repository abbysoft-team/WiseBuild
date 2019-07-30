package ru.abbysoft.wisebuild.browser;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.abbysoft.wisebuild.PartListFragment;
import ru.abbysoft.wisebuild.model.ComputerPart;

public class BrowserPagerAdapter extends FragmentPagerAdapter {
    public BrowserPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PartListFragment.newInstance(ComputerPart.ComputerPartType.values()[position]);
    }

    @Override
    public int getCount() { // number of categories
        return ComputerPart.ComputerPartType.values().length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return ComputerPart.ComputerPartType.values()[position].getReadableName();
    }
}
