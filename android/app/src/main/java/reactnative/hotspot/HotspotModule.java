package reactnative.hotspot;

import android.net.wifi.WifiConfiguration;
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

    public interface protocols {
        int RSN = 0;
        int WPA = 1;
        int BOTH = 2;
    }
    public interface authAlgorithm {
        int OPEN = 0;
        int SHARED = 1;
        int LEAP = 2;
    }
    public interface security {
        int WPA_PSK = 1;
        int WPA_EAP = 2;
        int IEEE8021X = 3;
        int WPA2_PSK = 4;
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
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        WritableMap protect = Arguments.createMap();
        protect.putInt("RSN", protocols.RSN);
        protect.putInt("WPA", protocols.WPA);
        protect.putInt("BOTH", protocols.BOTH);

        WritableMap authentication = Arguments.createMap();
        authentication.putInt("OPENED", authAlgorithm.OPEN);
        authentication.putInt("SHARED", authAlgorithm.SHARED);
        authentication.putInt("LEAP", authAlgorithm.LEAP);

        WritableMap access = Arguments.createMap();
        authentication.putInt("WPA_PSK", security.WPA_PSK);
        authentication.putInt("WPA_EAP", security.WPA_EAP);
        authentication.putInt("IEEE8021X", security.IEEE8021X);
        authentication.putInt("WPA2_PSK", security.WPA2_PSK);


        constants.put("protocols", protect);
        constants.put("auth", authentication);
        constants.put("security", access);

        return constants;
    }

    @Override
    public String getName() {
        return module;
    }

    @ReactMethod
    public void enable(Callback success, Callback error) {
        if(hotspot.isEnabled()) {
            success.invoke();
        }
        else
            error.invoke("Hotspot already running");
    }

    @ReactMethod
    public void disable(Callback success, Callback error) {
        if(hotspot.isDisabled())
            success.invoke();
        else
            error.invoke("Hotspot already closed");
    }

    @ReactMethod
    public void create(ReadableMap info, Callback success, Callback error) {
        if(hotspot.isCreated(info))
            success.invoke();
        else
            error.invoke("Hotspot creation has failed");
    }

    @ReactMethod
    public void getConfig(Callback success, Callback error) {
        WifiConfiguration session = hotspot.getConfig();
        if(session != null) {
            WritableMap config = Arguments.createMap();
            config.putString("ssid", session.SSID);
            config.putString("password", session.preSharedKey);
            config.putBoolean("status", session.status == 2 ? true : false);
            config.putInt("networkId", session.networkId);

            success.invoke(config);
        } else {
            error.invoke("Can't fitch your configuration");
        }
    }

    @ReactMethod
    public void peersList(final Callback success,final Callback error) {
        hotspot.setPeersCallback(new HotspotManager.peersList() {
            @Override
            public void onPeersScanned(ArrayList<ClientScanResult> peers) {
                if(!peers.isEmpty()) {
                    JSONArray arrayOfPeers = new JSONArray();
                    int count = 1;
                    for(ClientScanResult peer: peers) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("ip", peer.getIpAddr());
                            object.put("mac", peer.getHWAddr());
                            peer.setDevice("Device "+(count++)+"");
                            object.put("device", peer.getDevice());
                        } catch(JSONException e) {
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
