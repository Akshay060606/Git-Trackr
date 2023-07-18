package com.axay.gittrackr.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class URLOpener {
    @SuppressLint("QueryPermissionsNeeded")
    public static void openUrlInBrowser(Context context, String url) {
        Uri webpage = Uri.parse(url);
            Intent defaultIntent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(defaultIntent);
    }
}
