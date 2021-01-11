package py.com.personal.mimundo.services.backtones.models;

public class Artista {
	private String id;
	private String nombre;
	private Integer generoid;

	public Artista() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescripcion() {
		return nombre;
	}

	public void setDescripcion(String nombre) {
		this.nombre = nombre;
	}

	// Genero id.
	public Integer getGeneroId() {
		return generoid;
	}

	public void setGeneroId(Integer generoid) {
		this.generoid = generoid;
	}
}