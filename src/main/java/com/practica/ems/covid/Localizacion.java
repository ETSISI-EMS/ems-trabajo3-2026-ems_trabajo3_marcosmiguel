package com.practica.ems.covid;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class Localizacion {

	private List<PosicionPersona> lista;

	public Localizacion() {
		super();
		this.lista = new LinkedList<PosicionPersona>();
	}

	public void addLocalizacion (PosicionPersona p) throws EmsDuplicateLocationException {
		if (lista.contains(p)) {
			throw new EmsDuplicateLocationException();
		}
		lista.add(p);
	}

	public int findLocalizacion (String documento, FechaHora fh) throws EmsLocalizationNotFoundException {
		PosicionPersona pp = new PosicionPersona(null, documento, fh);
		int pos = lista.indexOf(pp);

		if (pos == -1) {
			throw new EmsLocalizationNotFoundException();
		}

		return pos + 1;
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {
		List<PosicionPersona> result = lista.stream().filter(pp -> pp.isThisPerson(documento)).collect(Collectors.toList());

		if (result.isEmpty()) {
			throw new EmsPersonNotFoundException();
		}

		return result;
	}

	public void delLocalizacion(String documento, FechaHora fh) throws EmsLocalizationNotFoundException {
		PosicionPersona pp = new PosicionPersona(null, documento, fh);
		if (!lista.remove(pp)) {
			throw new EmsLocalizationNotFoundException();
		}
	}

	void printLocalizacion() {
		System.out.printf(toString());
	}

	@Override
	public String toString() {
		String cadena = "";
		for (PosicionPersona pp: lista) {
			cadena += pp.toString();
			cadena += "\n";
		}
		//remove last \n
		cadena = cadena.substring(0, cadena.length()-1);
		return cadena;
	}

}