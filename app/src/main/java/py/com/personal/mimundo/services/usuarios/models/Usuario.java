package py.com.personal.mimundo.services.usuarios.models;

import java.util.Date;

public class Usuario {

	private String usuario;
	private String tipoUsuario;
	private String lineaAsignada;
	private Long codigoCliente;
	private String nombres;
	private String email;
    private boolean emailValidado;
    private boolean datosValidados;
    private Date fechaCreacion;
	private String personeria;

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTipoUsuario() {
		return this.tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public Long getCodigoCliente() {
		return this.codigoCliente;
	}

	public void setCodigoCliente(Long codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getLineaAsignada() {
		return this.lineaAsignada;
	}

	public void setLineaAsignada(String lineaAsignada) {
		this.lineaAsignada = lineaAsignada;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public boolean isEmailValidado() {
        return emailValidado;
    }

    public void setEmailValidado(boolean emailValidado) {
        this.emailValidado = emailValidado;
    }

    public boolean isDatosValidados() {
        return datosValidados;
    }

    public void setDatosValidados(boolean datosValidados) {
        this.datosValidados = datosValidados;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

	public String getPersoneria() {
		return personeria;
	}

	public void setPersoneria(String personeria) {
		this.personeria = personeria;
	}
}