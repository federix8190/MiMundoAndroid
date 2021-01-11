package py.com.personal.mimundo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
//import com.nineoldandroids.view.ViewHelper;
//import com.nineoldandroids.view.ViewPropertyAnimator;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
//import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.disenhos.Formateador;
import py.com.personal.mimundo.disenhos.GeneradorDeDialogo;
import py.com.personal.mimundo.disenhos.MensajesDeUsuario;
import py.com.personal.mimundo.services.ErrorResponse;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.SampleRetrofitSpiceService;
import py.com.personal.mimundo.services.backtones.LineaBacktonesService;
import py.com.personal.mimundo.services.backtones.models.DatosCompra;
import py.com.personal.mimundo.services.usuarios.models.DatosUsuario;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.JacksonConverter;
import retrofit.mime.TypedByteArray;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class ReproduccionBacktoneActivity extends AppCompatActivity { //TODO FEDE implements ObservableScrollViewCallbacks {

    public static final String URL_TONO = "tono";
    public static final String ARTISTA = "artista";
    public static final String GENERO = "genero";
    public static final String ID_TONO = "idTono";
    private static final boolean TOOLBAR_IS_STICKY = false;
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    public static final String NUMERO_LINEA = "numeroLinea";
    public static final String TEMA = "tema";
    public static final String ES_AUTORENOVABLE = "esAutorenovable";
    public static final String ES_PARA_TODAS_LAS_LLAMADAS = "esParaTodasLasLlamadas";
    public static final String MONEDA = "moneda";
    public static final String URL_IMAGEN = "urlImagen";

    private int idTono;
    private String numeroLinea;

    private View mToolbar;
    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private ImageButton mFab;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private int mToolbarColor;
    private boolean mFabIsShown;

    private SpiceManager spiceManager = new SpiceManager(SampleRetrofitSpiceService.class);
    private Activity contexto;

    private LineaBacktonesService lineaBacktonesService;
    private ProgressDialog progress;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private boolean progressRun;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproduccion_backtone);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final Intent intent = getIntent();
        mImageView = findViewById(R.id.image);
        idTono = intent.getIntExtra(ID_TONO, -1);
        numeroLinea = intent.getStringExtra(NUMERO_LINEA);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        //TODO FEDE  mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.primario);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(0);

        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        //TODO FEDE mScrollView.setScrollViewCallbacks(this);

        mTitleView = (TextView) findViewById(R.id.title);
        String titulo = intent.getStringExtra(TEMA);
        mTitleView.setText(titulo);
        setTitle(null);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        try {
            final Button botonReproducir = (Button) findViewById(R.id.botonReproducir);
            final String urlTono = intent.getStringExtra(URL_TONO);
            final int right = 60;
            final int bottom = 60;
            View.OnClickListener listenerReproducir = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (progressRun) {
                        progressRun = false;
                        mediaPlayer.pause();
                        Drawable img = getResources().getDrawable( R.drawable.ic_play_arrow_white );
                        img.setBounds( 0, 0, right, bottom );
                        botonReproducir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    } else {
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            progressRun = true;
                            Drawable img = getResources().getDrawable( R.drawable.ic_pause_white );
                            img.setBounds( 0, 0, right, bottom );
                            botonReproducir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        } else {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlaye) {
                                    Drawable img = getResources().getDrawable( R.drawable.ic_pause_white );
                                    img.setBounds( 0, 0, right, bottom );
                                    botonReproducir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                                    mediaPlayer.start();
                                    progressRun = true;
                                    final int duration = mediaPlayer.getDuration();
                                    final int amoungToupdate = duration / 100;
                                    mTimer = new Timer();
                                    mTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (!(amoungToupdate * progressBar.getProgress() >= duration)
                                                            && progressRun) {
                                                        int p = progressBar.getProgress();
                                                        p += 1;
                                                        progressBar.setProgress(p);
                                                    }
                                                }
                                            });

                                        }
                                    }, amoungToupdate, amoungToupdate);
                                }
                            });

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Drawable img = getResources().getDrawable( R.drawable.ic_play_arrow_white );
                                    img.setBounds( 0, 0, right, bottom );
                                    botonReproducir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                                    progressRun = false;
                                    progressBar.setProgress(0);
                                }
                            });

                            try {
                                mediaPlayer.setDataSource(urlTono);
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            };
            botonReproducir.setOnClickListener(listenerReproducir);
        } catch (Exception e) {
        }

        View botonCOmpra = findViewById(R.id.boton_compra_backtone);
        View.OnClickListener listenerCompra = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO FEDE crearDialogo();
            }
        };
        botonCOmpra.setOnClickListener(listenerCompra);

        View contenedorDetalle = findViewById(R.id.detalle);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
                mScrollView.scrollTo(0, 0);
            }
        });

        // Invocacion de servicios.
        contexto = this;
        /*TODO FEDE Picasso.with(contexto).load(R.drawable.ic_headset_big).into((ImageView) mImageView);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
        Converter converter = new JacksonConverter(objectMapper);
        lineaBacktonesService = new LineaBacktonesService(this, converter);
        getSpiceManager().execute(lineaBacktonesService.obtenerDatosCompra(numeroLinea, idTono), new DatosCompraRequestListener());

        final String urlImagen = intent.getStringExtra(URL_IMAGEN);
        if (urlImagen != null && urlImagen.length() > 0) {

            try {

                Picasso picasso = new Picasso.Builder(this)
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                picasso.load(urlImagen + ".png").skipMemoryCache()
                                        .error(R.drawable.ic_headset_big)
                                        .into((ImageView) mImageView);
                            }
                        })
                        .build();
                        picasso.load(urlImagen + ".jpg").skipMemoryCache()
                        .into((ImageView) mImageView);

            } catch (Exception e) {
                e.printStackTrace();
                Picasso.with(contexto).load(R.drawable.ic_headset_big).into((ImageView) mImageView);
            }

        }*/
        //TODO FEDE agregarDetalle((LinearLayout) contenedorDetalle, intent);
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    private String testImagen(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        com.squareup.okhttp.Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.header("Content-Type");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    /*TODO FEDE public final class DatosCompraRequestListener implements RequestListener<Response> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getBaseContext(), "No se pudo obtener el precio del Tono", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Response response) {
            if (response != null) {
                View contenedorDetalle = findViewById(R.id.detalle);
                if (response.getStatus() == 400) {
                    try {
                        TypedByteArray body = (TypedByteArray) response.getBody();
                        String json = slurp(body.in(), 1024);
                        ErrorResponse error = new Gson().fromJson(json, ErrorResponse.class);
                        System.err.println("Datos Compra - Error : " + error.getCodigo() + " - " + error.getMensaje());
                        agregarDetalle((LinearLayout) contenedorDetalle, "¡Ya cuenta con el tono!");
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "No se pudo obtener el precio del Tono", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.getStatus() == 200) {
                    try {
                        TypedByteArray body = (TypedByteArray) response.getBody();
                        String json = slurp(body.in(), 1024);
                        DatosCompra datos = new Gson().fromJson(json, DatosCompra.class);
                        String precio = "";
                        if (datos.getUnidad().equalsIgnoreCase("M")) {
                            precio = "$ " + datos.getPrecio() + "por " + datos.getDias() + " días.";
                        } else if (datos.getUnidad().equalsIgnoreCase("C")) {
                            if (datos.getEsTrial()) {
                                precio = "GRATIS! y estará a modo de prueba por " + datos.getDias() + " días.";
                            } else {
                                precio = "Canjeá por tu vale de regalo GRATIS por " + datos.getDias() + " días.";
                            }
                        } else if (datos.getUnidad().equalsIgnoreCase("F")) {
                            precio = "Gratis! por " + datos.getDias() + " días.";
                        }
                        agregarDetalle((LinearLayout) contenedorDetalle, precio);
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "No se pudo obtener el precio del Tono", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getBaseContext(), "No se pudo obtener el precio del Tono", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void agregarDetalle(LinearLayout contenedorDetalle, Intent intent) {
        String tema = intent.getStringExtra(TEMA);
        String genero = intent.getStringExtra(GENERO);
        String artista = intent.getStringExtra(ARTISTA);
        Boolean esAutoRenovable = intent.getBooleanExtra(ES_AUTORENOVABLE, false);

        ArrayList<DetalleBacktone> detallesDelTono = new ArrayList<>();
        detallesDelTono.add(new DetalleBacktone("Nombre del tono", tema, R.drawable.ic_audiotrack));
        detallesDelTono.add(new DetalleBacktone("Artista", artista, R.drawable.ic_person));
        if (genero != null && !genero.isEmpty()) {
            detallesDelTono.add(new DetalleBacktone("Genero", genero, R.drawable.ic_queue_music));
        }
        String autoRenovable = esAutoRenovable ? "Sí" : "No";
        detallesDelTono.add(new DetalleBacktone("Es autorenovable?", autoRenovable, R.drawable.ic_refresh));

        LayoutInflater inflater = getLayoutInflater();
        for (DetalleBacktone detalle: detallesDelTono) {
            View view = inflater.inflate(R.layout.item_dos_lineas_con_icono, null);
            view.setBackgroundColor(getResources().getColor(android.R.color.white));
            TextView titulo = (TextView) view.findViewById(R.id.titulo);
            titulo.setText(detalle.getTitulo());
            TextView descripcion = (TextView) view.findViewById(R.id.descripcion);
            descripcion.setText(detalle.getDescripcion());
            ImageView imagen = (ImageView) view.findViewById(R.id.icono);
            imagen.setImageResource(detalle.getIcono());
            contenedorDetalle.addView(view);
        }
    }

    private void agregarDetalle(LinearLayout contenedorDetalle, String precio) {
        DetalleBacktone detalle = new DetalleBacktone("Precio", precio, R.drawable.ic_glass);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_dos_lineas_con_icono, null);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        TextView titulo = (TextView) view.findViewById(R.id.titulo);
        titulo.setText(detalle.getTitulo());
        TextView descripcion = (TextView) view.findViewById(R.id.descripcion);
        descripcion.setText(detalle.getDescripcion());
        ImageView imagen = (ImageView) view.findViewById(R.id.icono);
        imagen.setImageResource(detalle.getIcono());
        contenedorDetalle.addView(view);
    }

    private void crearDialogo() {
        DialogFragment newFragment = ReproduccionBacktoneActivity.BacktoneDialogFragment.newInstance(getString(R.string.backtones_desea_activar),
                getIntent().getStringExtra(TEMA));
        newFragment.show(getSupportFragmentManager(), "ComprarTonoDialogFragment");
    }

    public void backtonePositiveClick() {
        ejecutarOperacion();
    }

    public void backtoneNegativeClick() {

    }

    /*TODO FEDE public static class BacktoneDialogFragment extends DialogFragment {

        public static ReproduccionBacktoneActivity.BacktoneDialogFragment newInstance(String title, String message) {
            ReproduccionBacktoneActivity.BacktoneDialogFragment frag = new ReproduccionBacktoneActivity.BacktoneDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.backtones_comprar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ReproduccionBacktoneActivity)getActivity()).backtonePositiveClick();
                                }
                            }
                    )
                    .setNegativeButton(getResources().getString(R.string.backtones_cancelar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ReproduccionBacktoneActivity)getActivity()).backtoneNegativeClick();
                                }
                            }
                    )
                    .create();
        }
    }



    private void ejecutarOperacion() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(FAIL_ON_IGNORED_PROPERTIES).disable(FAIL_ON_UNKNOWN_PROPERTIES);
            lineaBacktonesService = new LineaBacktonesService(this, new JacksonConverter(objectMapper));

            if (idTono == -1) {
                return;
            }
            try {
                getSpiceManager().execute(
                    lineaBacktonesService.comprarBacktone(numeroLinea, idTono),
                    new ComprarBacktoneRequestListener()
                );
                progress = ProgressDialog.show(this, getResources().getString(R.string.backtones_title),
                        getResources().getString(R.string.backtones_realizando_compra), true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            if (progress != null) {
                progress.dismiss();
            }
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public final class ComprarBacktoneRequestListener implements RequestListener<Resultado> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            progress.dismiss();

            /*TODO FEDE DialogFragment mensaje = GeneradorDeDialogo.GeneradorDialogFragment.newInstance(
                    getResources().getString(R.string.backtones_title),
                    MensajesDeUsuario.MENSAJE_SIN_SERVICIO,
                    getResources().getString(R.string.alta_usuario_btn_aceptar));
            mensaje.show(getSupportFragmentManager(), "Dialogo-Backtone");
        }

        @Override
        public void onRequestSuccess(Resultado respuesta) {
            if (respuesta != null && respuesta.isExitoso()) {
                progress.dismiss();

                DialogFragment mensaje = GeneradorDeDialogo.GeneradorDialogFragment.newInstance(
                        getResources().getString(R.string.backtones_title),
                        getResources().getString(R.string.backtones_compra_exitosa),
                        getResources().getString(R.string.alta_usuario_btn_aceptar));
                mensaje.show(getSupportFragmentManager(), "Dialogo-Backtone");
            } else {
                progress.dismiss();

                /*TODO FEDE DialogFragment mensaje = GeneradorDeDialogo.GeneradorDialogFragment.newInstance(
                        getResources().getString(R.string.backtones_title),
                        respuesta.getMensaje(),
                        getResources().getString(R.string.alta_usuario_btn_aceptar));
                mensaje.show(getSupportFragmentManager(), "Dialogo-Backtone");
            }
        }
    }

    public static class DetalleBacktone {

        public String titulo;
        public String descripcion;
        public int icono;

        public DetalleBacktone(String titulo, String descripcion, int icono) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.icono = icono;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public int getIcono() {
            return icono;
        }
    }

    private int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, Math.max(minOverlayTransitionY, Math.min(0, -scrollY)));
        ViewHelper.setTranslationY(mImageView, Math.max(minOverlayTransitionY, Math.min(0, -scrollY / 2)));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, Math.max(0, Math.min(1, (float) scrollY / flexibleRange)));

        //Translate FAB
        /*TODO FEDE if (mFab != null) {
            int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
            int fabTranslationY = Math.max(mActionBarSize - mFab.getHeight() / 2,
                    Math.min(maxFabTranslationY, -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2));
            // ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);

            //Show/hide FAB
            if (ViewHelper.getTranslationY(mFab) < mFlexibleSpaceShowFabOffset) {
                hideFab();
            } else {
                showFab();
            }
        }

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                setBackgroundAlpha(mToolbar, 1, mToolbarColor);
            } else {
                setBackgroundAlpha(mToolbar, 0, mToolbarColor);
            }
        } else {
            //Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        if (scrollState == scrollState.UP) {
//            hideFab();
//        } else {
//            showFab();
//        }
     }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    private void setBackgroundAlpha(View view, float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        view.setBackgroundColor(a + rgb);
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

    @Override
    public void onPause() {
        if (progressRun && mFab != null) {
            mFab.performClick();
        }
        super.onPause();
    }

    private String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (; ; ) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            } finally {
                in.close();
            }
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Error : " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error : " + ex.getMessage());
        }
        return out.toString();
    }*/
}
