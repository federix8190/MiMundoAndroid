package py.com.personal.mimundo.fragments.gestion.facturacion;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.FileDownloader;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.adapters.administracion.AdapterDatosPerfil;
import py.com.personal.mimundo.adapters.administracion.DatoPerfil;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.cache.facturacion.DetalleFacturaDigitalRequest;
import py.com.personal.mimundo.services.cache.facturacion.DetallesFactura;
import py.com.personal.mimundo.services.grupos.models.DetalleFactura;
import py.com.personal.mimundo.services.grupos.models.Factura;
import py.com.personal.mimundo.services.grupos.models.ListaDetalleFactura;
import py.com.personal.mimundo.utils.ListUtils;
import py.com.personal.mimundo.utils.NumbersUtils;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import retrofit.mime.TypedByteArray;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by Konecta on 07/08/2014.
 */
public class DetalleFacturaFragment extends Fragment {

    private static final int  MEGABYTE = 1024 * 1024;

    private static final String NOMBRE_ARCHIVO = "Detalle-Factura.pdf";
    public static final String CODIGO_SIN_DATO = "sinDato";
    public static final String CODIGO_LOADING = "loading";
    public static final String CODIGO_SIN_SERVICIO = "sinServicio";

    private BaseDrawerActivity activity;
    private String codigoGrupo;
    private Long numeroFactura;

    // Objeto que almacena los datos de la factura.
    private Factura datos;
    private String lineaSeleccionada;

    // Lista de detalle.
    private ListView listaDetalleView;
    private DetalleFacturaAdapter listaDetalleAdapter;
    private List<DatoPerfil> listaDetalle;

    // Lista resumenes.
    private ListView listaResumenesView;
    private ResumenesFacturaAdapter adapterResumenesFactura;
    private List<ResumenFactura> listaResumenesFactura;

    private DetallesFactura detallesFacturaRequest;
    private List<DetalleFactura> resumenesFactura;

    // Factura Digital
    private Button descargarButton;
    private DetalleFacturaDigitalRequest facturaDigitalRequest;
    private ProgressDialog progress;
    private TypedByteArray input;

