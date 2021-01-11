package py.com.personal.mimundo.models;

import java.math.BigDecimal;

public class DetallesConsumo {
	
	private String numeroLineaOrigen;
	private String numeroLineaDestino;
	private String fecha;
	private String hora;
	private Double cantidad;
	private String unidad;
	private BigDecimal costo;
	private String tipo;
	private String descripcion;

	public DetallesConsumo() {
	}	

	public String getNumeroLineaOrigen() {
		return numeroLineaOrigen;
	}

	public void setNumeroLineaOrigen(String numeroLineaOrigen) {
		this.numeroLineaOrigen = numeroLineaOrigen;
	}

	public String getNumeroLineaDestino() {
		return numeroLineaDestino;
	}

	public void setNumeroLineaDestino(String numeroLineaDestino) {
		this.numeroLineaDestino = numeroLineaDestino;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
