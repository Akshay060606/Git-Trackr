package com.axay.gittrackr.model;

import android.content.Context;
import android.content.Intent;

public class ShareRepository {
    public static void ShareRepo(Context context, String name, String url) {
        String shareMessage = "Check out this Awesome repository! \n"
                + name
                + "\n"
                + url;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Repository Details \n");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
