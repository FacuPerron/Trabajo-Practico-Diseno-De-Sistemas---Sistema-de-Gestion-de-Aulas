/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.entidades;

import dds.enumerados.Turno;
import dds.dtos.BedelDTO;
import java.util.List;

/**
 *
 * @author augus
 */
public class Bedel extends Usuario{
    private Turno turno;
    private Boolean activo;
    private List<Reserva> reservas;

    

    public Bedel(Turno turno, Boolean activo, List<Reserva> reservas, int id, String idUsuario, String nombre, String apellido, String email, String password) {
        super(id, idUsuario, nombre, apellido, email, password);
        this.turno = turno;
        this.activo = activo;
        this.reservas = reservas;
    }
    
    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    public Bedel(BedelDTO bdto) {
        super(bdto.getId(), bdto.getIdUsuario(), bdto.getNombre(), bdto.getApellido(), bdto.getEmail(), bdto.getPassword());
        this.turno = bdto.getTurno();
        this.activo = bdto.getActivo();
        //this.reservas = bdto.getReservas();
    }
   
}
