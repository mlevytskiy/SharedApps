package wumf.com.sharedapps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hugo.weaving.DebugLog;
import wumf.com.appsprovider.App;
import wumf.com.sharedapps.adapter.ViewPagerAdapter;
import wumf.com.sharedapps.dialog.ExitDialog;
import wumf.com.sharedapps.eventbus.GetNewCountryEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromViber;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.eventbus.SignInFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.SignOutFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.WeAlreadyGetCountryCodeFromSystemEvent;
import wumf.com.sharedapps.firebase.FavouriteAppsFirebase;
import wumf.com.sharedapps.firebase.FirebaseIcons;
import wumf.com.sharedapps.firebase.IconUrlCallback;
import wumf.com.sharedapps.firebase.UsersFirebase;
import wumf.com.sharedapps.fragment.PersonFragment;
import wumf.com.sharedapps.fragment.SearchFragment;
import wumf.com.sharedapps.fragment.SharedAppsFragment;
import wumf.com.sharedapps.memory.Key;
import wumf.com.sharedapps.memory.MemoryCommunicator;
import wumf.com.sharedapps.util.AppFinderUtils;
import wumf.com.sharedapps.util.GooglePlayIntentApi;

@DebugLog
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static final int REQUEST_CODE_CHIOCE_APP = 544;
    public static final int REQUEST_CODE_RC_SIGN_IN = 543;
    public static final String PACKAGE_NAME = "packageName";

    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private int currentFragmentIndex = 0;
    public GoogleApiClient gac;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private long firebaseAuthListenerCalledDate = 0;
    private ViewPager viewPager;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.tabanim_appbar);

        viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new OnPageChangeListener());

        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, ((MainApplication) getApplication()).gso)
                .addApi(LocationServices.API)
                .build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuthListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isFirstStart = !MemoryCommunicator.getInstance().hasKey(Key.isFirstStart);
            if (isFirstStart) {
                startActivity(new Intent(this, Android6HelpActivity.class));
                MemoryCommunicator.getInstance().saveBoolean(Key.isFirstStart, false);
            }
        }


    }

    public View getContainer() {
        return container;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        gac.connect();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        gac.disconnect();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(NewPhoneNumberFromViber event) {
        UsersFirebase.updatePhoneNumber(CurrentUser.getUID(), event.phone);
    }

    @Subscribe
    public void onEvent(SignOutFromFirebaseEvent event) {
        UsersFirebase.removeMe(CurrentUser.getUID());
        FirebaseAuth.getInstance().signOut();
        CurrentUser.set(null);
        MainApplication.instance.phoneNumber = null;
        Auth.GoogleSignInApi.signOut(gac);
    }

    @Override
    public void onBackPressed() {
        boolean doBack = adapter.getCurrentOnBackPressedListener(currentFragmentIndex).doBack(0);
        if (!doBack) {
            if (currentFragmentIndex == 0) {
                new ExitDialog(this);
            } else {
                viewPager.setCurrentItem(0);
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_CHIOCE_APP) {
            if (resultCode == RESULT_OK) {
                final App app = AppFinderUtils.find(data.getExtras().getString(PACKAGE_NAME));
                addAppInFirebase(app);
                adapter.getCurrentOnBackPressedListener(currentFragmentIndex).doBack(300);
            } else {
                //do nothing
            }
        } else if (requestCode == REQUEST_CODE_RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, updateMyApps UI appropriately
            }
        }
    }

    private void addAppInFirebase(final App app) {
        FirebaseIcons.getIconUrl(app.appPackage, new IconUrlCallback() {
            @Override
            public void receive(String icon) {
                FavouriteAppsFirebase.addApp(CurrentUser.getUID(), app.appPackage, app.name, icon);
            }
        }, app.icon);
    }

    @Subscribe
    public void onEvent(OnClickAppEvent event) {
        if ( event.isNeedAddOnFirebase ) {
            String packageName = event.appPackage;
            if (CurrentUser.get() == null) {
                Toast.makeText(MainActivity.this, "You need registration", Toast.LENGTH_LONG).show();
                return;
            }
            App app = AppFinderUtils.find(packageName);
            if (app == null) {
                Toast.makeText(MainActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
                return;
            }
            addAppInFirebase(app);
        } else {
            startActivity( GooglePlayIntentApi.getOpenAppPageIntent(event.appPackage) );
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new GoogleAuthOnCompleteListener(this));
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SharedAppsFragment(), getTabTitle(R.drawable.ic_heart));
        adapter.addFrag(new PersonFragment(), getTabTitle(R.drawable.ic_person));
        adapter.addFrag(new SearchFragment(), getTabTitle(R.drawable.ic_search));
        viewPager.setAdapter(adapter);
    }

    private CharSequence getTabTitle(@DrawableRes int drawableResId) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(" ");
        Drawable dr = getResources().getDrawable(drawableResId);
//        int color = getResources().getColor(R.color.photo);
//        dr.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(dr, DynamicDrawableSpan.ALIGN_BOTTOM);
        stringBuilder.setSpan(imageSpan, 0, 1, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed=" + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Location location = LocationServices.FusedLocationApi.getLastLocation(gac);
                if (location == null) {
                    return;
                }
                Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if ( !addresses.isEmpty() ) {
                        MainApplication.instance.country = addresses.get(0).getCountryCode();
                        if (CurrentUser.get() == null) {
                            EventBus.getDefault().post(new WeAlreadyGetCountryCodeFromSystemEvent());
                        } else {
                            UsersFirebase.updateCountryCode(CurrentUser.getUID(), MainApplication.instance.country);
                            EventBus.getDefault().post(new GetNewCountryEvent(MainApplication.instance.country));
                        }
                    }
                } catch (IOException e) {
                    Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @DebugLog
    private static class GoogleAuthOnCompleteListener implements OnCompleteListener<AuthResult> {

        private Context context;

        public GoogleAuthOnCompleteListener(Context context) {
            this.context = context;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            EventBus.getDefault().post(new SignInFromFirebaseEvent());

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful()) {
                Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @DebugLog
    private class FirebaseAuthListener implements FirebaseAuth.AuthStateListener {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            long newDate = new Date().getTime();
            if (newDate - firebaseAuthListenerCalledDate < 500) {
                return;
            } else {
                firebaseAuthListenerCalledDate = newDate;
            }
            FirebaseUser user = firebaseAuth.getCurrentUser();
            CurrentUser.set(user);
        }

    }

    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @DebugLog
        @Override
        public void onPageSelected(int position) {
            currentFragmentIndex = position;
            adapter.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
