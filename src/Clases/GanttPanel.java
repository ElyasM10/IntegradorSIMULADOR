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
    private static final int MARGEN = 50;
    private static final int DESPLAZAMIENTO_EJE = 70;


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
    /*
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
  */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int alturaGrafico = alto - 2 * MARGEN;

        // Calcular escalas
        double escalaY = (double)(alturaGrafico - DESPLAZAMIENTO_EJE) / (tamanioMemoria + 10); // +10 para el margen superior
        double escalaX = (double)(ancho - DESPLAZAMIENTO_EJE - MARGEN) / tiemporRetorno;

        dibujarEjeY(g2d, alturaGrafico, escalaY);
        dibujarEjeX(g2d, ancho, alturaGrafico, escalaX);
        dibujarParticiones(g2d, alturaGrafico, escalaX, escalaY);
        dibujarTitulos(g2d);
    }

    private void dibujarEjeY(Graphics2D g2d, int alturaGrafico, double escalaY) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(DESPLAZAMIENTO_EJE, MARGEN, DESPLAZAMIENTO_EJE, alturaGrafico);

        for (int i = 0; i <= tamanioMemoria; i += 10) {
            int y = alturaGrafico - (int)(i * escalaY) - MARGEN;
            g2d.drawLine(DESPLAZAMIENTO_EJE - 5, y, DESPLAZAMIENTO_EJE, y);
            g2d.drawString(i + "k", DESPLAZAMIENTO_EJE - 40, y + 5);
        }
    }

    private void dibujarEjeX(Graphics2D g2d, int ancho, int alturaGrafico, double escalaX) {
        g2d.drawLine(DESPLAZAMIENTO_EJE, alturaGrafico - MARGEN, ancho - MARGEN, alturaGrafico - MARGEN);

        for (int i = 0; i <= tiemporRetorno; i++) {
            int x = DESPLAZAMIENTO_EJE + (int)(i * escalaX);
            g2d.drawLine(x, alturaGrafico - MARGEN, x, alturaGrafico - MARGEN + 5);
            g2d.drawString(String.valueOf(i), x - 5, alturaGrafico - MARGEN + 20);
        }
    }

    private void dibujarParticiones(Graphics2D g2d, int alturaGrafico, double escalaX, double escalaY) {
        Color[] colores = {
                new Color(31, 119, 180),   // Azul
                new Color(255, 127, 14),   // Naranja
                new Color(44, 160, 44),    // Verde
                new Color(214, 39, 40),    // Rojo
                new Color(148, 103, 189),  // Morado
                new Color(140, 86, 75),    // Marrón
                new Color(227, 119, 194),  // Rosa
                new Color(127, 127, 127),  // Gris
                new Color(188, 189, 34),   // Verde-amarillo
                new Color(23, 190, 207)    // Turquesa
        };

        for (int i = 0; i < particiones.size(); i++) {
            Particion p = particiones.get(i);

            // Calcular dimensiones y posición
            double ejeY = p.getGraficarParticion();
            int altura = (int)(p.getTamanio() * escalaY);
            int y = alturaGrafico - MARGEN - (int)(ejeY * escalaY) - altura;
            int ancho = (int)((p.getTiempoFinalizacion() - p.getTiempoInicio()) * escalaX);
            int x = DESPLAZAMIENTO_EJE + (int)(p.getTiempoInicio() * escalaX);

            // Debug info
            System.out.println("Partición T" + p.getIdTarea() +
                    " - x:" + x +
                    " y:" + y +
                    " ancho:" + ancho +
                    " altura:" + altura +
                    " ejeY:" + ejeY);

            // Dibujar partición
            g2d.setColor(colores[i % colores.length]);
            g2d.fillRect(x, y, ancho, altura);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, ancho, altura);

            // Dibujar etiqueta
            String etiqueta = "T" + p.getIdTarea() + "-" + p.getTamanio() + "K";
            FontMetrics fm = g2d.getFontMetrics();
            int anchoEtiqueta = fm.stringWidth(etiqueta);
            g2d.setColor(Color.BLACK);
            g2d.drawString(etiqueta, x + (ancho - anchoEtiqueta) / 2, y + altura / 2);
        }
    }
    private void dibujarTitulos(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawString("FRAGMENTACION EXTERNA: " + fragmentacionExterna, 5, 20);
        g2d.drawString("POLITICA: " + politica, 5, 40);
    }


