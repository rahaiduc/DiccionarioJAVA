package org.example.Servidor.ServidorNormal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class RecibirMensajeJuego implements Runnable{
    BufferedReader recibirServidor;
    PrintStream enviarJugador;


    public RecibirMensajeJuego(BufferedReader recibirServidor, PrintStream enviarJugador) {
        this.recibirServidor = recibirServidor;
        this.enviarJugador = enviarJugador;
    }

    @Override
    public void run() {
        try{
            String mensajeServidor= recibirServidor.readLine();
            while(!mensajeServidor.equals("desconectar")){
                enviarJugador.println(mensajeServidor);
                mensajeServidor=recibirServidor.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
