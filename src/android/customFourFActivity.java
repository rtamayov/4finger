package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;

import android.os.Bundle;
import android.util.Log;


//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;

import com.veridiumid.sdk.fourf.FourFInterface;
import com.veridiumid.sdk.fourf.FourFBiometricsActivity;
import com.veridiumid.sdk.fourf.FourFUIInterface;
import com.veridiumid.sdk.model.data.persistence.IKVStore;
import com.veridiumid.sdk.fourf.ui.FourFUIFragment;
import com.veridiumid.sdk.model.data.persistence.impl.InMemoryKVStore;
import com.veridiumid.sdk.defaultdata.DataStorage;




/**
 *  Extends vFaceBiometricsActivity to set storage to use and which UI to show.
 */
public class customFourFActivity extends FourFBiometricsActivity {

/**
    private String TAG = "customFourFActivity";

    @Override protected <FourFFragmentInterfaceUnion extends Fragment & FourFUIInterface> FourFFragmentInterfaceUnion fragmentToShow(){
            return (FourFFragmentInterfaceUnion) new AuthenticatorFourFFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"Activity Started");
    }

    @Override
    protected IKVStore openStorage() {
        return DataStorage.getDefaultStorage();
    }

    public static boolean queryIsEnrolled(){
        IKVStore persistence;
        persistence = DataStorage.getDefaultStorage();
        if(persistence!=null){
            BytesTemplatesStorage templates_store = new BytesTemplatesStorage(FourFInterface.UID, persistence);
            return templates_store.isEnrolled();
        }
        return false;
    } */

    @Override
       protected <FourFFragmentInterfaceUnion extends Fragment & FourFUIInterface> FourFFragmentInterfaceUnion fragmentToShow() {
           return (FourFFragmentInterfaceUnion) new FourFUIFragment();
       }

        @Override
        protected IKVStore openStorage() {
            
//            if(android.os.Build.VERSION.SDK_INT <= 29) {
//                return DataStorage.getDefaultStorage();
//            } else {
                return new InMemoryKVStore();
//            }
            
        }

}
