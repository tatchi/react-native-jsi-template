//@ts-ignore we want to ignore everything
// else in global except what we need to access.
// Maybe there is a better way to do this.
import { NativeModules } from 'react-native';

const OrientationNativeModule = NativeModules.Orientation;

if (OrientationNativeModule) {
  if (typeof OrientationNativeModule.install === 'function') {
    OrientationNativeModule.install();
  }
}

//@ts-ignore
const orientationModule: {
  getCurrentOrientation(): string;
  lockToLandscape(): void;
  lockToPortrait(): void;
  activateListener(): void;
  setItem(key: string, value: string): boolean;
  getItem(key: string): string;
} = global;

export default orientationModule;
