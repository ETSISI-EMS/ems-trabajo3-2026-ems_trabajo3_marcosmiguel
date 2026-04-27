package com.practica.lista;

import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class ListaContactos {
	private NodoTemporal lista;
	private int size;

	/**
	 * Insertamos en la lista de nodos temporales, y a la vez inserto en la lista de nodos de coordenadas.
	 * En la lista de coordenadas metemos el documento de la persona que está en esa coordenada
	 * en un instante
	 */
	public void insertarNodoTemporal (PosicionPersona p) {
		NodoTemporal aux = lista, ant=null;
		boolean salir=false, encontrado = false;

		while (aux!=null && !salir) {
			if(aux.getFecha().compareTo(p.getFechaPosicion())==0) {
				encontrado = true;
				salir = true;
				actualizarOInsertarNodoPosicion(aux, p);
			} else if(aux.getFecha().compareTo(p.getFechaPosicion())<0) {
				ant = aux;
				aux=aux.getSiguiente();
			} else if(aux.getFecha().compareTo(p.getFechaPosicion())>0) {
				salir=true;
			}
		}

		if(!encontrado) {
			NodoTemporal nuevo = new NodoTemporal();
			nuevo.setFecha(p.getFechaPosicion());

			actualizarOInsertarNodoPosicion(nuevo, p);

			if(ant!=null) {
				nuevo.setSiguiente(aux);
				ant.setSiguiente(nuevo);
			} else {
				nuevo.setSiguiente(lista);
				lista = nuevo;
			}
			this.size++;
		}
	}

	// Método extraído para eliminar la duplicidad reportada por SonarQube
	private void actualizarOInsertarNodoPosicion(NodoTemporal nodo, PosicionPersona p) {
		NodoPosicion npActual = nodo.getListaCoordenadas();
		NodoPosicion npAnt = null;
		boolean npEncontrado = false;

		while (npActual != null && !npEncontrado) {
			if(npActual.getCoordenada().equals(p.getCoordenada())) {
				npEncontrado = true;
				npActual.setNumPersonas(npActual.getNumPersonas() + 1);
			} else {
				npAnt = npActual;
				npActual = npActual.getSiguiente();
			}
		}

		if(!npEncontrado) {
			NodoPosicion npNuevo = new NodoPosicion(p.getCoordenada(), 1, null);
			if(nodo.getListaCoordenadas() == null)
				nodo.setListaCoordenadas(npNuevo);
			else
				npAnt.setSiguiente(npNuevo);
		}
	}

	private boolean buscarPersona (String documento, NodoPersonas nodo) {
		NodoPersonas aux = nodo;
		while(aux!=null) {
			if(aux.getDocumento().equals(documento)) return true;
			else aux = aux.getSiguiente();
		}
		return false;
	}

	private void insertarPersona (String documento, NodoPersonas nodo) {
		NodoPersonas aux = nodo, nuevo = new NodoPersonas(documento, null);
		while(aux.getSiguiente()!=null) aux = aux.getSiguiente();
		aux.setSiguiente(nuevo);
	}

	public int personasEnCoordenadas () {
		NodoPosicion aux = this.lista.getListaCoordenadas();
		if(aux==null) return 0;
		else {
			int cont = 0;
			for(; aux != null; aux = aux.getSiguiente()) cont += aux.getNumPersonas();
			return cont;
		}
	}

	public int tamanioLista () {
		return this.size;
	}

	public String getPrimerNodo() {
		NodoTemporal aux = lista;
		return aux.getFecha().getFecha().toString() + ";" + aux.getFecha().getHora().toString();
	}

	private int contarEnRango(FechaHora inicio, FechaHora fin, boolean contarPersonas) {
		if (this.size == 0) return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		while (aux != null) {
			if (aux.getFecha().compareTo(inicio) >= 0 && aux.getFecha().compareTo(fin) <= 0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while (nodo != null) {
					cont += contarPersonas ? nodo.getNumPersonas() : 1;
					nodo = nodo.getSiguiente();
				}
			}
			aux = aux.getSiguiente();
		}
		return cont;
	}

	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		return contarEnRango(inicio, fin, true);
	}

	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		return contarEnRango(inicio, fin, false);
	}

	@Override
	public String toString() {
		String cadena="";
		int cont;
		NodoTemporal aux = lista;
		for(cont=1; cont<size; cont++) {
			cadena += aux.getFecha().getFecha().toString() + ";" + aux.getFecha().getHora().toString() + " ";
			aux=aux.getSiguiente();
		}
		cadena += aux.getFecha().getFecha().toString() + ";" + aux.getFecha().getHora().toString();
		return cadena;
	}
}