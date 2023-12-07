package org.example.Servidor;

import org.example.Servidor.ServidorJuego.ServidorJuego;
import org.example.Servidor.ServidorNormal.Servidor;

public class IniciarServidores {

    public static void main(String[] args) {
        // Iniciar el Servidor principal
        Thread servidorPrincipalThread = new Thread(() -> Servidor.main(args));
        servidorPrincipalThread.start();

        // Esperar un poco antes de iniciar el ServidorJuego
        try {
            Thread.sleep(2000); // Puedes ajustar el tiempo según sea necesario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Iniciar el ServidorJuego
        Thread servidorJuegoThread = new Thread(() -> ServidorJuego.main(args));
        servidorJuegoThread.start();
    }
}
