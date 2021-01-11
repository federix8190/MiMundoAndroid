package py.com.personal.mimundo.fragments.saldo.transferencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import py.com.personal.mimundo.activities.BaseDrawerActivity;
import py.com.personal.mimundo.activities.R;
import py.com.personal.mimundo.fragments.home.HomeFragment;
import py.com.personal.mimundo.fragments.saldo.packs.DetallePackFragment;
import py.com.personal.mimundo.fragments.saldo.packs.ListaPacksAdapter;
import py.com.personal.mimundo.services.Resultado;
import py.com.personal.mimundo.services.lineas.packs.Pack;

public class ResultadoTransferenciasFragment extends Fragment {

    private String titulo;
    private List<ResultadoTransferencia> listaResultados;
    private RecyclerView listaResultadosView;
    private ListaResultadosAdapter adapter;

    public static ResultadoTransferenciasFragment newInstance(List<ResultadoTransferencia> listaResultados) {
        ResultadoTransferenciasFragment f = new ResultadoTransferenciasFragment();
        f.listaResultados = listaResultados;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_resultado_transferencias, container, false);
        System.err.println("DetallePackFragment onCreateView");

        getActivity().setTitle(titulo);
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.salirApp = false;
        activity.setTitle("Estados de Tansferencias");

        listaResultadosView = (RecyclerView) v.findViewById(R.id.lista_resultados);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listaResultadosView.setLayoutManager(mLayoutManager);
        listaResultadosView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ListaResultadosAdapter(activity, listaResultados);
        listaResultadosView.setAdapter(adapter);

        Button volver = (Button) v.findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;

    }
}
