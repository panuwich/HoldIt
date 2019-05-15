package project.senior.holdit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Field;

import project.senior.holdit.fragment.TabAccount;
import project.senior.holdit.fragment.TabHome;
import project.senior.holdit.fragment.TabMyOrder;
import project.senior.holdit.fragment.TabPreOrder;

public class MainActivity extends AppCompatActivity implements TabHome.OnFragmentInteractionListener
        , TabPreOrder.OnFragmentInteractionListener, TabMyOrder.OnFragmentInteractionListener
        , TabAccount.OnFragmentInteractionListener {
    final int RequestPermissionCode = 1;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    final TabHome tabHome = new TabHome();
    final TabPreOrder tabPreOrder = new TabPreOrder();
    final TabAccount tabAccount = new TabAccount();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_message);

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck1 == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED
                || permissionCheck3 == PackageManager.PERMISSION_DENIED)
            RequestRuntimePermission();


        bottomNavigationView = findViewById(R.id.navi_main);
        bottomNavigationView.setSelectedItemId(R.id.icon_home);
        disableShiftMode(bottomNavigationView);
        if(getIntent().getBooleanExtra("order",false)){
            bottomNavigationView.setSelectedItemId(R.id.icon_my_order);
            loadFragment(new TabMyOrder());
        }else {
            loadFragment(tabHome);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.icon_home:
                        // do this event
                        fragment = tabHome;
                        break;
                    case R.id.icon_finding:
                        // do this event
                        fragment = tabPreOrder;
                        break;
                    case R.id.icon_my_order:
                        fragment = new TabMyOrder();
                        break;
                    case R.id.icon_account:
                        // do this event
                        fragment = tabAccount;
                        break;
                }
                return loadFragment(fragment);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_main, fragment )
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.icon_home != seletedItemId) {
            loadFragment(tabHome);
            bottomNavigationView.setSelectedItemId(R.id.icon_home);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void RequestRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            Toast.makeText(this, "CAMERA permission allows us to access CAMERA app", Toast.LENGTH_SHORT).show();
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Canceled", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}
