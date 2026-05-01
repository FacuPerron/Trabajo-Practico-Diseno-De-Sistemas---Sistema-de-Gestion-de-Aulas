/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.entidades;

import dds.enumerados.DiaSemana;
import java.time.LocalTime;

/**
 *
 * @author augus
 */
public class PeriodicaItem {
    private Aula aula;
    private LocalTime horaInicio; 
	private LocalTime horaFin; 
    private DiaSemana dia;
    
    public PeriodicaItem(Aula au, LocalTime horaInicio, LocalTime horaFin, DiaSemana d){
        this.aula = au;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.dia = d;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }
	public LocalTime getHoraFin() {
        return horaFin;
    }
	public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
	public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }
    
}
