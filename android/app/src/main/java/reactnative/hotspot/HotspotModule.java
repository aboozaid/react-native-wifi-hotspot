package reactnative.hotspot;

import android.net.wifi.WifiConfiguration;
import androidx.annotation.Nullable;
import android.telecom.Call;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.whitebyte.hotspotmanager.ClientScanResult;

public class HotspotModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static String module = "Hotspot";
    private HotspotManager hotspot;

    public HotspotModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
        hotspot = new HotspotManager(reactContext);
    }

    @Override
    public void onHostResume() {
        hotspot.setPermission();
    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }

    @Override
    public String getName() {
        return module;
    }

    @ReactMethod
    public void start(Callback success, Callback error) {
        if (hotspot.start()) {
            success.invoke();
        } else
            error.invoke("Hotspot already running");
    }

    @ReactMethod
    public void close(Callback success, Callback error) {
        if (hotspot.close())
            success.invoke();
        else
            error.invoke("Hotspot connection already closed");
    }

    @ReactMethod
    public void getConfig(Callback success, Callback error) {
        WifiConfiguration session = hotspot.getConfig();
        if (session != null) {
            WritableMap config = Arguments.createMap();
            config.putString("ssid", session.SSID);
            config.putString("password", session.preSharedKey);
            config.putBoolean("status", session.status == 2 ? true : false);
            config.putInt("networkId", session.networkId);

            success.invoke(config);
        } else {
            error.invoke("Failed to fetch configuration");
        }
    }

    @ReactMethod
    public void getPeers(final Callback success, final Callback error) {
        hotspot.setPeersCallback(new HotspotManager.peersList() {
            @Override
            public void onPeersScanned(ArrayList<ClientScanResult> peers) {
                if (!peers.isEmpty()) {
                    JSONArray arrayOfPeers = new JSONArray();
                    int count = 1;
                    for (ClientScanResult peer : peers) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("ip", peer.getIpAddr());
                            object.put("mac", peer.getHWAddr());
                            peer.setDevice("Device " + (count++) + "");
                            object.put("device", peer.getDevice());
                        } catch (JSONException e) {
                            error.invoke("Fetching has failed");
                        }
                        arrayOfPeers.put(object);
                    }
                    success.invoke(arrayOfPeers.toString());

                } else {
                    error.invoke("Hotspot is empty");
                }
            }
        });
    }
}
