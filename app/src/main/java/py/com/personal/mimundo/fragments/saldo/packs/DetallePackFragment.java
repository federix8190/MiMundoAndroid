package py.com.personal.mimundo.fragments.saldo.packs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.lineas.packs.OperacionPack;
import py.com.personal.mimundo.services.lineas.packs.Pack;
import py.com.personal.mimundo.services.lineas.packs.Packs;

/**
 * Created by Konecta on 17/09/2014.
 */
public class DetallePackFragment extends Fragment {

    private String titulo;
    private Pack[] listaPacks;
    private RecyclerView listaPacksView;
    private ListaPacksAdapter listaPacksAdapter;

    public static DetallePackFragment newInstance(String titulo, Pack[] listaPacks) {
        DetallePackFragment f = new DetallePackFragment();
        f.titulo = titulo;
        f.listaPacks = listaPacks;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            titulo = savedInstanceState.getString("titulo");
            String jsonObject = savedInstanceState.getString("listaPacks");
            Packs lista = new Gson().fromJson(jsonObject, Packs.class);
            listaPacks = lista.getLista();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Packs lista = new Packs(listaPacks);
        outState.putString("titulo", titulo);
        outState.putString("listaPacks", new Gson().toJson(lista));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalle_pack, container, false);
        System.err.println("DetallePackFragment onCreateView");

        getActivity().setTitle(titulo);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = false;
        //activity.setActionBar(false, null);
        listaPacksView = (RecyclerView) v.findViewById(R.id.lista_packs);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaPacksView.setLayoutManager(mLayoutManager);
        listaPacksView.setItemAnimator(new DefaultItemAnimator());
        String linea = activity.obtenerLineaSeleccionada();

        List<Pack> lista = new ArrayList<>();
        for (Pack p : listaPacks) {
            lista.add(p);
        }

        listaPacksAdapter = new ListaPacksAdapter(activity, linea, lista);
        listaPacksView.setAdapter(listaPacksAdapter);
        return v;
    }
}
