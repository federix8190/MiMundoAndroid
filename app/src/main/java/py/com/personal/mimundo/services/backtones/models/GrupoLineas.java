package py.com.personal.mimundo.services.backtones.models;

public class GrupoLineas {

	private Integer id;
	private String descripcion;

	public GrupoLineas() {
	}

	public GrupoLineas(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}