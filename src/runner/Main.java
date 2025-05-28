package runner;

import interfaz.Inicio;
import logica.PlanificadorGuardias; // <-- Este import debe existir y ser correcto

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        final PlanificadorGuardias planificador = new PlanificadorGuardias();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Inicio frame = new Inicio(planificador);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
