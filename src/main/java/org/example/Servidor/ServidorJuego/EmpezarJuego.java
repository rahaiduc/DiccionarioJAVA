package org.example.Servidor.ServidorJuego;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class EmpezarJuego implements Runnable {
    private List<Socket> jugadores;
    private File diccionario = new File("src/main/resources/diccionario.txt");
    private int lineas;

    public EmpezarJuego(List<Socket> jugadores) {
        this.jugadores = jugadores;
        this.lineas=obtenerLineas();
    }

    @Override
    public void run() {
        try {
            Participante participante1 = new Participante(jugadores.get(0));
            Participante participante2 = new Participante(jugadores.get(1));
            Participante participante3 = new Participante(jugadores.get(2));
            for (int i = 0; i < 3; i++) {
                CountDownLatch countDownLatch = new CountDownLatch(3);
                String[] pregunta = obtenerPregunta();
                String definicion = pregunta[1];
                String respuesta = pregunta[0];
                Thread tJugador1=new Thread(new HiloJugador(participante1,definicion,respuesta,countDownLatch));
                Thread tJugador2=new Thread(new HiloJugador(participante2,definicion,respuesta,countDownLatch));
                Thread tJugador3=new Thread(new HiloJugador(participante3,definicion,respuesta,countDownLatch));
                tJugador1.start();
                tJugador2.start();
                tJugador3.start();
                tJugador1.join();
                tJugador2.join();
                tJugador3.join();
                asignarPuntuacion(participante1,participante2,participante3);
            }
            participante1.getPrintStream().println("Tu puntuacion: "+participante1.getPuntuacion()+"\n"+"2 Participante: "+ participante2.getPuntuacion()+"\n"+"3 Participante: "+ participante3.getPuntuacion());
            participante2.getPrintStream().println("Tu puntuacion: "+participante2.getPuntuacion()+"\n"+"2 Participante: "+ participante1.getPuntuacion()+"\n"+"3 Participante: "+ participante3.getPuntuacion());
            participante3.getPrintStream().println("Tu puntuacion: "+participante3.getPuntuacion()+"\n"+"2 Participante: "+ participante1.getPuntuacion()+"\n"+"3 Participante: "+ participante2.getPuntuacion());
            participante1.getPrintStream().println("Fin del juego");
            participante2.getPrintStream().println("Fin del juego");
            participante3.getPrintStream().println("Fin del juego");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int obtenerLineas() {
        int contador=0;
        try (BufferedReader br = new BufferedReader(new FileReader(diccionario))) {
            while (br.readLine() != null) {
                contador++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contador;
    }
    public String[] obtenerPregunta() {
        String [] pregunta=null;
        try (BufferedReader br = new BufferedReader(new FileReader(diccionario))) {
            String linea;
            int numeroLineaActual = 0;
            int numeroLineaDeseada=new Random().nextInt(lineas);
            while ((linea = br.readLine()) != null) {
                numeroLineaActual++;

                if (numeroLineaActual == numeroLineaDeseada) {
                    // Has encontrado la línea deseada, ahora puedes trabajar con ella
                    pregunta=linea.split(": ");
                    break; // Puedes detener la lectura después de encontrar la línea deseada
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pregunta;
    }
    public static void asignarPuntuacion(Participante p1, Participante p2, Participante p3) {
        int puntosJugador1, puntosJugador2, puntosJugador3;

        // Comparar los tiempos y asignar los puntos en consecuencia
        if (p1.getTiempoRonda() < p2.getTiempoRonda() && p1.getTiempoRonda() < p3.getTiempoRonda()) {
            puntosJugador1 = 3;
            if (p2.getTiempoRonda() < p3.getTiempoRonda()) {
                puntosJugador2 = 2;
                puntosJugador3 = 1;
            } else {
                puntosJugador2 = 1;
                puntosJugador3 = 2;
            }
        } else if (p2.getTiempoRonda() < p1.getTiempoRonda() && p2.getTiempoRonda() < p3.getTiempoRonda()) {
            puntosJugador2 = 3;
            if (p1.getTiempoRonda() < p3.getTiempoRonda()) {
                puntosJugador1 = 2;
                puntosJugador3 = 1;
            } else {
                puntosJugador1 = 1;
                puntosJugador3 = 2;
            }
        } else {
            puntosJugador3 = 3;
            if (p1.getTiempoRonda() < p2.getTiempoRonda()) {
                puntosJugador1 = 2;
                puntosJugador2 = 1;
            } else {
                puntosJugador1 = 1;
                puntosJugador2 = 2;
            }
        }

        // Ahora puedes usar los valores de puntosJugador1, puntosJugador2 y puntosJugador3 según sea necesario
        p1.setPuntuacion(p1.getPuntuacion()+puntosJugador1);
        p2.setPuntuacion(p2.getPuntuacion()+puntosJugador2);
        p3.setPuntuacion(p3.getPuntuacion()+puntosJugador3);
    }
}

