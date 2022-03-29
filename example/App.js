import * as React from 'react';
import {StyleSheet, View, Text, TouchableOpacity} from 'react-native';
import simpleJsiModule, {isLoaded} from 'react-native-jsi-template';

export default function App() {
  const [result, setResult] = React.useState();
  const [deviceName, setDeviceName] = React.useState();
  const [orientation, setOrientation] = React.useState();
  const [getItemValue, setGetItemValue] = React.useState();

  const unregister1 = React.useRef();
  const unregister2 = React.useRef();

  React.useEffect(() => {
    setResult(simpleJsiModule.helloWorld());
  }, []);

  React.useEffect(() => {
    unregister1.current = simpleJsiModule.activateListener(res => {
      console.log({first: res});
    });
    unregister2.current = simpleJsiModule.activateListener(res => {
      console.log({second: res});
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Bindings Installed: {isLoaded().toString()}</Text>
      <Text>Result: {result}</Text>

      <TouchableOpacity
        onPress={() => {
          let value = simpleJsiModule.getDeviceName();
          setDeviceName(value);
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>Device Name: {deviceName}</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => {
          let value = simpleJsiModule.getCurrentOrientation();
          setOrientation(value);
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>Orientation: {orientation}</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => {
          simpleJsiModule.lockToPortrait();
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>Lock Portrait</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => {
          simpleJsiModule.lockToLandscape();
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>Lock Landscape</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => {
          unregister1.current();
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>Unregister Listener 1</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => {
          unregister2.current();
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>Unregister Listener 2.</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => {
          simpleJsiModule.setItem('helloworld', 'Hello World');
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>setItem: "Hello World"</Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => {
          setGetItemValue(simpleJsiModule.getItem('helloworld'));
        }}
        style={styles.button}>
        <Text style={styles.buttonTxt}>getItem: {getItemValue}</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  button: {
    width: '95%',
    alignSelf: 'center',
    height: 40,
    backgroundColor: 'green',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 5,
    marginVertical: 10,
  },
  buttonTxt: {
    color: 'white',
  },
});
