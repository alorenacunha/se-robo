package com.example.lorena.serobo05;

import android.app.Activity;
import android.os.Bundle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Lorena on 22/06/2015.
 */
public class Serobo extends Activity{
        private static final String TAG = "LEDOnOff";

        ImageButton btnUp, btnDown, btnLeft, btnRight;
        Button btnBT,btnInicio,btnFim, btnLoopFrente,btnCtnRight,btnCtnLeft,btnOpen, btnSave;
        TextView text_prog;

        protected String prog;

        private static final int REQUEST_ENABLE_BT = 1;
        private BluetoothAdapter btAdapter = null;
        private BluetoothSocket btSocket = null;
        private OutputStream outStream = null;

        // Well known SPP UUID
        private static final UUID MY_UUID =
                UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        // Insert your bluetooth devices MAC address
        private static String address = "00:06:66:4D:65:19";

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            prog = "";
            Log.d(TAG, "In onCreate()");

            setContentView(R.layout.serobo_main);

            btnUp = (ImageButton) findViewById(R.id.btn_up);
            btnDown = (ImageButton) findViewById(R.id.btn_down);
            btnLeft = (ImageButton) findViewById(R.id.btn_left);
            btnRight = (ImageButton) findViewById(R.id.btn_right);
            // Button btnBT,btnInicio,btnFim, btnLoopFrente,btnCtnRight,btnCtnLeft,btnOpen, btnSave;
            btnBT = (Button) findViewById(R.id.btn_bt);
            btnInicio = (Button) findViewById(R.id.btn_inicio);
            btnFim = (Button) findViewById(R.id.btn_fim);
            btnLoopFrente = (Button) findViewById(R.id.btn_loop_frente);
            btnCtnRight = (Button) findViewById(R.id.btn_ctnRight);
            btnCtnLeft = (Button) findViewById(R.id.btn_ctnLeft);
            btnOpen = (Button) findViewById(R.id.btn_prog);
            btnSave = (Button) findViewById(R.id.btn_slv);
            //TExtView
            text_prog = (TextView) findViewById(R.id.text_prog);

            btAdapter = BluetoothAdapter.getDefaultAdapter();
            checkBTState();

            btnInicio.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("1");
                    prog+="\nIn�cio";
                    text_prog.setText(prog);
                }
            });


            btnFim.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("1");
                    prog+="\nFim";
                    text_prog.setText(prog);
                }
            });

            btnUp.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("1");
                    prog+="\n\tMova para frente 10 cm";
                    text_prog.setText(prog);
                }
            });

            btnDown.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("0");
                    prog+="\n\tMova para tr�s 10 cm";
                    text_prog.setText(prog);
                }
            });


            btnLeft.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("2");
                    prog+="\n\tVire para a esquerda";
                    text_prog.setText(prog);
                }
            });


            btnRight.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("3");
                    prog+="\n\tVire para a direita";
                    text_prog.setText(prog);
                }
            });
            btnCtnLeft.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("4");
                    prog += "\n\tContorne pela esquerda";
                    text_prog.setText(prog);
                }
            });
            btnCtnRight.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("5");
                    prog+="\n\tContorne pela direita";
                    text_prog.setText(prog);
                }
            });
            btnLoopFrente.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //sendData("6");
                    prog+="\n\tMova para frente at� encontrar um obst�culo";
                    text_prog.setText(prog);
                }
            });

            btnBT.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Serobo.this, MainActivity.class));
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();

            Log.d(TAG, "...In onResume - Attempting client connect...");

            // Set up a pointer to the remote node using it's address.
            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            // Two things are needed to make a connection:
            //   A MAC address, which we got above.
            //   A Service ID or UUID.  In this case we are using the
            //     UUID for SPP.
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() socket create: " + e.getMessage() + ".");
            }

            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            btAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            Log.d(TAG, "...Connecting to Remote...");
            try {
                btSocket.connect();
                Log.d(TAG, "...Connection established and data link opened...");
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    errorExit("Fatal Error", "In onResume() socket" + e2.getMessage() + ".");
                }
            }

            // Create a data stream so we can talk to server.
            Log.d(TAG, "...Creating Socket...");

            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() outstream:" + e.getMessage() + ".");
            }
        }

        @Override
        public void onPause() {
            super.onPause();

            Log.d(TAG, "...In onPause()...");

            if (outStream != null) {
                try {
                    outStream.flush();
                } catch (IOException e) {
                    errorExit("Fatal Error", "In onPause() flush: " + e.getMessage() + ".");
                }
            }

            try     {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onPause() socket." + e2.getMessage() + ".");
            }
        }

        private void checkBTState() {
            // Check for Bluetooth support and then check to make sure it is turned on

            // Emulator doesn't support Bluetooth and will return null
            if(btAdapter==null) {
                errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
            } else {
                if (btAdapter.isEnabled()) {
                    Log.d(TAG, "...Bluetooth is enabled...");
                } else {
                    //Prompt user to turn on Bluetooth
                    Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        }

        private void errorExit(String title, String message){
            Toast msg = Toast.makeText(getBaseContext(),
                    title + " - " + message, Toast.LENGTH_SHORT);
            msg.show();
            finish();
        }

        private void sendData(String message) {
            byte[] msgBuffer = message.getBytes();

            Log.d(TAG, "...Sending data: " + message + "...");

            try {
                outStream.write(msgBuffer);
            } catch (IOException e) {
                String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
                if (address.equals("00:00:00:00:00:00"))
                    msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
                msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

                errorExit("Fatal Error", msg);
            }
        }

}
