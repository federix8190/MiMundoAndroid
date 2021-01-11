package py.com.personal.mimundo.services.PuntosCercanos.models;

import java.util.List;

public class PuntoCercano {
	
	private Integer idPdv;
	private String razonSocial;
	private String titularPdv;
	private String aplicacion;
	private String codigoDepartamento;
	private String callePrincipal;
	private String callePrincipalNumero;
	private String calleSecundaria1;
	private String calleSecundaria2;
	private String referencias;
	private Double latitud;
	private Double longitud;
	private List<String> tipos;	
	
	public PuntoCercano() {
	}	
	
	public Integer getIdPdv() {
		return idPdv;
	}

	public void setIdPdv(Integer idPdv) {
		this.idPdv = idPdv;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTitularPdv() {
		return titularPdv;
	}

	public void setTitularPdv(String titularPdv) {
		this.titularPdv = titularPdv;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getCallePrincipal() {
		return callePrincipal;
	}

	public void setCallePrincipal(String callePrincipal) {
		this.callePrincipal = callePrincipal;
	}

	public String getCallePrincipalNumero() {
		return callePrincipalNumero;
	}

	public void setCallePrincipalNumero(String callePrincipalNumero) {
		this.callePrincipalNumero = callePrincipalNumero;
	}

	public String getCalleSecundaria1() {
		return calleSecundaria1;
	}

	public void setCalleSecundaria1(String calleSecundaria1) {
		this.calleSecundaria1 = calleSecundaria1;
	}

	public String getCalleSecundaria2() {
		return calleSecundaria2;
	}

	public void setCalleSecundaria2(String calleSecundaria2) {
		this.calleSecundaria2 = calleSecundaria2;
	}

	public String getReferencias() {
		return referencias;
	}

	public void setReferencias(String referencias) {
		this.referencias = referencias;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public List<String> getTipos() {
		return tipos;
	}

	public void setTipos(List<String> tipos) {
		this.tipos = tipos;
	}		

}
