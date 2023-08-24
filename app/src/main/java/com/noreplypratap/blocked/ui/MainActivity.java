package com.noreplypratap.blocked.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noreplypratap.blocked.R;
import com.noreplypratap.blocked.utils.Constants;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButton();

    }

    private void setupButton() {
        Button btn = findViewById(R.id.btnAccessibility);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, Constants.REQUEST_OVERLAY_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this,"Accepted !!!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Permission Denied !!!",Toast.LENGTH_SHORT).show();
                //Toast.makeText(this,"Please allow this permission !!!",Toast.LENGTH_SHORT).show();
            }
        }
    }

}