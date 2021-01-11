package py.com.personal.mimundo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * Created by mabpg on 23/05/17.
 */

public class MensajesGenericosSiNo extends DialogFragment {

    public static MensajesGenericosSiNo newInstance(String title, String message, boolean esRegalo) {

        MensajesGenericosSiNo dialogFragment = new MensajesGenericosSiNo();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putBoolean("esRegalo", esRegalo);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final Intent resultIntent = getActivity().getIntent();
        resultIntent.putExtra("esRegalo", getArguments().getBoolean("esRegalo"));
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);

                            }
                        }
                )
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, resultIntent);

                    }
                })
                .create();

    }
    public static MensajesGenericosSiNo newInstance() {
        MensajesGenericosSiNo f = new MensajesGenericosSiNo();
        return f;
    }
}
