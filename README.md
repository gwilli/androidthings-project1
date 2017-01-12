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

Wiring diagram
==============

Output (LED) circut
```
   o GPIO 17
   |
   Z
   Z  220 Ohm
   Z
   |
   V LED
   -
   |
  --- Gnd
   -
```
Input (button) circut
```
   o Vcc
   |
   Z
   Z  10k Ohm
   Z
   |
   o --- GPIO 19
   |
   |
   o
      |     momentary
      |--| push button
      |
   o
   |
   |
  --- Gnd
   -
```