package wumf.com.sharedapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

import wumf.com.sharedapps.view.MyAccountView;

/**
 * Created by max on 13.09.16.
 */
public class PersonFragment extends Fragment implements IHideShow, OnBackPressedListener {

    private SignInButton signInButton;
    private MyAccountView myAccountView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        GoogleSignInOptions gso = ((MainApplication) getActivity().getApplication()).gso;
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        myAccountView = (MyAccountView) view.findViewById(R.id.my_account_view);

        return view;
    }

    public void onStart() {
        super.onStart();
        FirebaseUser user = ((MainActivity) getActivity()).currentUser;
        if (user != null) {
            myAccountView.setUser(user);
            myAccountView.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
        } else {
            myAccountView.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
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

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(((MainActivity) getActivity()).gac);
        getActivity().startActivityForResult(signInIntent, MainActivity.REQUEST_CODE_RC_SIGN_IN);
    }

}
