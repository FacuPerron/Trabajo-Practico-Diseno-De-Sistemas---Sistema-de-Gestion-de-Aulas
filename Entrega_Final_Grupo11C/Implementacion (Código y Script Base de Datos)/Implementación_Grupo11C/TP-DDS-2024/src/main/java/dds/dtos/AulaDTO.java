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
public abstract class AulaDTO {
    private int id;
    private int capacidad;
    private Ubicacion ubicacion;
    private boolean habilitada; 
    private boolean aireAcondicionado;
    private TipoPizarron tipoPizarron;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean getHabilitada() {
        return habilitada;
    }

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }

    public boolean getAireAcondicionado() {
        return aireAcondicionado;
    }

    public void setAireAcondicionado(boolean aireAcondicionado) {
        this.aireAcondicionado = aireAcondicionado;
    }

    public TipoPizarron getTipoPizarron() {
        return tipoPizarron;
    }

    public void setTipoPizarron(TipoPizarron tipoPizarron) {
        this.tipoPizarron = tipoPizarron;
    }

    public AulaDTO(int id, int capacidad, Ubicacion ubicacion, boolean habilitada, boolean aireAcondicionado, TipoPizarron tipoPizarron) {
        this.id = id;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.habilitada = habilitada;
        this.aireAcondicionado = aireAcondicionado;
        this.tipoPizarron = tipoPizarron;
    }
    
    
}
