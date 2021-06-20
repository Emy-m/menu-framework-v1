package martin.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

/* Este framework muestra un menu con las acciones que se le indican.
 * Para usarlo, usted tiene que extender de Accion e indicarlas en un 
 * archivo de propiedades asi "acciones: paquete.clase" donde cada clase 
 * estara separada por ";". Luego, cree una instancia de Menu pasando 
 * por parámetro la ruta del archivo de propiedades creado en su aplicación.
*/

public class Menu {

	private ArrayList<Accion> clasesDelMenu = new ArrayList<Accion>();

	public Menu(String rutaArchivoDePropiedades) {
		try (InputStream implFile = getClass().getResourceAsStream(rutaArchivoDePropiedades)) {
			Properties properties = new Properties();
			properties.load(implFile);
			String clases[] = properties.getProperty("acciones").split(";");

			for (int i = 0; i < clases.length; i++) {
				Class clase = Class.forName(clases[i].trim());
				Accion claseAccion = (Accion) clase.getDeclaredConstructor().newInstance();
				this.clasesDelMenu.add(claseAccion);
			}

			imprimirMenuEnConsola();
		} catch (IOException e) {
			throw new RuntimeException("Error cargando el archivo de propiedades.", e);
		} catch (Exception e) {
			throw new RuntimeException("Error en el nombre de una clase en el archivo de propiedades.", e);
		}
	}

	private void imprimirMenuEnConsola() {
		int opcion = 0;
		int eleccion = 0;
		while (eleccion != clasesDelMenu.size() + 1) {
			for (opcion = 0; opcion < clasesDelMenu.size(); opcion++) {
				Accion clase = clasesDelMenu.get(opcion);
				System.out
						.println(opcion + 1 + "-" + clase.nombreItemMenu() + "(" + clase.descripcionItemMenu() + ").");
			}
			System.out.println(opcion + 1 + "-" + "Salir");

			System.out.println("Numero de Opcion:");
			Scanner input = new Scanner(System.in);
			try {
				eleccion = input.nextInt();
				ejecutarOpcionDelMenu(eleccion);
			} catch (InputMismatchException e) {
				eleccion = 0;
				System.out.println("Error, ingrese un numero.");
			}
		}
	}

	private void ejecutarOpcionDelMenu(int opcionDelMenu) {
		try {
			clasesDelMenu.get(opcionDelMenu - 1).ejecutar();
		} catch (IndexOutOfBoundsException e) {
			if (opcionDelMenu != clasesDelMenu.size() + 1) {
				System.out.println("Error, ingrese una opcion valida.");
			} else {
				System.out.println("Salir.");
			}
		}
	}
}
