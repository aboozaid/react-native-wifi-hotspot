/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useState} from 'react';
import {StyleSheet, Button, View, Text, ToastAndroid} from 'react-native';
import Hotspot from 'react-native-wifi-hotspot';

const App = () => {
  const [peersList, setPeersList] = useState([]);
  const [config, setConfig] = useState({});

  const start = () => {
    Hotspot.start(
      () => {
        ToastAndroid.show('Hotspot connection started', ToastAndroid.SHORT);
      },
      (err) => {
        ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
      },
    );
  };

  const close = () => {
    Hotspot.close(
      () => {
        ToastAndroid.show('Hotspot connection closed', ToastAndroid.SHORT);
        setPeersList([]);
        setConfig({});
      },
      (err) => {
        ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
      },
    );
  };

  const getConfig = () => {
    Hotspot.getConfig(
      (hotspotConfig) => {
        setConfig(hotspotConfig);
      },
      (err) => {
        ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
      },
    );
  };

  const getPeers = () => {
    Hotspot.getPeers(
      (data) => {
        setPeersList(JSON.parse(data));
      },
      (err) => {
        ToastAndroid.show(err.toString(), ToastAndroid.SHORT);
      },
    );
  };

  return (
    <View style={styles.container}>
      <Text style={styles.welcome}>React native Hotspot library</Text>
      <Text style={styles.welcome}>Creates a local only hotspot</Text>
      <View style={{marginBottom: 10}}>
        <Button title="start" onPress={start} />
      </View>
      <View style={{marginBottom: 10}}>
        <Button title="close" onPress={close} />
      </View>
      <View style={{marginBottom: 10}}>
        <Button title="show peers" onPress={getPeers} />
      </View>
      <View style={{marginBottom: 10}}>
        <Button title="show config" onPress={getConfig} />
      </View>
      <View>
        <Text style={styles.welcome}>Peers List</Text>
        {peersList.map((peer, i) => (
          <View style={styles.viewList} key={i}>
            <Text style={styles.viewText}>{peer.device}</Text>
            <Text style={styles.viewText}>{peer.ip}</Text>
            <Text style={styles.viewText}>{peer.mac}</Text>
          </View>
        ))}
      </View>
      <View>
        <Text style={styles.welcome}>Config</Text>
        {Object.entries(config).map(([key, val], i) => (
          <Text key={i}>{`${key}: ${val}`}</Text>
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    backgroundColor: '#F5FCFF',
    margin: 8,
  },
  welcome: {
    fontSize: 20,
    height: 60,
    lineHeight: 50,
  },
  subtitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  viewList: {
    backgroundColor: '#F1F1F1',
    marginBottom: 10,
  },
  viewText: {
    fontSize: 15,
    fontWeight: 'bold',
    color: 'black',
  },
});

export default App;
