package org.example.Cliente;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class HiloJuego implements Runnable{

    private BufferedReader br;
    private JTextArea jTextArea;

    public HiloJuego(BufferedReader br, JTextArea jTextArea) {
        this.br = br;
        this.jTextArea = jTextArea;
    }

    @Override
    public void run() {
        try{
            String mensaje= br.readLine();
            while(!mensaje.equals("Fin del juego")){
                jTextArea.append("\n"+mensaje);
                mensaje=br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
