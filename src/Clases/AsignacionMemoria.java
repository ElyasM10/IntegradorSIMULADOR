package Clases;


import java.util.Collections;
import java.util.List;

public class AsignacionMemoria {


    // Lista de particiones de memoria
    private int ultimaParticionIndex = 0; // indice de la ultima particion asignada
    public int  tamanoRestante = 0;

    public int fragmentacionExterna = 0;
    public int tiempoActual = 0;


    //Significado estados: true libre / false ocupada
    public Particion firstFit(List<Particion> listaParticiones, Proceso proceso, int tiempoActual, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion,Resultado resultado) {

        // Muestro todas las particiones disponibles
        for (Particion particion : listaParticiones) {
            System.out.println("Particiones disponibles: [" + particion + "]");
        }


        int i = 0;
        boolean carga = true;

        while (carga && i < listaParticiones.size()) {
            Particion particion = listaParticiones.get(i);

            // Verifico si la partición esta libre y tiene suficiente tamanio
            if (particion.getEstado() && particion.getTamanio() >= proceso.getTamanio()) {
                // Se encontró una partición que puede acomodar el proceso

                if (particion.getTamanio() == proceso.getTamanio()) {// Verifico si la particion es exactamente del tamanio del proceso

                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;

                    Particion particionX = new Particion(i, tiempoInicio, proceso.getTamanio(), false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                    System.out.println("El trabajo " + proceso.getNombre() + " encontro una partición");
                    System.out.println(particionX);

                    // Actualizar la partición original para indicar que está ocupada
                    listaParticiones.set(i, particionX);
                    carga = false; // Detener la búsqueda

                } else if (particion.getTamanio() > proceso.getTamanio()) {
                    //Verifico si la particion es mayor que el tamanio del proceso
                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;

                    Particion particionX = new Particion(i, tiempoInicio, proceso.getTamanio(), false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                    Particion particionSobrante = new Particion(i, -1, particion.getTamanio() - proceso.getTamanio(), true, -1);

                    System.out.println("El trabajo " + proceso.getNombre() + " encontró una partición");
                    System.out.println(particionX);

                    // Actualizar la partición original
                    listaParticiones.set(i, particionX);

                    // Insertar la partición sobrante en la lista
                    listaParticiones.add(i + 1, particionSobrante);
                    carga = false; // Detener la búsqueda
                }
            }

            i++; // Incrementar el índice para verificar la siguiente partición
        }

        // Ahora calculamos  la fragmentacion Externa
        for (Particion particion : listaParticiones) {
            // Si la particion está libre (no esta asignada)
            if (particion.getEstado()) {
                fragmentacionExterna += particion.getTamanio();
            }
        }
        /*

        for (Particion particion : listaParticiones) {
            System.out.println("Particiones disponibles después de actualizar: [" + particion + "]");
        }
         */


        System.out.println("Asignador: Fragmentación externa: " + fragmentacionExterna);

        //Devuelvo la fragmentacion
        resultado.setFragmentacion(fragmentacionExterna);


        // Crear y devolver el resultado como un namedtuple

        //return resultado;
        return !carga ? listaParticiones.get(i - 1) : null;

    }



    public Particion bestFit(List<Particion>listaParticiones,Proceso proceso, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion) {
        Particion mejorParticion = null;
        int menorDiferencia = Integer.MAX_VALUE; // Inicialmente, la mayor diferencia posible

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
    }


    public Particion nextFit(List<Particion>listaParticiones,Proceso proceso, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion){
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

        return null; // No hay particion disponible
    }


    public Particion worstFit(List<Particion>listaParticiones,Proceso proceso, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion) {
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