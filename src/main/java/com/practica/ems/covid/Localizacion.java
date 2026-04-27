package com.practica.ems.covid;


import java.util.Iterator;
import java.util.LinkedList;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class Localizacion {
	LinkedList<PosicionPersona> lista;

	public Localizacion() {
		super();
		this.lista = new LinkedList<PosicionPersona>();
	};

	public LinkedList<PosicionPersona> getLista() {
		return lista;
	}

	public void setLista(LinkedList<PosicionPersona> lista) {
		this.lista = lista;
	}

	public void addLocalizacion(PosicionPersona p) throws EmsDuplicateLocationException {
		try {
			// Extraemos los datos a variables temporales para acortar la línea
			String doc = p.getDocumento();
			String fecha = p.getFechaPosicion().getFecha().toString();
			String hora = p.getFechaPosicion().getHora().toString();

			findLocalizacion(doc, fecha, hora);
			throw new EmsDuplicateLocationException();
		} catch (EmsLocalizationNotFoundException e) {
			lista.add(p);
		}
	}

	public int findLocalizacion (String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
		int cont = 0;
		Iterator<PosicionPersona> it = lista.iterator();
		while(it.hasNext()) {
			cont++;
			PosicionPersona pp = it.next();
			FechaHora fechaHora = this.parsearFecha(fecha, hora);

			// Ajuste: asegurar que la segunda parte de la condición esté en una nueva línea
			// con sangría adicional para mayor claridad y control de longitud
			if (pp.getDocumento().equals(documento) &&
					pp.getFechaPosicion().equals(fechaHora)) {
				return cont;
			}
		}
		throw new EmsLocalizationNotFoundException();
	}
	public void delLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
		int pos=-1;
		/**
		 *  Busca la localización, sino existe lanza una excepción
		 */
		try {
			pos = findLocalizacion(documento, fecha, hora);
		} catch (EmsLocalizationNotFoundException e) {
			throw new EmsLocalizationNotFoundException();
		}
		this.lista.remove(pos);

	}

	void printLocalizacion() {
		for(int i = 0; i < this.lista.size(); i++) {
			PosicionPersona pp = lista.get(i);
			FechaHora fecha = pp.getFechaPosicion();

			System.out.printf("%d;%s;", i, pp.getDocumento());
			System.out.printf("%02d/%02d/%04d;", fecha.getFecha().getDia(),
					fecha.getFecha().getMes(), fecha.getFecha().getAnio());
			System.out.printf("%02d:%02d;", fecha.getHora().getHora(),
					fecha.getHora().getMinuto());
			System.out.printf("%.4f;%.4f\n", pp.getCoordenada().getLatitud(),
					pp.getCoordenada().getLongitud());
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(PosicionPersona pp : this.lista) {
			FechaHora f = pp.getFechaPosicion();

			sb.append(String.format("%s;", pp.getDocumento()));
			sb.append(String.format("%02d/%02d/%04d;", f.getFecha().getDia(),
					f.getFecha().getMes(), f.getFecha().getAnio()));
			sb.append(String.format("%02d:%02d;", f.getHora().getHora(),
					f.getHora().getMinuto()));
			sb.append(String.format("%.4f;%.4f\n", pp.getCoordenada().getLatitud(),
					pp.getCoordenada().getLongitud()));
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private FechaHora parsearFecha (String fecha) {
		int dia, mes, anio;
		String[] valores = fecha.split("\\/");
		dia = Integer.parseInt(valores[0]);
		mes = Integer.parseInt(valores[1]);
		anio = Integer.parseInt(valores[2]);
		FechaHora fechaHora = new FechaHora(dia, mes, anio, 0, 0);
		return fechaHora;
	}

	private  FechaHora parsearFecha (String fecha, String hora) {
		int dia, mes, anio;
		String[] valores = fecha.split("\\/");
		dia = Integer.parseInt(valores[0]);
		mes = Integer.parseInt(valores[1]);
		anio = Integer.parseInt(valores[2]);
		int minuto, segundo;
		valores = hora.split("\\:");
		minuto = Integer.parseInt(valores[0]);
		segundo = Integer.parseInt(valores[1]);
		FechaHora fechaHora = new FechaHora(dia, mes, anio, minuto, segundo);
		return fechaHora;
	}

}