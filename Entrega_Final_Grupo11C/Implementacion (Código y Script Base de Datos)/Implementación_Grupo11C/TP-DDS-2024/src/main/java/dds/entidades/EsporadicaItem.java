/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.entidades;

import dds.enumerados.DiaSemana;
import dds.util.FechaUtil;
import java.time.DayOfWeek;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author augus
 */
public class EsporadicaItem {
    private Aula aula;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    LocalDate fecha;
    DiaSemana diaSemana;
    
    public EsporadicaItem(Aula aula, LocalTime horaInicio, LocalTime horaFin, LocalDate fecha){
        this.aula = aula;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fecha = fecha;
        this.diaSemana = FechaUtil.diaDeSemana(this.fecha);
    }
    
    
    public void setDiaSemana(){
        this.diaSemana = FechaUtil.diaDeSemana(this.fecha);
    }
    
    public DiaSemana getDiaSemana(){
        return diaSemana;
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
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
}
