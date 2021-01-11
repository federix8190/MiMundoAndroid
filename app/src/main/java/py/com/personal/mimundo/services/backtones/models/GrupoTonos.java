package py.com.personal.mimundo.services.backtones.models;


import java.util.Set;

public class GrupoTonos {

	private Integer idGrupo;
	private String descripcion;
	private Boolean activo;
	private String tipo;
	private Integer idCategoria;
	private Set<GrupoTonosDetalle> tonos;

	public GrupoTonos() {
	}

	public GrupoTonos(Integer idGrupo, String descripcion, Boolean activo, String tipo, Integer idCategoria) {
		this.idGrupo = idGrupo;
		this.descripcion = descripcion;
		this.activo = activo;
		this.tipo = tipo;
		this.idCategoria = idCategoria;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

}
