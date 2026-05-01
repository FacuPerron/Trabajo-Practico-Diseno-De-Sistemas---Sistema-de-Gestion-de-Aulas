/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.util;

import dds.enumerados.DiaSemana;
import java.time.DayOfWeek;
import java.time.LocalDate;


/**
 *
 * @author Lenovo
 */
public class FechaUtil {
    
    public static DiaSemana diaDeSemana(LocalDate fecha){
        
        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        
        switch (dayOfWeek) {
            case MONDAY:
                return DiaSemana.Lunes;
            case TUESDAY:
                return DiaSemana.Martes;
            case WEDNESDAY:
                return DiaSemana.Miercoles;
            case THURSDAY:
                return DiaSemana.Jueves;
            case FRIDAY:
                return DiaSemana.Viernes;
            case SATURDAY:
                return DiaSemana.Sabado;
            default:
                throw new IllegalStateException();
        }
    }
}
