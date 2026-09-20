package com.v2nell.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
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

        TextView status = text("● Disconnected", 18, Color.GRAY);

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

        TextView serverInfo = text("No server selected", 16, Color.GRAY);
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

        LinearLayout.LayoutParams settingsParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                );
        content.addView(settings, settingsParams);

        TextView about = text("ⓘ  About V2nell", 18, Color.DKGRAY);
        about.setGravity(Gravity.LEFT);

        LinearLayout.LayoutParams aboutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(55)
                );
        content.addView(about, aboutParams);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("● Disconnected");
            }
        });

        addServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverInfo.setText("Server configuration coming soon");
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
