package py.com.personal.mimundo.fragments;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.okhttp.Response;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;

import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.ClubPersonalRequest;
import py.com.personal.mimundo.services.clubpersonal.models.RespuestaDatosUsuarioClub;
import py.com.personal.mimundo.services.clubpersonal.models.RespuestaTotalPuntosClub;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class ClubPersonalFragment extends Fragment {

    private TextView txtNombreTitle;
    private TextView txtApellidoTitle;
    private TextView txtAdheshionTitle;
    private TextView txtNombre;
    private TextView txtApellido;
    private TextView txtAdheshion;
    private TextView txtPuntos;
    private TextView totalPuntos1;
    private TextView totalPuntos2;
    private TextView clubPersonalTitle1;
    private TextView clubPersonalMsg1;
    private TextView contactoTitle;
    private TextView contactoEmail;
    private TextView contactoTelefono;

    private ClubPersonalRequest clubPersonalRequest;
    private ClubPersonalRequest clubPersonalPuntosRequest;

   private LinearLayout contenedorPuntos;

    private LinearLayout asociarButton;
    //private ImageView googlePlayImg;
    private ImageView descargarAppBtn;
    private LinearLayout contenedorAsociate;

    public static ClubPersonalFragment newInstance() {
        ClubPersonalFragment f = new ClubPersonalFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_club_personal, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle("Club Personal");
        activity.setActionBar(false, "ClubPersonalFragment");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        clubPersonalRequest = new ClubPersonalRequest(getActivity(), new JacksonConverter(objectMapper));
        clubPersonalPuntosRequest = new ClubPersonalRequest(getActivity(), new JacksonConverter(objectMapper));

        descargarAppBtn = (ImageView)v.findViewById(R.id.button_descargar_app);

        txtNombreTitle = (TextView)v.findViewById(R.id.txt_Nombres_Club_title);
        txtApellidoTitle = (TextView)v.findViewById(R.id.txt_Apellidos_Club_title);
        txtAdheshionTitle = (TextView)v.findViewById(R.id.txt_Adhesion_Club_title);
        txtNombre = (TextView)v.findViewById(R.id.txt_Nombres_Club);
        txtApellido = (TextView)v.findViewById(R.id.txt_Apellidos_Club);
        txtAdheshion = (TextView)v.findViewById(R.id.txt_Adhesion_Club);
        txtPuntos = (TextView)v.findViewById(R.id.txt_PuntosClub);
        totalPuntos1 = (TextView)v.findViewById(R.id.total_puntos_1);
        totalPuntos2 = (TextView)v.findViewById(R.id.total_puntos_2);
        clubPersonalTitle1 = (TextView)v.findViewById(R.id.club_personal_title_1);
        clubPersonalMsg1 = (TextView)v.findViewById(R.id.club_personal_msg_1);
        contactoTitle = (TextView)v.findViewById(R.id.contacto_title);
        contactoEmail = (TextView)v.findViewById(R.id.contacto_email);
        contactoTelefono = (TextView)v.findViewById(R.id.contacto_telefono);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView)v.findViewById(R.id.beneficios_club_title)).setTypeface(tf1);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CarroisGothic-Regular.ttf");
        txtNombreTitle.setTypeface(tf);
        txtApellidoTitle.setTypeface(tf);
        txtAdheshionTitle.setTypeface(tf);
        txtNombre.setTypeface(tf);
        txtApellido.setTypeface(tf);
        txtAdheshion.setTypeface(tf);
        txtPuntos.setTypeface(tf);
        totalPuntos1.setTypeface(tf);
        totalPuntos2.setTypeface(tf);
        clubPersonalTitle1.setTypeface(tf);
        clubPersonalMsg1.setTypeface(tf);
        contactoTitle.setTypeface(tf);
        contactoEmail.setTypeface(tf);
        contactoTelefono.setTypeface(tf);

        contenedorAsociate = (LinearLayout)v.findViewById(R.id.contenedor_asociate);
        contenedorPuntos = (LinearLayout) v.findViewById(R.id.card_total_puntos);
        asociarButton = (LinearLayout) v.findViewById(R.id.asociarButton);
        contenedorPuntos.setVisibility(View.GONE);

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        myOptions.inDither = false;
        myOptions.inPurgeable = true;
        Resources res = getActivity().getResources();

        // Cargar imagen de los Beneficios del Club Personal
        Bitmap beneficiosClubBitmap = BitmapFactory.decodeResource(res, R.drawable.beneficios_club, myOptions);
        Drawable beneficiosClubDrawable = new BitmapDrawable(getActivity().getResources(), beneficiosClubBitmap);
        ImageView beneficiosClubView = (ImageView) v.findViewById(R.id.beneficios_club);
        beneficiosClubView.setImageDrawable(beneficiosClubDrawable);

        // Cargar imagen del icono de google play
        Bitmap googlePlayBitmap = BitmapFactory.decodeResource(res, R.drawable.gooplay_club, myOptions);
        Drawable googlePlayDrawable = new BitmapDrawable(getActivity().getResources(), googlePlayBitmap);
        //googlePlayImg.setImageDrawable(googlePlayDrawable);

        //RelativeLayout descargarAppClub = (RelativeLayout) v.findViewById(R.id.into_appClub);
        //descargarAppClub.setOnClickListener(new View.OnClickListener() {
        descargarAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = "py.com.personal.webvas.clubpersonal.app"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        //contenedorAsociate.setOnClickListener(new View.OnClickListener() {
        asociarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeroLinea = "*2582";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + numeroLinea.trim()));
                PackageManager pm = getActivity().getPackageManager();
                ComponentName cn = intent.resolveActivity(pm);
                if (cn == null) {
                    Uri marketUri = Uri.parse("market://search?q=call");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
                    if (marketIntent.resolveActivity(pm) != null) {
                        startActivity(marketIntent);
                    } else {
                        Log.d("App de llamada", "Play store no disponible");
                    }
                } else {
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        setearEstadoLoadingServ();
        setearEstadoLoadingServPuntos();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        try {
            activity.getSpiceManager().execute(clubPersonalRequest.solicitarDatosUsuarioClub(), new ClubPersonalRequestListener());
            activity.getSpiceManager().execute(clubPersonalPuntosRequest.solicitarTotalPuntosClub(), new ClubPersonalPuntosRequestListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public final class ClubPersonalRequestListener implements RequestListener<RespuestaDatosUsuarioClub> {
        Boolean  marca = false;
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Throwable cause = spiceException.getCause();
            String mensajeError = cause.getMessage();
            if (mensajeError.compareTo("404 Not Found") == 0 && getActivity() != null ) {
                marca = true;
                mostrarBotonAsociar();
            } else if (getActivity() != null) {
                removerEstadoLoading();
                setearMensajeDeFalla();
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final RespuestaDatosUsuarioClub datos) {
            if (getActivity() != null) {
                removerEstadoLoading();
                if (datos != null) {
                    cargarDatosServ(datos, marca);
                } else {
                    setearMensajeDeFalla();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setearEstadoLoadingServ() {
        txtAdheshion.setText(MensajesDeUsuario.TITULO_LOADING);
        txtApellido.setText(MensajesDeUsuario.TITULO_LOADING);
        txtNombre.setText(MensajesDeUsuario.TITULO_LOADING);
    }

    private void removerEstadoLoading() {
        txtAdheshion.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
    }

    private void setearMensajeDeFalla() {
        //contenedorDatosUsuario.setVisibility(View.GONE);
        contenedorPuntos.setVisibility(View.GONE);
        contenedorAsociate.setVisibility(View.GONE);
        // txtMensajeError.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        txtAdheshion.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        txtApellido.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
        txtNombre.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
    }

    private void cargarDatosServ(RespuestaDatosUsuarioClub datos, boolean marca) {
        if(datos.getFechaAlta() != null && !marca) {
            //contenedorDatosUsuario.setVisibility(View.VISIBLE);
            contenedorPuntos.setVisibility(View.VISIBLE);
            contenedorAsociate.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            String fechaAdesion = "";
            long tiempoMilisegundos = 0;
            try {
                tiempoMilisegundos = Long.parseLong(datos.getFechaAlta());
                calendar.setTimeInMillis(tiempoMilisegundos);
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                fechaAdesion = formater.format(calendar.getTime());
            } catch (Exception ex) {
                fechaAdesion = "No tiene Fecha de Adhesión";
            }
            txtAdheshion.setText(fechaAdesion);
            txtApellido.setText(datos.getApellido());
            txtNombre.setText(datos.getNombre());
        }else{
            //mostrar boton asociacion
            //contenedorDatosUsuario.setVisibility(View.GONE);
            contenedorPuntos.setVisibility(View.GONE);
            contenedorAsociate.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarBotonAsociar(){
        //contenedorDatosUsuario.setVisibility(View.GONE);
        contenedorPuntos.setVisibility(View.GONE);
        contenedorAsociate.setVisibility(View.VISIBLE);
    }

    public final class ClubPersonalPuntosRequestListener implements RequestListener<RespuestaTotalPuntosClub> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Throwable cause = spiceException.getCause();
            String mensajeError = cause.getMessage();
            if(mensajeError.compareTo("400 Bad Request") == 0 && getActivity() != null ){
                //nada
            } else if (getActivity() != null) {
                removerEstadoLoadingPuntos();
                setearMensajeDeFallaPuntos();
                Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final RespuestaTotalPuntosClub datos) {
            if (getActivity() != null) {
                removerEstadoLoadingPuntos();
                if (datos != null) {
                    cargarDatosServPuntos(datos);
                } else {
                    setearMensajeDeFallaPuntos();
                    Toast.makeText(getActivity(), MensajesDeUsuario.MENSAJE_FALLO_PETICION, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setearEstadoLoadingServPuntos() {
        //txtPuntos.setText(MensajesDeUsuario.TITULO_LOADING);
    }

    private void removerEstadoLoadingPuntos() {
        txtPuntos.setText("");
    }

    private void setearMensajeDeFallaPuntos() {
        txtPuntos.setText(MensajesDeUsuario.MENSAJE_SIN_SERVICIO);
    }

    private void cargarDatosServPuntos(RespuestaTotalPuntosClub datos) {
        txtPuntos.setText(datos.getTotal());
    }
}
