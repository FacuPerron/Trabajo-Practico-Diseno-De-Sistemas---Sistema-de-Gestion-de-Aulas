/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.entidades;

import dds.enumerados.TipoPizarron;
import dds.enumerados.Ubicacion;
import dds.dtos.AulaSinRecursosDTO;

/**
 *
 * @author augus
 */
public class AulaSinRecursos extends Aula {
    private boolean ventiladores;

    public boolean isVentiladores() {
        return ventiladores;
    }

    public void setVentiladores(boolean ventiladores) {
        this.ventiladores = ventiladores;
    }

    public AulaSinRecursos(int id, int capacidad, Ubicacion ubicacion, boolean habilitada, boolean aireAcondicionado, TipoPizarron tipoPizarron, boolean ventiladores) {
        super(id, capacidad, ubicacion, habilitada, aireAcondicionado, tipoPizarron);
        this.ventiladores = ventiladores;
    }
    public AulaSinRecursos(AulaSinRecursosDTO adto) {
        super(adto);
        this.ventiladores = adto.isVentiladores();
    }
    
}
