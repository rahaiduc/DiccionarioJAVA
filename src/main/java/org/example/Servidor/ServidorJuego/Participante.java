package org.example.Servidor.ServidorJuego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

public class Participante {
    private Socket socket;
    private String nombre;

    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private int puntuacion;

    private long tiempoRonda;

    public Participante(Socket socket, String nombre) {
        this.socket = socket;
        this.nombre = nombre;
        this.puntuacion = 0;
    }
    public Participante(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printStream=new PrintStream(socket.getOutputStream());
        this.nombre = "Anonimo"+ new Random().nextInt(100);
        this.puntuacion = 0;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }


    public long getTiempoRonda() {
        return tiempoRonda;
    }

    public void setTiempoRonda(long tiempoRonda) {
        this.tiempoRonda = tiempoRonda;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }
}
