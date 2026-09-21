
package com.v2nell.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private LinearLayout root;
    private TextView serverInfo;
    private TextView status;

    private String serverName = "";
    private String serverAddress = "";
    private String serverPort = "";
    private String serverUuid = "";
    private String serverNetwork = "";
    private String serverPath = "";

    int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    TextView text(String value, float size, int color) {
        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextSize(size);
        tv.setTextColor(color);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    EditText input(String hint) {
        EditText edit = new EditText(this);
        edit.setHint(hint);
        edit.setTextSize(16);
        edit.setSingleLine(true);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                );

        params.setMargins(0, 0, 0, dp(12));
        edit.setLayoutParams(params);

        return edit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showMainScreen();
    }

    private void showMainScreen() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        root.setPadding(dp(24), dp(30), dp(24), dp(24));

        ScrollView scrollView = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        TextView title = text("V2nell", 32, Color.BLACK);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.setMargins(0, dp(20), 0, dp(8));
        content.addView(title, titleParams);

        status = text("● Disconnected", 18, Color.GRAY);

        LinearLayout.LayoutParams statusParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        statusParams.setMargins(0, 0, 0, dp(30));
        content.addView(status, statusParams);

        Button connectButton = new Button(this);
        connectButton.setText("CONNECT");
        connectButton.setTextSize(16);

        LinearLayout.LayoutParams connectParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                );

        connectParams.setMargins(0, 0, 0, dp(30));
        content.addView(connectButton, connectParams);

        TextView serverTitle = text("Server", 20, Color.BLACK);
        serverTitle.setGravity(Gravity.LEFT);
        serverTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(serverTitle);

        if (serverName.isEmpty()) {
            serverInfo = text("No server selected", 16, Color.GRAY);
        } else {
            serverInfo = text(
                    serverName + "\n" +
                    serverAddress + ":" + serverPort + "\n" +
                    "VMess • " + serverNetwork,
                    16,
                    Color.DKGRAY
            );
        }

        serverInfo.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams serverInfoParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        serverInfoParams.setMargins(0, dp(8), 0, dp(16));
        content.addView(serverInfo, serverInfoParams);

        Button addServer = new Button(this);
        addServer.setText("+ Add Server");
        addServer.setTextSize(16);

        LinearLayout.LayoutParams addParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                );

        addParams.setMargins(0, 0, 0, dp(30));
        content.addView(addServer, addParams);

        TextView settings = text("⚙  Settings", 18, Color.DKGRAY);
        settings.setGravity(Gravity.LEFT);

        content.addView(
                settings,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        TextView about = text("ⓘ  About V2nell", 18, Color.DKGRAY);
        about.setGravity(Gravity.LEFT);

        content.addView(
                about,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serverName.isEmpty()) {
                    Toast.makeText(
                            MainActivity.this,
                            "Tambahkan server terlebih dahulu",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Toast.makeText(
                        MainActivity.this,
                        "VPN engine belum terpasang",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        addServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddServerScreen();
            }
        });

        scrollView.addView(content);

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    private void showAddServerScreen() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        root.setPadding(dp(24), dp(30), dp(24), dp(24));

        ScrollView scrollView = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        TextView title = text("Add VMess Server", 28, Color.BLACK);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.LEFT);

        content.addView(title);

        TextView subtitle = text(
                "Masukkan konfigurasi server VMess",
                15,
                Color.GRAY
        );

        subtitle.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        subtitleParams.setMargins(0, dp(8), 0, dp(25));
        content.addView(subtitle, subtitleParams);

        EditText nameInput = input("Server name");
        EditText addressInput = input("Address / Domain");
        EditText portInput = input("Port");
        EditText uuidInput = input("UUID");
        EditText networkInput = input("Network (ws / tcp)");
        EditText pathInput = input("WebSocket Path");

        content.addView(nameInput);
        content.addView(addressInput);
        content.addView(portInput);
        content.addView(uuidInput);
        content.addView(networkInput);
        content.addView(pathInput);

        Button saveButton = new Button(this);
        saveButton.setText("SAVE SERVER");
        saveButton.setTextSize(16);

        LinearLayout.LayoutParams saveParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                );

        saveParams.setMargins(0, dp(10), 0, dp(12));
        content.addView(saveButton, saveParams);

        Button cancelButton = new Button(this);
        cancelButton.setText("CANCEL");
        cancelButton.setTextSize(16);

        content.addView(
                cancelButton,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameInput.getText().toString().trim();
                String address = addressInput.getText().toString().trim();
                String port = portInput.getText().toString().trim();
                String uuid = uuidInput.getText().toString().trim();
                String network = networkInput.getText().toString().trim();
                String path = pathInput.getText().toString().trim();

                if (name.isEmpty() ||
                        address.isEmpty() ||
                        port.isEmpty() ||
                        uuid.isEmpty()) {

                    Toast.makeText(
                            MainActivity.this,
                            "Name, Address, Port dan UUID wajib diisi",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                serverName = name;
                serverAddress = address;
                serverPort = port;
                serverUuid = uuid;
                serverNetwork = network.isEmpty() ? "ws" : network;
                serverPath = path.isEmpty() ? "/" : path;

                Toast.makeText(
                        MainActivity.this,
                        "Server berhasil ditambahkan",
                        Toast.LENGTH_SHORT
                ).show();

                showMainScreen();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainScreen();
            }
        });

        scrollView.addView(content);

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(root);
    }
}
