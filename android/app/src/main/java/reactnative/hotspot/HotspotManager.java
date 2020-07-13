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

    public boolean start() {
        wifi.start();
        return true;
    }

    public boolean close() {
        wifi.close();
        return true;
    }

    public WifiConfiguration getConfig() {
        if (wifi.isStarted()) {
            return wifi.getConfiguration();
        } else {
            return null;
        }
    }

    private void getPeers() {
        if (wifi.isStarted()) {
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
        getPeers();
    }
}
