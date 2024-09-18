
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import Clases.*;




public class interfazGrafica extends JFrame {
    private JTextField txtNombreArchivo;
    private JComboBox<String> cmbPolitica;
    private JTextField txtTamanioMemoria;
    private JTextField txtTiempoSeleccion;
    private JTextField txtTiempoCargaPromedio;
    private JTextField txtTiempoLiberacion;
    private Simulador simulador;
    private GanttPanel ganttPanel;
    public static final int FIRST_FIT = 1;
    public static final int BEST_FIT = 2;
    public static final int NEXT_FIT = 3;
    public static final int WORST_FIT = 4;



    public interfazGrafica() {
        setTitle("Simulador de Asignacion de Memoria");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH); //pantalla completa

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel de entrada con GridLayout
        JPanel panelEntrada = new JPanel(new GridLayout(4, 4, 5, 5)); // 4 filas, 4 columnas, margen de 5px

        // Etiquetas y campos de entrada
        panelEntrada.add(new JLabel("Nombre del archivo:"));
        txtNombreArchivo = new JTextField();
        panelEntrada.add(txtNombreArchivo);

        panelEntrada.add(new JLabel("Politica de asignacion:"));
        cmbPolitica = new JComboBox<>(new String[]{"FirstFit", "BestFit", "NextFit", "WorstFit"});
        panelEntrada.add(cmbPolitica);


        panelEntrada.add(new JLabel("Tamanio de memoria fisica disponible:"));
        txtTamanioMemoria = new JTextField();
        panelEntrada.add(txtTamanioMemoria);

        panelEntrada.add(new JLabel("Tiempo de seleccion de particion:"));
        txtTiempoSeleccion = new JTextField();
        panelEntrada.add(txtTiempoSeleccion);

        panelEntrada.add(new JLabel("Tiempo de carga promedio:"));
        txtTiempoCargaPromedio = new JTextField();
        panelEntrada.add(txtTiempoCargaPromedio);

        panelEntrada.add(new JLabel("Tiempo de liberacion de particion:"));
        txtTiempoLiberacion = new JTextField();
        panelEntrada.add(txtTiempoLiberacion);

        // Crear el panel de Gantt para mostrar el diagrama
        ganttPanel = new GanttPanel(new ArrayList<>(), 0);

        // Añadir paneles al contenedor principal
        panel.add(panelEntrada, BorderLayout.NORTH);
      panel.add(ganttPanel, BorderLayout.CENTER);


        // Boton de cargar y simular
        JButton btnCargar = new JButton("Cargar y Simular");
        btnCargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEntrada.add(btnCargar);

        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica de simulación al hacer clic
                cargarYSimular(); // Descomentar si deseas utilizar esta función
            }
        });


        add(panel);
    }

    private List<Proceso> cargarProcesosDesdeArchivo(String nombreArchivo) {
        List<Proceso> procesos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] datos = linea.split(",");
                if (datos.length != 5) {
                    JOptionPane.showMessageDialog(this, "Linea en el archivo no válida: " + linea);
                    continue;
                }

                try {
                    int id = Integer.parseInt(datos[0].trim());
                    String nombre = datos[1].trim();
                    int memoriaRequerida = Integer.parseInt(datos[2].trim());
                    int duracion = Integer.parseInt(datos[3].trim());
                    int instanteArribo = Integer.parseInt(datos[4].trim());

                    Proceso proceso = new Proceso(id, nombre, memoriaRequerida, duracion, instanteArribo);
                    procesos.add(proceso);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Error de formato en la linea: " + linea + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage());
        }
        return procesos;
    }


    // Método para cargar el archivo y ejecutar la simulacion
    private void cargarYSimular() {
        String nombreArchivo = txtNombreArchivo.getText();
        String politicaSeleccionada = (String) cmbPolitica.getSelectedItem();

        List<Proceso> listaProcesos = new ArrayList<>();
        listaProcesos = cargarProcesosDesdeArchivo(nombreArchivo);

        if (listaProcesos == null || listaProcesos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar procesos del archivo.");
            return;
        }


        // Configurar la estrategia de asignación seleccionada
        int estrategiaAsignacion;

        switch (politicaSeleccionada.toLowerCase()) {
            case "firstfit":
                estrategiaAsignacion = FIRST_FIT;
                break;
            case "bestfit":
                estrategiaAsignacion = BEST_FIT;
                break;
            case "nextfit":
                estrategiaAsignacion = NEXT_FIT;
                break;
            case "worstfit":
                estrategiaAsignacion = WORST_FIT;
                break;
            default:
                System.out.println("Política no reconocida. Se usará FIRST_FIT por defecto.");
                estrategiaAsignacion = FIRST_FIT;
                break;
        }

        try {
            int tamanioMemoria = Integer.parseInt(txtTamanioMemoria.getText());
            int tiempoSeleccion = Integer.parseInt(txtTiempoSeleccion.getText());
            int tiempoCargaPromedio = Integer.parseInt(txtTiempoCargaPromedio.getText());
            int tiempoLiberacion = Integer.parseInt(txtTiempoLiberacion.getText());

            // Crear el simulador con la estrategia seleccionada
            simulador = new Simulador(listaProcesos, tamanioMemoria, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, estrategiaAsignacion);

            // Actualizar el panel de Gantt con los nuevos datos
            ganttPanel.setProcesos(listaProcesos, tamanioMemoria);

            // Redibujar el panel de Gantt
            ganttPanel.repaint();

            // Simular y actualizar el panel de Gantt
            simulador.simular();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error en el formato de los números: " + ex.getMessage());
        }
    }


    // Método para mostrar los resultados de la simulacion como un diagrama de Gantt
   //private void mostrarResultados() {
      // ganttPanel.setProcesos(simulador.getProcesos(),tamanioMemoria); // Actualizar el panel de Gantt con los procesos simulados
    //}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new interfazGrafica().setVisible(true);
        });
    }

}