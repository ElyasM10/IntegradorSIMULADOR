package Clases.Politicas;

import Clases.Particion;
import Clases.Proceso;
import Clases.Resultado;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PoliticaWorstFit {

    public PoliticaWorstFit() {
    }

    public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                int tamanioUnificado = particionActual.getTamanio() + particionSiguiente.getTamanio();

                // Crear una nueva partición unificada
                Particion nuevaParticion = new Particion(-1, tamanioUnificado, true, -1, 0,-1);
                listaParticiones.set(i, nuevaParticion);  // Reemplazar la partición actual por la unificada

                // Eliminar la partición siguiente
                listaParticiones.remove(i + 1);
            } else {
                i++;
            }
        }
        return listaParticiones;
    }

    public int calcularGraficoParticion(List<Particion> listaParticiones, Particion particion, int graficarParticion) {
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


    public Resultado worstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {

        System.out.println("Estoy en la politica worstFit ");

        List<Particion> particiones = new ArrayList<>();
        int index = 0;
        int tiempoActual = 0;
        int fragmentacionExterna = 0;


        while (!listaProcesos.isEmpty()) {
            Proceso ProcesoActual = listaProcesos.get(index);
            System.out.println("Proceso: " + ProcesoActual.getNombre() + " esperando particion");
            System.out.println("Tiempo actual: " + tiempoActual);

            // Buscar particiones con tiempo de finalización igual al tiempo actual y ponerlas libres
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            listaParticiones = unificarParticiones(listaParticiones);

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: " + particion);
            }

            List<Particion> listaParticionesLibres = new ArrayList<>();
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    listaParticionesLibres.add(particion);
                }
            }

            listaParticionesLibres.sort(Comparator.comparingInt(Particion::getTamanio).reversed());


            int i = 0;
            boolean carga = true;

            // Buscar la primera partición libre que pueda abarcar el tamaño necesario
            while (carga && i < listaParticionesLibres.size()) {
                Particion particionL = listaParticionesLibres.get(i);
                if (particionL.getTamanio() >= ProcesoActual.getTamanio()) {
                    carga = false;

                    if (particionL.getTamanio() == ProcesoActual.getTamanio()) {
                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

                        int graficarParticion = 0;

                        graficarParticion = calcularGraficoParticion(listaParticiones, particionL, graficarParticion);

                        Particion particionEncontrada = new Particion(
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion,
                                ProcesoActual.getID()
                        );

                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particionL), particionEncontrada);
                        listaParticiones.remove(particionL);
                        listaProcesos.remove(ProcesoActual);

                    } else if (particionL.getTamanio() > ProcesoActual.getTamanio()) {
                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;
                        int graficarParticion = 0;

                        graficarParticion = calcularGraficoParticion(listaParticiones, particionL, graficarParticion);

                            // Crear partición ocupada
                            Particion particionEncontrada = new Particion(
                                    tiempoInicio,
                                    ProcesoActual.getTamanio(),
                                    false,
                                    tiempoFinalizacion,
                                    graficarParticion,
                                    ProcesoActual.getID()
                            );
                            particiones.add(particionEncontrada);
                            listaParticiones.set(listaParticiones.indexOf(particionL), particionEncontrada); // Reemplaza la partición ocupada

                            // Crear partición libre
                            Particion particionLibre = new Particion(
                                    -1,
                                    particionL.getTamanio() - ProcesoActual.getTamanio(),
                                    true,
                                    -1,
                                    0,
                                    -1
                            );

                            // Añadir partición libre
                            listaParticiones.add(listaParticiones.indexOf(particionEncontrada) + 1, particionLibre);
                            listaProcesos.remove(ProcesoActual);
                        }
                }
                i++;
            }
            // Calcular fragmentación externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado() && !listaProcesos.isEmpty()) {
                    fragmentacionExterna += particion.getTamanio();

                }
            }

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles después de actualizar: [" + particion + "]");
            }



            System.out.println("Fragmentacion externa: " + fragmentacionExterna);
            tiempoActual++;

            System.out.println("Longitud de la lista de Procesos: " + listaProcesos.size());
            System.out.println("------------------------------");


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  // Pausa de 1 segundo

            Particion particionConMayorTiempo = listaParticiones != null && !listaParticiones.isEmpty()
                    ? listaParticiones.stream()
                    .max((p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()))
                    .orElse(null)
                    : null;

            int longitud = particionConMayorTiempo != null
                    ? particionConMayorTiempo.getTiempoFinalizacion()
                    : tiempoActual;


            System.out.println("fragmentación externa TOTAL: " + fragmentacionExterna);
            resultado.setlistaDeParticiones(particiones);
            resultado.setLongitudTrabajo(longitud);


        }
        return resultado;
    }

/*
    Como deberia ser:
    particiones: [id: 15,estado: False. tiempo Inicio:17 Tiempo Finalizacion:29 tamaño:30 idTrabajo:7]
    particiones: [id: 4,estado: False. tiempo Inicio:5 Tiempo Finalizacion:22 tamaño:20 idTrabajo:2]
    particiones: [id: 16,estado: False. tiempo Inicio:18 Tiempo Finalizacion:23 tamaño:10 idTrabajo:8]
    particiones: [id: 20,estado: True. tiempo Inicio:-1 Tiempo Finalizacion:-1 tamaño:30 idTrabajo:-1]
    particiones: [id: 13,estado: False. tiempo Inicio:16 Tiempo Finalizacion:26 tamaño:20 idTrabajo:6]
    particiones: [id: 18,estado: False. tiempo Inicio:19 Tiempo Finalizacion:26 tamaño:10 idTrabajo:9]
    particiones: [id: 19,estado: True. tiempo Inicio:-1 Tiempo Finalizacion:-1 tamaño:10 idTrabajo:-1]

    como lo esta haciendo el codigo en java:
    Particiones disponibles después de actualizar: [Particion{id=15, tiempoInicio=17, tamanio=30, estado=false, tiempoFinalizacion=29, graficarParticion=30,idTarea=7}]
    Particiones disponibles después de actualizar: [Particion{id=4, tiempoInicio=5, tamanio=20, estado=false, tiempoFinalizacion=22, graficarParticion=0,idTarea=2}]
    Particiones disponibles después de actualizar: [Particion{id=16, tiempoInicio=18, tamanio=10, estado=false, tiempoFinalizacion=23, graficarParticion=0,idTarea=8}]
    Particiones disponibles después de actualizar: [Particion{id=17, tiempoInicio=-1, tamanio=10, estado=true, tiempoFinalizacion=-1, graficarParticion=0,idTarea=-1}]
    Particiones disponibles después de actualizar: [Particion{id=20, tiempoInicio=23, tamanio=20, estado=false, tiempoFinalizacion=33, graficarParticion=30,idTarea=10}]
    Particiones disponibles después de actualizar: [Particion{id=13, tiempoInicio=16, tamanio=20, estado=false, tiempoFinalizacion=26, graficarParticion=0,idTarea=6}]
    Particiones disponibles después de actualizar: [Particion{id=18, tiempoInicio=19, tamanio=10, estado=false, tiempoFinalizacion=26, graficarParticion=0,idTarea=9}]
    Particiones disponibles después de actualizar: [Particion{id=19, tiempoInicio=-1, tamanio=10, estado=true, tiempoFinalizacion=-1, graficarParticion=0,idTarea=-1}]

    hay una particion de mas
*/

}


