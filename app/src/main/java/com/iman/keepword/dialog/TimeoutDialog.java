package com.iman.keepword.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.iman.keepword.R;

public class TimeoutDialog {

    public final static int ERROR = 0;
    public final static int CHOICE = 1;

    private Button ok, rOk, lCancel;
    private TextView message;
    private Dialog alertDialogBuilder;
    private View view;

    private int type;
    private String messageText;
    private Activity activity;

    public TimeoutDialog(Activity activity, int type, String message) {
        alertDialogBuilder = new Dialog(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_timeout, null);
        this.activity = activity;
        messageText = message;
        this.type = type;
        init();
    }


    private void init() {
        ok = view.findViewById(R.id.ok);
        rOk = view.findViewById(R.id.rOk);
        lCancel = view.findViewById(R.id.lCancel);
        message = view.findViewById(R.id.message);

        if (type == ERROR) {
            ok.setVisibility(View.VISIBLE);
            ok.setText("OK");
            message.setText(messageText);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                dis();
            }
        });

        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setContentView(view);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogBuilder.show();
    }

    void dis() {
        alertDialogBuilder.dismiss();
    }

}
