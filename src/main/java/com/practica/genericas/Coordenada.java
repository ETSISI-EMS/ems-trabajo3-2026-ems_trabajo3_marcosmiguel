package com.practica.genericas;


public class Coordenada {
	private float latitud, longitud;


	public Coordenada() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Coordenada(Coordenada co) {
		this.latitud = co.latitud;
		this.longitud = co.longitud;
	}

	public Coordenada(float latitud, float longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(latitud);
		result = prime * result + Float.floatToIntBits(longitud);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Coordenada other = (Coordenada) obj;
		return latitud == other.latitud && longitud == other.longitud;
	}

	@Override
	public String toString() {
		return String.format("%.4f;%.4f\n", latitud, longitud);
	}

}