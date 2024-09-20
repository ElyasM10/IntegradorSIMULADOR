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
    public List<Particion> firstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoActual, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {
        fragmentacionExterna = 0;
        int indiceProceso = 0;

        while (indiceProceso < listaProcesos.size()) {
            Proceso proceso = listaProcesos.get(indiceProceso);
            System.out.println("La tarea: " + proceso.getNombre() + " está en la cola, tamanio: " + proceso.getTamanio());
            System.out.println("Tiempo: " + tiempoActual);

            // Liberar particiones que han terminado su tiempo
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);  // Liberar partición
                    System.out.println("Partición liberada: " + particion);
                }
            }

            // Unificar particiones antes de intentar asignar
            listaParticiones = unificarParticiones(listaParticiones);

            // Verificar si hay particiones disponibles
            boolean hayParticionDisponible = listaParticiones.stream().anyMatch(p -> p.getEstado() && p.getTamanio() >= proceso.getTamanio());

            if (!hayParticionDisponible) {
                // No hay particiones disponibles, incrementa el tiempo y continua
                tiempoActual++;
                continue; // Repetir el ciclo con el mismo proceso
            }

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: [" + particion + "]");
            }

            // Intentar asignar el proceso a una partición libre
            for (int i = 0; i < listaParticiones.size(); i++) {
                Particion particion = listaParticiones.get(i);

                if (particion.getEstado() && particion.getTamanio() >= proceso.getTamanio()) {
                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;

                    if (particion.getTamanio() == proceso.getTamanio()) {
                        // Si la partición tiene el tamaño exacto del proceso
                        System.out.println("El trabajo " + proceso.getNombre() + " encontró una partición");
                        Particion particionAsignada = new Particion(i, tiempoInicio, proceso.getTamanio(), false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                        listaParticiones.set(i, particionAsignada);
                    } else {
                        // Si la partición es mayor que el proceso, dividirla
                        Particion particionAsignada = new Particion(i, tiempoInicio, proceso.getTamanio(), false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                        Particion particionRestante = new Particion(i, -1, particion.getTamanio() - proceso.getTamanio(), true, -1);

                        listaParticiones.set(i, particionAsignada);
                        listaParticiones.add(i + 1, particionRestante);  // Insertar partición sobrante
                    }

                    System.out.println("El trabajo " + proceso.getNombre() + " fue asignado.");
                    listaProcesos.remove(indiceProceso); // Elimina el proceso de la cola
                    break; // Salir del bucle, ya se asignó el proceso
                }
            }

            // Calcular fragmentación externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    fragmentacionExterna += particion.getTamanio();
                }
            }

            // Incrementar tiempo para el siguiente ciclo
            tiempoActual++;
        }

        System.out.println("Fragmentación externa: " + fragmentacionExterna);
        resultado.setFragmentacion(fragmentacionExterna);
        return listaParticiones;
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