/*
    falta los valores del eje y , que se basa en el tamaño de la memoriaPrincipal si el tamaño es de 130 el eje y su maximo es 130
    y tiene que ir de 10 en 10, es decir 10k,20k,30k,40k,50k,60k,70k,80k,90k,100k,110k,120k,130k.
            y las particiones tienes que adapter al alto del eje y la tarea 1 ocupa 20k tiene que ocupar esos 20k.
            y ademas no se tiene que pasar de los limites del frame


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int chartWidth = getWidth();
        int chartHeight = getHeight();

        // Ajustar offsets y dimensiones
        int xOffset = 70;  // Aumentado para dar más espacio a las etiquetas del eje Y
        int yOffset = 50;
        int xAxisHeight = chartHeight - 100;

        // Configurar el rango del eje Y
        int maxMemY =getTamanioMemoria() ;  // Máximo valor en Y (130k)
        int intervalY = 10; // Intervalo entre marcas del eje Y
        double yScale = (double)(xAxisHeight - yOffset) / maxMemY;  // Factor de escala para convertir valores de memoria a píxeles

        // Dibujar eje Y
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xOffset, yOffset, xOffset, xAxisHeight);
        for (int i = 0; i <= maxMemY; i += intervalY) {
            int y = xAxisHeight - (int)(i * yScale);
            g2d.drawLine(xOffset - 5, y, xOffset, y);
            g2d.drawString(i + "k", xOffset - 40, y + 5);
        }

        // Configurar el rango del eje X
        int maxX = getTiemporRetorno();  // Máximo valor en X
        double xScale = (double)(chartWidth - xOffset - 50) / maxX;  // Factor de escala para convertir tiempo a píxeles

        // Dibujar eje X
        g2d.drawLine(xOffset, xAxisHeight, chartWidth - 50, xAxisHeight);
        for (int i = 0; i <= maxX; i++) {
            int x = xOffset + (int)(i * xScale);
            g2d.drawLine(x, xAxisHeight, x, xAxisHeight + 5);
            g2d.drawString(String.valueOf(i), x - 5, xAxisHeight + 20);
        }

        // Colores predefinidos para las particiones
        Color[] colors = {
                new Color(31, 119, 180),   // T1 - Azul
                new Color(255, 127, 14),   // T2 - Naranja
                new Color(44, 160, 44),    // T3 - Verde
                new Color(214, 39, 40),    // T4 - Rojo
                new Color(148, 103, 189),  // T5 - Morado
                new Color(140, 86, 75),    // T6 - Marrón
                new Color(227, 119, 194),  // T7 - Rosa
                new Color(127, 127, 127),  // T8 - Gris
                new Color(188, 189, 34),   // T9 - Verde-amarillo
                new Color(23, 190, 207)    // T10 - Turquesa
        };

        // Dibujar las particiones
        for (int i = 0; i < particiones.size(); i++) {
            Particion p = particiones.get(i);

            // Calcular dimensiones y posición
            double altura = p.getTamanio() * yScale;
            double y = xAxisHeight - (p.getGraficarParticion() * yScale) - altura;
            double ancho = (p.getTiempoFinalizacion() - p.getTiempoInicio()) * xScale;
            double x = xOffset + (p.getTiempoInicio() * xScale);

            // Dibujar el rectángulo de la partición
            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect((int)x, (int)y, (int)ancho, (int)altura);
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int)x, (int)y, (int)ancho, (int)altura);

            // Agregar la etiqueta centrada
            String label = "T" + p.getIdTarea() + "-" + p.getTamanio() + "K";
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            int labelHeight = fm.getHeight();
            g2d.setColor(Color.BLACK);
            g2d.drawString(label,
                    (int)(x + (ancho - labelWidth) / 2),
                    (int)(y + (altura + labelHeight) / 2));
        }

        // Dibujar títulos
        g2d.setColor(Color.BLACK);
        g2d.drawString("FRAGMENTACION EXTERNA: " + fragmentacionExterna, 5, 20);
        g2d.drawString("POLITICA: " + politica, 5, 40);
    }



*/
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
