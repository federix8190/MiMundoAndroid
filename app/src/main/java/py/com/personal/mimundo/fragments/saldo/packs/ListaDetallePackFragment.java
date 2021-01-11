package py.com.personal.mimundo.fragments.saldo.packs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.services.lineas.packs.NodoPack;
import py.com.personal.mimundo.services.lineas.packs.Pack;

/**
 * Created by Konecta on 17/09/2014.
 */
public class ListaDetallePackFragment extends Fragment {

    private String titulo;
    private NodoPack nodoPack;
    private GridView listaPacksView;
    private ListaPackDetalleItemAdapter listaPacksAdapter;
    private List<NodoPack> listaNodosPacks;
    private List<Pack> listaPacks;
    private List<String> items;

    public static ListaDetallePackFragment newInstance(String titulo, NodoPack nodoPack) {
        ListaDetallePackFragment f = new ListaDetallePackFragment();
        f.titulo = titulo;
        f.nodoPack = nodoPack;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            titulo = savedInstanceState.getString("titulo");
            String jsonObject = savedInstanceState.getString("nodoPack");
            nodoPack = new Gson().fromJson(jsonObject, NodoPack.class);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("titulo", titulo);
        outState.putString("nodoPack", new Gson().toJson(nodoPack));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout v = (FrameLayout) inflater.inflate(R.layout.fragment_lista_packs, container, false);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = false;

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (listaNodosPacks != null && listaNodosPacks.size() > 0) {
                    NodoPack nodoPack = listaNodosPacks.get(position);
                    if (nodoPack != null) {
                        if (nodoPack.getHijos() != null) {
                            Fragment fragment = ListaDetallePackFragment.newInstance(nodoPack.getDescripcion(), nodoPack);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            if (nodoPack.getPacks() != null && nodoPack.getPacks().length > 0) {
                                String tituloDetalle = nodoPack.getDescripcion() + " - " + titulo;
                                Fragment fragment = DetallePackFragment.newInstance(tituloDetalle, nodoPack.getPacks());
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.opcion_sin_pakcs),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        };

        listaPacksView = (GridView) v.findViewById(R.id.lista_packs);
        listaPacksView.setOnItemClickListener(listener);
        cargarListaPacks(nodoPack);

        return v;
    }

    private void cargarListaPacks(NodoPack datos) {
        items = new ArrayList<String>();
        if (datos.getPacks() != null) {
            listaPacks = new ArrayList<Pack>();
            for (Pack pack : datos.getPacks()) {
                listaPacks.add(pack);
            }
        }
        if (datos.getHijos() != null) {
            listaNodosPacks = new ArrayList<NodoPack>();
            for (NodoPack nodoPack : datos.getHijos()) {
                items.add(nodoPack.getDescripcion());
                listaNodosPacks.add(nodoPack);
            }
            listaPacksAdapter = new ListaPackDetalleItemAdapter(getActivity());
            listaPacksAdapter.addCollection(listaNodosPacks);
            listaPacksView.setAdapter(listaPacksAdapter);
        }
    }

}
