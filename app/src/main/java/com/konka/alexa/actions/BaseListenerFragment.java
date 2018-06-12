package com.konka.alexa.actions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.konka.alexa.alexalib.AlexaManager;
import com.konka.alexa.alexalib.callbacks.AsyncCallback;
import com.konka.alexa.alexalib.interfaces.AvsResponse;

import static com.konka.alexa.global.Constants.PRODUCT_ID;

/**
 * @author will on 5/30/2016.
 */

public abstract class BaseListenerFragment extends Fragment {

    protected AlexaManager alexaManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get our AlexaManager instance for convenience
        alexaManager = AlexaManager.getInstance(getActivity(), PRODUCT_ID);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* if(getActivity() != null) {
            getActivity().setTitle(getTitle());
        }*/
    }

    protected AsyncCallback<AvsResponse, Exception> getRequestCallback(){
        if(getActivity() != null && getActivity() instanceof AvsListenerInterface){
            return ((AvsListenerInterface) getActivity()).getRequestCallback();
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.code_menu, menu);

    }



    public abstract void startListening();
//    protected abstract String getTitle();
    @RawRes
    protected abstract int getRawCode();

    public interface AvsListenerInterface{
        AsyncCallback<AvsResponse, Exception> getRequestCallback();
    }

}
