package org.example.Cliente;

// TITULO : Diccionario con hilos virtuales
// FECHA : 23/11/2023
// AUTORES : Raúl Dan Haiduc & Mario Gil Sáenz
// DESCRIPCIÓN :


import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClienteMain {

    //Esta es la clase Cliente del proyecto

    public static void main(String[] args) {
        try{
            Socket socket=new Socket("localhost",5000);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new DiccionarioApp(socket).setVisible(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
