package wumf.com.sharedapps.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import java.util.List;

import wumf.com.sharedapps.adapter.PeopleAdapter;
import wumf.com.sharedapps.firebase.pojo.Profile;

/**
 * Created by max on 14.01.17.
 */

public class PeopleRecycleView extends RecyclerViewEmptySupport {

    private PeopleAdapter adapter;

    public PeopleRecycleView(Context context) {
        super(context);
        setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PeopleAdapter();
        setAdapter(adapter);
    }

    public PeopleRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new PeopleAdapter();
        setAdapter(adapter);
    }

    public void update(List<Profile> _people) {
        adapter.updatePeople(_people);
    }

}
