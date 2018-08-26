package ro.stefanhalus.android.blindtransport.Utils;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import ro.stefanhalus.android.blindtransport.R;
import ro.stefanhalus.android.blindtransport.WaitingActivity;

public class MessageUtil {
    public MessageUtil(Context context, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.icon_blind_transport_2);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ubil_button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        });
        builder.create();
        builder.show();
    }
}
