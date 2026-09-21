package com.v2nell.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout content;
    private TextView statusText;
    private TextView serverNameText;
    private TextView serverInfoText;
    private TextView pingText;
    private TextView trafficText;
    private Button connectButton;

    private boolean connected = false;

    private final List<Server> servers = new ArrayList<Server>();

    private final int BG = Color.rgb(10, 14, 24);
    private final int CARD = Color.rgb(20, 27, 40);
    private final int CARD2 = Color.rgb(25, 33, 48);
    private final int WHITE = Color.rgb(240, 244, 250);
    private final int MUTED = Color.rgb(150, 160, 175);
    private final int ACCENT = Color.rgb(90, 150, 255);
    private final int GREEN = Color.rgb(55, 210, 130);
    private final int RED = Color.rgb(255, 85, 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        createDemoServer();
        showMainScreen();
    }

    private void createDemoServer() {
        Server server = new Server();
        server.name = "Singapore 01";
        server.address = "example.com";
        server.port = "443";
        server.protocol = "VMess";
        servers.add(server);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, float size, int color) {
        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextSize(size);
        tv.setTextColor(color);
        return tv;
    }

    private GradientDrawable background(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp((int) radius));
        return drawable;
    }

    private View space(int height) {
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                1,
                dp(height)
        ));
        return v;
    }

    private LinearLayout horizontal() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        return layout;
    }

    private LinearLayout vertical() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private TextView makeNavItem(String title) {
        TextView tv = text(title, 13, MUTED);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(0, dp(60), 1);

        tv.setLayoutParams(params);
        return tv;
    }

    private void showMainScreen() {

        root = vertical();
        root.setBackgroundColor(BG);

        // =========================
        // TOP BAR
        // =========================

        LinearLayout topBar = horizontal();
        topBar.setPadding(dp(20), dp(14), dp(12), dp(8));

        LinearLayout.LayoutParams topParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
                );

        topBar.setLayoutParams(topParams);

        TextView title = text("V2nell", 25, WHITE);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        topBar.addView(title,
                new LinearLayout.LayoutParams(0, dp(55), 1));

        TextView plus = text("+", 32, WHITE);
        plus.setGravity(Gravity.CENTER);

        GradientDrawable plusBg = background(CARD2, 18);
        plus.setBackground(plusBg);

        LinearLayout.LayoutParams plusParams =
                new LinearLayout.LayoutParams(dp(48), dp(48));
        plusParams.setMargins(dp(5), 0, 0, 0);

        topBar.addView(plus, plusParams);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMenu(v);
            }
        });

        root.addView(topBar);

        // =========================
        // SCROLL CONTENT
        // =========================

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);

        content = vertical();
        content.setPadding(dp(18), dp(5), dp(18), dp(20));

        scrollView.addView(content);

        root.addView(scrollView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                ));

        // =========================
        // STATUS
        // =========================

        statusText = text(
                "●  DISCONNECTED",
                14,
                RED
        );

        statusText.setGravity(Gravity.CENTER);
        statusText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(statusText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(35)
                ));

        content.addView(space(5));

        // =========================
        // CONNECT BUTTON
        // =========================

        connectButton = new Button(this);
        connectButton.setText("CONNECT");
        connectButton.setTextSize(17);
        connectButton.setTextColor(WHITE);
        connectButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        connectButton.setAllCaps(false);
        connectButton.setBackground(background(ACCENT, 100));

        LinearLayout.LayoutParams connectParams =
                new LinearLayout.LayoutParams(
                        dp(190),
                        dp(70)
                );

        connectParams.gravity = Gravity.CENTER_HORIZONTAL;

        content.addView(connectButton, connectParams);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConnection();
            }
        });

        content.addView(space(25));

        // =========================
        // ACTIVE SERVER LABEL
        // =========================

        TextView activeLabel =
                text("ACTIVE SERVER", 12, MUTED);

        activeLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(activeLabel);

        content.addView(space(8));

        // =========================
        // SERVER CARD
        // =========================

        LinearLayout serverCard = vertical();
        serverCard.setPadding(
                dp(16),
                dp(14),
                dp(12),
                dp(14)
        );
        serverCard.setBackground(background(CARD, 18));

        LinearLayout serverTop = horizontal();

        LinearLayout serverInfo = vertical();

        serverNameText = text("Singapore 01", 18, WHITE);
        serverNameText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        serverInfoText =
                text("VMess • example.com:443", 13, MUTED);

        serverInfo.addView(serverNameText);
        serverInfo.addView(space(4));
        serverInfo.addView(serverInfoText);

        serverTop.addView(
                serverInfo,
                new LinearLayout.LayoutParams(0, dp(65), 1)
        );

        TextView more = text("⋮", 28, WHITE);
        more.setGravity(Gravity.CENTER);

        serverTop.addView(
                more,
                new LinearLayout.LayoutParams(dp(45), dp(65))
        );

        serverCard.addView(serverTop);

        // Ping row

        LinearLayout pingRow = horizontal();
        pingRow.setPadding(0, dp(10), 0, 0);

        pingText = text("Ping: -- ms", 13, MUTED);

        pingRow.addView(
                pingText,
                new LinearLayout.LayoutParams(0, dp(35), 1)
        );

        Button pingButton = new Button(this);
        pingButton.setText("PING");
        pingButton.setTextSize(12);
        pingButton.setTextColor(WHITE);
        pingButton.setAllCaps(false);
        pingButton.setBackground(background(CARD2, 12));

        pingRow.addView(
                pingButton,
                new LinearLayout.LayoutParams(dp(85), dp(38))
        );

        serverCard.addView(pingRow);

        content.addView(serverCard);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showServerMenu(v);
            }
        });

        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingServer();
            }
        });

        // =========================
        // TRAFFIC
        // =========================

        content.addView(space(18));

        TextView trafficLabel =
                text("TRAFFIC", 12, MUTED);

        trafficLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(trafficLabel);

        content.addView(space(8));

        LinearLayout trafficCard = horizontal();
        trafficCard.setPadding(
                dp(15),
                dp(12),
                dp(15),
                dp(12)
        );
        trafficCard.setBackground(background(CARD, 18));

        trafficText =
                text("↓ 0 B          ↑ 0 B", 15, WHITE);

        trafficText.setGravity(Gravity.CENTER);

        trafficCard.addView(
                trafficText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        content.addView(trafficCard);

        // =========================
        // SERVER LIST
        // =========================

        content.addView(space(22));

        TextView serversLabel =
                text("SERVERS", 12, MUTED);

        serversLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(serversLabel);

        content.addView(space(8));

        for (int i = 0; i < servers.size(); i++) {
            addServerListItem(servers.get(i));
        }

        // =========================
        // BOTTOM NAV
        // =========================

        LinearLayout nav = horizontal();
        nav.setBackground(background(CARD, 0));

        TextView serversNav = makeNavItem("SERVERS");
        TextView logsNav = makeNavItem("LOGS");
        TextView moreNav = makeNavItem("MORE");

        serversNav.setTextColor(ACCENT);

        nav.addView(serversNav);
        nav.addView(logsNav);
        nav.addView(moreNav);

        root.addView(nav,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                ));

        logsNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogs();
            }
        });

        moreNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });

        setContentView(root);
    }

    private void addServerListItem(final Server server) {

        LinearLayout card = horizontal();
        card.setPadding(dp(14), dp(10), dp(10), dp(10));
        card.setBackground(background(CARD, 16));

        LinearLayout info = vertical();

        TextView name = text(server.name, 16, WHITE);
        name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView details = text(
                server.protocol + " • " +
                        server.address + ":" + server.port,
                12,
                MUTED
        );

        info.addView(name);
        info.addView(space(3));
        info.addView(details);

        card.addView(
                info,
                new LinearLayout.LayoutParams(0, dp(60), 1)
        );

        TextView select = text("SELECT", 12, ACCENT);
        select.setGravity(Gravity.CENTER);

        card.addView(
                select,
                new LinearLayout.LayoutParams(dp(70), dp(55))
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(80)
                );

        cardParams.setMargins(0, 0, 0, dp(8));

        content.addView(card, cardParams);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectServer(server);
            }
        });
    }

    private void selectServer(Server server) {

        serverNameText.setText(server.name);

        serverInfoText.setText(
                server.protocol + " • " +
                        server.address + ":" +
                        server.port
        );

        pingText.setText("Ping: -- ms");

        Toast.makeText(
                this,
                "Server dipilih: " + server.name,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void toggleConnection() {

        if (!connected) {

            connected = true;

            statusText.setText("●  CONNECTED");
            statusText.setTextColor(GREEN);

            connectButton.setText("DISCONNECT");

            Toast.makeText(
                    this,
                    "VPN engine akan dipasang pada tahap berikutnya.",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            connected = false;

            statusText.setText("●  DISCONNECTED");
            statusText.setTextColor(RED);

            connectButton.setText("CONNECT");
        }
    }

    private void pingServer() {

        if (servers.size() == 0) {
            Toast.makeText(
                    this,
                    "Belum ada server.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        final Server server = servers.get(0);

        pingText.setText("Ping: checking...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                long start = System.currentTimeMillis();

                try {

                    InetAddress address =
                            InetAddress.getByName(server.address);

                    boolean reachable =
                            address.isReachable(3000);

                    long elapsed =
                            System.currentTimeMillis() - start;

                    final long result = elapsed;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pingText.setText(
                                    "Ping: " + result + " ms"
                            );

                            pingText.setTextColor(GREEN);
                        }
                    });

                } catch (Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pingText.setText(
                                    "Ping: Timeout"
                            );

                            pingText.setTextColor(RED);
                        }
                    });
                }
            }
        }).start();
    }

    private void showAddMenu(View anchor) {

        PopupMenu menu = new PopupMenu(this, anchor);

        menu.getMenu().add("Import from Clipboard");
        menu.getMenu().add("Scan QR Code");
        menu.getMenu().add("Import Config File");
        menu.getMenu().add("Manual Configuration");

        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(
                            android.view.MenuItem item) {

                        String title =
                                item.getTitle().toString();

                        if (title.equals("Manual Configuration")) {
                            showManualConfiguration();
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    title + " akan kita aktifkan.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        return true;
                    }
                }
        );

        menu.show();
    }

    private void showServerMenu(View anchor) {

        PopupMenu menu = new PopupMenu(this, anchor);

        menu.getMenu().add("Connect");
        menu.getMenu().add("Ping Server");
        menu.getMenu().add("Edit");
        menu.getMenu().add("Duplicate");
        menu.getMenu().add("Delete");

        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(
                            android.view.MenuItem item) {

                        String title =
                                item.getTitle().toString();

                        if (title.equals("Connect")) {
                            toggleConnection();
                        } else if (title.equals("Ping Server")) {
                            pingServer();
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    title + " akan kita aktifkan.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        return true;
                    }
                }
        );

        menu.show();
    }

    private void showManualConfiguration() {

        final LinearLayout layout = vertical();
        layout.setPadding(dp(22), dp(20), dp(22), dp(20));
        layout.setBackgroundColor(BG);

        TextView title =
                text("Add Server", 23, WHITE);

        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        layout.addView(title);

        layout.addView(space(18));

        TextView protocolLabel =
                text("Protocol", 13, MUTED);

        layout.addView(protocolLabel);

        Spinner protocolSpinner = new Spinner(this);

        String[] protocols = {
                "VMess",
                "VLESS",
                "Trojan",
                "Shadowsocks",
                "SOCKS",
                "HTTP"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        protocols
                );

        protocolSpinner.setAdapter(adapter);

        layout.addView(
                protocolSpinner,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(50)
                )
        );

        layout.addView(space(10));

        TextView address =
                text("Server Address / Domain", 13, MUTED);

        layout.addView(address);

        final android.widget.EditText addressInput =
                new android.widget.EditText(this);

        addressInput.setHint("example.com");
        addressInput.setTextColor(WHITE);
        addressInput.setHintTextColor(MUTED);

        layout.addView(addressInput);

        layout.addView(space(8));

        TextView portLabel =
                text("Port", 13, MUTED);

        layout.addView(portLabel);

        final android.widget.EditText portInput =
                new android.widget.EditText(this);

        portInput.setHint("443");
        portInput.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );
        portInput.setTextColor(WHITE);
        portInput.setHintTextColor(MUTED);

        layout.addView(portInput);

        layout.addView(space(15));

        Button save = new Button(this);
        save.setText("SAVE SERVER");
        save.setTextColor(WHITE);
        save.setAllCaps(false);
        save.setBackground(background(ACCENT, 15));

        layout.addView(
                save,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address =
                        addressInput.getText().toString().trim();

                String port =
                        portInput.getText().toString().trim();

                if (address.length() == 0 ||
                        port.length() == 0) {

                    Toast.makeText(
                            MainActivity.this,
                            "Address dan port wajib diisi.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                Toast.makeText(
                        MainActivity.this,
                        "Server berhasil disiapkan.",
                        Toast.LENGTH_SHORT
                ).show();

                showMainScreen();
            }
        });

        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);

        setContentView(scroll);
    }

    private void showLogs() {

        LinearLayout layout = vertical();
        layout.setPadding(dp(20), dp(25), dp(20), dp(20));
        layout.setBackgroundColor(BG);

        TextView title =
                text("Connection Logs", 24, WHITE);

        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        layout.addView(title);
        layout.addView(space(15));

        TextView logs =
                text(
                        "V2nell started.\n\n" +
                        "No connection logs yet.",
                        14,
                        MUTED
                );

        layout.addView(logs);

        Button back = new Button(this);
        back.setText("BACK");
        back.setTextColor(WHITE);
        back.setAllCaps(false);
        back.setBackground(background(CARD2, 15));

        layout.addView(space(20));
        layout.addView(back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainScreen();
            }
        });

        setContentView(layout);
    }

    private void showMore() {

        PopupMenu menu = new PopupMenu(
                this,
                findViewById(android.R.id.content)
        );

        menu.getMenu().add("Settings");
        menu.getMenu().add("About V2nell");

        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(
                            android.view.MenuItem item) {

                        Toast.makeText(
                                MainActivity.this,
                                item.getTitle() +
                                        " akan kita buat.",
                                Toast.LENGTH_SHORT
                        ).show();

                        return true;
                    }
                }
        );

        menu.show();
    }

    private static class Server {

        String name;
        String address;
        String port;
        String protocol;
    }
}