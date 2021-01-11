package py.com.personal.mimundo.services.backtones.models;

public class TonoConfiguracion {
	private Boolean esParaTodasLasLlamadas;
	private Boolean esAleatorio;
	private Boolean esAutoRenovable;

	public TonoConfiguracion() {
	}

	public Boolean getEsParaTodasLasLlamadas() {
		return esParaTodasLasLlamadas;
	}

	public void setEsParaTodasLasLlamadas(Boolean esParaTodasLasLlamadas) {
		this.esParaTodasLasLlamadas = esParaTodasLasLlamadas;
	}

	public Boolean getEsAleatorio() {
		return esAleatorio;
	}

	public void setEsAleatorio(Boolean esAleatorio) {
		this.esAleatorio = esAleatorio;
	}

	public Boolean getEsAutoRenovable() {
		return esAutoRenovable;
	}

	public void setEsAutoRenovable(Boolean esAutoRenovable) {
		this.esAutoRenovable = esAutoRenovable;
	}

}
