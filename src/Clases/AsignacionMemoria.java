package Clases;


import java.util.ArrayList;
import java.util.List;

public class AsignacionMemoria {


    
    private int ultimaParticionIndex = 0; // indice de la ultima particion asignada
    public int  tamanoRestante = 0;

    public int fragmentacionExterna = 0;
    public int tiempoActual = 0;



    public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                particionActual.setTamanio(particionActual.getTamanio() + particionSiguiente.getTamanio());
                listaParticiones.remove(i + 1);  // Eliminar partición unificada
                i--;  // Volver a verificar la partición unificada
            }
        }
        return listaParticiones;
    }



    //Significado estados: true libre / false ocupada
    public List<Particion> firstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoActual, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion,Resultado resultado) {
        int fragmentacionExterna = 0;
        int indiceProceso = 0;
        List<Particion> particiones = new ArrayList<>();

        while (!listaProcesos.isEmpty()) {
            Proceso trabajoActual = listaProcesos.get(0);
            System.out.println("Trabajo: " + trabajoActual.getNombre() + " esperando partición");
            System.out.println("Tiempo actual: " + tiempoActual);

            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            listaParticiones = unificarParticiones(listaParticiones);

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: " + particion);
            }

            boolean carga = true;
            int i = 0;

            while (carga && i < listaParticiones.size()) {
                Particion particion = listaParticiones.get(i);

                if (particion.getEstado() && particion.getTamanio() >= trabajoActual.getTamanio()) {
                    carga = false;
                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;

                    if (particion.getTamanio() == trabajoActual.getTamanio()) {
                        Particion particionX = new Particion(
                                i,
                                tiempoInicio,
                                trabajoActual.getTamanio(),
                                false,
                                tiempoInicio + trabajoActual.getDuracion() + tiempoLiberacion

                        );
                        System.out.println("El trabajo " + trabajoActual.getNombre() + " encontró partición");
                        System.out.println(particionX);
                        particiones.add(particionX);
                        listaParticiones.set(i, particionX);
                    } else {
                        Particion particionX = new Particion(
                                i,
                                tiempoInicio,
                                trabajoActual.getTamanio(),
                                false,
                                tiempoInicio + trabajoActual.getDuracion() + tiempoLiberacion

                        );
                        particiones.add(particionX);
                        listaParticiones.add(i + 1, particionX);

                        Particion particionSobrante = new Particion(
                                -1,
                                -1,
                                particion.getTamanio() - trabajoActual.getTamanio(),
                                true,
                                -1
                        );
                        listaParticiones.add(i + 2, particionSobrante);

                        listaParticiones.remove(i);
                        System.out.println("El trabajo " + trabajoActual.getNombre() + " encontró partición");
                    }
                    listaProcesos.remove(0);
                }
                i++;
            }

            // Calcular fragmentación externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    fragmentacionExterna += particion.getTamanio();
                }
            }
            System.out.println("Fragmentación externa: " + fragmentacionExterna);


            tiempoActual++;
            System.out.println("Longitud de la lista de trabajos: " + listaProcesos.size());
            System.out.println("------------------------------");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Índice de fragmentación externa es: " + fragmentacionExterna);
        return particiones;
    }

    public Particion bestFit(List<Particion> listaParticiones, List<Proceso> listaprocesos, int tiempoActual, int tiempoSeleccion, int tiempocargaPromedio, int tiempoLiberacion) {
      /* 
        Particion mejorParticion = null;
        int menorDiferencia = Intege     for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: [" + particion + "]");
            }     for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: [" + particion + "]");
            }r.MAX_VALUE; // Inicialmente, la mayor diferencia posible

        // Buscar la mejor particion disponible
        for (Particion particion : listaParticiones) {
            int diferencia = particion.getTamanio() - proceso.getTamanio();
            // Encontrar la particion más pequena que aún puede contener el Proceso
            if (!particion.getEstado() && diferencia >= 0 && diferencia < menorDiferencia) {
                mejorParticion = particion;
                menorDiferencia = diferencia;
            }
        }

        // Si encontramos una particion adecuada, la asignamos al Proceso
        if (mejorParticion != null) {
            mejorParticion.setEstado(false);
            mejorParticion.setTamanio(proceso.getTamanio());
        }
        return mejorParticion;
       */
       return null;
}


    public Particion nextFit(List<Particion>listaParticiones,List<Proceso> procesos, int tamanioMemoria, int tiempoSeleccion, int tiempocargaPromedio, int tiempoLiberacion){
       /* 
        int n = listaParticiones.size(); // Numero total de particiones
        int comienzoIndex = ultimaParticionIndex; // Comienza desde la ultima particion asignada

        // Recorre las particiones desde la última asignada hasta el final
        for (int i = comienzoIndex; i < n; i++) {
            if (!listaParticiones.get(i).getEstado() && listaParticiones.get(i).getTamanio() >= proceso.getTamanio()) {
                listaParticiones.get(i).setEstado(true);
                listaParticiones.get(i).setTamanio(proceso.getTamanio());

                // Actualizar el indice de la última particion asignada
                ultimaParticionIndex = i;
                return listaParticiones.get(i); // Retorna la particion asignada
            }
        }

        // Si no se encuentra una particin, continuar la busqueda desde el principio hasta la ultima particin asignada
        for (int i = 0; i < comienzoIndex; i++) {
            if (!listaParticiones.get(i).getEstado() && listaParticiones.get(i).getTamanio() >= proceso.getTamanio()) {
                listaParticiones.get(i).setEstado(true);
                listaParticiones.get(i).setTamanio(proceso.getTamanio());

                // Actualizar el indice de la ultima particion asignada
                ultimaParticionIndex = i;
                return listaParticiones.get(i);
            }
        }
            */

        return null; // No hay particion disponible
    }


    public Particion worstFit(List<Particion>listaParticiones,List<Proceso> procesos, int tamanioMemoria, int tiempoSeleccion, int tiempocargaPromedio, int tiempoLiberacion) {
      /*
        Particion peorParticion = null;
        int mayorDiferencia = -1; // Inicialmente, la diferencia más baja posible

        // Buscar la peor particion disponible
        for (Particion particion : listaParticiones) {
            int diferencia = particion.getTamanio() - Proceso.getTamanio();
            // Encontrar la particion mas grande que  puede contener el Proceso
            if (!particion.isOcupada() && diferencia >= 0 && diferencia > mayorDiferencia) {
                peorParticion = particion;
                mayorDiferencia = diferencia;
            }
        }

        // Si encontramos una particion se le asignam el Proceso
        if (peorParticion != null) {
            peorParticion.setOcupada(true);
            peorParticion.settamanio(Proceso.getTamanio());
        }

        return peorParticion;
        */
        return null;

    }
/*
    // Obtener el objeto con el mayor tiempoFinalizacion
    Particion objetoMayorTiempoFinalizacion = Collections.max(listaParticiones, (p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()));

    // Aquí se define la longitud X como el tiempoFinalizacion del objeto con mayor tiempoFinalizacion
    int longitudX = objetoMayorTiempoFinalizacion.getTiempoFinalizacion();
*/

}