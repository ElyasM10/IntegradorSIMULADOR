package Clases;

import java.util.List;

public class Resultado {
    public final int LongitudX;
    public final List<Particion> ListaParticiones;
    public final int fragmentacion;

    public Resultado(int LongitudX, List<Particion> ListaParticiones, int fragmentacion) {
        this.LongitudX = LongitudX;
        this.ListaParticiones = ListaParticiones;
        this.fragmentacion = fragmentacion;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "LongitudX=" + LongitudX +
                ", ListaParticiones=" + ListaParticiones +
                ", fragmentacion=" + fragmentacion +
                '}';
    }
}