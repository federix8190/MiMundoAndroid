package py.com.personal.mimundo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import py.com.personal.mimundo.activities.R;

/**
 * Created by mabpg on 18/05/17.
 */

public class MensajesGenericosAceptarRechazar extends DialogFragment {

    public static MensajesGenericosAceptarRechazar newInstance(String title, String message, String codigoPlan, boolean esActivacion){
        MensajesGenericosAceptarRechazar dialogFragment = new MensajesGenericosAceptarRechazar();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("codigoPlan", codigoPlan);
        bundle.putBoolean("esActivacion", esActivacion);
        dialogFragment.setArguments(bundle);

        return dialogFragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final Intent resultIntent = getActivity().getIntent();
        resultIntent.putExtra("codigoPlan", getArguments().getString("codigoPlan"));
        resultIntent.putExtra("esActivacion", getArguments().getBoolean("esActivacion"));

        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);

                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, resultIntent);

                    }
                })
                .create();

    }
    public static MensajesGenericosAceptarRechazar newInstance() {
        MensajesGenericosAceptarRechazar f = new MensajesGenericosAceptarRechazar();
        return f;
    }
}
