package py.com.personal.mimundo.services.grupos.models;

/**
 * Created by Konecta on 25/07/2014.
 */
public class Direccion {

    private String id;
    private String direccion;
    private String direccionComplementaria;
    private String numeroDepartamento;
    private String numeroPiso;
    private String barrio;
    private String ciudad;
    private String esDireccionDeFacturacion;
    private String tipoDireccion;

    public Direccion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionComplementaria() {
        return direccionComplementaria;
    }

    public void setDireccionComplementaria(String direccionComplementaria) {
        this.direccionComplementaria = direccionComplementaria;
    }

    public String getNumeroDepartamento() {
        return numeroDepartamento;
    }

    public void setNumeroDepartamento(String numeroDepartamento) {
        this.numeroDepartamento = numeroDepartamento;
    }

    public String getNumeroPiso() {
        return numeroPiso;
    }

    public void setNumeroPiso(String numeroPiso) {
        this.numeroPiso = numeroPiso;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEsDireccionDeFacturacion() {
        return esDireccionDeFacturacion;
    }

    public void setEsDireccionDeFacturacion(String esDireccionDeFacturacion) {
        this.esDireccionDeFacturacion = esDireccionDeFacturacion;
    }

    public String getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }
}
