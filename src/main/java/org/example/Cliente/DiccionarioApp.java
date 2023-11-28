package org.example.Cliente;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DiccionarioApp extends JFrame {
    private JTextField palabraBuscarTextField;
    private JButton buscarButton;
    private JLabel definicionLabel;

    private JTextField palabraAnadirTextField;
    private JTextField definicionAnadirTextField;
    private JButton anadirButton;

    // Constructor
    public DiccionarioApp(Socket socket) throws IOException {

        PrintStream ps=new PrintStream(socket.getOutputStream(), true,StandardCharsets.UTF_8);
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
        // Configurar el JFrame
        setTitle("Diccionario");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // PESTAÑA DE BÚSQUEDA ---------------------------------------------------------------------
        JPanel buscarPanel = new JPanel();
        palabraBuscarTextField = new JTextField(20);
        buscarButton = new JButton("Buscar");
        definicionLabel = new JLabel("Definición: ");

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPalabra(ps,br);
            }
        });

        buscarPanel.add(new JLabel("Palabra a buscar: "));
        buscarPanel.add(palabraBuscarTextField);
        buscarPanel.add(buscarButton);
        buscarPanel.add(definicionLabel);

        tabbedPane.addTab("Buscar", buscarPanel);

        // PESTAÑA DE AÑADIR ---------------------------------------------------------------------
        JPanel anadirPanel = new JPanel();
        palabraAnadirTextField = new JTextField(20);
        definicionAnadirTextField = new JTextField(20);
        anadirButton = new JButton("Añadir");

        anadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirPalabra(ps,br);
            }
        });

        anadirPanel.add(new JLabel("Palabra a añadir: "));
        anadirPanel.add(palabraAnadirTextField);
        anadirPanel.add(new JLabel("Definición: "));
        anadirPanel.add(definicionAnadirTextField);
        anadirPanel.add(anadirButton);

        tabbedPane.addTab("Añadir", anadirPanel);

        // Agregar el JTabbedPane al JFrame
        add(tabbedPane);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSocket(ps,br,socket);
            }
        });
    }

    private void buscarPalabra(PrintStream ps,BufferedReader br) {
        String palabra = palabraBuscarTextField.getText();
        String definicion = obtenerDefinicion(palabra,ps,br);
        definicionLabel.setText("Definicion: " + definicion);
    }

    private void anadirPalabra(PrintStream ps,BufferedReader br) {
        String nuevaPalabra = palabraAnadirTextField.getText();
        String nuevaDefinicion = definicionAnadirTextField.getText();
        try {
            ps.println("agregar");
            ps.println(nuevaPalabra);
            ps.println(nuevaDefinicion);
            String resultado = br.readLine();
            JOptionPane.showMessageDialog(this, resultado);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        palabraAnadirTextField.setText("");
        definicionAnadirTextField.setText("");
    }

    private String obtenerDefinicion(String palabra,PrintStream ps,BufferedReader br) {
        try{
            ps.println("buscar");
            ps.println(palabra.toLowerCase());
            return br.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "Palabra no encontrada en el diccionario";
    }
    private void cerrarSocket(PrintStream ps,BufferedReader br,Socket socket) {
        try {
            if (socket != null) {
                ps.println("desconectar");
                ps.close();
                br.close();
                socket.close();
                JOptionPane.showMessageDialog(this, "Socket cerrado correctamente.");
                System.out.println("Socket cerrado correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

