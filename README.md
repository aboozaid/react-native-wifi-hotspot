<h2 align="center">
  <img src="/docs/wifi-hotspot-app.jpg" /><br>
  React Native Hotspot [Android]
</h2>

[![NPM Version](https://img.shields.io/badge/npm-1.0.0-orange.svg)](https://www.npmjs.com/package/react-native-wifi-hotspot)
[![NPM Version](https://img.shields.io/badge/yarn-1.0.0-red.svg)](https://yarnpkg.com/en/package/react-native-wifi-hotspot)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](#)

# Introduction

React Native (RN) does not provide an APIs to interact with the underlying Android APIs that control the Hotspot on a device.

This library enables RN developers to programatically enable and disable Hotspot mode on an Android device using [`startLocalOnlyHotspot`](<https://developer.android.com/reference/android/net/wifi/WifiManager#startLocalOnlyHotspot(android.net.wifi.WifiManager.LocalOnlyHotspotCallback,%20android.os.Handler)>).

**Full documentation on hotpost startup, lifecycle, etc. is [available on the Android docs site](<https://developer.android.com/reference/android/net/wifi/WifiManager#startLocalOnlyHotspot(android.net.wifi.WifiManager.LocalOnlyHotspotCallback,%20android.os.Handler)>).**

## Local Only

As of Android APK 28+, it is not possible to programatically create a hotspot that has internet connectivity. The only available option is to create a "local only" hotspot that is not connected to the internet.

## Setup

1. Include the dependency:

```
## npm
npm i --save react-native-wifi-hotspot

## yarn
yarn add react-native-wifi-hotspot
```

2. Link the dependency:

```
react-native link react-native-wifi-hotspot
```

3. In the `Settings.gradle` of your RN project, there are two updates that need to be made after linking:

- Update the path of `react-native-wifi-hotspot`

```diff
-project(':react-native-wifi-hotspot').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-wifi-hotspot/android')
+project(':react-native-wifi-hotspot').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-wifi-hotspot/android/app')
```

- Add the `hotspotmanager` dependency:

```
include ':hotspotmanager'
project(':hotspotmanager').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-wifi-hotspot/android/hotspotmanager')
```

4. Add the required app permisisons to the `AndroidManifest.xml` of your RN project:

```xml
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## Example

🔥 [Sample React Native application](https://github.com/assemmohamedali/react-native-wifi-hotspot/tree/master/TestApp)

## Features

- Create a connection to a local only hotspot
- Close a connection to a local only hotspot
- Fetch hotspot settings
- Get a list of connected peers

## Demo

![Test App](/docs/image.gif)

## Usage

- Request a local only hotspot that an application can use to communicate between co-located devices connected to the created WiFi hotspot.

```js
Hotspot.start(
  () => {
    ToastAndroid.show('Hotspot connection started', ToastAndroid.SHORT);
  },
  (err) => {
    ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
  },
);
```

- Close a hotspot connection. Note that since the hotspot may be shared among multiple applications, closing a connection does not necessarily teardown the hotspot.

```js
Hotspot.close(
  () => {
    ToastAndroid.show('Hotspot connection closed', ToastAndroid.SHORT);
  },
  (err) => {
    ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
  },
);
```

- Fetch the configuration for the hotspot. Contains the following fields:

  - `networkId`
  - `status`
  - `password`
  - `ssid`

```js
Hotspot.getConfig(
  (hotspotConfig) => {
    console.log(hotspotConfig);
  },
  (err) => {
    ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
  },
);
```

- Fetch list of connected devices to your hotspot. Contains the following fields:

  - `IP`
  - `MAC address`
  - `Device number`

```js
data: [
  results: {
    ip: 192.168.x.x,
    mac: A3:76:E1:33:79:F3,
    device: number
  }
]
Hotspot.getPeers((data) => {
      const peers = JSON.parse(data);
    }, (err) => {
      ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
    })
```
