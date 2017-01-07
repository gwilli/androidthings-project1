Android Things project #1
=====================================

My first Android Things project based on the Android Things project template:
https://github.com/androidthings/new-project-template


Pre-requisites
--------------

- Android Things compatible board
- Android Studio 2.2+


Build and install
=================

On Android Studio, click on the "Run" button.

If you prefer to run on the command line, type

```bash
./gradlew installDebug
adb shell am start com.example.androidthings.myproject/.MainActivity
```
