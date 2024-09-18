package Clases;

import java.awt.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class GanttPanel extends JPanel {
    private List<Proceso> procesos;

    public int getTamanioMemoria() {
        return tamanioMemoria;
    }

    public void setTamanioMemoria(int tamanioMemoria) {
        this.tamanioMemoria = tamanioMemoria;
    }

    private  int tamanioMemoria;

    public GanttPanel(List<Proceso> procesos,int tamanioMemoria)
    {
        this.procesos = procesos;
        this.tamanioMemoria = tamanioMemoria;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (procesos == null || procesos.isEmpty()) {
            return; // No hay procesos para mostrar
        }

        int chartHeight = getHeight();
        int chartWidth = getWidth();
        int alturaRectangulo = 30;
        int xOffset = 50;  // Posición inicial x para los rectángulos
        int yOffset = 50;  // Posición inicial y para los rectángulos
        int xAxisHeight = chartHeight - 50;
        int separacionVertical = 10;
        int spacingX = 50; // Espaciado entre los números del eje X

        // Configurar el rango y el tamaño de las particiones
        g2d.setStroke(new BasicStroke(2));

        // Dibujar el eje Y (invirtiendo el sentido del eje Y)
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xOffset - 10, yOffset, xOffset - 10, xAxisHeight); // Eje Y
        g2d.drawLine(xOffset - 10, xAxisHeight, chartWidth - 10, xAxisHeight); // Eje X

        // Etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Calcular el rango máximo para el eje Y
        int intervalos = (int) Math.ceil((double) tamanioMemoria / 10.0);


        for (int i = 0; i <= intervalos; i++) {
            int y = xAxisHeight - i * 50;
            g2d.drawLine(xOffset - 10, y, xOffset, y);
            // Añadir "k" a las etiquetas del eje Y
            g2d.drawString(String.valueOf(i * 10) + "k", xOffset - 40, y + 5);
        }

        // Etiquetas del eje X
        for (int i = 0; i <= (chartWidth - xOffset) / spacingX; i++) {
            int x = xOffset + i * spacingX;
            g2d.drawLine(x, xAxisHeight, x, xAxisHeight + 5);
            g2d.drawString(String.valueOf(i * 10), x - 10, xAxisHeight + 20); // Multiplica i por 10 para mantener el escalado original
        }

        // Dibujar los procesos
        Random random = new Random();
        int y = yOffset; // Reajustamos la posición inicial de y según el eje Y

        // Calcular el ancho total disponible para los rectángulos
        int maxAnchoDisponible = chartWidth - xOffset - 50; // Dejar un margen de 50 px a la derecha

        // Encontrar la duración máxima de los procesos para calcular la escala
        int maxDuracion = procesos.stream().mapToInt(Proceso::getDuracion).max().orElse(1);

        // Calcular el factor de escala para que todos los procesos quepan dentro del gráfico
        double escala = (double) maxAnchoDisponible / (maxDuracion * 10); // Ajuste de escala

        for (Proceso proceso : procesos) {
            int duracion = proceso.getDuracion();
            int anchoRectangulo = (int) (duracion * 10 * escala); // Escalar el ancho del rectángulo

            // Generar un color aleatorio para cada proceso
            Color colorProceso = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(colorProceso);
            g2d.fillRect(xOffset, y, anchoRectangulo, alturaRectangulo);

            // Dibujar borde del rectángulo
            g2d.setColor(Color.BLACK);
            g2d.drawRect(xOffset, y, anchoRectangulo, alturaRectangulo);

            // Mostrar el nombre del proceso
            g2d.setColor(Color.WHITE); // Usar blanco para que sea visible sobre los colores de fondo
            g2d.drawString(proceso.getNombre(), xOffset + 5, y + 20);

            // Mover la posición y para el siguiente proceso
            y += alturaRectangulo + separacionVertical;
        }
    }

    // Método para actualizar la lista de procesos y redibujar el panel
    public void setProcesos(List<Proceso> procesos,int tamanioMemoria) {
        this.procesos = procesos;
        this.tamanioMemoria = tamanioMemoria;
        repaint();
    }
}
