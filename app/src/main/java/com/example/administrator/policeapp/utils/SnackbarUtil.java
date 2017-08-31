package com.example.administrator.policeapp.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtil
{

    public static void showMessage(View view, String text)
    {

        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
