package wumf.com.sharedapps.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wumf.com.sharedapps.IHideShow;
import wumf.com.sharedapps.OnBackPressedListener;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.adapter.ViewPagerAdapter;
import wumf.com.sharedapps.mockfragment.MockFragment;

/**
 * Created by max on 22.08.16.
 */
public class SearchFragment extends Fragment implements IHideShow, OnBackPressedListener {

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {
        setupViewPager(viewPager);
    }

    @Override
    public boolean doBack(int delay) {
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AllAppsFragment(), "all apps");
        adapter.addFrag(new MockFragment(), "by people" );
        viewPager.setAdapter(adapter);
    }

}
