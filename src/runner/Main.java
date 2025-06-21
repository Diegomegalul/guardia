package runner;

import interfaz.Cargando;

import java.awt.EventQueue;

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cargando frame = new Cargando();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}); 
	}
}

//Diooooos como hago para sacarla de mi cabeza

//TIMØ - Quiero Saber ;)


