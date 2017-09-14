package com.java.fourteen.mvp.ui.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.java.fourteen.R;
import com.java.fourteen.utils.BanwordNewsDetailFilter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanwordDialogFragment extends DialogFragment {
    public static final String TAG = "BanwordDialogFragment";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater li = getActivity().getLayoutInflater();
        final View v = li.inflate(R.layout.dialog_banword, null);
        EditText et = (EditText) v.findViewById(R.id.banword_et);
        et.setText(BanwordNewsDetailFilter.getInstance().getBanwordRaw());
        builder.setView(v)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et = (EditText) v.findViewById(R.id.banword_et);
                        String rawStr = et.getText().toString();
                        BanwordNewsDetailFilter.getInstance().setBanwordRaw(rawStr);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
