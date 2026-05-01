/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.dtos;

import dds.entidades.Aula;
import dds.enumerados.DiaSemana;
import dds.util.FechaUtil;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Lenovo
 */
public class EsporadicaItemDTO {
    private int aula;
    private LocalTime horario; 
    private LocalTime horaFin;
    LocalDate fecha;
    DiaSemana diaSemana;
    
    public EsporadicaItemDTO(int idaula, LocalTime horario, LocalTime horaFin, LocalDate fecha){
        this.aula = idaula;
        this.horario = horario;
        this.horaFin = horaFin;
        this.fecha = fecha;
        this.diaSemana = FechaUtil.diaDeSemana(this.fecha);
    }
    
    public EsporadicaItemDTO(int idaula, LocalTime horario, LocalDate fecha){
        this.aula = idaula;
        this.horario = horario;
        this.fecha = fecha;
        this.diaSemana = FechaUtil.diaDeSemana(this.fecha);
    }
    public EsporadicaItemDTO(){
        
    }
    
    public void setDiaSemana(){
        this.diaSemana = FechaUtil.diaDeSemana(this.fecha);
    }
    
    public DiaSemana getDiaSemana(){
        return diaSemana;
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
    
    

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        this.diaSemana = FechaUtil.diaDeSemana(this.fecha);
    }
    
    public LocalTime horarioFin(LocalTime horaInicio,int duracion){
        return horaInicio.plusMinutes(duracion);
    }
}
