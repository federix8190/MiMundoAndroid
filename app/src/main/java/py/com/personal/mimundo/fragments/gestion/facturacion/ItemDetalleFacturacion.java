package py.com.personal.mimundo.fragments.gestion.facturacion;

/**
 * Created by Usuario on 8/12/2014.
 */
public class ItemDetalleFacturacion {

    private String nroFactura;
    private String vencimiento;
    private String estado;
    private String monto;

    public ItemDetalleFacturacion(String nroFactura, String vencimiento, String estado, String monto) {
        this.nroFactura = nroFactura;
        this.vencimiento = vencimiento;
        this.estado = estado;
        this.monto = monto;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public String getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

}
