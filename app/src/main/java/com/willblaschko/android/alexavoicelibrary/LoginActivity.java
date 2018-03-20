package com.willblaschko.android.alexavoicelibrary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.AuthorizationManager;
import com.willblaschko.android.alexa.callbacks.AsyncCallback;
import com.willblaschko.android.alexa.callbacks.AuthorizationCallback;
import com.willblaschko.android.alexa.interfaces.AvsResponse;
import com.willblaschko.android.alexavoicelibrary.global.Constants;

import org.jetbrains.annotations.Nullable;

import static com.willblaschko.android.alexavoicelibrary.global.Constants.PRODUCT_ID;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button mLogin;
    private Button mCancel;
    private static final String TAG = "LoginActivity";
    private AlexaManager mAlexaManager;
    private View mloginLayout;
    private View mThinsTryLayout;
    private Button mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initView();
        initListener();
    }

    private void initListener() {
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
                mAlexaManager = AlexaManager.getInstance(this, PRODUCT_ID);
                logIn(new AlexaManager.ImplAuthorizationCallback<AvsResponse>(null) {

                    @Override
                    public void onSuccess() {
                        //call our function again
                        Log.d(TAG, "onSuccess: ");
                        //show things to try
                        mThinsTryLayout.setVisibility(View.VISIBLE);
                        mloginLayout.setVisibility(View.GONE);
                        getSharedPreferences(Constants.ALEXA, MODE_PRIVATE).edit().putBoolean(Constants.LOGIN, false).apply();
                    }

                });
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_done:
                finish();
                break;
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
                    authorizationManager.authorizeUser(callback);
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
