package py.com.personal.mimundo.disenhos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by Usuario on 11/8/2014.
 */
public class GeneradorDeDialogo {

    private FragmentActivity context;

    /*public static void crearDialogo(final String titulo, final String mensaje,
                                              final String textButton) {

        DialogFragment newFragment = GeneradorDialogFragment.newInstance(titulo, mensaje, textButton);
        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mensaje).setTitle(titulo);
                builder.setPositiveButton(textButton, aceptarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
    }*/

    public void GeneradorNeutralClick() {

    }

    public static class GeneradorDialogFragment extends DialogFragment {

        public static GeneradorDialogFragment newInstance(String title, String message, final String textbutton) {
            GeneradorDialogFragment frag = new GeneradorDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            args.putString("textbutton", textbutton);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");
            String textbutton = getArguments().getString("textbutton");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton(textbutton,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //((GeneradorDeDialogo)getActivity().getSupportFragmentManager().findFragmentByTag("")
                                    //)).GeneradorNeutralClick();
                                }
                            }
                    )
                    .create();
        }
    }
}
