package com.practica.genericas;

import java.time.LocalDateTime;

public class FechaHora implements Comparable<FechaHora> {

	// Se añade 'static' aquí
	public static class Fecha {
		private int dia, mes, anio;

		public Fecha(int dia, int mes, int anio) {
			this.dia = dia;
			this.mes = mes;
			this.anio = anio;
		}

		public int getDia() { return dia; }
		public void setDia(int dia) { this.dia = dia; }
		public int getMes() { return mes; }
		public void setMes(int mes) { this.mes = mes; }
		public int getAnio() { return anio; }
		public void setAnio(int anio) { this.anio = anio; }

		@Override
		public String toString() {
			return String.format("%2d/%02d/%4d", dia, mes, anio);
		}
	}

	// Se añade 'static' aquí
	public static class Hora {
		private int hora, minuto;

		public Hora(int hora, int minuto) {
			this.hora = hora;
			this.minuto = minuto;
		}

		public int getHora() { return hora; }
		public void setHora(int hora) { this.hora = hora; }
		public int getMinuto() { return minuto; }
		public void setMinuto(int minuto) { this.minuto = minuto; }

		@Override
		public String toString() {
			return String.format("%02d:%02d", hora, minuto);
		}
	}

	private Fecha fecha;
	private Hora hora;

	public FechaHora(Fecha fecha, Hora hora) {
		this.fecha = fecha;
		this.hora = hora;
	}

	public FechaHora(int dia, int mes, int anio, int hora, int minuto) {
		this.fecha = new Fecha(dia, mes, anio);
		this.hora = new Hora(hora, minuto);
	}

	public Fecha getFecha() { return fecha; }
	public void setFecha(Fecha fecha) { this.fecha = fecha; }
	public Hora getHora() { return hora; }
	public void setHora(Hora hora) { this.hora = hora; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		FechaHora other = (FechaHora) obj;
		return getFecha().getDia() == other.getFecha().getDia() &&
				getFecha().getMes() == other.getFecha().getMes() &&
				getFecha().getAnio() == other.getFecha().getAnio() &&
				getHora().getHora() == other.getHora().getHora() &&
				getHora().getMinuto() == other.getHora().getMinuto();
	}

	@Override
	public int compareTo(FechaHora o) {
		LocalDateTime dt1 = LocalDateTime.of(this.getFecha().getAnio(), this.getFecha().getMes(),
				this.getFecha().getDia(), this.getHora().getHora(), this.getHora().getMinuto());
		LocalDateTime dt2 = LocalDateTime.of(o.getFecha().getAnio(), o.getFecha().getMes(),
				o.getFecha().getDia(), o.getHora().getHora(), o.getHora().getMinuto());
		return dt1.compareTo(dt2);
	}
}