package com.practica.lista;

import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class ListaContactos {
	private NodoTemporal lista;
	private int size;

	// --- Interfaz interna para abstraer la lógica de cálculo ---
	private interface OperacionNodo {
		int aplicar(NodoPosicion nodo);
	}

	public void insertarNodoTemporal(PosicionPersona p) {
		NodoTemporal aux = lista, ant = null;
		boolean salir = false, encontrado = false;

		while (aux != null && !salir) {
			int comparacion = aux.getFecha().compareTo(p.getFechaPosicion());
			if (comparacion == 0) {
				encontrado = true;
				salir = true;
				insertarOActualizarCoordenada(aux, p.getCoordenada());
			} else if (comparacion < 0) {
				ant = aux;
				aux = aux.getSiguiente();
			} else {
				salir = true;
			}
		}

		if (!encontrado) {
			NodoTemporal nuevo = new NodoTemporal();
			nuevo.setFecha(p.getFechaPosicion());
			insertarOActualizarCoordenada(nuevo, p.getCoordenada());

			if (ant != null) {
				nuevo.setSiguiente(aux);
				ant.setSiguiente(nuevo);
			} else {
				nuevo.setSiguiente(lista);
				lista = nuevo;
			}
			this.size++;
		}
	}

	private void insertarOActualizarCoordenada(NodoTemporal nodoTemp, Coordenada coord) {
		NodoPosicion actual = nodoTemp.getListaCoordenadas();
		NodoPosicion anterior = null;
		boolean encontrado = false;

		while (actual != null && !encontrado) {
			if (actual.getCoordenada().equals(coord)) {
				encontrado = true;
				actual.setNumPersonas(actual.getNumPersonas() + 1);
			} else {
				anterior = actual;
				actual = actual.getSiguiente();
			}
		}

		if (!encontrado) {
			NodoPosicion nuevo = new NodoPosicion(coord, 1, null);
			if (nodoTemp.getListaCoordenadas() == null)
				nodoTemp.setListaCoordenadas(nuevo);
			else
				anterior.setSiguiente(nuevo);
		}
	}

	// --- Métodos refactorizados ---

	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		return procesarRangoInstantes(inicio, fin, NodoPosicion::getNumPersonas);
	}

	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		return procesarRangoInstantes(inicio, fin, nodo -> 1);
	}

	private int procesarRangoInstantes(FechaHora inicio, FechaHora fin, OperacionNodo op) {
		if (this.size == 0) return 0;
		int cont = 0;
		NodoTemporal aux = lista;
		while (aux != null) {
			if (aux.getFecha().compareTo(inicio) >= 0 && aux.getFecha().compareTo(fin) <= 0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while (nodo != null) {
					cont += op.aplicar(nodo);
					nodo = nodo.getSiguiente();
				}
			}
			aux = aux.getSiguiente();
		}
		return cont;
	}

	// --- Métodos auxiliares mantenidos ---

	public int personasEnCoordenadas() {
		NodoPosicion aux = this.lista != null ? this.lista.getListaCoordenadas() : null;
		int cont = 0;
		while (aux != null) {
			cont += aux.getNumPersonas();
			aux = aux.getSiguiente();
		}
		return cont;
	}

	public int tamanioLista() { return this.size; }

	public String getPrimerNodo() {
		return lista.getFecha().getFecha() + ";" + lista.getFecha().getHora();
	}

	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		NodoTemporal aux = lista;
		while (aux != null) {
			cadena.append(aux.getFecha().getFecha()).append(";").append(aux.getFecha().getHora());
			if (aux.getSiguiente() != null) cadena.append(" ");
			aux = aux.getSiguiente();
		}
		return cadena.toString();
	}
}