package com.konka.alexa;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.konka.alexa.alexalib.AlexaManager;
import com.konka.alexa.alexalib.AuthorizationManager;
import com.konka.alexa.alexalib.callbacks.AsyncCallback;
import com.konka.alexa.alexalib.callbacks.AuthorizationCallback;
import com.konka.alexa.alexalib.interfaces.AvsResponse;
import com.konka.alexa.display.DisplayService;
import com.konka.alexa.global.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.konka.alexa.global.Constants.ALEXA;
import static com.konka.alexa.global.Constants.LOGIN;


public class LoginActivity extends Activity implements View.OnClickListener{

    private Button mLogin;
    private Button mCancel;
    private static final String TAG = "LoginActivity";
    private AlexaManager mAlexaManager;
    private View mloginLayout;
    private View mThinsTryLayout;
    private Button mDone;
    private final static int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean fromVoiceReceiver = getIntent().getBooleanExtra("fromVoiceReceiver", false);
        if (!fromVoiceReceiver) {
            SharedPreferences alexa = getSharedPreferences(ALEXA, Context.MODE_PRIVATE);
            boolean needLogin = alexa.getBoolean(LOGIN, true);

            Log.d(TAG, "onCreate: needLogin " + needLogin);
            if (!needLogin) {
                // show education login UI
                startService(new Intent(this, DisplayService.class));
                finish();
            }
        }
        setContentView(R.layout.activity_login2);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {
            String[] permissionRequest = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this, permissionRequest, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Record audio permission deny", Toast.LENGTH_SHORT).show();
                }
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initListener() {
        EventBus.getDefault().register(this);
        mLogin.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mDone.setOnClickListener(this);
    }

    private void initView() {
        mloginLayout = findViewById(R.id.ll_login);
        mThinsTryLayout = findViewById(R.id.ll_things_to_try);
        mLogin = (Button) findViewById(R.id.btn_login);
        mCancel = (Button) findViewById(R.id.btn_cancel);
        mDone = (Button) findViewById(R.id.btn_done);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Log.d(TAG, "onClick: ");
                mAlexaManager = AlexaManager.getInstance(this, Constants.PRODUCT_ID);
                logIn(new AlexaManager.ImplAuthorizationCallback<AvsResponse>(null) {
                    @Override
                    public void onSuccess() {
                        //call our function again
                        Log.d(TAG, "onSuccess: ");
                        //show things to try
                    }

                });
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_done:
                startService(new Intent(this, DisplayService.class));
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(String auth) {
        if ("success".equals(auth)) {
            mThinsTryLayout.setVisibility(View.VISIBLE);
            mloginLayout.setVisibility(View.GONE);
            getSharedPreferences(ALEXA, Context.MODE_PRIVATE).edit().putBoolean(LOGIN, false).apply();
        }
    }

    public void logIn(@Nullable final AuthorizationCallback callback){
        //check if we're already logged in
        final AuthorizationManager authorizationManager = mAlexaManager.getAuthorizationManager();
        authorizationManager.checkLoggedIn(this, new AsyncCallback<Boolean, Throwable>() {
            @Override
            public void start() {

            }

            @Override
            public void success(Boolean result) {
                //if we are, return a success
                if(result){
                    if(callback != null){
                        callback.onSuccess();
                    }
                    Log.d(TAG, "success: --------");
                    finish();
                }else{
                    //otherwise start the authorization process
                    Log.d(TAG, "success: authorizationManager");
//                    authorizationManager.authorizeUser(callback);
                    authorizationManager.authenticateByCBL(callback);
                }
            }

            @Override
            public void failure(Throwable error) {
                if(callback != null) {
                    callback.onError(new Exception(error));
                }
                Log.d(TAG, "failure: ------ " + error.getMessage());
            }

            @Override
            public void complete() {

            }
        });
    }


}
