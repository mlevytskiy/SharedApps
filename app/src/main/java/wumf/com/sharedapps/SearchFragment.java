package wumf.com.sharedapps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by max on 22.08.16.
 */
public class SearchFragment extends Fragment implements IHideShow, OnBackPressedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public boolean doBack() {
        return false;
    }
}
