package wumf.com.sharedapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.R;
import wumf.com.sharedapps.firebase.pojo.Profile;
import wumf.com.sharedapps.viewholder.PersonViewHolder;

/**
 * Created by max on 14.01.17.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private List<Profile> people = new ArrayList<>();

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.bind(people.get(position));
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public void updatePeople(List<Profile> _people) {
        people.clear();
        if (_people != null) {
            people.addAll(_people);
        }
        notifyDataSetChanged();
    }

}
