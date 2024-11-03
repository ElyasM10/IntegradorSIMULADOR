package Clases;

import java.awt.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class GanttPanel extends JPanel {
    private List<Particion> particiones;

    public int getTamanioMemoria() {
        return tamanioMemoria;
    }

    public void setTamanioMemoria(int tamanioMemoria) {
        this.tamanioMemoria = tamanioMemoria;
    }

    private int tamanioMemoria;

    private int fragmentacionExterna;
    private String politica;
    private int tiemporRetorno;


    public GanttPanel(List<Particion> particiones, int tamanioMemoria) {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;

    }

    /*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (particiones == null || particiones.isEmpty()) {
            return; // No hay particiones para mostrar
        }

        int chartHeight = getHeight();
        int chartWidth = getWidth();
        int alturaRectangulo = 30;
        int xOffset = 50;  // Posición inicial x para los rectángulos
        int yOffset = 50;  // Posición inicial y para los rectángulos
        int xAxisHeight = chartHeight - 50;
        int separacionVertical = 10;
        int spacingX = 50; // Espaciado entre los números del eje X

        // Dibujar el eje Y
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xOffset - 10, yOffset, xOffset - 10, xAxisHeight); // Eje Y
        g2d.drawLine(xOffset - 10, xAxisHeight, chartWidth - 10, xAxisHeight); // Eje X

        // Etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Calcular el rango máximo para el eje Y, basado en el tamaño de memoria
        int intervalos = (int) Math.ceil((double) tamanioMemoria / 10.0);

        // Dibujar intervalos en el eje Y
        for (int i = 0; i <= intervalos; i++) {
            int y = xAxisHeight - (i * (xAxisHeight - yOffset)) / intervalos;
            g2d.drawLine(xOffset - 10, y, xOffset, y);
            g2d.drawString(String.valueOf(i * 10) + "k", xOffset - 40, y + 5);
        }

        // Etiquetas del eje X
        for (int i = 0; i <= (chartWidth - xOffset) / spacingX; i++) {
            int x = xOffset + i * spacingX;
            g2d.drawLine(x, xAxisHeight, x, xAxisHeight + 5);
            g2d.drawString(String.valueOf(i * 10), x - 10, xAxisHeight + 20); // Multiplica i por 10 para mantener el escalado original
        }

        // Dibujar las particiones
        Random random = new Random();
        int y = yOffset; // Reajustamos la posición inicial de y según el eje Y

        // Calcular el ancho total disponible para los rectángulos
        int maxAnchoDisponible = chartWidth - xOffset - 50; // Dejar un margen de 50 px a la derecha

        // Encontrar la duración máxima de las particiones para calcular la escala
        int maxDuracion = particiones.stream().mapToInt(Particion::getGraficarParticion).max().orElse(1);

        // Calcular el factor de escala para que todos los particiones quepan dentro del gráfico
        double escala = (double) maxAnchoDisponible / (maxDuracion * 10); // Ajuste de escala

        // Escalar el tamaño de las particiones al eje Y según el tamaño de la memoria
        double escalaY = (double) (xAxisHeight - yOffset) / tamanioMemoria;

        for (Particion particion : particiones) {
            int duracion = particion.getGraficarParticion();
            int anchoRectangulo = (int) (duracion * 10 * escala); // Escalar el ancho del rectángulo

            // Escalar el tamaño en el eje Y
            int altoRectangulo = (int) (particion.getTamanio() * escalaY);

            // Generar un color aleatorio para cada partición
            Color colorParticion = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(colorParticion);
            g2d.fillRect(xOffset, y, anchoRectangulo, altoRectangulo);

            // Dibujar borde del rectángulo
            g2d.setColor(Color.BLACK);
            g2d.drawRect(xOffset, y, anchoRectangulo, altoRectangulo);

            // Mostrar el nombre del partición centrado
            g2d.setColor(Color.WHITE); // Usar blanco para que sea visible sobre los colores de fondo
            g2d.drawString("T" + particion.getId() + "-" + particion.getTamanio() + "K", xOffset + anchoRectangulo / 2 - 10, y + altoRectangulo / 2);

            // Mover la posición y para el siguiente partición
            y += altoRectangulo + separacionVertical;
        }
    }
*/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (particiones == null || particiones.isEmpty()) {
            return;
        }

        int chartHeight = getHeight();
        int chartWidth = getWidth();
        int xOffset = 50;
        int yOffset = 50;
        int xAxisHeight = chartHeight - 100;
        int spacingY = 10;
        int spacingX = 50;

        // Dibujar el eje Y con marcas y etiquetas
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xOffset - 10, yOffset, xOffset - 10, xAxisHeight); // Línea del eje Y

        int intervalosY = (int) Math.ceil((double) tamanioMemoria / 10);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i <= intervalosY; i++) {
            int y = xAxisHeight - (i * (xAxisHeight - yOffset)) / intervalosY;
            g2d.drawLine(xOffset - 10, y, xOffset, y); // Marcas del eje Y
            g2d.drawString(i * 10 + "k", xOffset - 40, y + 5);
        }

        // Dibujar el eje X con marcas y etiquetas
        int tiempoFinal = tiemporRetorno + 2;
        g2d.drawLine(xOffset, xAxisHeight, chartWidth - 50, xAxisHeight); // Línea del eje X
        for (int i = 0; i <= tiempoFinal; i++) {
            int x = xOffset + i * spacingX;
            g2d.drawLine(x, xAxisHeight, x, xAxisHeight + 5); // Marcas del eje X
            g2d.drawString(String.valueOf(i), x - 10, xAxisHeight + 20);
        }

        // Dibujar particiones
        Random random = new Random();
        for (Particion particion : particiones) {
            int anchoRectangulo = (int) ((particion.getTiempoFinalizacion() - particion.getTiempoInicio()) * spacingX);
            int altoRectangulo = (int) (particion.getTamanio() * ((double) (xAxisHeight - yOffset) / tamanioMemoria));
            int x = xOffset + particion.getTiempoInicio() * spacingX;
            int y = xAxisHeight - altoRectangulo - particion.getGraficarParticion();

            Color colorParticion = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(colorParticion);
            g2d.fillRect(x, y, anchoRectangulo, altoRectangulo);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, anchoRectangulo, altoRectangulo);

            g2d.setColor(Color.WHITE);
            g2d.drawString("T" + particion.getIdTarea() + "-" + particion.getTamanio() + "K", x + anchoRectangulo / 2 - 10, y + altoRectangulo / 2);
        }

        // Etiquetas de fragmentación y política en la parte inferior izquierda
        g2d.setColor(Color.BLACK);
        g2d.drawString("FRAGMENTACION EXTERNA: " + fragmentacionExterna, 5, chartHeight - 30);
        g2d.drawString("POLITICA: " + politica, 5, chartHeight - 15);
    }

    // Metodo para actualizar la lista de Particions y redibujar el panel
    public void setDatos(List<Particion> particiones,int tamanioMemoria,int fragmentacion,String politica,int TRT) {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;
        this.fragmentacionExterna = fragmentacion;
        this.politica = politica;
        this.tiemporRetorno = TRT;
        repaint();
    }
}
