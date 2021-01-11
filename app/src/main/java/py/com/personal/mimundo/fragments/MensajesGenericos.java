package py.com.personal.mimundo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MensajesGenericos extends DialogFragment {

    public static MensajesGenericos newInstance(String title, String message, final boolean volver){
        MensajesGenericos dialogFragment = new MensajesGenericos();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putBoolean("volver", volver);
        dialogFragment.setArguments(bundle);

        return dialogFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final Intent intent = getActivity().getIntent();
        intent.putExtra("volver", getArguments().getBoolean("volver"));
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                            }
                        }
                )
                .create();

    }
    public static MensajesGenericos newInstance() {
        MensajesGenericos f = new MensajesGenericos();
        return f;
    }
}
