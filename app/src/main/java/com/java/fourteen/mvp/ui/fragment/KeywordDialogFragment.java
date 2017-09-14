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

/**
 * A simple {@link Fragment} subclass.
 */
public class KeywordDialogFragment extends DialogFragment {
    public static final String TAG = "KeywordDialogFragment";

    public interface ClickListener {
        void onPositiveClick(String kw);
        void onNegativeClick();
    }

    private ClickListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement KeywordDialogFragment.Listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater li = getActivity().getLayoutInflater();
        final View v = li.inflate(R.layout.dialog_searchkw, null);
        builder.setView(v)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et = (EditText) v.findViewById(R.id.search_keyword);
                        listener.onPositiveClick(et.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNegativeClick();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
