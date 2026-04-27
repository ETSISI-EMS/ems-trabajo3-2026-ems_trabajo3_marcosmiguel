package com.practica.lista;

import java.util.LinkedList;

import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;


/**
 * Nodo para guardar un instante de tiempo. Además guardamos una lista con las coordeandas
 * y las personas (solo número) que en ese instante están en una coordeanda en concreto
 *
 */
public class NodoTemporal implements Comparable<NodoTemporal> {

	private final LinkedList<NodoPosicion> listaCoordenadas;
	private FechaHora fecha;

	public static NodoTemporal fromPosicionPersona(PosicionPersona pp) {
		NodoTemporal nt =  new NodoTemporal();
		nt.fecha = pp.getFechaPosicion();

		NodoPosicion np = NodoPosicion.fromPosicionPersona(pp);
		nt.listaCoordenadas.add(np);

		return nt;
	}

	public NodoTemporal() {
		super();
		listaCoordenadas = new LinkedList<>();
		fecha = null;
	}

	public FechaHora getFecha() {
		return fecha;
	}

	public void combineNodes(NodoTemporal other) {
		if (!this.fecha.equals(other.fecha)) {
			return;
		}

		for (NodoPosicion it: other.listaCoordenadas) {
			int pos = listaCoordenadas.indexOf(it);

			if (pos == -1) {
				listaCoordenadas.add(new NodoPosicion(it));
			} else {
				listaCoordenadas.get(pos).combine(it);
			}
		}
	}

	public int countPersonas() {
		return listaCoordenadas.stream().map(NodoPosicion::getNumPersonas).reduce(0, Integer::sum);
	}

	public int countCoordenadas() {
		return listaCoordenadas.size();
	}

	public boolean betweenTimes(FechaHora inicio, FechaHora fin) {
		return fecha.compareTo(inicio) >= 0 && fecha.compareTo(fin) <= 0;
	}

	@Override
	public int compareTo(NodoTemporal arg0) {
		return this.fecha.compareTo(arg0.fecha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		NodoTemporal other = (NodoTemporal) obj;
		return this.fecha.equals(other.fecha);
	}

	@Override
	public String toString() {
		return fecha.toString();
	}
}