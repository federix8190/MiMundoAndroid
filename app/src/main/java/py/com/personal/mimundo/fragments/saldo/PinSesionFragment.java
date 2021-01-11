package py.com.personal.mimundo.fragments.saldo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.Constantes;
import py.com.personal.mimundo.disenhos.Roles;
import py.com.personal.mimundo.fragments.home.HomeFragment;
import py.com.personal.mimundo.fragments.saldo.packs.ListaPacksFragment;
import py.com.personal.mimundo.fragments.saldo.prestamo.PrestamoSaldoFragment;
import py.com.personal.mimundo.fragments.saldo.recarga.RecargaContraFacturaFragment;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaCorporativaPaso1;
import py.com.personal.mimundo.fragments.saldo.transferencias.TransferenciaSaldoIndividualFragment;
import py.com.personal.mimundo.services.pines.models.ValidarPinSesion;
import py.com.personal.mimundo.services.pines.service.PinValidacionService;
import py.com.personal.mimundo.utils.PreferenceUtils;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by mabpg on 07/06/17.
 */

public class PinSesionFragment extends Fragment {

    private PinValidacionService service;
    private boolean pinCreado = false;
    private TextView mContinuar;
    private TextView mCancelar;
    private EditText mPinIngresado;

    // SubMenu de saldos
   private String[] CODIGO_MENU_SALDO;
    private List<String> navSubMenuSaldoTitles;
    private List<Fragment> pantallasSaldos;
    public String fragmentActual;
    private String pinSesionId;

    @Inject
    private PreferenceUtils preferenceUtils;

    public static PinSesionFragment newInstance(int childPosition, String nroLinea,int groupPosition) {
        PinSesionFragment myFragment = new PinSesionFragment();

        Bundle args = new Bundle();
        args.putInt("childPosition", childPosition);
        args.putString("nombreUsuario",nroLinea);
        args.putInt("groupPosition",groupPosition);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.controlar_pin_sesion, container, false);
        final BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.setTitle("Validación de Pin");
        activity.setActionBar(false, "PinSesionFragment");

        /*recuperar del editText*/
        mPinIngresado = (EditText) v.findViewById(R.id.pin_sesion_edit_text);
        mContinuar = (Button) v.findViewById(R.id.pin_sesion_boton_continuar);
        mCancelar = (Button) v.findViewById(R.id.pin_sesion_boton_cancelar);

        mContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    cargarMenu();

                    if (mPinIngresado.getText() != null && !mPinIngresado.getText().toString().isEmpty()) {

                        if (!pinSesionId.isEmpty()) {
                            String respuestaPin = mPinIngresado.getText().toString();
                            activity.getSpiceManager().execute(service.validarPinSesion(getArguments().getString("nombreUsuario"),pinSesionId,respuestaPin), new ValidacionPinSesionListener());
                        }

                    } else {
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.error_pin_invalido),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                     Toast.makeText(getActivity(), "Error al crear la vista de validación de pin", Toast.LENGTH_LONG).show();
                }
            }
        });

        mCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HomeFragment();

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                        fragmentManager.popBackStack();
                    }
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                } else {
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        return v;
    }

    public List<String> obtenerRolesUsuario() {
        preferenceUtils = new PreferenceUtils(getActivity());
        return preferenceUtils.getRolesUsuario();
    }


    public void cargarMenu(){

        CODIGO_MENU_SALDO = getResources().getStringArray(R.array.codigo_nav_drawer_items_saldo);
        pantallasSaldos = new ArrayList<Fragment>();
        navSubMenuSaldoTitles = new ArrayList<String>();

        /*if (obtenerRolesUsuario().contains(Roles.MODIFICAR_LIMITE_CONSUMO)) {
            pantallasSaldos.add(new AumentoLimiteConsumoFragment());
        }*/
        if (obtenerRolesUsuario().contains(Roles.COMPRA_PACKS)) {
            pantallasSaldos.add(new ListaPacksFragment());
        }
        if (obtenerRolesUsuario().contains(Roles.PEDIR_SALDO)) {
            pantallasSaldos.add(new PedirSaldoFragment());
        }
        if (obtenerRolesUsuario().contains(Roles.PRESTAR_SALDO)) {
            pantallasSaldos.add(new PrestamoSaldoFragment());
        }
        if (obtenerRolesUsuario().contains(Roles.RECARGA_CONTRA_FACTURA)) {
            pantallasSaldos.add(new RecargaContraFacturaFragment());
        }
        if (obtenerRolesUsuario().contains(Roles.TRANSFERENCIA_SALDO)) {
            if (preferenceUtils.getTipoPersona().equals(Constantes.TIPO_PERSONA_FISICA)) {
                pantallasSaldos.add(TransferenciaSaldoIndividualFragment.newInstance());
            } else if (preferenceUtils.getTipoPersona().equals(Constantes.TIPO_PERSONA_JURIDICA)) {
                pantallasSaldos.add(TransferenciaCorporativaPaso1.newInstance());
            }
        }
    }

    @Override
    public void onStart() {

        try {
            super.onStart();

            if (!pinCreado) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                service = new PinValidacionService(getActivity(), new JacksonConverter(objectMapper));

                /*Se genera el pin*/
                final BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
                activity.getSpiceManager().execute(
                        service.generarPin(getArguments().getString("nombreUsuario"), "SMS"),
                        new CreacionPinSesionListener()
                );
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al inicializar Pin Sesion ", Toast.LENGTH_LONG).show();

        }
    }

    public final class CreacionPinSesionListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Error al crear pin de sesión", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Response respuesta) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {
                List<Header> headers = respuesta.getHeaders();
                for (int i = 0; i < headers.size(); i++) {
                    if (headers.get(i).getName().equals("Location")) {
                        String  cadena = headers.get(i).getValue().toString();
                        String[] numerosComoArray = cadena.split("/");
                        String pinId = numerosComoArray[numerosComoArray.length-1];
                        pinSesionId = pinId; /*id del pin creado*/
                        pinCreado = true;
                        break;
                    }
                }
            }
        }
    }

    public final class ValidacionPinSesionListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Error al validar el pin de sesión", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Response respuesta) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            if (activity != null) {

                if (respuesta!=null) {
                    if (respuesta.getStatus() == 200) {
                        SharedPreferences pref = getActivity().getSharedPreferences("MiMundoPreferences", BaseDrawerActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("pinSesion", true);
                        editor.commit();
                        redirigir();
                    } else {
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.error_pin_invalido),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.error_pin_validar),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void redirigir() {

        Fragment fragment = null;

        fragmentActual = CODIGO_MENU_SALDO[getArguments().getInt("groupPosition")];
        fragment = pantallasSaldos.get(getArguments().getInt("childPosition"));

        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment, "").commit();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

    }
}
