package wumf.com.sharedapps;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wumf.com.sharedapps.adapter.ViewPagerAdapter;
import wumf.com.sharedapps.mockfragment.MockFragment;

/**
 * Created by max on 22.08.16.
 */
public class SearchFragment extends Fragment implements IHideShow, OnBackPressedListener {

    private ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        return view;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public boolean doBack(int delay) {
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new MockFragment(), "all apps");
        adapter.addFrag(new MockFragment(), "by people" );
        viewPager.setAdapter(adapter);
    }

}
