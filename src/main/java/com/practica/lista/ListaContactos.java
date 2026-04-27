package com.practica.lista;

import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;
import java.util.LinkedList;

public class ListaContactos {

	private final LinkedList<NodoTemporal> lista;

	public ListaContactos() {
		lista = new LinkedList<>();
	}

	public void insertarNodoTemporal (PosicionPersona p) {
		NodoTemporal nt = NodoTemporal.fromPosicionPersona(p);

		if (lista.isEmpty()) {
			lista.addFirst(nt);
			return;
		}

		int index = lista.indexOf(nt);
		if (index == -1) {
			insertInPosition(nt);
		} else {
			lista.get(index).combineNodes(nt);
		}
	}

	public int tamanioLista () {
		return this.lista.size();
	}

	public String getPrimerNodo() {
		return lista.getFirst().toString();
	}

	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		return lista.stream().filter(nt -> nt.betweenTimes(inicio, fin)).map(NodoTemporal::countPersonas).reduce(0, Integer::sum);
	}
	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		return lista.stream().filter(nt -> nt.betweenTimes(inicio, fin)).map(NodoTemporal::countCoordenadas).reduce(0, Integer::sum);
	}

	@Override
	public String toString() {
		String cadena="";
		for (NodoTemporal aux: lista) {
			cadena += aux.toString() + " ";
		}
		return cadena.trim();
	}

	private void insertInPosition(NodoTemporal nt) throws IllegalArgumentException {
		if (lista.contains(nt)) {
			throw new IllegalArgumentException("Date is already in the list");
		}

		int pos = 0;
		for (NodoTemporal actual : lista) {
			if (nt.compareTo(actual) < 0) {
				break;
			}
			pos++;
		}

		if (pos == lista.size()) {
			lista.addLast(nt);
		} else {
			lista.add(pos, nt);
		}
	}

}