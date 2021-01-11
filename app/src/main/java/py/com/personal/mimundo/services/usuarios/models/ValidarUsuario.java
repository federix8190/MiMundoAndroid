package py.com.personal.mimundo.services.usuarios.models;

/**
 * Created by Konecta on 23/07/2014.
 */
public class ValidarUsuario {

    private String tipoUsuario;
    private Long codigoCliente;
    private String nombreUsuario;
    private String numeroLinea;

    public ValidarUsuario(){
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreCliente) {
        this.nombreUsuario = nombreCliente;
    }

    public String getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }
}
