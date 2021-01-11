package py.com.personal.mimundo.services.backtones.models;

public class Genero {
	private Integer id;
	private String descripcion;
	private Integer categoriaid;

	public Genero() {
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

	// Categoria id.
	public Integer getCategoriaId() {
		return categoriaid;
	}

	public void setCategoriaId(Integer categoriaid) {
		this.categoriaid = categoriaid;
	}
}
