package py.com.personal.mimundo.fragments.gestion.facturacion;

import py.com.personal.mimundo.services.grupos.models.DetalleFactura;

/**
 * Created by Konecta on 03/11/2015.
 */
public class ResumenFactura {

    private boolean isHeader;
    private String nroLinea;
    private DetalleFactura datos;

    public ResumenFactura() {
    }

    public ResumenFactura(boolean isHeader, String nroLinea) {
        this.isHeader = isHeader;
        this.nroLinea = nroLinea;
    }

    public ResumenFactura(boolean isHeader, DetalleFactura datos) {
        this.isHeader = isHeader;
        this.datos = datos;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public String getNroLinea() {
        return nroLinea;
    }

    public void setNroLinea(String nroLinea) {
        this.nroLinea = nroLinea;
    }

    public DetalleFactura getDatos() {
        return datos;
    }

    public void setDatos(DetalleFactura datos) {
        this.datos = datos;
    }
}
