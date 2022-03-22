https://blog.birost.com/a?ID=00400-b995c3b5-10a1-4807-8821-78b306187c1d

https://github.com/barthap/discovering-turbomodules/issues/1

https://github.com/expo/expo/pull/16075/files


Hi, actually I'm doing something similar here: https://github.com/expo/expo/pull/16075, but in your case, it could be simplified more:
1. you call the `runNativeTask` JSI method from JS
2. its first argument is callback, so in C++ it will be `jsi::Function` - store it somewhere in c++
3. call Java from C++ via JNI / fbjni from inside `runNativeTask` on the c++ side
4. meanwhile, export from C++ to Java the JNI native method `native void onTaskSuccess(result)` - its c++ body should call previously stored `jsi::Function` callback. Here you might have problems with threading, in this case you'd need to learn about "JSI Call Invoker" (even more complicated example here: https://github.com/expo/expo/pull/14904)
5. call that JNI method from Java inside `onSuccessListener`

_Originally posted by @barthap in https://github.com/barthap/discovering-turbomodules/issues/1#issuecomment-1036414036_
