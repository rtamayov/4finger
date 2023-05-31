package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;
// The native Toast API
import android.widget.Toast;

import android.util.Log;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.Context;

import android.app.Activity;

public class EntelFingerPlugin extends CordovaPlugin {


  private CallbackContext scanCallbackContext;

  private String TAG = "EntelFingerPlugin-Cordova";
  private int BIOM_REQ_CODE = 1;

  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {

      // Verify that the user sent a 'show' action
      if (!action.equals("getwsq")) {
          callbackContext.error("\"" + action + "\" is not a recognized action.");
          return false;
      }
	    scanCallbackContext = callbackContext;
      
      int LeftFinger;
      int RightFinger;
      boolean Liveness;
      int Type;
      try {
          JSONObject options = args.getJSONObject(0);
          LeftFinger = options.getInt("leftFingerCode");
          RightFinger = options.getInt("righFingerCode");
          Liveness = options.getBoolean("liveness");
          Type = options.getInt("type");
          Log.d(TAG,"JsonOpened Left " + String.valueOf(LeftFinger) + "Right " + String.valueOf(RightFinger) );
      } catch (JSONException e) {
          callbackContext.error("Error encountered: " + e.getMessage());
          return false;
      }

      Context appCtx = cordova.getActivity().getApplicationContext();
      Intent getwsqIntent = new Intent(appCtx, FourfingerActivity.class);
      getwsqIntent.putExtra("LeftFinger", LeftFinger);
      getwsqIntent.putExtra("RightFinger", RightFinger);
      getwsqIntent.putExtra("Liveness", Liveness);
      getwsqIntent.putExtra("Type", Type);
      cordova.startActivityForResult(this, getwsqIntent, BIOM_REQ_CODE);
      
      return true;
  }

  @Override
  public void onActivityResult (int requestCode, int resultCode, Intent data)
  {
      Log.d(TAG,"OnActivityResult code = " + String.valueOf(requestCode));

      if(requestCode == BIOM_REQ_CODE){

          if(data != null){

            if(resultCode == Activity.RESULT_OK){
		    

                String wsqb64 = (String) data.getExtras().get("base64String");
                String hand = (String) data.getExtras().get("hand");
	 	        String img = (String) data.getExtras().get("img");
				String minutia = (String) data.getExtras().get("minutia");

                JSONObject cordoResponse = new JSONObject();
                try {

                    cordoResponse.put("wsq", wsqb64);
                    cordoResponse.put("hand", hand);
                    cordoResponse.put("img", img);
					cordoResponse.put("minutia", minutia);

                    scanCallbackContext.success(cordoResponse);
                    Log.d(TAG, "WsqB64: " + wsqb64);
                    Log.d(TAG, "Hand: " + hand);
		            Log.d(TAG, "img: " + img);
					Log.d(TAG, "minutia: " + minutia);
			  
                } catch (JSONException e) {
                    scanCallbackContext.error("ERROR");
                    Log.d(TAG, "ERROR: JSONException");
                    e.printStackTrace();
                }


            }else{
                String status = (String) data.getExtras().get("status");
                scanCallbackContext.error("CANCEL");
                Log.d(TAG, "Status: " + status);
            }

          }else{

              Log.d(TAG, "Data is empty");
              
          }

      }else{

          Log.d(TAG, "Result code not recognized");

      }

    
  }


}
