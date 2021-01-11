package py.com.personal.mimundo.services.backtones.models;

public class TonoDetalle {

	private Integer idTonoDetalle;
	private Integer idTonoPlataforma;
	private byte[] imagen;
	private String nombreTonoPlataforma;

	public TonoDetalle() {
	}

	public Integer getIdTonoDetalle() {
		return idTonoDetalle;
	}

	public void setIdTonoDetalle(Integer idTonoDetalle) {
		this.idTonoDetalle = idTonoDetalle;
	}

	public Integer getIdTonoPlataforma() {
		return idTonoPlataforma;
	}

	public void setIdTonoPlataforma(Integer idTonoPlataforma) {
		this.idTonoPlataforma = idTonoPlataforma;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNombreTonoPlataforma() {
		return nombreTonoPlataforma;
	}

	public void setNombreTonoPlataforma(String nombreTonoPlataforma) {
		this.nombreTonoPlataforma = nombreTonoPlataforma;
	}

}
