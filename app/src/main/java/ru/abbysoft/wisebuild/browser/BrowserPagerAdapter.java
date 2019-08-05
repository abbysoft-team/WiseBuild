package ru.abbysoft.wisebuild.browser;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.abbysoft.wisebuild.PartListFragment;
import ru.abbysoft.wisebuild.model.ComputerPart;

public class BrowserPagerAdapter extends FragmentPagerAdapter {
    private PartListFragment.OnPartListItemClickedListener clickedListener;

    BrowserPagerAdapter(FragmentManager fm, PartListFragment.OnPartListItemClickedListener clickedListener) {
        super(fm);

        this.clickedListener = clickedListener;
    }

    @Override
    public Fragment getItem(int position) {
        PartListFragment fragment = PartListFragment.newInstance(
                ComputerPart.ComputerPartType.values()[position]);

        fragment.setPartClickedListener(this.clickedListener);
        return fragment;
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
