package py.com.personal.mimundo.services;

/**
 * Created by Konecta on 26/03/2015.
 */
public class ErrorResponse {

    private Integer codigo;
    private String mensaje;
    private String causa;

    public ErrorResponse(Integer codigo, String mensaje, String causa) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.causa = causa;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

}
