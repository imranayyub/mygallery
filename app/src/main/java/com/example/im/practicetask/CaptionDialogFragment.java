package com.example.im.practicetask;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Im on 17-11-2017.
 */

public class CaptionDialogFragment  extends DialogFragment implements View.OnClickListener, android.text.TextWatcher{
    EditText captionEdittext;
    Button  ok,cancel;
    String caption;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflating the dialogfragment layout
        View rootView = inflater.inflate(R.layout.captiondialogfragment, container, false);
        ok = (Button) rootView.findViewById(R.id.ok);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        captionEdittext = (EditText) rootView.findViewById(R.id.captionEdittext);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        //Applying asstextChangedListener on the EditTexts so that can validate data entered in EditText.
        captionEdittext.addTextChangedListener(this);
        getDialog().setTitle("Set Caption!"); //Sets title of Dialog Fragment.
        getDialog().setCancelable(false);
        ok.setEnabled(false);
         return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        ok.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        caption=captionEdittext.getText().toString();
        if (caption.isEmpty()) {
            captionEdittext.setError("Please enter Name");
            ok.setEnabled(false);
        }
        else
            ok.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ok: {
                //Retrieves data from EditText.
                caption = captionEdittext.getText().toString();
                Toast.makeText(getActivity(), "Ok button is Clicked", Toast.LENGTH_SHORT).show();
                ((EditText) captionEdittext.findViewById(R.id.captionEdittext)).setText("");
                dismiss(); //Dismisses the Dialog Fragment.
                break;
            }
            case R.id.cancel: {
                Toast.makeText(getActivity(), "Cancel button is Pressed", Toast.LENGTH_SHORT).show();
                ((EditText) captionEdittext.findViewById(R.id.captionEdittext)).setText("");
                dismiss();
                break;
            }
        }

    }
}
