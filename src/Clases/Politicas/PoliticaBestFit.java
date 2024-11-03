package Clases.Politicas;

import Clases.Particion;
import Clases.Proceso;
import Clases.Resultado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PoliticaBestFit {



    public PoliticaBestFit(){}


    public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                int tamanioUnificado = particionActual.getTamanio() + particionSiguiente.getTamanio();

                // Crear una nueva partición unificada
                Particion nuevaParticion = new Particion(-1,  tamanioUnificado, true, -1,0,-1);
                listaParticiones.set(i, nuevaParticion);  // Reemplazar la partición actual por la unificada

                // Eliminar la partición siguiente
                listaParticiones.remove(i + 1);
            }else{
                i++;
            }
        }
        return listaParticiones;
    }

    public int calcularGraficoParticion(List<Particion> listaParticiones,Particion particion ,int graficarParticion) {
        for (Particion part : listaParticiones) {
            if (part.getEstado()) {
                graficarParticion += part.getTamanio();
            }
            if (part == particion) {
                // Rompe el bucle al llegar a la partición actual
                break;
            }
        }
        return graficarParticion;
    }


    public Resultado bestFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {


        List<Particion> particiones = new ArrayList<>();
        int indice = 0;
        int tiempoActual = 0;
        int fragmentacionExterna = 0;

        while (!listaProcesos.isEmpty())  {
            Proceso ProcesoActual = listaProcesos.get(indice);
            System.out.println("Proceso: " + ProcesoActual.getNombre() + " esperando partición, Tamanio: "+ProcesoActual.getTamanio());
            System.out.println("Tiempo actual: " + tiempoActual);

            // Verificar si alguna particion debe liberarse en el tiempo actual
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            // Unificar particiones libres
            listaParticiones = unificarParticiones(listaParticiones);

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones Disponibles: [" + particion + "]");
            }

            // Crear lista filtrada de particiones libres
            List<Particion> listaParticionesLibres = new ArrayList<>();
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    listaParticionesLibres.add(particion);
                }
            }

            // Ordenar la lista de particiones libres por tamaño ascendente
            listaParticionesLibres.sort(Comparator.comparingInt(Particion::getTamanio));

            // Buscar partición adecuada (Best Fit)
            boolean carga = true;
            int i = 0;
            while (carga && i < listaParticionesLibres.size()) {
                Particion particionAdecuada = listaParticionesLibres.get(i);

                if (particionAdecuada.getTamanio() >= ProcesoActual.getTamanio()) {
                    carga = false;  // Se encontró una partición adecuada

                    // Buscar la partición en la lista original
                    Particion particion = listaParticiones.stream()
                            .filter(p -> p.getId() == particionAdecuada.getId())
                            .findFirst()
                            .orElse(null);



                    int graficarParticion = 0;
                    graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                    int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;
                    Particion particionEncontrada = new Particion(
                            tiempoInicio,
                            ProcesoActual.getTamanio(),
                            false,
                            tiempoFinalizacion,
                            graficarParticion,
                            ProcesoActual.getID()
                    );
                    System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición: " + particionEncontrada);

                    particiones.add( particionEncontrada);
                    listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                    listaParticiones.remove(particion);

                    if (particion.getTamanio() > ProcesoActual.getTamanio()) {

                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        Particion particionLibre = new Particion(
                                -1,
                                particion.getTamanio() - ProcesoActual.getTamanio(),
                                true,
                                -1,
                                0,
                                -1

                        );
                        listaParticiones.add(listaParticiones.indexOf(particionEncontrada) + 1, particionLibre);
                        System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición.");
                    }
                    listaProcesos.remove(indice);
                }
                i++;
            }


            for (Particion particion : listaParticiones) {
                if (particion.getEstado() && !listaProcesos.isEmpty()) {
                    fragmentacionExterna += particion.getTamanio();
                    System.out.println("Fragmentación externa: " + fragmentacionExterna);
                }
            }

            // Incrementar el tiempo actual
            tiempoActual++;
            System.out.println("Longitud de la lista de Procesos: " + listaProcesos.size());
            System.out.println("------------------------------");
            // Simular retardo de 1 segundo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  // Pausa de 1 segundo
        }

        Particion particionConMayorTiempo = listaParticiones != null && !listaParticiones.isEmpty()
                ? listaParticiones.stream()
                .max((p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()))
                .orElse(null)
                : null;

        int longitud = particionConMayorTiempo != null
                ? particionConMayorTiempo.getTiempoFinalizacion()
                : tiempoActual;




        System.out.println("Fragmentación externa TOTAL: " + fragmentacionExterna);
        resultado.setlistaDeParticiones(particiones);
        resultado.setLongitudTrabajo(longitud);
        return resultado;
    }


}