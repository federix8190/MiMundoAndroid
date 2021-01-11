package py.com.personal.mimundo.fragments.saldo.prestamo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.AdapterSpinner;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptarRechazar;
import py.com.personal.mimundo.fragments.MensajesGenericosAceptar;
import py.com.personal.mimundo.services.lineas.service.SolicitarRecargaSosRequest;
import py.com.personal.mimundo.services.cache.recargas.MontosRecargaSosRequest;
import py.com.personal.mimundo.services.lineas.models.MontosRecargaSos;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class RecargaSosFragment extends Fragment {

    private String linea;
    private ScrollView pantalla;
    private ProgressBar progressBar;
    private ViewStub stub;

    // Opciones de recarga de saldo.
    private Spinner opcionesRecargaSos;
    private TextView mensajeMontosView;
    private AdapterSpinner adapterListaOpcionesRecargaSos;
    private List<String> listaOpcionesRecargaSos;
    private String montoSeleccionado = null;
    private MontosRecargaSosRequest montosRecargaRequest;
    private Button solicitarRecarga;

    // Peticion de saldo.
    private SolicitarRecargaSosRequest solicitarRecargaSos;
    private ProgressDialog progress;

    // Listas.
    private RelativeLayout contenedorSpinnerDeMontos;
    private TextView mensajePrestamoPendiente;

    public static final int RECARGA_SOS_CREAR_DIALOG_1 = 1;
    public static final int RECARGA_SOS_CREAR_DIALOG_2 = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_prestar_saldo, container, false);

        pantalla = (ScrollView)v.findViewById(R.id.pantalla);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar);
        stub = (ViewStub) v.findViewById(R.id.stub);

        // Select para el monto a prestar.
        opcionesRecargaSos = (Spinner)v.findViewById(R.id.opciones_recarga_sos);
        opcionesRecargaSos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                montoSeleccionado = listaOpcionesRecargaSos.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Boton para solicitar el prestamo.
        mensajeMontosView = (TextView) v.findViewById(R.id.mensaje);
        solicitarRecarga = (Button) v.findViewById(R.id.solicitar_recarga_sos);
        solicitarRecarga.setEnabled(false);
        solicitarRecarga.setTextColor(getResources().getColor(R.color.gris_texto_boton));
        solicitarRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogo = MensajesGenericosAceptarRechazar.newInstance(
                        getResources().getString(R.string.confirmar_title),
                        getResources().getString(R.string.confirmar_prestamo_saldo),
                        "", false);

                dialogo.setTargetFragment(RecargaSosFragment.this, RECARGA_SOS_CREAR_DIALOG_2);

                //DialogFragment dialogo = crearDialogo();
                dialogo.show(getActivity().getSupportFragmentManager(), "Prestamo Saldo");
            }
        });

        // Lista de opciones de recarga.
        mensajePrestamoPendiente = (TextView) v.findViewById(R.id.prestamoPendiente);
        contenedorSpinnerDeMontos = (RelativeLayout) v.findViewById(R.id.cuerpo_presta_saldo);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.title_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.title_2)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");
        mensajePrestamoPendiente.setTypeface(tf2);
        solicitarRecarga.setTypeface(tf2);
        ((TextView) v.findViewById(R.id.monto_title)).setTypeface(tf2);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        montosRecargaRequest = new MontosRecargaSosRequest(getActivity(), new JacksonConverter(objectMapper));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        if (activity.getListaLineas() != null && activity.getListaLineas().size() > 0) {
            showLoader(true);
            linea = activity.obtenerLineaSeleccionada();
            activity.getSpiceManager().execute(montosRecargaRequest.consultar(linea), new MontosRequestListener());
        } else {
            setearMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_LINEAS);
        }
    }

    private void setearMensajeFalla(String mensaje) {
        if (stub != null && stub.getParent() != null) {
            stub.setLayoutResource(R.layout.widget_mensaje_generico);
            View inflatedView = stub.inflate();
            stub.setVisibility(View.VISIBLE);
            if (mensaje != null) {
                TextView mensajeTextView = (TextView) inflatedView.findViewById(R.id.mensaje_falla);
                mensajeTextView.setText(mensaje);
            }
        }
    }

    public final class SolicitudRequestListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();

                DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                        getResources().getString(R.string.prestamo_saldo_title),
                        getResources().getString(R.string.error_prestamo_saldo));

                dialogo.setTargetFragment(RecargaSosFragment.this, RECARGA_SOS_CREAR_DIALOG_1);

                /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.prestamo_saldo_title),
                        getResources().getString(R.string.error_prestamo_saldo));*/
                dialogo.show(getActivity().getSupportFragmentManager(), "Prestamo Saldo");
            }
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (getActivity() != null) {
                final String linea = ((BaseDrawerActivity) getActivity()).obtenerLineaSeleccionada();
                progress.dismiss();
                if (response != null && response.getStatus() == 200) {

                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.prestamo_saldo_title),
                            getResources().getString(R.string.prestamo_saldo_exitoso));

                    dialogo.setTargetFragment(RecargaSosFragment.this, RECARGA_SOS_CREAR_DIALOG_1);

                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.prestamo_saldo_title),
                            getResources().getString(R.string.prestamo_saldo_exitoso));*/

                    dialogo.show(getActivity().getSupportFragmentManager(), "Prestamo Saldo");
                    BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                    activity.getSpiceManager().cancel(montosRecargaRequest.consultar(linea));
                    RecargaSosFragment fragment = new RecargaSosFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                } else {

                    DialogFragment dialogo = MensajesGenericosAceptar.newInstance(
                            getResources().getString(R.string.prestamo_saldo_title),
                            getResources().getString(R.string.error_prestamo_saldo));

                    dialogo.setTargetFragment(RecargaSosFragment.this, RECARGA_SOS_CREAR_DIALOG_1);

                    /*DialogFragment dialogo = crearDialogo(getResources().getString(R.string.prestamo_saldo_title),
                            getResources().getString(R.string.error_prestamo_saldo));*/
                    dialogo.show(getActivity().getSupportFragmentManager(), "Prestamo Saldo");
                }
            }
        }
    }

    public final class MontosRequestListener implements RequestListener<MontosRecargaSos> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                showLoader(false);
                Toast.makeText(getActivity(), MensajesDeUsuario.TITULO_SIN_DATOS, Toast.LENGTH_SHORT).show();
                cargarMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
            }
        }

        @Override
        public void onRequestSuccess(final MontosRecargaSos datos) {
            if (getActivity() != null) {
                showLoader(false);
                if (datos != null) {
                    if (datos.getItems() != null) {
                        Button botonDeSolicitud = (Button)getView().findViewById(R.id.solicitar_recarga_sos);
                        mensajePrestamoPendiente.setVisibility(View.GONE);
                        contenedorSpinnerDeMontos.setVisibility(View.VISIBLE);
                        cargarOpcionesRecarga(datos.getItems());
                        botonDeSolicitud.setEnabled(true);
                        botonDeSolicitud.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        cargarMensajeFalla(datos.getMensaje());
                    }
                    if (listaOpcionesRecargaSos!= null && listaOpcionesRecargaSos.size() > 0) {
                        adapterListaOpcionesRecargaSos = new AdapterSpinner(true, getActivity());
                        for (String item : listaOpcionesRecargaSos) {
                            adapterListaOpcionesRecargaSos.addItem(item);
                        }
                        opcionesRecargaSos.setAdapter(adapterListaOpcionesRecargaSos);
                        mensajeMontosView.setVisibility(View.GONE);
                        adapterListaOpcionesRecargaSos.notifyDataSetChanged();
                    } else {
                        solicitarRecarga.setVisibility(View.GONE);
                        mensajeMontosView.setText(datos.getMensaje());
                        mensajeMontosView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), MensajesDeUsuario.TITULO_SIN_DATOS, Toast.LENGTH_SHORT).show();
                    cargarMensajeFalla(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
                }
            }
        }
    }

    private void cargarMensajeFalla(String msg) {
        if (getView() != null) {
            Button botonDeSolicitud = (Button)getView().findViewById(R.id.solicitar_recarga_sos);
            contenedorSpinnerDeMontos.setVisibility(View.GONE);
            mensajePrestamoPendiente.setVisibility(View.VISIBLE);
            botonDeSolicitud.setEnabled(false);
            botonDeSolicitud.setTextColor(getResources().getColor(R.color.gris_texto_boton));
            cargarOpcionesRecarga(msg);
        }
    }

    private void cargarOpcionesRecarga(List<String> listaDeRecargas) {
        mensajePrestamoPendiente.setText("");
        listaOpcionesRecargaSos = listaDeRecargas;
    }

    private void cargarOpcionesRecarga(String mensaje) {
        listaOpcionesRecargaSos = new ArrayList<String>();
        listaOpcionesRecargaSos.add(mensaje);
        mensajePrestamoPendiente.setText(mensaje);
    }

    public void showLoader(final boolean show) {
        pantalla.setVisibility(show? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show? View.VISIBLE : View.GONE);
    }

    private void ejecutarPedirRecarga() {
        if (montoSeleccionado != null) {
            progress = ProgressDialog.show(getActivity(), getResources().getString(R.string.prestamo_saldo_title),
                    getResources().getString(R.string.solicitando_prestamo), true);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
            solicitarRecargaSos = new SolicitarRecargaSosRequest(getActivity(), new JacksonConverter(objectMapper));
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            try {
                activity.getSpiceManager().execute(
                        solicitarRecargaSos.solicitar(linea, montoSeleccionado),
                        new SolicitudRequestListener()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*private DialogFragment crearDialogo() {
        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ejecutarPedirRecarga();
                    }
                };
                DialogInterface.OnClickListener rechazarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.confirmar_prestamo_saldo))
                        .setTitle(getResources().getString(R.string.confirmar_title));
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                builder.setNegativeButton(getResources().getString(R.string.alta_usuario_btn_rechazar), rechazarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
    }*/

    /*private DialogFragment crearDialogo(final String titulo, final String mensaje) {



        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                DialogInterface.OnClickListener aceptarListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(mensaje).setTitle(titulo);
                builder.setPositiveButton(getResources().getString(R.string.alta_usuario_btn_aceptar), aceptarListener);
                AlertDialog dialog = builder.create();
                return dialog;
            }
        };
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {

            case RECARGA_SOS_CREAR_DIALOG_1:

                break;

            case RECARGA_SOS_CREAR_DIALOG_2:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(getActivity(),"Vino OK",Toast.LENGTH_SHORT).show();
                    ejecutarPedirRecarga();
                }

                break;


        }
    }

}
