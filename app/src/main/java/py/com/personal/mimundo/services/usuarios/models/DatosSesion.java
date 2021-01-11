package py.com.personal.mimundo.services.usuarios.models;

/**
 * Created by Konecta on 24/07/2014.
 */
public class DatosSesion {

    private Long cantidadIngresos;
    private Long ultimoLogin;
    private String ultimoIp;
    private boolean bloqueado;
    private boolean activado;

    public DatosSesion() {
    }

    public Long getCantidadIngresos() {
        return cantidadIngresos;
    }

    public void setCantidadIngresos(Long cantidadIngresos) {
        this.cantidadIngresos = cantidadIngresos;
    }

    public Long getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(Long ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public String getUltimoIp() {
        return ultimoIp;
    }

    public void setUltimoIp(String ultimoIp) {
        this.ultimoIp = ultimoIp;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }
}
