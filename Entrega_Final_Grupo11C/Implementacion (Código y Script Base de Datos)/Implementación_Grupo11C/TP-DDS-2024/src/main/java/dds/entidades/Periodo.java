/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.entidades;

import dds.enumerados.TipoPeriodo;
import java.time.LocalDate;

/**
 *
 * @author augus
 */
public class Periodo {
    private int id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private TipoPeriodo tipo;

    public Periodo(int id, LocalDate fechaInicio, LocalDate fechaFin, TipoPeriodo tipo) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public TipoPeriodo getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeriodo tipo) {
        this.tipo = tipo;
    }
    
    
    
    
    
}

