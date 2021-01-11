package py.com.personal.mimundo.services.backtones.models;

public class Album {
	private String id;
	private String nombre;
	private Integer artistaid;

	public Album() {
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

	// Artista id.
	public Integer getGeneroId() {
		return artistaid;
	}

	public void setGeneroId(Integer artistaid) {
		this.artistaid = artistaid;
	}
}