    public static DetalleFacturaFragment newInstance(Factura factura) {
        DetalleFacturaFragment f = new DetalleFacturaFragment();
        f.datos = factura;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            String jsonDatosFactura = savedInstanceState.getString("datosFactura");
            datos = new Gson().fromJson(jsonDatosFactura, Factura.class);
            String jsonObject = savedInstanceState.getString("listaResumenesFactura");
            ListaDetalleFactura lista = new Gson().fromJson(jsonObject, ListaDetalleFactura.class);
            resumenesFactura = lista.getLista();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ListaDetalleFactura lista = new ListaDetalleFactura();
        lista.setLista(resumenesFactura);
        outState.putString("listaResumenesFactura", new Gson().toJson(lista));
        outState.putString("datosFactura", new Gson().toJson(datos));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_detalle_factura, container, false);
        activity = (BaseDrawerActivity) getActivity();
        activity.setTitle("Datos de la factura");
        //activity.hideSelector();
        activity.fragmentActual = "DetalleFacturaFragment";
        lineaSeleccionada = activity.obtenerLineaSeleccionada();
        activity.setActionBar(false, "DetalleFacturaFragment");
        activity.salirApp = false;

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.primerTitulo)).setTypeface(tf);
        //((TextView) v.findViewById(R.id.segundoTitulo)).setTypeface(tf);

        listaDetalleView = (ListView)v.findViewById(R.id.listaDetalle);
        listaResumenesView = (ListView)v.findViewById(R.id.listaDetalleResumen);
        cargarDatosDetalle(datos);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();

                progress = ProgressDialog.show(getActivity(), getResources().getString(R.string.factura_en_pdf),
                        getResources().getString(R.string.descargando_datos), true);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
                facturaDigitalRequest = new DetalleFacturaDigitalRequest(activity, new JacksonConverter(objectMapper));
                codigoGrupo = datos.getGrupoFacturacion();
                String codigoDocumento = datos.getCodigoDocumento();
                numeroFactura = datos.getNumeroFactura();
                try {
                    activity.getSpiceManager().execute(
                            facturaDigitalRequest.consultar(codigoGrupo, codigoDocumento, numeroFactura),
                            new FacturaDigitalRequestListener()
                    );
                } catch (Exception e) {
                    progress.dismiss();
                }

            }
        };
        descargarButton = (Button) v.findViewById(R.id.descargar_factura_btn);
        descargarButton.setTypeface(tf);
        descargarButton.setOnClickListener(listener);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        detallesFacturaRequest = new DetallesFactura(getActivity(), new JacksonConverter(objectMapper));
        return v;
    }

    /**
     * Obtiene el detalle de la factura en formato PDF.
     */
    public final class FacturaDigitalRequestListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            if (getActivity() != null) {
                progress.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_detalle_factura),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final Response response) {
            System.err.println("FacturaDigitalRequestListener onRequestSuccess : " + response.getStatus());
            System.err.println("FacturaDigitalRequestListener onRequestSuccess : " + response.getBody().length());
            if (getActivity() != null) {
                progress.dismiss();
                if (response != null) {

                    try {

                        input = (TypedByteArray) response.getBody();
                        //File file = Environment.getExternalStoragePublicDirectory(NOMBRE_ARCHIVO);
                        /*File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + NOMBRE_ARCHIVO);
                        OutputStream out = new FileOutputStream(file);
                        out.write(input.getBytes());
                        out.close();
                        openFile();*/

                        Activity activity = getActivity();
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Permission is not granted Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                System.err.println("No tiene permiso de lectura");
                            } else {
                                // No explanation needed; request the permission
                                requestPermissions(new String[] {
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                },0);
                            }

                        } else {

                            // Permission has already been granted
                            descargarFactura();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_detalle_factura),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void descargarFactura() throws IOException {

        Activity activity = getActivity();
        String fileName = codigoGrupo + "_" + numeroFactura + ".pdf";
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), fileName);

        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!pdfFile.exists()) {
            System.err.println("Archivo no existe");
            return;
        }

        FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

        byte[] buffer = new byte[MEGABYTE];
        int bufferLength = 0;

        fileOutputStream.write(input.getBytes());
        fileOutputStream.close();

        if (pdfFile == null) {
            return;
        }
        System.err.println("Abrir PDF : " + pdfFile.getAbsolutePath() + " - " + pdfFile.length());

        //Uri path = Uri.fromFile(pdfFile);
        Uri path = FileProvider.getUriForFile(getActivity(),
                "py.com.personal.mimundo.activities", pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        System.err.println("Path URI : " + path);
        //Intent intent = Intent.createChooser(pdfIntent, "Open File");
        try {
            startActivity(pdfIntent);
        } catch(ActivityNotFoundException e) {
            System.err.println("No se puede Abrir PDF");
            Toast.makeText(activity, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        descargarFactura();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (resumenesFactura == null) {
            BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
            setearMensajeLoading();
            activity.getSpiceManager().execute(detallesFacturaRequest.obtenerDetallesDeFactura(
                    datos.getGrupoFacturacion(),
                    datos.getCodigoDocumento(),
                    datos.getNumeroFactura()
                    ), new DetallesFacturaListener()
            );
        } else {
            cargarResumenesDeFactura(resumenesFactura);
        }
    }

    public final class DetallesFacturaListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Activity context = getActivity();
            if(context != null) {
                removerMensajeLoading();
                setearMensajeFalla();
                Toast.makeText(context, MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestSuccess(final List datos) {
            Activity context = getActivity();
            if(context != null) {
                removerMensajeLoading();
                if (datos != null) {
                    cargarResumenesDeFactura(datos);
                    resumenesFactura = datos;
                } else {
                    setearMensajeFalla();
                    Toast.makeText(context, MensajesDeUsuario.MENSAJE_SIN_SERVICIO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void cargarResumenesDeFactura(List<DetalleFactura> datos) {
        if (datos != null && datos.size() > 0) {
            String nroLinea = datos.get(0).getNumeroLinea();
            ResumenFactura res = new ResumenFactura(true, nroLinea);
            listaResumenesFactura = new ArrayList<>();
            if (nroLinea == null || nroLinea.isEmpty()) {
                nroLinea = lineaSeleccionada;
            } else {
                listaResumenesFactura.add(res);
            }
            for (DetalleFactura d : datos) {
                if (nroLinea != null && nroLinea.equals(d.getNumeroLinea())) {
                    res = new ResumenFactura(false, d);
                    listaResumenesFactura.add(res);
                } else {
                    nroLinea = d.getNumeroLinea();
                    if (nroLinea == null || nroLinea.isEmpty()) {
                        nroLinea = lineaSeleccionada;
                    }
                    res = new ResumenFactura(true, nroLinea);
                    listaResumenesFactura.add(res);
                    res = new ResumenFactura(false, d);
                    listaResumenesFactura.add(res);
                }
            }
        } else {
            DetalleFactura vacio = new DetalleFactura();
            vacio.setDescripcion(CODIGO_SIN_DATO);
            ResumenFactura res = new ResumenFactura(false, vacio);
            listaResumenesFactura = new ArrayList<>();
            listaResumenesFactura.add(res);
        }
        System.err.println("listaResumenesFactura : " + listaResumenesFactura.size());
        adapterResumenesFactura = new ResumenesFacturaAdapter(getActivity(), listaResumenesFactura);
        listaResumenesView.setAdapter(adapterResumenesFactura);
        adapterResumenesFactura.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaResumenesView, 380);
    }

    public void setearMensajeLoading() {
        listaResumenesFactura = new ArrayList<>();
        adapterResumenesFactura = new ResumenesFacturaAdapter(getActivity(), listaResumenesFactura);
        listaResumenesView.setAdapter(adapterResumenesFactura);
        DetalleFactura vacio = new DetalleFactura();
        vacio.setDescripcion(CODIGO_LOADING);
        ResumenFactura res = new ResumenFactura(false, vacio);
        listaResumenesFactura.add(res);
        adapterResumenesFactura.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaResumenesView, 32);
    }

    public void removerMensajeLoading() {
        listaResumenesFactura.remove(0);
        adapterResumenesFactura.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaResumenesView, 32);
    }

    public void setearMensajeFalla() {
        listaResumenesFactura = new ArrayList<>();
        adapterResumenesFactura = new ResumenesFacturaAdapter(getActivity(), listaResumenesFactura);
        listaResumenesView.setAdapter(adapterResumenesFactura);
        DetalleFactura vacio = new DetalleFactura();
        vacio.setDescripcion(CODIGO_SIN_SERVICIO);
        ResumenFactura res = new ResumenFactura(false, vacio);
        listaResumenesFactura.add(res);
        adapterResumenesFactura.notifyDataSetChanged();
        ListUtils.setListViewHeightBasedOnChildren(listaResumenesView, 32);
    }

    private void openFile() {
        /*File file = Environment.getExternalStoragePublicDirectory(NOMBRE_ARCHIVO);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);*/
        System.err.println("Open File !!!");
    }

    public void cargarDatosDetalle(Factura datos) {
        listaDetalle =  new ArrayList<>();
        listaDetalleAdapter =  new DetalleFacturaAdapter(getActivity(), listaDetalle);
        listaDetalleView.setAdapter(listaDetalleAdapter);

        if (datos != null) {
            String numeroFacturaString = datos.getNumeroFactura() == null? MensajesDeUsuario.MENSAJE_NULL:
                    String.valueOf(datos.getNumeroFactura());
            String fechaEmisionString = datos.getFechaEmision() == null? MensajesDeUsuario.MENSAJE_NULL:
                    formatearFecha(datos.getFechaEmision());
            String fechaVencimientoString = datos.getFechaVencimiento() == null? MensajesDeUsuario.MENSAJE_NULL:
                    formatearFecha(datos.getFechaVencimiento());
            String direccion = datos.getDireccion() == null? MensajesDeUsuario.MENSAJE_NULL:
                    datos.getDireccion();
            String montoNetoString = datos.getMontoNeto() == null? MensajesDeUsuario.MENSAJE_NULL:
                    NumbersUtils.formatear(datos.getMontoNeto().longValue()) + " Gs.";
            String montoIvaString = datos.getMontoIva() == null? MensajesDeUsuario.MENSAJE_NULL:
                    NumbersUtils.formatear(datos.getMontoIva().longValue()) + " Gs.";
            String montoTotalString = datos.getMontoTotal() == null? MensajesDeUsuario.MENSAJE_NULL:
                    NumbersUtils.formatear(datos.getMontoTotal().longValue()) + " Gs.";

            // Se inyectan los datos.
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.numero_factura_label),
                    numeroFacturaString, R.drawable.ic_hash_fact));
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.fecha_emision_label),
                    fechaEmisionString, R.drawable.ic_emision_fact));
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.fecha_vencimiento_label),
                    fechaVencimientoString, R.drawable.ic_vencimiento_fact));
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.direccion_label),
                    direccion, R.drawable.ic_direccion_fact));
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.monto_label),
                    montoNetoString, R.drawable.ic_monto));
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.iva_label),
                    montoIvaString, R.drawable.ic_iva_fact));
            listaDetalle.add(new DatoPerfil(getResources().getString(R.string.total_label),
                    montoTotalString, R.drawable.ic_total_fact));
            listaDetalleAdapter.notifyDataSetChanged();
            ListUtils.setListViewHeightBasedOnChildren(listaDetalleView, 60);

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.error_obtener_datos_factura),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public String formatearFecha(Long vencimiento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date vencimientoDate = new Date(vencimiento);
        return sdf.format(vencimientoDate);
    }
}
