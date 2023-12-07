package org.example.Servidor.ServidorNormal;

// TITULO : Diccionario con hilos virtuales
// FECHA : 23/11/2023
// AUTORES : Raúl Dan Haiduc & Mario Gil Sáenz
// DESCRIPCIÓN :

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class AtenderPeticion implements Runnable{

    //Clase que implementa la lógica para atender las peticiones de los clientes
    Socket socket;
    File diccionario=new File("src/main/resources/diccionario.txt");

    AtenderPeticion(Socket socket){
        this.socket=socket;
    }


    @Override
    public void run() {
        try(BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream ps=new PrintStream(socket.getOutputStream())){
            String leido=br.readLine();
            while (!leido.contains("desconectar")) {
                if (leido.equals("agregar")) {
                    agregarPalabra(br,ps);
                } else if(leido.equals("buscar")){
                    buscarPalabra(br, ps);
                }else if(leido.equals("jugar")){
                    empezarPartida(ps,br);
                }
                leido= br.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void buscarPalabra(BufferedReader br,PrintStream ps) {
        try(ExecutorService pool=Executors.newVirtualThreadPerTaskExecutor()){
            String palabra= br.readLine();
            int hilos=50;
            long longitud= diccionario.length();
            long particion=longitud/hilos;
            int i=0;
            List<Future<Boolean>> futures = new ArrayList<>();
            while(i<hilos-1){
                futures.add(pool.submit(new HiloBuscador(i*particion,(i+1)*particion-1,diccionario,palabra,ps)));
                i++;
            }
            futures.add(pool.submit(new HiloBuscador(i*particion,diccionario.length(),diccionario,palabra,ps)));
            boolean encontrado=false;
            // Espera a que todos los hilos completen y recopila los resultados
            for (int j = 0; j < futures.size(); j++) {
                try {
                    if (futures.get(j).get()) {
                        System.out.println("La palabra fue encontrada en el hilo " + j );
                        encontrado=true;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if(!encontrado){
                ps.println("La palabra no se ha encontrado en el diccionario");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void agregarPalabra(BufferedReader br,PrintStream ps) {
        try{
            String nuevaPalabra = br.readLine();
            String nuevaDefinicion = br.readLine();

            //Agregamos la palabra y su definición al final del diccionario
            try (FileWriter fw = new FileWriter(diccionario, StandardCharsets.UTF_8, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)){
                out.println(nuevaPalabra + ": " + nuevaDefinicion);
                ps.println("Palabra agregada con exito");
            }
            catch (IOException e) {
                e.printStackTrace();
                ps.println("Error al agregar la palabra al diccionario");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void empezarPartida(PrintStream ps,BufferedReader br) {
        try(Socket socket=new Socket("localhost",6000);
            BufferedReader recibir=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream enviar=new PrintStream(socket.getOutputStream())){
            ps.println("Esperando la conexion de otros 2 jugadores para dar comienzo a la partida...");
            Thread TEnviar=new Thread(new EnviarMensajeJuego(enviar,br));
            Thread TRecibir=new Thread(new RecibirMensajeJuego(recibir,ps));
            TEnviar.start();
            TRecibir.start();
            TEnviar.join();
            TRecibir.join();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
