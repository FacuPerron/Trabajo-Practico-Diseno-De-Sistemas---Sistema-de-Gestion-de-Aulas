/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;

import dds.entidades.*;
import dds.enumerados.*;
import dds.util.Pair;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author augus
 */
public interface ReservaDAO {
    
    //cambiar sql time por localtime??
    
    public boolean create(Reserva r, Periodo periodo);
    public boolean createAnual(Reserva r, Periodo periodo1c,Periodo periodo2c);
    public Periodo buscarPeriodoEsporadica(LocalDate fecha);
    public Periodo buscarPeriodoPeriodica(TipoPeriodo tipo);
    
    //consultas de 
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> consultarSuperpuestasPeriodica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
        LocalTime inicioLocal, LocalTime finLocal,int idPeriodo);
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> consultarSuperpuestasEsporadica(LocalDate fecha,int capacidad,TipoAula tipo,
        LocalTime inicioLocal, LocalTime finLocal);
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> consultarSuperpuestasPeriodicaAEsporadica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
        LocalTime inicioLocal, LocalTime finLocal,LocalDate inicio_periodo,LocalDate fin_periodo);
    
    
    public List<Aula> aulasDisponibles(TipoAula tipo, List<Integer> idsAulasSuperpuestas, int capacidad);
    
    
}
