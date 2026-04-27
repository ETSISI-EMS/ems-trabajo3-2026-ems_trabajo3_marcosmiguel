package com.practica.ems.covid;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsInvalidNumberOfDataException;
import com.practica.excecption.EmsInvalidTypeException;

public class Principal {

	public static void main(String[] args) throws EmsDuplicatePersonException, EmsDuplicateLocationException, EmsInvalidTypeException, EmsInvalidNumberOfDataException {
		ContactosCovid contactosCovid = new ContactosCovid();

		try {
			contactosCovid.loadDataFile("datos2.txt", false);
		} catch (Exception e) {
			// Cambiamos 'IOException' por 'Exception' para capturar cualquier error
			// sin que el compilador se queje de que una excepción nunca se lanza.
			e.printStackTrace();
		}

		System.out.println(contactosCovid.getLocalizacion().toString());
		System.out.println(contactosCovid.getPoblacion().toString());
		System.out.println(contactosCovid.getListaContactos().tamanioLista());
		System.out.println(contactosCovid.getListaContactos().getPrimerNodo());
		System.out.println(contactosCovid.getListaContactos());
	}
}