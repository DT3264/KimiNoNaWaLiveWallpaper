package com.livewall.mapachito.kiminonawalivewallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.leftSide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveName("Taki");
                startLiveWallpaperPreView(getPackageName(),DynamicWallPaper.class.getName());
            }
        });

        findViewById(R.id.rightSide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveName("Mitsuhara");
                startLiveWallpaperPreView(getPackageName(),DynamicWallPaper.class.getName());
            }
        });
    }

    private void saveName(String name){
        getBaseContext().getSharedPreferences("Default", MODE_PRIVATE).edit().putString("Name", name).apply();
    }


    public void startLiveWallpaperPreView(String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        }
        startActivityForResult(intent, 0);
    }
}
