package py.com.personal.mimundo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import py.com.personal.mimundo.utils.PreferenceUtils;

import android.os.Bundle;
import android.view.Menu;

public class HomeActivity extends BaseDrawerActivity {

    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        preferenceUtils = new PreferenceUtils(this);
        System.err.println("guardarLineaSeleccionada home activity : " + preferenceUtils.getLineaSeleccionada());

        inicializarVista(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}