package Clases;
/*
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Registro {
    private PrintWriter writerEventos;
    private PrintWriter writerEstadoParticiones;

    public Registro(String archivoEventos, String archivoEstadoParticiones) throws IOException {
        // Abrimos los archivos en modo de sobrescritura para limpiarlos al inicio
        this.writerEventos = new PrintWriter(new FileWriter(archivoEventos, false));
        this.writerEstadoParticiones = new PrintWriter(new FileWriter(archivoEstadoParticiones, false));
    }

    public void registrarEvento(String mensaje) {
        writerEventos.println(mensaje);
        writerEventos.flush();
    }

    public void registrarEstadoParticiones(List<Particion> particiones) {
        writerEstadoParticiones.println("Estado de particiones en tiempo " + System.currentTimeMillis() + ":");
        for (Particion particion : particiones) {
            writerEstadoParticiones.printf("ID: %d, Comienzo: %d, Tamaño: %d, Estado: %s%n",
                    particion.getId(), particion.getTiempoInicio(), particion.getTamanio(),
                    particion.getEstado() ? "Ocupada" : "Libre");
        }
        writerEstadoParticiones.flush();
    }

    public void cerrar() throws IOException {
        writerEventos.close();
        writerEstadoParticiones.close();
    }
}

 */