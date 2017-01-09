package wumf.com.sharedapps.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.IHideShow;
import wumf.com.sharedapps.OnBackPressedListener;

/**
 * Created by max on 09.01.17.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<CharSequence> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, CharSequence title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public OnBackPressedListener getCurrentOnBackPressedListener(int position) {
        return (OnBackPressedListener) mFragmentList.get(position);
    }

    public void onPageSelected(int position) {
        for (int i = 0; i < mFragmentList.size(); i++) {
            IHideShow fr = (IHideShow) mFragmentList.get(i);
            if (i == position) {
                fr.show();
            } else {
                fr.hide();
            }
        }
    }
}
