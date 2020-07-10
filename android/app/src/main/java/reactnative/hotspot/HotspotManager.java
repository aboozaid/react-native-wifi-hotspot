package reactnative.hotspot;

import android.content.Context;
import android.net.wifi.WifiConfiguration;

import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;

import info.whitebyte.hotspotmanager.ClientScanResult;
import info.whitebyte.hotspotmanager.FinishScanListener;
import info.whitebyte.hotspotmanager.WifiApManager;

public class HotspotManager {
    private WifiApManager wifi;
    private peersList callback;

    interface peersList {
        void onPeersScanned(ArrayList<ClientScanResult> peers);
    }

    public HotspotManager(Context context) {
        wifi = new WifiApManager(context);
    }

    public void setPermission() {
        wifi.showWritePermissionSettings(false);
    }

    public boolean create() {
        wifi.turnOnHotspot();
        return true;
    }

    public boolean destroy() {
        wifi.turnOffHotspot();
        return true;
    }

    public WifiConfiguration getConfig() {
        if (wifi.isHotspotStarted()) {
            return wifi.getConfiguration();
        } else {
            return null;
        }
    }

    private void peersList() {
        if (wifi.isHotspotStarted()) {
            wifi.getClientList(true, new FinishScanListener() {
                @Override
                public void onFinishScan(ArrayList<ClientScanResult> clients) {
                    callback.onPeersScanned(clients);
                }
            });
        }
    }

    public void setPeersCallback(peersList callback) {
        this.callback = callback;
        peersList();
    }
}
