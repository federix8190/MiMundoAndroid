package py.com.personal.mimundo.services.backtones.models;

public class GrupoTonosDetalle {

	private Integer idDetalle;
	private int idTono;
	private Integer orden;
	private GrupoTonos idGrupo;

	public GrupoTonosDetalle() {
	}

	public Integer getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
	}

	public int getIdTono() {
		return idTono;
	}

	public void setIdTono(int idTono) {
		this.idTono = idTono;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

}
