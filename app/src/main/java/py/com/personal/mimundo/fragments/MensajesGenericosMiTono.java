package py.com.personal.mimundo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import py.com.personal.mimundo.activities.R;

/**
 * Created by mabpg on 25/05/17.
 */

public class MensajesGenericosMiTono extends DialogFragment {

    public static MensajesGenericosMiTono newInstance(String title, String message){
        MensajesGenericosMiTono dialogFragment = new MensajesGenericosMiTono();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        dialogFragment.setArguments(bundle);

        return dialogFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());

                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());

                    }
                })
                .create();

    }
    public static MensajesGenericosMiTono newInstance() {
        MensajesGenericosMiTono f = new MensajesGenericosMiTono();
        return f;
    }
}