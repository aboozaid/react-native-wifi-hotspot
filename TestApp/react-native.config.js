module.exports = {
  project: {
    ios: {},
    android: {},
  },
  dependencies: {
    'react-native-wifi-hotspot': {
      platforms: {
        android: null, // disable Android platform, other platforms will still autolink if provided
      },
    },
  },
};
