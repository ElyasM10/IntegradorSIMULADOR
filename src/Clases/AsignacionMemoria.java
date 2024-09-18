package Clases;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.util.Collections;
import java.util.List;

public class AsignacionMemoria {


    // Lista de particiones de memoria
    private int ultimaParticionIndex = 0; // indice de la ultima particion asignada
    public int  tamanoRestante = 0;

    public int fragmentacionExterna = 0;

/*
    public Particion firstFit(Proceso Proceso,int tamanioMemoria,int tiempoSeleccion,int tiempoCargaPromedio,int tiempoLiberacion) {
        for (Particion particion : particiones) {
            // Verificar si la particion es libre y tiene suficiente espacio para el Proceso
            if (!particion.isOcupada() && particion.gettamanio() >= Proceso.getMemoriaRequerida()) {
                // Marcar la particion actual como ocupada
                particion.setOcupada(true);
                particion.settamanio(Proceso.getMemoriaRequerida());
                return particion; // Retornar la particion asignada
            }
        }
        return null;
    }
*/
//trabajo = proceso
    // Metodo para asignar un trabajo de tamano 'tamanoTrabajo'


    /*
      public Particion firstFit(List<Particion> listaParticiones, Proceso proceso, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion) {
        for (Particion particion : listaParticiones) {
              //True libre y false ocupado
            if (!particion.getEstado() && particion.getTamanio() >= proceso.getTamanio()) {
                // Asignar el proceso a esta particion
                particion.setEstado(true); // Marcar como ocupada
                particion.setTiempoInicio(tiempoSeleccion); // Ajustar tiempo de inicio
                particion.setTiempoFinalizacion(tiempoSeleccion + proceso.getDuracion() + tiempoCargaPromedio + tiempoLiberacion); // Ajustar tiempo de finalizacion
                return particion;
            }
        }
        return null; // No se encontro una particion adecuada
    }
    */
/*
public Particion firstFit(List<Particion> listaParticiones, Proceso proceso, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion) {
    System.out.println("Buscando una particion adecuada para el proceso:");
  //  System.out.println("Tamano del proceso: " + proceso.getTamanio());


    for (int i = 0; i < listaParticiones.size(); i++) {
        Particion particion = listaParticiones.get(i);

     //   System.out.println("Comparando con partición:");
     //   System.out.println("Tamaño de partición: " + particion.getTamanio());
       // System.out.println("Estado de partición (true = ocupada, false = libre): " + (particion.getEstado() ? "Ocupada" : "Libre"));


           int tamanioPar = particion.getTamanio();
           int tamanioProceso = proceso.getTamanio();

        //   int tamanioPar = 130;
        //   int tamanioProceso = 30;


           System.out.println("Particion: "+tamanioPar+" Proceso: "+tamanioProceso);


        if (tamanioPar > tamanioProceso) {

            if (!particion.getEstado()) {
                System.out.println("Partición libre y adecuada encontrada:");
                particion.setEstado(true); // Marcar como ocupada
                particion.setProceso(proceso);
                particion.setTiempoInicio(tiempoSeleccion);
                particion.setTiempoFinalizacion(tiempoSeleccion + proceso.getDuracion() + tiempoCargaPromedio + tiempoLiberacion);

                // Verificar si sobra espacio para crear una nueva partición libre
                tamanoRestante = particion.getTamanio() - proceso.getTamanio();
                if (tamanoRestante > 0) {
                    Particion nuevaParticion = new Particion(listaParticiones.size() + 1, -1, tamanoRestante, true, -1);
                    listaParticiones.add(nuevaParticion); // Crear una nueva partición libre con el espacio sobrante
                    System.out.println("Nueva partición libre creada con tamaño: " + tamanoRestante);
                }

                return particion; // Proceso asignado, salir del método
            }else {
                System.out.println("Partición ocupada, creando una nueva partición con el espacio sobrante...");

                tamanoRestante = particion.getTamanio() - proceso.getTamanio();
                if (tamanoRestante > 0) {
                    Particion nuevaParticion = new Particion(listaParticiones.size() + 1, -1, tamanoRestante, true, -1);
                    listaParticiones.add(nuevaParticion); // Agregar la nueva partición a la lista
                    System.out.println("Nueva partición libre creada con tamaño: " + tamanoRestante);
                }
            }
        } else {
            System.out.println("Partición no adecuada.");
        }

    }

    System.out.println("No se encontro una particion adecuada para el proceso.");
    return null; // No se encontro una particion adecuada
}
    */
    public Particion firstFit(List<Particion> listaParticiones, Proceso proceso, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion) {
        // Mostrar todas las particiones disponibles
        for (Particion particion : listaParticiones) {
            System.out.println("Particiones disponibles: [" + particion + "]");
        }

        int i = 0;
        boolean carga = true;

        while (carga && i < listaParticiones.size()) {
            Particion particion = listaParticiones.get(i);

            // Verificar si la partición está libre y tiene suficiente tamaño
            if (particion.getEstado() && particion.getTamanio() >= proceso.getTamanio()) {
                // Se encontró una partición que puede acomodar el proceso
                int tamanioParticion = particion.getTamanio();
                int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion;

                if (tamanioParticion == proceso.getTamanio()) {
                    // La partición es exactamente del tamaño del proceso
                    Particion particionX = new Particion(i, tiempoInicio, proceso.getTamanio(), false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                    System.out.println("El trabajo " + proceso.getNombre() + " encontró una partición");
                    System.out.println(particionX);

                    // Actualizar la partición original para indicar que está ocupada
                    listaParticiones.set(i, particionX);
                    carga = false; // Detener la búsqueda

                } else if (tamanioParticion > proceso.getTamanio()) {
                    // La partición es mayor que el tamaño del proceso
                    Particion particionX = new Particion(i, tiempoInicio, proceso.getTamanio(), false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                    Particion particionSobrante = new Particion(-1, tiempoInicio + proceso.getDuracion() + tiempoLiberacion, tamanioParticion - proceso.getTamanio(), true, -1);

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

        // Calcular la fragmentación externa
        fragmentacionExterna = listaParticiones.stream()
                .filter(Particion::getEstado)
                .mapToInt(Particion::getTamanio)
                .sum();

        // Mostrar las particiones disponibles después de actualizar
        for (Particion particion : listaParticiones) {
            System.out.println("Particiones disponibles después de actualizar: [" + particion + "]");
        }

        // Mostrar la fragmentación externa
        System.out.println("Fragmentación externa: " + fragmentacionExterna);

        // Obtener el objeto con el mayor tiempoFinalizacion
        Particion objetoMayorTiempoFinalizacion = Collections.max(listaParticiones, (p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()));

        // Aquí se define la longitud X como el tiempoFinalizacion del objeto con mayor tiempoFinalizacion
        int longitudX = objetoMayorTiempoFinalizacion.getTiempoFinalizacion();

        // Crear y devolver el resultado como un namedtuple
        Resultado resultado = new Resultado(longitudX, listaParticiones, fragmentacionExterna);
        //return resultado;
         return null;

    }



    // Mostrar todas las particiones disponibles
      /*
        for (Particion particion : listaParticiones) {
            System.out.println("Particiones disponibles: [" + particion + "]");
        }

        int i = 0;
        boolean carga = true;

        // Iterar sobre las particiones disponibles para encontrar la primera que sirva
        while (carga && i < listaParticiones.size()) {
            Particion particion = listaParticiones.get(i);

            // Verificar si la partición está libre y tiene suficiente tamaño
            if (particion.getEstado() && particion.getTamanio() >= proceso.getTamanio()) {
                // Se encontró una partición que puede acomodar el proceso
                carga = false; // Detener la búsqueda ya que hemos encontrado una partición

               // El tamanioo de la partición es exactamente igual al del proceso
                if (particion.getTamanio() == proceso.getTamanio()) {
                    // Inicializamos la posición Y de la partición
                    int ejeY = 0;
                    for (Particion p : listaParticiones) {
                        if (p.getEstado() && p == listaParticiones.get(listaParticiones.size() - 1)) {
                            break;
                        }
                        if (p == particion) {
                            break;
                        }
                        ejeY += p.getTamanio();
                    }

                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion;
                    // Particion particionX = new Particion(false, tiempoInicio, tiempoInicio + proceso.getDuracion() + tiempoLiberacion, proceso.getTamanio(), ejeY, proceso.getID());
                    Particion particionX = new Particion(i, tiempoInicio, tamanioMemoria, false, tiempoInicio + proceso.getDuracion() + tiempoLiberacion);
                    System.out.println("El trabajo " + proceso.getNombre() + " encontró una partición");
                    System.out.println(particionX);

                    // Agregar la nueva partición a la lista
                    listaParticiones.add(listaParticiones.indexOf(particion), particionX);
                    // Eliminar la partición original
                    listaParticiones.remove(particion);

                    // SITUACIÓN 2: La partición es mayor que el tamaño del proceso
                } else if (particion.getTamanio() > proceso.getTamanio()) {
                    // Inicializamos la posición Y de la partición
                    int ejeY = 0;
                    for (Particion p : listaParticiones) {
                        if (p.getEstado() && p == listaParticiones.get(listaParticiones.size() - 1)) {
                            break;
                        }
                        if (p == particion) {
                            break;
                        }
                        ejeY += p.getTamanio();
                    }

                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion;
                    //  Particion particionX = new Particion(i, tiempoInicio, proceso.getTamanio() + proceso.getDuracion() + tiempoLiberacion, proceso.getTamanio(),);
                    Particion particionX = new Particion(i, tiempoInicio, tamanioMemoria, carga, i);
                    System.out.println("El trabajo " + proceso.getNombre() + " encontró una partición");

                    // Agregar la nueva partición que toma el tamaño del proceso
                    listaParticiones.add(listaParticiones.indexOf(particion) + 1, particionX);

                    // Crear la partición sobrante (libre)
                    Particion particionSobrante = new Particion(-1, -1, particion.getTamanio() - proceso.getTamanio(), true, -1);
                    listaParticiones.add(listaParticiones.indexOf(particion) + 2, particionSobrante);

                    // Eliminar la partición original
                    listaParticiones.remove(particion);
                }
            }
            i++; // Incrementar el índice para verificar la siguiente partición
        }

        // Retornar la partición encontrada o null si no se encontró ninguna
        return !carga ? listaParticiones.get(i - 1) : null;
    }
   */
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



}