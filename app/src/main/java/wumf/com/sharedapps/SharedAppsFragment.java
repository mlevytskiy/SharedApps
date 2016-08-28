package wumf.com.sharedapps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

/**
 * Created by max on 22.08.16.
 */
public class SharedAppsFragment extends Fragment {

    private SpringFloatingActionMenu springFloatingActionMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_apps, container, false);

        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(getContext())

                .build();

        return view;
    }

    private FloatingActionButton createFloatingActionButton() {
        final FloatingActionButton fab = new FloatingActionButton(getContext());
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        return fab;
    }

}
