package py.com.personal.mimundo.services.usuarios.models;

import py.com.personal.mimundo.services.personas.models.Persona;

/**
 * Created by Konecta on 12/11/2014.
 */
public class DatosUsuarioLogueado {

    private Usuario usuario;
    private Persona persona;
    private DatosSesion sesion;

    public DatosUsuarioLogueado(){
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public DatosSesion getSesion() {
        return sesion;
    }

    public void setSesion(DatosSesion sesion) {
        this.sesion = sesion;
    }
}
