package py.com.personal.mimundo.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.R;

public class BilleteraPersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billetera_personal);

        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Billetera Personal");

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) findViewById(R.id.title_1)).setTypeface(tf1);
        ((TextView) findViewById(R.id.title_2)).setTypeface(tf1);
        ((TextView) findViewById(R.id.billetera_personal_msg_1)).setTypeface(tf1);
        ((TextView) findViewById(R.id.billetera_personal_msg_2)).setTypeface(tf1);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/CarroisGothic-Regular.ttf");
        ((TextView) findViewById(R.id.billetera_personal_msg_3)).setTypeface(tf2);
        ((TextView) findViewById(R.id.billetera_personal_msg_4)).setTypeface(tf2);
        ((TextView) findViewById(R.id.boton_descarga_1)).setTypeface(tf2);
        ((TextView) findViewById(R.id.boton_descarga_2)).setTypeface(tf2);
        ((TextView) findViewById(R.id.boton_descarga_3)).setTypeface(tf2);
        ((TextView) findViewById(R.id.boton_descarga_4)).setTypeface(tf2);

        LinearLayout descargarAppBtn = (LinearLayout) findViewById(R.id.button_descargar_app);
        descargarAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = "py.com.personal.billetera";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException e) {
                }
            }
        });

    }

}
