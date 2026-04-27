package com.practica.genericas;

public class FechaHora implements Comparable<FechaHora>{

	public static class Fecha implements Comparable<Fecha> {
		private int dia, mes, anio;

		public static  Fecha parseFecha(String data) throws IllegalArgumentException{
			String[] valores = data.split("\\/");
			if (valores.length != 3) {
				throw new IllegalArgumentException("Formato de fecha incorrecto");
			}

			int dia = Integer.parseInt(valores[0]);
			int mes = Integer.parseInt(valores[1]);
			int anio = Integer.parseInt(valores[2]);

			return new Fecha(dia, mes, anio);
		}

		public Fecha(int dia, int mes, int anio) {
			super();
			this.dia = dia;
			this.mes = mes;
			this.anio = anio;
		}

		public Fecha(Fecha other) {
			dia = other.dia;
			mes = other.mes;
			anio = other.anio;
		}

		public int getDia() {
			return dia;
		}

		public void setDia(int dia) {
			this.dia = dia;
		}

		public int getMes() {
			return mes;
		}

		public void setMes(int mes) {
			this.mes = mes;
		}

		public int getAnio() {
			return anio;
		}

		public void setAnio(int anio) {
			this.anio = anio;
		}

		@Override
		public String toString() {
			return String.format("%2d/%02d/%4d",dia,mes,anio);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Fecha other = (Fecha) obj;
			return (this.dia == other.dia) && (this.mes == other.mes) && (this.anio == other.anio);
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 97 * hash + this.dia;
			hash = 97 * hash + this.mes;
			hash = 97 * hash + this.anio;
			return hash;
		}

		@Override
		public int compareTo(Fecha arg0) {
			if (anio != arg0.anio) {
				return anio - arg0.anio;
			} else if (mes != arg0.mes) {
				return mes - arg0.mes;
			} else {
				return dia - arg0.dia;
			}
		}
	}

	public static class Hora implements Comparable<Hora> {
		private int hora, minuto;

		public static Hora parseHora(String data) throws IllegalArgumentException {
			String[] valores = data.split("\\:");
			if (valores.length != 2) {
				throw new IllegalArgumentException("Formato de hora incorrecto");
			}

			int hora = Integer.parseInt(valores[0]);
			int minuto = Integer.parseInt(valores[1]);

			return new Hora(hora, minuto);
		}

		public Hora(int hora, int minuto) {
			super();
			this.hora = hora;
			this.minuto = minuto;
		}

		public Hora(Hora other) {
			hora = other.hora;
			minuto = other.minuto;
		}

		@Override
		public String toString() {
			return String.format("%02d:%02d", hora,minuto);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Hora other = (Hora) obj;
			return (this.hora == other.hora) && (this.minuto == other.minuto);
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 97 * hash + this.hora;
			hash = 97 * hash + this.minuto;
			return hash;
		}

		@Override
		public int compareTo(Hora arg0) {
			if (hora != arg0.hora) {
				return hora - arg0.hora;
			} else {
				return minuto - arg0.minuto;
			}
		}
	}

	Fecha fecha;
	Hora hora;

	public static FechaHora parseFecha(String fecha) throws IllegalArgumentException{
		Fecha date = Fecha.parseFecha(fecha);
		Hora time = new Hora(0, 0);
		return new FechaHora(date, time);
	}

	public static FechaHora parseFecha(String fecha, String hora) throws IllegalArgumentException{
		Fecha date = Fecha.parseFecha(fecha);
		Hora time = Hora.parseHora(hora);
		return new FechaHora(date, time);
	}

	public FechaHora(FechaHora other) {
		fecha = new Fecha(other.fecha);
		hora = new Hora(other.hora);
	}

	public FechaHora(Fecha fecha, Hora hora) {
		super();
		this.fecha = fecha;
		this.hora = hora;
	}

	public FechaHora(int dia, int mes, int anio, int hora, int minuto) {
		this.fecha = new Fecha(dia, mes, anio);
		this.hora = new Hora(hora, minuto);
	}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FechaHora other = (FechaHora) obj;
		return fecha.equals(other.fecha) &&  hora.equals(other.hora);
	}

	@Override
	public int compareTo(FechaHora o) {
		if (fecha.compareTo(o.fecha) != 0) {
			return fecha.compareTo(o.fecha);
		} else {
			return hora.compareTo(o.hora);
		}
	}

	@Override
	public String toString() {
		return String.format("%s;%s",
				fecha.toString(),
				hora.toString());
	}

}