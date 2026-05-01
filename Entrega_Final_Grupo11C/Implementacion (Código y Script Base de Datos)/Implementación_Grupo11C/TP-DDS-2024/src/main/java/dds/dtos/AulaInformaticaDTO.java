/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.dtos;

import dds.enumerados.TipoPizarron;
import dds.enumerados.Ubicacion;

/**
 *
 * @author augus
 */
public class AulaInformaticaDTO extends AulaDTO {
    private int cantidadPCs;
    private boolean cañon;

    public boolean isCañon() {
        return cañon;
    }

    public void setCañon(boolean cañon) {
        this.cañon = cañon;
    }

    public int getCantidadPCs() {
        return cantidadPCs;
    }

    public void setCantidadPCs(int cantidadPCs) {
        this.cantidadPCs = cantidadPCs;
    }

    public AulaInformaticaDTO(int id, int capacidad, Ubicacion ubicacion, boolean habilitada, boolean aireAcondicionado, TipoPizarron tipoPizarron, int cantidadPCs,boolean cañon) {
        super(id, capacidad, ubicacion, habilitada, aireAcondicionado, tipoPizarron);
        this.cantidadPCs = cantidadPCs;
        this.cañon = cañon;
    }
    
    
}
