package org.example.Servidor.ServidorNormal;

// TITULO : Diccionario con hilos virtuales
// FECHA : 23/11/2023
// AUTORES : Raúl Dan Haiduc & Mario Gil Sáenz
// DESCRIPCIÓN :

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    //Implementa el servidor concurrente al que se conectarán los clientes

    public static void main(String[] args) {
        try(ExecutorService pool= Executors.newVirtualThreadPerTaskExecutor();
            ServerSocket ss=new ServerSocket(5000)){
            while(true){
                Socket s=ss.accept();
                pool.submit(new AtenderPeticion(s));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
