package wumf.com.sharedapps.view.findAndFollowSearchBarImpl;

import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import wumf.com.sharedapps.eventbus.SearchByNickOrNameOnClickEvent;

/**
 * Created by max on 17.01.17.
 */

public class SearchByNameOnClickListener implements View.OnClickListener {

    private EditText editText;

    public SearchByNameOnClickListener(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onClick(View view) {
        String query = editText.getText().toString();
        EventBus.getDefault().post( new SearchByNickOrNameOnClickEvent(query) );
    }

}
