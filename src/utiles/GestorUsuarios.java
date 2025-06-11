package utiles;

import java.util.HashMap;
import java.util.Map;

public class GestorUsuarios {
	private static final Map<String, String> usuarios = new HashMap<String, String>();

	static {
		// Usuario por defecto
		usuarios.put("Diego", "1234");
		usuarios.put("Daniela", "1234");
	}

	public static boolean validarUsuario(String usuario, String contrasena) {
		boolean valido = false;
		if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contrasena)) {
			valido = true;
		}
		return valido;
	}

	public static void agregarUsuario(String usuario, String contrasena) {
		usuarios.put(usuario, contrasena);
	}

	public static void eliminarUsuario(String usuario) {
		usuarios.remove(usuario);
	}
}
