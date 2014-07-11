# SMSgateway - Android

This version of the SMSgateway network node runs on android devices. It's meant to as an entry level node that can support up to a few hundred active users.

The application can be installed from the [play store](https://play.google.com/store/apps/details?id=org.btc4all.btc2sms)

This application is derived from [EnvayaSMS](http://sms.envaya.org/) which was used as SMS and MMS gateway in various projects to collect data and enable transactional applications. EnvayaSMS's code and SMSgateway code is released under the MIT license.

## Signup example

some pictures here...

## Build

1. enter the project directory:
```
cd mobile/btc2sms
```

2. execute build:
```
mvn clean package -Dandroid.sdk.path=<path>
```

## Build for Release

1. enter the project directory:
```
cd mobile/btc2sms
```

2. execute signed build:
```cmd
mvn install -P release -Dsign.keystore=<keystore> -Dsign.alias=<alias> -Dsign.storepass=<storepass> -Dsign.keypass=<keypass>
```

Example:
```
mvn install -P release
-Dsign.keystore=/home/dmitrycrocodilys/37coins_client/secure_area/37coins_dummy_keystore.keystore
-Dsign.alias=37dev
-Dsign.storepass="KeYsToRe PaSsWoRd GoEs HeRe"
-Dsign.keypass="37DeV KeY PaSsWoRd GoEs HeRe"
```

further instruction for publishing android applications can be found here: [http://developer.android.com/tools/publishing/app-signing.html](http://developer.android.com/tools/publishing/app-signing.html)

