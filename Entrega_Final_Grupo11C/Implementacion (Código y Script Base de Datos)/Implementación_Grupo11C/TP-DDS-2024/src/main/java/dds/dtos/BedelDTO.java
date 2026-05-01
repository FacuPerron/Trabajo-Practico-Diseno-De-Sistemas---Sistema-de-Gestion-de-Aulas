/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.dtos;

import dds.entidades.Reserva;
import dds.enumerados.Turno;
import java.util.List;

/**
 *
 * @author augus
 */
public class BedelDTO extends UsuarioDTO{

private Turno turno;

private Boolean activo;


    public BedelDTO(int id, String idUsuario, String nombre, String apellido, String email, String password,String confirmacion, Turno turno) {
        super(id, idUsuario, nombre, apellido, email, password,confirmacion);
        this.turno = turno;
        this.activo = true;
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

}
