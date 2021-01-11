package py.com.personal.mimundo.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import py.com.personal.mimundo.MyApplication;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;

/**
 * Created by Konecta on 07/10/2015.
 */
public class BilleteraPersonalFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_billetera_personal, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = true;
        activity.setTitle("Billetera Personal");
        activity.setActionBar(false, "BilleteraPersonalFragment");

        Typeface tf1 = Typeface.createFromAsset(activity.getAssets(), "fonts/Platform-Regular.otf");
        ((TextView) v.findViewById(R.id.title_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.title_2)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.billetera_personal_msg_1)).setTypeface(tf1);
        ((TextView) v.findViewById(R.id.billetera_personal_msg_2)).setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(activity.getAssets(), "fonts/CarroisGothic-Regular.ttf");
        ((TextView) v.findViewById(R.id.billetera_personal_msg_3)).setTypeface(tf2);
        ((TextView) v.findViewById(R.id.billetera_personal_msg_4)).setTypeface(tf2);

        ImageView descargarAppBtn = (ImageView) v.findViewById(R.id.button_descargar_app);
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

        return v;

    }
}
