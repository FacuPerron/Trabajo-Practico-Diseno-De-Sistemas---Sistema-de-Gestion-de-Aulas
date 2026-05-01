/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.dtos;

import dds.entidades.Bedel;
import dds.entidades.EsporadicaItem;
import dds.entidades.PeriodicaItem;
import dds.entidades.Periodo;
import dds.enumerados.TipoAula;
import dds.enumerados.TipoPeriodo;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class ReservaDTO {
    private int id;
    private String mailDeContacto;
    private int cantidadDeAlumnos;
    private String idCurso;
    private String nombreapellidodocente;

    
    private TipoAula tipoAula;
    private LocalDate fechaReserva;
    private boolean esporadica;
    private String idbedel; //idUsuario

    public ReservaDTO(int id, String mailDeContacto, int cantidadDeAlumnos, String idCurso, String nombreapellidodocente, TipoAula tipoAula, LocalDate fechaReserva, boolean esporadica, String idbedel, TipoPeriodo periodo, List<EsporadicaItemDTO> itemsEsporadica, List<PeriodicaItemDTO> itemsPeriodica) {
        this.id = id;
        this.mailDeContacto = mailDeContacto;
        this.cantidadDeAlumnos = cantidadDeAlumnos;
        this.idCurso = idCurso;
        this.nombreapellidodocente = nombreapellidodocente;
        this.tipoAula = tipoAula;
        this.fechaReserva = fechaReserva;
        this.esporadica = esporadica;
        this.idbedel = idbedel;
        this.periodo = periodo;
        this.itemsEsporadica = itemsEsporadica;
        this.itemsPeriodica = itemsPeriodica;
    }
    
    //relaciones
    //private BedelDTO bedelResponsable;
    private TipoPeriodo periodo;
    private List<EsporadicaItemDTO> itemsEsporadica;
    private List<PeriodicaItemDTO> itemsPeriodica;
    
    public void agregarItem(Object it){
        if(esporadica)itemsEsporadica.add((EsporadicaItemDTO)it);
        else itemsPeriodica.add((PeriodicaItemDTO)it);
    }

    
    public List<EsporadicaItemDTO> getItemsEsporadica() {
        return itemsEsporadica;
    }

    public void setItemsEsporadica(List<EsporadicaItemDTO> itemsEsporadica) {
        this.itemsEsporadica = itemsEsporadica;
    }

    public List<PeriodicaItemDTO> getItemsPeriodica() {
        return itemsPeriodica;
    }

    public void setItemsPeriodica(List<PeriodicaItemDTO> itemsPeriodica) {
        this.itemsPeriodica = itemsPeriodica;
    }

    public String getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreapellidodocente() {
        return nombreapellidodocente;
    }

    public void setNombreapellidodocente(String nombreapellidodocente) {
        this.nombreapellidodocente = nombreapellidodocente;
    }

    public String getIdbedel() {
        return idbedel;
    }

    public void setIdbedel(String idbedel) {
        this.idbedel = idbedel;
    }

    public TipoPeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(TipoPeriodo periodo) {
        this.periodo = periodo;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMailDeContacto() {
        return mailDeContacto;
    }

    public void setMailDeContacto(String mailDeContacto) {
        this.mailDeContacto = mailDeContacto;
    }

    public int getCantidadDeAlumnos() {
        return cantidadDeAlumnos;
    }

    public void setCantidadDeAlumnos(int cantidadDeAlumnos) {
        this.cantidadDeAlumnos = cantidadDeAlumnos;
    }

    public TipoAula getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(TipoAula tipoAula) {
        this.tipoAula = tipoAula;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public boolean isEsporadica() {
        return esporadica;
    }

    public void setEsporadica(boolean esporadica) {
        this.esporadica = esporadica;
    }

    
    
    
}
