package com.varsitycollege.vinyl_warehouse;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class LoadDialogBar {

    //Variable Declarations
    Context dialogContext;
    Dialog dialog;

    public LoadDialogBar(Context context) {
        this.dialogContext = context;
    }

    //Method for when custom dialog is loaded
    public void ShowDialog() {
        dialog = new Dialog(dialogContext);
        dialog.setContentView(R.layout.custom_progess_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.create();
        dialog.show();
    }

    //method to hide custom dialog
    public void HideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

}
