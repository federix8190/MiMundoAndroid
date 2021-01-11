package py.com.personal.mimundo.services.usuarios.models;

import java.util.List;

/**
 * Created by Konecta on 12/11/2014.
 */
public class ListaRol {

    private List<Rol> roles;

    public ListaRol() {
    }

    public ListaRol(List<Rol> roles) {
        this.roles = roles;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
