/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Skeleton of the main Android Things activity. Implement your device's logic
 * in this class.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String GPIO17_NAME = "BCM17";
    private static final String GPIO19_NAME = "BCM19";

    private ToggleButton button;
    private Gpio inputIO;
    private Gpio outputIO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        button = (ToggleButton) findViewById(R.id.toggle_button);
        button.setChecked(true);

        setupGPIO();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //inputIO.setValue(!((ToggleButton)view).isChecked());
                    toggleLight();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to access GPIO on", e);
                }
            }
        });

    }

    private void setupGPIO() {
        PeripheralManagerService manager = new PeripheralManagerService();
        try {
            inputIO = manager.openGpio(GPIO17_NAME);

            inputIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            inputIO.setActiveType(Gpio.ACTIVE_LOW);

            outputIO = manager.openGpio(GPIO19_NAME);
            outputIO.setDirection(Gpio.DIRECTION_IN);
            outputIO.setActiveType(Gpio.ACTIVE_LOW);

            outputIO.setEdgeTriggerType(Gpio.EDGE_RISING);
            outputIO.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    try {
                        toggleLight();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                public void onGpioError(Gpio gpio, int error) {
                    Log.e(TAG, gpio + ": Error event " + error);
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Unable to access GPIO", e);
        }
    }

    private void toggleLight() throws IOException{
        inputIO.setValue(!inputIO.getValue());
        button.setChecked(!inputIO.getValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        if (inputIO != null) {
            try {
                inputIO.close();
                inputIO = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO input", e);
            }
        }

        if (outputIO != null) {
            try {
                outputIO.close();
                outputIO = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO output", e);
            }
        }
    }
}
