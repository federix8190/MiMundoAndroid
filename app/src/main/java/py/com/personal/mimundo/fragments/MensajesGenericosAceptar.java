package py.com.personal.mimundo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import py.com.personal.mimundo.activities.R;

/**
 * Created by mabpg on 22/05/17.
 */

public class MensajesGenericosAceptar extends DialogFragment {

    public static MensajesGenericosAceptar newInstance(String title, String message){
        MensajesGenericosAceptar dialogFragment = new MensajesGenericosAceptar();
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
                .setNeutralButton(getResources().getString(R.string.alta_usuario_btn_aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());

                            }
                        }
                )
                .create();

    }
    public static MensajesGenericosAceptar newInstance() {
        MensajesGenericosAceptar f = new MensajesGenericosAceptar();
        return f;
    }
}
