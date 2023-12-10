package org.example.Cliente;
import javax.swing.*;
import java.awt.*;
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
    //private JLabel definicionLabel;
    private JTextPane definicionLabel;
    private JTextField palabraAnadirTextField;
    private JTextField definicionAnadirTextField;
    private JButton anadirButton;
    JTextArea jTextoServer;
    // Constructor
    public DiccionarioApp(Socket socket) throws IOException {

        PrintStream ps=new PrintStream(socket.getOutputStream(), true,StandardCharsets.UTF_8);
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
        // Configurar el JFrame
        setTitle("Diccionario");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout());

        // Crear un JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // PESTAÑA DE BÚSQUEDA ---------------------------------------------------------------------
        JPanel buscarPanel = new JPanel(new GridLayout(2,2));
        JPanel buscarPanelsub1 = new JPanel(new GridBagLayout());

        // Crear un JPanel con GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Configurar el JLabel y el JTextField en la primera fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes
        buscarPanelsub1.add(new JLabel("Palabra a buscar:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // El JTextField se expandirá horizontalmente
        palabraBuscarTextField = new JTextField(20);
        buscarPanelsub1.add(palabraBuscarTextField, gbc);

        // Configurar el JButton en la segunda fila
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centra el botón
        buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPalabra(ps,br);
            }
        });
        buscarPanelsub1.add(buscarButton, gbc);

        JPanel buscarPanelsub2 = new JPanel(new GridLayout());
        definicionLabel=new JTextPane();
        definicionLabel.setEditable(false);
        buscarPanelsub2.add(definicionLabel);

        //Añadir los dos subpaneles al panel padre
        buscarPanel.add(buscarPanelsub1);
        buscarPanel.add(buscarPanelsub2);

        tabbedPane.addTab("Buscar", buscarPanel);

        // PESTAÑA DE AÑADIR ---------------------------------------------------------------------
        JPanel anadirPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        // Configurar el JLabel y el JTextField en la primera fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes
        anadirPanel.add(new JLabel("Palabra a añadir:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // El JTextField se expandirá horizontalmente
        palabraAnadirTextField = new JTextField(20);
        anadirPanel.add(palabraAnadirTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes
        anadirPanel.add(new JLabel("Definicion:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // El JTextField se expandirá horizontalmente
        definicionAnadirTextField = new JTextField(20);
        anadirPanel.add(definicionAnadirTextField, gbc);
        // Configurar el JButton en la segunda fila
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER; // Centra el botón
        anadirButton = new JButton("Añadir");
        anadirPanel.add(anadirButton,gbc);
        anadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirPalabra(ps,br);
            }
        });

        tabbedPane.addTab("Añadir", anadirPanel);
        // PESTAÑA DE JUEGO ---------------------------------------------------------------------

        JPanel juegoPanel = new JPanel(new BorderLayout());
        JButton botonConectar=new JButton("Conectar");
        botonConectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comenzarJuego(ps,br);
            }
        });
        juegoPanel.add(botonConectar, BorderLayout.NORTH);
        jTextoServer=new JTextArea();
        jTextoServer.setEditable(false);
        juegoPanel.add(jTextoServer, BorderLayout.CENTER);
        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendMessageButton = new JButton("Enviar respuesta");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ps.println(messageField.getText().toLowerCase());

            }
        });
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendMessageButton, BorderLayout.EAST);
        juegoPanel.add(messagePanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Juego",juegoPanel);
        // Agregar el JTabbedPane al JFrame
        add(tabbedPane);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSocket(ps,br,socket);
            }
        });
    }


    private void comenzarJuego(PrintStream ps,BufferedReader br) {
        ps.println("jugar");
        Thread thread=new Thread(new HiloJuego(br,jTextoServer));
        thread.start();
    }
    private void buscarPalabra(PrintStream ps,BufferedReader br) {
        String palabra = palabraBuscarTextField.getText();
        if(palabra.isEmpty() || palabra.isBlank()){
            JOptionPane.showMessageDialog(this, "No has escrito una palabra");
        }else{
            String definicion = obtenerDefinicion(palabra,ps,br);
            definicionLabel.setText("Definicion: " + definicion);
        }
    }

    private void anadirPalabra(PrintStream ps,BufferedReader br) {
        String nuevaPalabra = palabraAnadirTextField.getText();
        String nuevaDefinicion = definicionAnadirTextField.getText();
        if (nuevaPalabra.isEmpty() || nuevaPalabra.isBlank() || nuevaDefinicion.isEmpty() || nuevaDefinicion.isBlank()) {
            JOptionPane.showMessageDialog(this, "Algún campo esta vacío. Rellenalo");
        } else{
            try {
                ps.println("agregar");
                ps.println(nuevaPalabra);
                ps.println(nuevaDefinicion);
                String resultado = br.readLine();
                JOptionPane.showMessageDialog(this, resultado);
            } catch (IOException e) {
                e.printStackTrace();
            }
            palabraAnadirTextField.setText("");
            definicionAnadirTextField.setText("");
        }
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

