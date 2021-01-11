package py.com.personal.mimundo.fragments.gestion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.cache.TiposDocumentoRequest;
import py.com.personal.mimundo.services.cache.facturacion.DireccionesGrupoFacturacionRequest;
import py.com.personal.mimundo.services.cache.facturacion.ObtenerGruposFacturacionRequest;
import py.com.personal.mimundo.services.grupos.models.Direccion;
import py.com.personal.mimundo.services.grupos.service.ModificarDireccionGrupo;
import py.com.personal.mimundo.services.pines.models.ValidarMail;
import py.com.personal.mimundo.services.pines.service.PinValidacionService;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuario;
import py.com.personal.mimundo.services.usuarios.service.ModificarDatosUsuarioService;
import py.com.personal.mimundo.services.usuarios.service.UsuarioService;
import py.com.personal.mimundo.utils.PreferenceUtils;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class EditarPerfilFragment extends Fragment {

    private EditText nombre;
    private EditText emailView;
    private EditText numeroDocumento;
    private EditText telefonoContacto;

    //private EditText ciudad;
    //private EditText barrio;
    private EditText direccionParticular;
    //private EditText direccionFacturacion;
    private String email;
    //private Spinner tipoDocumento;

    private UsuarioService datosUsuarioRequest;
    private ObtenerGruposFacturacionRequest gruposFacturacionRequest;
    private DireccionesGrupoFacturacionRequest direccionesGrupoRequest;
    private TiposDocumentoRequest tiposDocumentoRequest;
    private PinValidacionService pinService;

    private ProgressDialog progress;
    private ModificarDatosUsuarioService modificarDatosUsuarioRequest;
    private ModificarDireccionGrupo modificarDireccionGrupoRequest;

    private List<String> codTipoDocumento;
    private List<String> descTipoDocumento;
    private String codigoGrupo;
    private String lineaMadre;
    private String tipoDocumentoActual;
    private Direccion direccionActual;
    private String nuevaDireccion;
    private Button modificarDatos;

    private PreferenceUtils preferenceUtils;
    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);
    private BaseDrawerActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_modificar_datos_usuario, container, false);
        activity = (BaseDrawerActivity) getActivity();
        activity.setTitle("Editar Perfil");
        activity.salirApp = true;
        activity.setActionBar(false, "EditarPerfilFragment");

        nombre = (EditText) v.findViewById(R.id.nombre_field);
        emailView = (EditText) v.findViewById(R.id.email_field);
        numeroDocumento = (EditText) v.findViewById(R.id.numero_documento_field);
        telefonoContacto = (EditText) v.findViewById(R.id.telefono_contacto_field);
        direccionParticular = (EditText) v.findViewById(R.id.direccion_particular_field);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        };


        modificarDatos = (Button) v.findViewById(R.id.button_editar_datos_usuario);
        modificarDatos.setOnClickListener(listener);

        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "fonts/Platform-Regular.otf");
        nombre.setTypeface(tf1);
        emailView.setTypeface(tf1);
        numeroDocumento.setTypeface(tf1);
        telefonoContacto.setTypeface(tf1);
        direccionParticular.setTypeface(tf1);
        /*ciudad.setTypeface(tf1);
        barrio.setTypeface(tf1);
        direccionFacturacion.setTypeface(tf1);*/
        modificarDatos.setTypeface(tf1);
        ((TextView) v.findViewById(R.id.nombre_label)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.email_label)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.numero_documento_label)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.telefono_contacto_label)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.direccion_particular_label)).setTypeface(tf1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        Converter converter = new JacksonConverter(objectMapper);
        datosUsuarioRequest = new UsuarioService(activity, converter, true);
        gruposFacturacionRequest = new ObtenerGruposFacturacionRequest(activity, converter);
        tiposDocumentoRequest = new TiposDocumentoRequest(activity, converter);
        direccionesGrupoRequest = new DireccionesGrupoFacturacionRequest(activity, converter);
        modificarDireccionGrupoRequest = new ModificarDireccionGrupo(activity, converter);
        pinService = new PinValidacionService(activity, converter, true);

        progress = ProgressDialog.show(activity, getResources().getString(R.string.editar_perfil_title),
                getResources().getString(R.string.cargando_datos), true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        System.err.println("Usuario logueado : " + obtenerUser());
        System.err.println("Usuario logueado : " + activity.obtenerCodigoUsuario());
        activity.getSpiceManager().execute(datosUsuarioRequest.datosUsuario(activity.obtenerCodigoUsuario()), new DatosUsuarioRequestListener());
    }

    private void enablePantalla() {

        boolean marca = true;//marcaDatosUsuario && marcaDatosGrupo;

        nombre.setVisibility(marca? View.VISIBLE: View.GONE);
        emailView.setVisibility(marca? View.VISIBLE: View.GONE);
        numeroDocumento.setVisibility(marca? View.VISIBLE: View.GONE);
        telefonoContacto.setVisibility(marca? View.VISIBLE: View.GONE);
        direccionParticular.setVisibility(marca? View.VISIBLE: View.GONE);
        modificarDatos.setVisibility(marca? View.VISIBLE: View.GONE);

        if (marca && progress != null) {
            progress.dismiss();
        }
    }

    /**
     * Obtiene los datos del usuario.
     */
    public final class DatosUsuarioRequestListener implements RequestListener<DatosUsuario> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            enablePantalla();
            Toast.makeText(activity, "No se pudo obtener los datos del usuario", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final DatosUsuario datos) {
            enablePantalla();
            if (datos != null) {
                cargarDatosUsuario(datos);
                email = datos.getEmail();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
            } else {
                Toast.makeText(activity, "No se pudo obtener los datos del usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarDatosUsuario(DatosUsuario datos) {
        tipoDocumentoActual = datos.getTipoDocumento();
        String titular = "";
        if (datos.getNombres() != null && datos.getApellidos() == null) {
            titular = datos.getNombres();
        } else if (datos.getNombres() == null && datos.getApellidos() != null) {
            titular = datos.getApellidos();
        } else if (datos.getNombres() != null && datos.getApellidos() != null) {
            titular = datos.getNombres() + " " + datos.getApellidos();
        }
        nombre.setText(titular);
        nombre.setHint(null);
        emailView.setText(datos.getEmail());
        //apellido.setText(datos.getApellidos());
        numeroDocumento.setText(datos.getNumeroDocumento());
        telefonoContacto.setText(datos.getTelefonoContacto());
        //ciudad.setText(datos.getCiudad());
        //barrio.setText(datos.getBarrio());
        direccionParticular.setText(datos.getDireccion());
    }

    private String obtenerDato(EditText parametro, String msg) throws Exception {
        if(parametro.getText() != null && !parametro.getText().toString().isEmpty()) {
            return parametro.getText().toString();
        } else {
            return null;
            //throw new Exception(msg);
        }
    }

    private String obtenerUser() {
        final SharedPreferences pref = activity.getSharedPreferences("MiMundoPreferences", 0);
        String user = pref.getString("user", "");
        return user;
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        editText.setText(email);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        email = editText.getText().toString();
                        final ValidarMail datos = new ValidarMail();
                        if (email != null && !email.trim().isEmpty()) {
                            try {
                                datos.setNombreUsuario(activity.obtenerCodigoUsuario());
                                datos.setAsunto("Pin de validación");
                                datos.setCanalEnvio("MAIL");
                                datos.setEmail(email);
                                datos.setMensaje("Pin de validación de mail.");
                                activity.getSpiceManager().execute(pinService.validarMail(datos), new ValidarMailRequestListener());
                            } catch (Exception e) {
                                Toast.makeText(activity, "Ocurrió un error inesperado. Comuníquese al *111 para más información",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "Ingrese el email", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public final class ValidarMailRequestListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            System.err.println("Erorr correo : " + spiceException.getMessage());
            Toast.makeText(activity, "Ocurrió un error inesperado. Comuníquese al *111 para más información", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Response datos) {

            if (datos != null) {
                Toast.makeText(activity, "Se ha enviado un correo a la direccion registrada.", Toast.LENGTH_SHORT).show();
            } else {
                System.err.println("Erorr validar correo null ");
                Toast.makeText(activity, "Ocurrió un error inesperado. Comuníquese al *111 para más información", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
