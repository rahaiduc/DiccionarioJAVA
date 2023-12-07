package org.example.Servidor.ServidorNormal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class EnviarMensajeJuego implements Runnable{
    PrintStream enviarServidor;
    BufferedReader recibirJugador;


    public EnviarMensajeJuego(PrintStream enviarServidor, BufferedReader recibirJugador) {
        this.enviarServidor = enviarServidor;
        this.recibirJugador = recibirJugador;
    }

    @Override
    public void run() {
        try{
            String mensajeServidor= recibirJugador.readLine();
            while(!mensajeServidor.equals("desconectar")){
                enviarServidor.println(mensajeServidor);
                mensajeServidor=recibirJugador.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
