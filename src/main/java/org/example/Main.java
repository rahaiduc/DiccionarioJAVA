package org.example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        try(ExecutorService pool= Executors.newVirtualThreadPerTaskExecutor();
            RandomAccessFile raf=new RandomAccessFile("src/main/resources/diccionario.txt","r")){
            raf.seek(22);
            raf.readLine();
            System.out.println(raf.getFilePointer());

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}