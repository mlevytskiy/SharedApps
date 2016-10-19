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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import wumf.com.sharedapps.eventbus.GetNewCountryEvent;
import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.SignInFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.SignOutFromFirebaseEvent;
import wumf.com.sharedapps.firebase.UsersFirebase;
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
        myAccountView.setOnViberClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getContext(), ViberTransparentActivity.class));
            }
        });

        return view;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(GetNewCountryEvent event) {
        myAccountView.updateCountry(event.country);
    }

    @Subscribe
    public void onEvent(NewCountryCodeFromFirebaseEvent event) {
        myAccountView.updateCountry(event.countryCode);
    }

    @Subscribe
    public void onEvent(SignInFromFirebaseEvent event) {
        FirebaseUser user = ((MainActivity) getActivity()).currentUser;
        myAccountView.setUser(user);
        UsersFirebase.addMe(user);
        myAccountView.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEvent(SignOutFromFirebaseEvent event) {
        myAccountView.setVisibility(View.GONE);
        signInButton.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onEvent(NewPhoneNumberFromFirebaseEvent event) {
        myAccountView.updatePhoneNumber(event.phone);
        MainApplication.instance.phoneNumber = event.phone;
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
