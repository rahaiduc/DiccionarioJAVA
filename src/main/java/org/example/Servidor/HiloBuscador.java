package org.example.Servidor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class HiloBuscador implements Callable<Boolean>{

    long inicio;
    long fin;

    File diccionario;
    String palabra;
    PrintStream printStream;
    public HiloBuscador(long inicio, long fin,File diccionario,String palabra,PrintStream ps) {
        this.inicio = inicio;
        this.fin = fin;
        this.diccionario=diccionario;
        this.palabra=palabra;
        this.printStream=ps;
    }

    public Boolean call() {
        boolean result=false;
        try(RandomAccessFile raf=new RandomAccessFile(diccionario,"r")){
            raf.seek(inicio);
            while(raf.getFilePointer()<fin){
                String [] partes=raf.readLine().split(": ");
                if(partes[0].toLowerCase().equals(palabra)){
                    printStream.println(partes[1]);
                    result=true;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}