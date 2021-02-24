package com.rav.raverp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.rav.raverp.BuildConfig;
import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.View.ExpandableNavigationListView;
import com.rav.raverp.data.interfaces.StoragePermissionListener;
import com.rav.raverp.data.model.ChildModel;
import com.rav.raverp.data.model.HeaderModel;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.GetProfileModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.WalletAccessResponse;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;


import com.rav.raverp.ui.fragment.Associate.AddAssociateFragment;
import com.rav.raverp.ui.fragment.Associate.AvailablePlotFragment;
import com.rav.raverp.ui.fragment.Associate.AddTicketFragment;
import com.rav.raverp.ui.fragment.Associate.ClosedTicketFragment;
import com.rav.raverp.ui.fragment.Associate.DashBoardAssociateFragment;
import com.rav.raverp.ui.fragment.Associate.FollowUpLeadListFragment;
import com.rav.raverp.ui.fragment.Associate.LeadListFragment;
import com.rav.raverp.ui.fragment.Associate.MyBookingFragment;
import com.rav.raverp.ui.fragment.Associate.MyGoalListFragment;

import com.rav.raverp.ui.fragment.Associate.PendingTicketFragment;
import com.rav.raverp.ui.fragment.Associate.SiteVisitRequestFragment;
import com.rav.raverp.ui.fragment.Associate.SiteVisitRequestStatusFragment;
import com.rav.raverp.ui.fragment.Associate.WalletPinFragment;
import com.rav.raverp.ui.fragment.Customer.DashBoardCustomerFragment;
import com.rav.raverp.ui.fragment.Customer.PlotBookingAccountDetailsFragment;
import com.rav.raverp.ui.fragment.Customer.PlotBookingDetailsFragment;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements StoragePermissionListener, NavigationView.OnNavigationItemSelectedListener {

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    public static String mobile = "mobile";
    public static String userid = "userid";
    public static Toolbar toolbar;
    TextView editprodile;
    DrawerLayout drawer;
    private LoginModel login;
    private ApiHelper apiHelper;
    TextView Name, Role;
    ImageView iamgeprofile;
    TextView textView;
    private boolean isPermissionGranted = false;
    private boolean isFromPermissionSettings = false;
    private int lastExpandedPosition = -1;


    private ExpandableNavigationListView navigationExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.checkPermissions(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        setStoragePermissionListener(this);
        login = MyApplication.getLoginModel();
        textView = (TextView) findViewById(R.id.txtversion);
        // textView.setText("Version : " + BuildConfig.VERSION_NAME + ", Date : " + CommonUtils.getCurrentDate());
        textView.setText("Version : " + BuildConfig.VERSION_NAME);


        navigationExpandableListView = (ExpandableNavigationListView) findViewById(R.id.expandable_navigation);


        drawer = findViewById(R.id.drawer_layouts);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        Name = (TextView) header.findViewById(R.id.name);
        iamgeprofile = (ImageView) header.findViewById(R.id.iamgeprofile);
        Role = (TextView) header.findViewById(R.id.role);


        apiHelper = ApiClient.getClient().create(ApiHelper.class);


        LoginModel login = MyApplication.getLoginModel();
        if (login.getStrRole().equalsIgnoreCase("Associate")) {
            show1stDashboard();
        }
        if (login.getStrRole().equalsIgnoreCase("Customer")) {
            show2ndDashboard();
        }
        if (login.getStrRole().equalsIgnoreCase("Admin")) {

        }
        if (login.getStrRole().equalsIgnoreCase("")) {

        }

        navigationExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    navigationExpandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProfileApi();
    }

    private void GetProfileApi() {

        String loginid = login.getStrLoginID();
        Integer role = login.getIntRoleID();
        Call<ApiResponse<List<GetProfileModel>>> GetProfileCall = apiHelper.getProfile(loginid, role);
        GetProfileCall.enqueue(new Callback<ApiResponse<List<GetProfileModel>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<GetProfileModel>>> call,
                                   @NonNull Response<ApiResponse<List<GetProfileModel>>> response) {

                if (response.isSuccessful()) {

                    if (response != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {
                            List<GetProfileModel> login1 = response.body().getBody();

                            String profile = login1.get(0).getStrProfilePic();
                            String email = login1.get(0).getStrEmail();
                            String mobile = login1.get(0).getStrPhone();
                            String role = login1.get(0).getStrRole();
                            Role.setText(role);


                            String name = login1.get(0).getStrDisplayName();
                            Name.setText(name);

                            Glide.with(iamgeprofile.getContext()).load("https://ravgroup.org" + profile)
                                    .placeholder(R.drawable.account)
                                    .into(iamgeprofile);

                        } else {


                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<GetProfileModel>>> call,
                                  @NonNull Throwable t) {
                if (!call.isCanceled()) {

                    ViewUtils.showToast(t.getLocalizedMessage());
                }
                t.printStackTrace();
            }
        });

    }

    private void show1stDashboard() {
        navigationExpandableListView
                .init(this)
                .addHeaderModel(new HeaderModel("Dashboard", R.drawable.ic_home_black_24dp))
                .addHeaderModel(
                        new HeaderModel("Plot Moduls", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Plot Availability", R.drawable.ic_baseline_arrow_forward_24))
                                .addChildModel(new ChildModel("My Booking", R.drawable.ic_baseline_arrow_forward_24)))
                .addHeaderModel(
                        new HeaderModel("Wallet", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Add Wallet", R.drawable.ic_baseline_arrow_forward_24)))

                .addHeaderModel(
                        new HeaderModel("Lead Management", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Lead List", R.drawable.ic_baseline_arrow_forward_24))
                                .addChildModel(new ChildModel("Follow UP Lead List", R.drawable.ic_baseline_arrow_forward_24)))
                .addHeaderModel(
                        new HeaderModel("Site Visit", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Site Visit Request", R.drawable.ic_baseline_arrow_forward_24))
                                .addChildModel(new ChildModel("Site Visit Request Status", R.drawable.ic_baseline_arrow_forward_24)))
                .addHeaderModel(
                        new HeaderModel("Associate", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Add Associate", R.drawable.ic_baseline_arrow_forward_24)))
                .addHeaderModel(
                        new HeaderModel("Goal", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("My Goal", R.drawable.ic_baseline_arrow_forward_24)))
                .addHeaderModel(new HeaderModel("Support", R.drawable.ic_baseline_my_location_24, true)
                        .addChildModel(new ChildModel("Create New Ticket", R.drawable.ic_baseline_arrow_forward_24))
                        .addChildModel(new ChildModel("Ticket List", R.drawable.ic_baseline_arrow_forward_24)))

                .build()

                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                        navigationExpandableListView.setSelected(groupPosition);
                        //drawer.closeDrawer(GravityCompat.START);
                        if (id == 0) {
                            toolbar.setTitle("Dashboard");
                            Fragment fragment = new DashBoardAssociateFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment, "MYFRAGMENT");
                            transaction.addToBackStack("MYFRAGMENT").commit();
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (id == 1) {


                        } else if (id == 2) {

                        } else if (id == 3) {

                        }
                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition, childPosition);

                        if (groupPosition == 1) {
                            if (id == 0) {
                                toolbar.setTitle("Plot Available");
                                loadFragment(new AvailablePlotFragment());
                            }
                            if (id == 1) {
                                toolbar.setTitle("My Booking");
                                loadFragment(new MyBookingFragment());
                            }
                        } else if (groupPosition == 2) {
                            if (id == 0) {
                                PinAccess();

                            }
                        } else if (groupPosition == 3) {
                            if (id == 0) {
                                toolbar.setTitle("Lead List");
                                loadFragment(new LeadListFragment());
                            }
                            if (id == 1) {
                                toolbar.setTitle("Follow UP List");
                                loadFragment(new FollowUpLeadListFragment());


                            }
                        } else if (groupPosition == 4) {
                            if (id == 0) {
                                toolbar.setTitle("Site Visit");
                                loadFragment(new SiteVisitRequestFragment());

                            }

                            if (id == 1) {
                                toolbar.setTitle("Site Visit List");
                                loadFragment(new SiteVisitRequestStatusFragment());

                            }
                        } else if (groupPosition == 5) {
                            if (id == 0) {
                                toolbar.setTitle("Associate Registration...!");
                                loadFragment(new AddAssociateFragment());

                            }
                        } else if (groupPosition == 6) {
                            if (id == 0) {
                                toolbar.setTitle("Goal List");
                                loadFragment(new MyGoalListFragment());
                            }

                        } else if (groupPosition == 7) {
                            if (id == 0) {
                                toolbar.setTitle("Create New Ticket");
                                loadFragment(new AddTicketFragment());

                            }
                            if (id == 1) {
                                toolbar.setTitle("Ticket List");
                                loadFragment(new PendingTicketFragment());
                            }

                        }


                        return false;
                    }
                });

        Fragment fragment = new DashBoardAssociateFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.homepage, fragment, "MYFRAGMENT");
        transaction.addToBackStack("MYFRAGMENT").commit();
        navigationExpandableListView.setSelected(0);
    }

    private void PinAccess() {

        String loginid = login.getStrLoginID();
        int roleid = login.getIntRoleID();


        ViewUtils.startProgressDialog((this));

        Call<WalletAccessResponse> getWalletAccessResponseCall =
                apiHelper.getWalletPinAccess(loginid, roleid);

        getWalletAccessResponseCall.enqueue(new Callback<WalletAccessResponse>() {
            @Override
            public void onResponse(Call<WalletAccessResponse> call,
                                   Response<WalletAccessResponse> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getWalletPinStatus().toString().equalsIgnoreCase("false")) {

                            toolbar.setTitle("Wallet");
                            Fragment fragment = new WalletPinFragment("false");
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment);
                            transaction.addToBackStack(null).commit();
                            drawer.closeDrawer(GravityCompat.START);
                        } else if (response.body().getWalletPinStatus().toString().equalsIgnoreCase("true")) {
                            toolbar.setTitle("Wallet");
                            Fragment fragment = new WalletPinFragment("true");
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment);
                            transaction.addToBackStack(null).commit();
                            drawer.closeDrawer(GravityCompat.START);

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<WalletAccessResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });
    }


    private void show2ndDashboard() {
        navigationExpandableListView
                .init(this)
                .addHeaderModel(new HeaderModel("Dashboard", R.drawable.ic_home_black_24dp))
                .addHeaderModel(
                        new HeaderModel("My Accounts", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Account", R.drawable.ic_baseline_arrow_forward_24)))

                .addHeaderModel(
                        new HeaderModel("Plot Booking ", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Plot Booking Details", R.drawable.ic_baseline_arrow_forward_24)))
                .addHeaderModel(
                        new HeaderModel("KYC", R.drawable.ic_baseline_my_location_24, true)
                                .addChildModel(new ChildModel("Customer KYC", R.drawable.ic_baseline_arrow_forward_24)))

                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                        navigationExpandableListView.setSelected(groupPosition);
                        //drawer.closeDrawer(GravityCompat.START);
                        if (id == 0) {
                            toolbar.setTitle("Dashboard");
                            Fragment fragment = new DashBoardCustomerFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homepage, fragment, "MYFRAGMENT");
                            transaction.addToBackStack("MYFRAGMENT").commit();
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition, childPosition);

                        if (groupPosition == 1) {
                            if (id == 0) {
                                toolbar.setTitle("Plot Booking Account Details");
                                Fragment fragment = new PlotBookingAccountDetailsFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            if (id == 1) {

                            }
                        } else if (groupPosition == 2) {
                            if (id == 0) {
                                toolbar.setTitle("Plot Booking Details");
                                Fragment fragment = new PlotBookingDetailsFragment();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homepage, fragment);
                                transaction.addToBackStack(null).commit();
                                drawer.closeDrawer(GravityCompat.START);
                            }

                            if (id == 1) {


                            }
                        } else if (groupPosition == 3) {
                            if (id == 0) {


                            }

                            if (id == 1) {


                            }
                        }

                        return false;
                    }
                });

        Fragment fragment = new DashBoardCustomerFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.homepage, fragment, "MYFRAGMENT");
        transaction.addToBackStack("MYFRAGMENT").commit();
        navigationExpandableListView.setSelected(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layouts);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layouts);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            DashBoardAssociateFragment fragment;
            fragment = (DashBoardAssociateFragment) getSupportFragmentManager().findFragmentByTag("MYFRAGMENT");
            if (fragment == null) {
                toolbar.setTitle("Dashboard");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.homepage, new DashBoardAssociateFragment(), "MYFRAGMENT");
                fragmentTransaction.addToBackStack("MYFRAGMENT").commit();
            } else {
                if (fragment.isVisible()) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                } else {
                    toolbar.setTitle("Dashboard");
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.homepage, new DashBoardAssociateFragment(), "MYFRAGMENT");
                    fragmentTransaction.addToBackStack("MYFRAGMENT").commit();
                }
            }


        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] scrcoords = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void isStoragePermissionGranted(boolean granted) {
        isPermissionGranted = granted;
    }

    @Override
    public void isUserPressedSetting(boolean pressed) {
        isFromPermissionSettings = pressed;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.homepage, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    private void popupSnackbarForCompleteUpdate() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView,
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE);


        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }
}