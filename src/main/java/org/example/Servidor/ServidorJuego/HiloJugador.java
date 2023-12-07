package org.example.Servidor.ServidorJuego;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class HiloJugador implements Runnable{
    private Participante participante;
    private String definicion;
    private String respuesta;
    private CountDownLatch countDownLatch;

    public HiloJugador(Participante participante, String definicion, String respuesta,CountDownLatch countDownLatch) {
        this.participante = participante;
        this.definicion = definicion;
        this.respuesta = respuesta;
        this.countDownLatch=countDownLatch;
    }

    @Override
    public void run() {
        try{
            countDownLatch.countDown();
            countDownLatch.await();
            participante.getPrintStream().println("¿Cual es la palabra cuya definicion es esta?");
            participante.getPrintStream().println(definicion);
            while (!participante.getBufferedReader().readLine().equals(respuesta)){
                participante.getPrintStream().println("Respuesta Incorrecta. Vuelve a intentarlo.");
            }
            participante.getPrintStream().println("¡¡¡Exitoo!!! Espera a los demas para la siguiente ronda.");
            participante.setTiempoRonda(new Date().getTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
