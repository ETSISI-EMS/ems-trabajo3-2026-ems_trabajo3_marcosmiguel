package com.practica.ems.covid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.practica.excecption.*;
import com.practica.genericas.*;
import com.practica.lista.ListaContactos;

public class ContactosCovid {
	private Poblacion poblacion;
	private Localizacion localizacion;
	private ListaContactos listaContactos;

	public ContactosCovid() {
		resetDatos();
	}

	private void resetDatos() {
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	// --- Métodos de carga unificados ---

	public void loadData(String data, boolean reset) throws EmsInvalidTypeException,
			EmsInvalidNumberOfDataException, EmsDuplicatePersonException, EmsDuplicateLocationException {
		if (reset) resetDatos();

		for (String linea : dividirEntrada(data)) {
			procesarLineaContenida(linea);
		}
	}

	public void loadDataFile(String fichero, boolean reset) {
		if (reset) resetDatos();

		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				procesarLineaContenida(linea);
			}
		} catch (IOException | EmsInvalidTypeException | EmsInvalidNumberOfDataException |
				 EmsDuplicatePersonException | EmsDuplicateLocationException e) {
			e.printStackTrace();
		}
	}

	private void procesarLineaContenida(String linea) throws EmsInvalidTypeException,
			EmsInvalidNumberOfDataException, EmsDuplicatePersonException, EmsDuplicateLocationException {
		String lineaTrim = linea.trim();
		if (lineaTrim.isEmpty()) return;

		for (String subLinea : dividirEntrada(lineaTrim)) {
			procesarLinea(dividirLineaData(subLinea));
		}
	}

	private void procesarLinea(String[] datos) throws EmsInvalidTypeException,
			EmsInvalidNumberOfDataException, EmsDuplicatePersonException, EmsDuplicateLocationException {

		if (!datos[0].equals("PERSONA") && !datos[0].equals("LOCALIZACION")) {
			throw new EmsInvalidTypeException();
		}

		if (datos[0].equals("PERSONA")) {
			validarNumeroDatos(datos, Constantes.MAX_DATOS_PERSONA);
			this.poblacion.addPersona(this.crearPersona(datos));
		} else {
			validarNumeroDatos(datos, Constantes.MAX_DATOS_LOCALIZACION);
			PosicionPersona pp = this.crearPosicionPersona(datos);
			this.localizacion.addLocalizacion(pp);
			this.listaContactos.insertarNodoTemporal(pp);
		}
	}

	private void validarNumeroDatos(String[] datos, int maximo) throws EmsInvalidNumberOfDataException {
		if (datos.length != maximo) {
			throw new EmsInvalidNumberOfDataException("El número de datos es incorrecto");
		}
	}

	// --- Métodos de búsqueda y gestión ---

	public int findPersona(String documento) throws EmsPersonNotFoundException {
		return this.poblacion.findPersona(documento);
	}

	public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
		return localizacion.findLocalizacion(documento, fecha, hora);
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {
		List<PosicionPersona> lista = new ArrayList<>();
		for (PosicionPersona pp : this.localizacion.getLista()) {
			if (pp.getDocumento().equals(documento)) {
				lista.add(pp);
			}
		}
		if (lista.isEmpty()) throw new EmsPersonNotFoundException();
		return lista;
	}

	public boolean delPersona(String documento) throws EmsPersonNotFoundException {
		List<Persona> lista = this.poblacion.getLista();
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getDocumento().equals(documento)) {
				lista.remove(i);
				return true;
			}
		}
		throw new EmsPersonNotFoundException();
	}

	public Poblacion getPoblacion() { return poblacion; }
	public Localizacion getLocalizacion() { return localizacion; }
	public ListaContactos getListaContactos() { return listaContactos; }

	// --- Métodos auxiliares y de parseo ---

	private String[] dividirEntrada(String input) { return input.split("\\n"); }
	private String[] dividirLineaData(String data) { return data.split("\\;"); }

	private Persona crearPersona(String[] data) {
		Persona persona = new Persona();
		persona.setDocumento(data[1]);
		persona.setNombre(data[2]);
		persona.setApellidos(data[3]);
		persona.setEmail(data[4]);
		persona.setDireccion(data[5]);
		persona.setCp(data[6]);
		persona.setFechaNacimiento(parsearFecha(data[7]));
		return persona;
	}

	private PosicionPersona crearPosicionPersona(String[] data) {
		PosicionPersona pp = new PosicionPersona();
		pp.setDocumento(data[1]);
		pp.setFechaPosicion(parsearFecha(data[2], data[3]));
		pp.setCoordenada(new Coordenada(Float.parseFloat(data[4]), Float.parseFloat(data[5])));
		return pp;
	}

	private FechaHora parsearFecha(String fecha) {
		String[] v = fecha.split("\\/");
		return new FechaHora(Integer.parseInt(v[0]), Integer.parseInt(v[1]), Integer.parseInt(v[2]), 0, 0);
	}

	private FechaHora parsearFecha(String fecha, String hora) {
		String[] vF = fecha.split("\\/");
		String[] vH = hora.split("\\:");
		return new FechaHora(Integer.parseInt(vF[0]), Integer.parseInt(vF[1]), Integer.parseInt(vF[2]),
				Integer.parseInt(vH[0]), Integer.parseInt(vH[1]));
	}
}