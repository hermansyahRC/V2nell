package com.v2nell.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private LinearLayout configList;
    private TextView statusText;
    private Button connectButton;

    private boolean connected = false;
    private int selectedConfig = 0;

    private final List<Config> configs = new ArrayList<>();

    private final int BG = Color.rgb(9, 13, 22);
    private final int CARD = Color.rgb(20, 27, 40);
    private final int CARD_ACTIVE = Color.rgb(27, 38, 58);
    private final int WHITE = Color.rgb(240, 244, 250);
    private final int MUTED = Color.rgb(150, 160, 175);
    private final int ACCENT = Color.rgb(82, 145, 255);
    private final int GREEN = Color.rgb(55, 210, 130);
    private final int RED = Color.rgb(255, 85, 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        createDemoConfigs();
        showMainScreen();
    }

    private void createDemoConfigs() {

        Config vmess = new Config();
        vmess.name = "Singapore 01";
        vmess.protocol = "VMess";
        vmess.address = "example.com";
        vmess.port = "443";
        configs.add(vmess);

        Config vless = new Config();
        vless.name = "Japan 01";
        vless.protocol = "VLESS";
        vless.address = "jp.example.com";
        vless.port = "443";
        configs.add(vless);

        Config trojan = new Config();
        trojan.name = "Indonesia 01";
        trojan.protocol = "Trojan";
        trojan.address = "id.example.com";
        trojan.port = "443";
        configs.add(trojan);
    }

    private int dp(int value) {
        return (int) (
                value *
                getResources().getDisplayMetrics().density +
                0.5f
        );
    }

    private TextView text(
            String value,
            float size,
            int color
    ) {
        TextView tv = new TextView(this);
        tv.setText(value);
        tv.setTextSize(size);
        tv.setTextColor(color);
        return tv;
    }

    private GradientDrawable background(
            int color,
            float radius
    ) {
        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);
        drawable.setCornerRadius(dp((int) radius));

        return drawable;
    }

    private View space(int height) {

        View v = new View(this);

        v.setLayoutParams(
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );

        return v;
    }

    private LinearLayout vertical() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        return layout;
    }

    private LinearLayout horizontal() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.HORIZONTAL
        );

        layout.setGravity(
                Gravity.CENTER_VERTICAL
        );

        return layout;
    }

    private void showMainScreen() {

        root = vertical();
        root.setBackgroundColor(BG);

        // TOP BAR

        LinearLayout topBar = horizontal();

        topBar.setPadding(
                dp(18),
                dp(12),
                dp(12),
                dp(8)
        );

        TextView title =
                text("V2nell", 25, WHITE);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        topBar.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(55),
                        1
                )
        );

        TextView addButton =
                text("+", 31, WHITE);

        addButton.setGravity(Gravity.CENTER);
        addButton.setBackground(
                background(CARD, 18)
        );

        topBar.addView(
                addButton,
                new LinearLayout.LayoutParams(
                        dp(48),
                        dp(48)
                )
        );

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddMenu(v);
                    }
                }
        );

        root.addView(
                topBar,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
                )
        );

        // CONTENT

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(18),
                dp(5),
                dp(18),
                dp(20)
        );

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        // STATUS

        statusText =
                text(
                        "●  DISCONNECTED",
                        14,
                        RED
                );

        statusText.setGravity(
                Gravity.CENTER
        );

        statusText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                statusText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(35)
                )
        );

        content.addView(space(4));

        // CONNECT

        connectButton =
                new Button(this);

        connectButton.setText("CONNECT");
        connectButton.setTextSize(17);
        connectButton.setTextColor(WHITE);
        connectButton.setAllCaps(false);

        connectButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        connectButton.setBackground(
                background(ACCENT, 100)
        );

        LinearLayout.LayoutParams connectParams =
                new LinearLayout.LayoutParams(
                        dp(190),
                        dp(70)
                );

        connectParams.gravity =
                Gravity.CENTER_HORIZONTAL;

        content.addView(
                connectButton,
                connectParams
        );

        connectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleConnection();
                    }
                }
        );

        content.addView(space(25));

        // CONFIG TITLE

        TextView configTitle =
                text(
                        "CONFIGS",
                        12,
                        MUTED
                );

        configTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(configTitle);

        content.addView(space(8));

        // CONFIG LIST

        configList = vertical();

        content.addView(configList);

        refreshConfigList();

        // TRAFFIC

        content.addView(space(12));

        TextView trafficTitle =
                text(
                        "TRAFFIC",
                        12,
                        MUTED
                );

        trafficTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(trafficTitle);

        content.addView(space(8));

        LinearLayout traffic =
                horizontal();

        traffic.setPadding(
                dp(15),
                dp(12),
                dp(15),
                dp(12)
        );

        traffic.setBackground(
                background(CARD, 18)
        );

        TextView trafficText =
                text(
                        "↓ 0 B          ↑ 0 B",
                        15,
                        WHITE
                );

        trafficText.setGravity(
                Gravity.CENTER
        );

        traffic.addView(
                trafficText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        content.addView(traffic);

        // BOTTOM NAV

        LinearLayout nav =
                horizontal();

        nav.setBackground(
                background(CARD, 0)
        );

        TextView configs =
                navItem("CONFIGS");

        TextView logs =
                navItem("LOGS");

        TextView more =
                navItem("MORE");

        configs.setTextColor(ACCENT);

        nav.addView(configs);
        nav.addView(logs);
        nav.addView(more);

        root.addView(
                nav,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        logs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLogs();
                    }
                }
        );

        more.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMore();
                    }
                }
        );

        setContentView(root);
    }

    private TextView navItem(String title) {

        TextView tv =
                text(title, 13, MUTED);

        tv.setGravity(Gravity.CENTER);

        tv.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        tv.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        dp(60),
                        1
                )
        );

        return tv;
    }

    private void refreshConfigList() {

        configList.removeAllViews();

        for (
                int i = 0;
                i < configs.size();
                i++
        ) {
            addConfigCard(
                    configs.get(i),
                    i
            );
        }
    }

    private void addConfigCard(
            final Config config,
            final int index
    ) {

        LinearLayout card =
                vertical();

        card.setPadding(
                dp(15),
                dp(12),
                dp(10),
                dp(12)
        );

        int cardColor =
                index == selectedConfig
                        ? CARD_ACTIVE
                        : CARD;

        card.setBackground(
                background(cardColor, 18)
        );

        LinearLayout top =
                horizontal();

        TextView indicator =
                text(
                        index == selectedConfig
                                ? "●"
                                : "○",
                        16,
                        index == selectedConfig
                                ? GREEN
                                : MUTED
                );

        top.addView(
                indicator,
                new LinearLayout.LayoutParams(
                        dp(25),
                        dp(55)
                )
        );

        LinearLayout info =
                vertical();

        TextView name =
                text(
                        config.name,
                        17,
                        WHITE
                );

        name.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        TextView protocol =
                text(
                        config.protocol,
                        12,
                        ACCENT
                );

        TextView address =
                text(
                        config.address +
                                ":" +
                                config.port,
                        12,
                        MUTED
                );

        info.addView(name);
        info.addView(space(2));
        info.addView(protocol);
        info.addView(space(2));
        info.addView(address);

        top.addView(
                info,
                new LinearLayout.LayoutParams(
                        0,
                        dp(70),
                        1
                )
        );

        TextView menu =
                text("⋮", 27, WHITE);

        menu.setGravity(Gravity.CENTER);

        top.addView(
                menu,
                new LinearLayout.LayoutParams(
                        dp(40),
                        dp(60)
                )
        );

        card.addView(top);

        LinearLayout pingRow =
                horizontal();

        pingRow.setPadding(
                dp(25),
                dp(5),
                0,
                0
        );

        TextView ping =
                text(
                        "Ping: -- ms",
                        12,
                        MUTED
                );

        pingRow.addView(
                ping,
                new LinearLayout.LayoutParams(
                        0,
                        dp(38),
                        1
                )
        );

        Button pingButton =
                new Button(this);

        pingButton.setText("PING");
        pingButton.setTextSize(11);
        pingButton.setTextColor(WHITE);
        pingButton.setAllCaps(false);

        pingButton.setBackground(
                background(CARD, 12)
        );

        pingRow.addView(
                pingButton,
                new LinearLayout.LayoutParams(
                        dp(78),
                        dp(38)
                )
        );

        card.addView(pingRow);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(145)
                );

        params.setMargins(
                0,
                0,
                0,
                dp(9)
        );

        configList.addView(
                card,
                params
        );

        card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedConfig = index;

                        Toast.makeText(
                                MainActivity.this,
                                config.name +
                                        " dipilih.",
                                Toast.LENGTH_SHORT
                        ).show();

                        refreshConfigList();
                    }
                }
        );

        menu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfigMenu(
                                v,
                                config
                        );
                    }
                }
        );

        pingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pingConfig(
                                config,
                                ping
                        );
                    }
                }
        );
    }

    private void pingConfig(
            final Config config,
            final TextView result
    ) {

        result.setText("Ping: checking...");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        final long start =
                                System.currentTimeMillis();

                        try {

                            InetAddress address =
                                    InetAddress.getByName(
                                            config.address
                                    );

                            address.isReachable(3000);

                            final long ms =
                                    System.currentTimeMillis()
                                            - start;

                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            result.setText(
                                                    "Ping: " +
                                                            ms +
                                                            " ms"
                                            );

                                            result.setTextColor(
                                                    GREEN
                                            );
                                        }
                                    }
                            );

                        } catch (Exception e) {

                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            result.setText(
                                                    "Ping: Timeout"
                                            );

                                            result.setTextColor(
                                                    RED
                                            );
                                        }
                                    }
                            );
                        }
                    }
                }
        ).start();
    }

    private void toggleConnection() {

        if (!connected) {

            connected = true;

            statusText.setText(
                    "●  CONNECTED"
            );

            statusText.setTextColor(
                    GREEN
            );

            connectButton.setText(
                    "DISCONNECT"
            );

            Toast.makeText(
                    this,
                    "VPN engine belum dipasang.",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            connected = false;

            statusText.setText(
                    "●  DISCONNECTED"
            );

            statusText.setTextColor(
                    RED
            );

            connectButton.setText(
                    "CONNECT"
            );
        }
    }

    private void showAddMenu(View anchor) {

        PopupMenu menu =
                new PopupMenu(
                        this,
                        anchor
                );

        menu.getMenu().add(
                "Import from Clipboard"
        );

        menu.getMenu().add(
                "Scan QR Code"
        );

        menu.getMenu().add(
                "Import Config File"
        );

        menu.getMenu().add(
                "Manual Configuration"
        );

        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(
                            android.view.MenuItem item
                    ) {

                        String title =
                                item.getTitle()
                                        .toString();

                        if (
                                title.equals(
                                        "Manual Configuration"
                                )
                        ) {

                            showManualConfiguration();

                        } else {

                            Toast.makeText(
                                    MainActivity.this,
                                    title +
                                            " akan kita aktifkan.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        return true;
                    }
                }
        );

        menu.show();
    }

    private void showConfigMenu(
            View anchor,
            final Config config
    ) {

        PopupMenu menu =
                new PopupMenu(
                        this,
                        anchor
                );

        menu.getMenu().add("Connect");
        menu.getMenu().add("Ping");
        menu.getMenu().add("Edit");
        menu.getMenu().add("Duplicate");
        menu.getMenu().add("Delete");

        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(
                            android.view.MenuItem item
                    ) {

                        String action =
                                item.getTitle()
                                        .toString();

                        if (
                                action.equals("Connect")
                        ) {

                            toggleConnection();

                        } else if (
                                action.equals("Ping")
                        ) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Gunakan tombol PING.",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    MainActivity.this,
                                    action +
                                            " akan kita aktifkan.",
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

        LinearLayout layout =
                vertical();

        layout.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        layout.setBackgroundColor(BG);

        TextView title =
                text(
                        "Add Config",
                        24,
                        WHITE
                );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        layout.addView(title);

        layout.addView(space(20));

        TextView protocolTitle =
                text(
                        "Protocol",
                        13,
                        MUTED
                );

        layout.addView(protocolTitle);

        TextView protocols =
                text(
                        "VMess   •   VLESS   •   Trojan",
                        15,
                        WHITE
                );

        protocols.setPadding(
                0,
                dp(12),
                0,
                dp(12)
        );

        layout.addView(protocols);

        layout.addView(space(10));

        TextView addressTitle =
                text(
                        "Address / Domain",
                        13,
                        MUTED
                );

        layout.addView(addressTitle);

        EditText address =
                new EditText(this);

        address.setHint(
                "example.com"
        );

        address.setTextColor(WHITE);
        address.setHintTextColor(MUTED);

        layout.addView(address);

        layout.addView(space(8));

        TextView portTitle =
                text(
                        "Port",
                        13,
                        MUTED
                );

        layout.addView(portTitle);

        EditText port =
                new EditText(this);

        port.setHint("443");
        port.setTextColor(WHITE);
        port.setHintTextColor(MUTED);

        layout.addView(port);

        layout.addView(space(18));

        Button save =
                new Button(this);

        save.setText(
                "SAVE CONFIG"
        );

        save.setTextColor(WHITE);
        save.setAllCaps(false);

        save.setBackground(
                background(ACCENT, 15)
        );

        layout.addView(
                save,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(
                                MainActivity.this,
                                "Config siap disimpan.",
                                Toast.LENGTH_SHORT
                        ).show();

                        showMainScreen();
                    }
                }
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.addView(layout);

        setContentView(scroll);
    }

    private void showLogs() {

        LinearLayout layout =
                vertical();

        layout.setPadding(
                dp(20),
                dp(25),
                dp(20),
                dp(20)
        );

        layout.setBackgroundColor(BG);

        TextView title =
                text(
                        "Connection Logs",
                        24,
                        WHITE
                );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

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

        Button back =
                new Button(this);

        back.setText("BACK");
        back.setTextColor(WHITE);
        back.setAllCaps(false);

        back.setBackground(
                background(CARD, 15)
        );

        layout.addView(space(20));

        layout.addView(back);

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMainScreen();
                    }
                }
        );

        setContentView(layout);
    }

    private void showMore() {

        PopupMenu menu =
                new PopupMenu(
                        this,
                        root
                );

        menu.getMenu().add("Settings");
        menu.getMenu().add("About V2nell");

        menu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(
                            android.view.MenuItem item
                    ) {

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

    private static class Config {

        String name;
        String protocol;
        String address;
        String port;
    }
}