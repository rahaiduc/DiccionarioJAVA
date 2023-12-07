package org.example.Servidor.ServidorJuego;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorJuego {
    public static void main(String[] args) {
        try(ExecutorService pool= Executors.newVirtualThreadPerTaskExecutor();
            ServerSocket ss=new ServerSocket(6000)){
            while(true){
                List<Socket> jugadores=new ArrayList<>();
                for(int i=0;i<3;i++) {
                    Socket s = ss.accept();
                    jugadores.add(s);
                }
                pool.submit(new EmpezarJuego(jugadores));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
