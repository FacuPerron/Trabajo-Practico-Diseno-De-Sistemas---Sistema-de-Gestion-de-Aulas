/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.dtos;

import dds.entidades.Aula;
import dds.enumerados.DiaSemana;
import java.time.LocalTime;

/**
 *
 * @author Lenovo
 */
public class PeriodicaItemDTO {
    
    private int aula;
    private LocalTime horario; 
    private LocalTime horaFin;
    private DiaSemana dia;
    
    public PeriodicaItemDTO(int aula, LocalTime hor, LocalTime horaFin, DiaSemana d){
        this.aula = aula;
        this.horario = hor;
        this.horaFin = horaFin;
        this.dia = d;
    }
    public PeriodicaItemDTO(int aula, LocalTime hor, DiaSemana d){
        this.aula = aula;
        this.horario = hor;
        this.dia = d;
    }
    public PeriodicaItemDTO(){
        
    }

    public int getAula() {
        return aula;
    }

    public void setAula(int aula) {
        this.aula = aula;
    }
    
    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public LocalTime getHoraFin() {
        return horaFin;
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
    
    public LocalTime horarioFin(LocalTime horaInicio,int duracion){
        return horaInicio.plusMinutes(duracion);
    }
}
