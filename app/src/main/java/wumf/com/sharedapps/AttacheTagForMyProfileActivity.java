package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import wumf.com.sharedapps.firebase.TagsFirebase;
import wumf.com.sharedapps.firebase.TransactionResultListener;
import wumf.com.sharedapps.util.KeyboardUtils;
import wumf.com.sharedapps.view.CustomTopBar;

import static com.sdsmdg.tastytoast.TastyToast.makeText;

/**
 * Created by max on 07.12.16.
 */

public class AttacheTagForMyProfileActivity extends Activity {

    public static final String KEY_USER_UID = "uid";

    private String uid;
    private TagCloudLinkView tagsTextView;
    private EditText editText;
    private View attacheTag;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        uid = getIntent().getExtras().getString(KEY_USER_UID);
        setContentView(R.layout.activity_attache_tag_for_my_profile);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Attache tag for my firebaseUser").bind(this);
        attacheTag = findViewById(R.id.attache_tag);
        attacheTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                KeyboardUtils.hideKeyboard(AttacheTagForMyProfileActivity.this);
                String tag = editText.getText().toString();
                if (TextUtils.isEmpty(tag)) {
                    makeText(view.getContext(), "You need enter a tag", TastyToast.LENGTH_LONG,
                            TastyToast.INFO);
                    return;
                }
                final Toast loadingToast = TastyToast.makeText(view.getContext(), "Loading", TastyToast.LENGTH_LONG,
                        TastyToast.DEFAULT);
                TagsFirebase.attachTag(uid, tag, new TransactionResultListenerImpl(loadingToast, AttacheTagForMyProfileActivity.this));

            }
        });

        tagsTextView = (TagCloudLinkView) findViewById(R.id.tags_text_view);
        editText = (EditText) findViewById(R.id.edit_text);
        tagsTextView.enableAutocompleteMode(editText);
        TagsFirebase.listenAllTags(new OnChangeAllTagsListener() {
            @Override
            public void onChange(List<String> tags) {
                tagsTextView.setAll(tags);
            }
        });
        tagsTextView.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener() {
            @Override
            public void onTagSelected(String tag, int position) {
                editText.getText().clear();
                editText.append(tag);
            }
        });
    }

    private static class TransactionResultListenerImpl implements TransactionResultListener {

        private Toast loadingToast;
        private Activity activity;

        public TransactionResultListenerImpl(Toast loadingToast, Activity activity) {
            this.loadingToast = loadingToast;
            this.activity = activity;
        }


        @Override
        public void onSuccess() {
            goBack();
            makeText(MainApplication.instance, "Successful!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }

        @Override
        public void onError() {
            goBack();
            makeText(MainApplication.instance, "Check you internet connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

        private void goBack() {
            loadingToast.cancel();
            activity.finish();
            activity = null;
            loadingToast = null;
        }

    }

}
