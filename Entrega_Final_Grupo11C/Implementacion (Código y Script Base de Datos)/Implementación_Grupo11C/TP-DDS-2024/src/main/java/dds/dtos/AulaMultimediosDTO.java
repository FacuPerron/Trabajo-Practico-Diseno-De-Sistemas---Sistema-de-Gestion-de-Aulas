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
public class AulaMultimediosDTO extends AulaDTO {
    private boolean ventiladores;
    private boolean cañon;
    private boolean computadora;
    private boolean televisor;

    public boolean isCañon() {
        return cañon;
    }

    public void setCañon(boolean cañon) {
        this.cañon = cañon;
    }

    public boolean isComputadora() {
        return computadora;
    }

    public void setComputadora(boolean computadora) {
        this.computadora = computadora;
    }

    public boolean isTelevisor() {
        return televisor;
    }

    public void setTelevisor(boolean televisor) {
        this.televisor = televisor;
    }

    public boolean isVentiladores() {
        return ventiladores;
    }

    public void setVentiladores(boolean ventiladores) {
        this.ventiladores = ventiladores;
    }

    public AulaMultimediosDTO(int id, int capacidad, Ubicacion ubicacion, boolean habilitada, boolean aireAcondicionado, TipoPizarron tipoPizarron, boolean ventiladores, boolean cañon,boolean computadora,boolean televisor) {
        super(id, capacidad, ubicacion, habilitada, aireAcondicionado, tipoPizarron);
        this.ventiladores = ventiladores;
        this.televisor = televisor;
        this.cañon = cañon;
        this.computadora = computadora;
    }
    
    
}