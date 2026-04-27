package com.practica.ems.covid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsInvalidNumberOfDataException;
import com.practica.excecption.EmsInvalidTypeException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.Constantes;
import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.Persona;
import com.practica.genericas.PosicionPersona;
import com.practica.lista.ListaContactos;

public class ContactosCovid {
	private Poblacion poblacion;
	private Localizacion localizacion;
	private ListaContactos listaContactos;

	public ContactosCovid() {
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	public Poblacion getPoblacion() { return poblacion; }
	public void setPoblacion(Poblacion poblacion) { this.poblacion = poblacion; }
	public Localizacion getLocalizacion() { return localizacion; }
	public void setLocalizacion(Localizacion localizacion) { this.localizacion = localizacion; }
	public ListaContactos getListaContactos() { return listaContactos; }
	public void setListaContactos(ListaContactos listaContactos) { this.listaContactos = listaContactos; }

	public void loadData(String data, boolean reset) throws EmsInvalidTypeException, EmsInvalidNumberOfDataException,
			EmsDuplicatePersonException, EmsDuplicateLocationException {
		if (reset) {
			this.poblacion = new Poblacion();
			this.localizacion = new Localizacion();
			this.listaContactos = new ListaContactos();
		}
		String datas[] = dividirEntrada(data);
		for (String linea : datas) {
			procesarLinea(linea);
		}
	}

	// --- NUEVO MÉTODO PARA ELIMINAR DUPLICIDAD ---
	private void procesarLinea(String linea) throws EmsInvalidTypeException, EmsInvalidNumberOfDataException,
			EmsDuplicatePersonException, EmsDuplicateLocationException {
		String datos[] = this.dividirLineaData(linea);
		if (!datos[0].equals("PERSONA") && !datos[0].equals("LOCALIZACION")) {
			throw new EmsInvalidTypeException();
		}
		if (datos[0].equals("PERSONA")) {
			if (datos.length != Constantes.MAX_DATOS_PERSONA) {
				throw new EmsInvalidNumberOfDataException("El número de datos para PERSONA es menor de 8");
			}
			this.poblacion.addPersona(this.crearPersona(datos));
		} else if (datos[0].equals("LOCALIZACION")) {
			if (datos.length != Constantes.MAX_DATOS_LOCALIZACION) {
				throw new EmsInvalidNumberOfDataException("El número de datos para LOCALIZACION es menor de 6");
			}
			PosicionPersona pp = this.crearPosicionPersona(datos);
			this.localizacion.addLocalizacion(pp);
			this.listaContactos.insertarNodoTemporal(pp);
		}
	}

	public void loadDataFile(String fichero, boolean reset) {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		String datas[] = null, data = null;
		loadDataFile(fichero, reset, archivo, fr, br, datas, data);
	}

	@SuppressWarnings("resource")
	public void loadDataFile(String fichero, boolean reset, File archivo, FileReader fr, BufferedReader br, String datas[], String data ) {
		try {
			archivo = new File(fichero);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			if (reset) {
				this.poblacion = new Poblacion();
				this.localizacion = new Localizacion();
				this.listaContactos = new ListaContactos();
			}
			while ((data = br.readLine()) != null) {
				datas = dividirEntrada(data.trim());
				for (String linea : datas) {
					procesarLinea(linea);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public int findPersona(String documento) throws EmsPersonNotFoundException {
		return this.poblacion.findPersona(documento);
	}

	public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
		return localizacion.findLocalizacion(documento, fecha, hora);
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {
		int cont = 0;
		List<PosicionPersona> lista = new ArrayList<PosicionPersona>();
		Iterator<PosicionPersona> it = this.localizacion.getLista().iterator();
		while (it.hasNext()) {
			PosicionPersona pp = it.next();
			if (pp.getDocumento().equals(documento)) {
				cont++;
				lista.add(pp);
			}
		}
		if (cont == 0) throw new EmsPersonNotFoundException();
		else return lista;
	}

	public boolean delPersona(String documento) throws EmsPersonNotFoundException {
		int cont = 0, pos = -1;
		Iterator<Persona> it = this.poblacion.getLista().iterator();
		while (it.hasNext()) {
			Persona persona = it.next();
			if (persona.getDocumento().equals(documento)) pos = cont;
			cont++;
		}
		if (pos == -1) throw new EmsPersonNotFoundException();
		this.poblacion.getLista().remove(pos);
		return false;
	}

	private String[] dividirEntrada(String input) { return input.split("\\n"); }
	private String[] dividirLineaData(String data) { return data.split("\\;"); }

	private Persona crearPersona(String[] data) {
		Persona persona = new Persona();
		for (int i = 1; i < Constantes.MAX_DATOS_PERSONA; i++) {
			String s = data[i];
			switch (i) {
				case 1: persona.setDocumento(s); break;
				case 2: persona.setNombre(s); break;
				case 3: persona.setApellidos(s); break;
				case 4: persona.setEmail(s); break;
				case 5: persona.setDireccion(s); break;
				case 6: persona.setCp(s); break;
				case 7: persona.setFechaNacimiento(parsearFecha(s)); break;
			}
		}
		return persona;
	}

	private PosicionPersona crearPosicionPersona(String[] data) {
		PosicionPersona posicionPersona = new PosicionPersona();
		String fecha = null, hora;
		float latitud = 0, longitud;
		for (int i = 1; i < Constantes.MAX_DATOS_LOCALIZACION; i++) {
			String s = data[i];
			switch (i) {
				case 1: posicionPersona.setDocumento(s); break;
				case 2: fecha = data[i]; break;
				case 3:
					hora = data[i];
					posicionPersona.setFechaPosicion(parsearFecha(fecha, hora));
					break;
				case 4: latitud = Float.parseFloat(s); break;
				case 5:
					longitud = Float.parseFloat(s);
					posicionPersona.setCoordenada(new Coordenada(latitud, longitud));
					break;
			}
		}
		return posicionPersona;
	}

	private FechaHora parsearFecha (String fecha) {
		String[] valores = fecha.split("\\/");
		return new FechaHora(Integer.parseInt(valores[0]), Integer.parseInt(valores[1]), Integer.parseInt(valores[2]), 0, 0);
	}

	private FechaHora parsearFecha (String fecha, String hora) {
		String[] valoresFecha = fecha.split("\\/");
		String[] valoresHora = hora.split("\\:");
		return new FechaHora(Integer.parseInt(valoresFecha[0]), Integer.parseInt(valoresFecha[1]), Integer.parseInt(valoresFecha[2]),
				Integer.parseInt(valoresHora[0]), Integer.parseInt(valoresHora[1]));
	}
}