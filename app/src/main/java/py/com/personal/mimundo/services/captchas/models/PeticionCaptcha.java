package py.com.personal.mimundo.services.captchas.models;

import java.util.Date;

import retrofit.mime.TypedByteArray;

/**
 * Created by Konecta on 25/07/2014.
 */
public class PeticionCaptcha {

    private long id;
    private String nombreUsuario;
    private Long fechaCreacion;
    private long validez;
    private String imagen;

    public PeticionCaptcha(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Long getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Long fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public long getValidez() {
        return validez;
    }

    public void setValidez(long validez) {
        this.validez = validez;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
