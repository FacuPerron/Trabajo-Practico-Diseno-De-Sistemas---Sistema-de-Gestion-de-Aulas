/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.entidades;

import dds.enumerados.TipoAula;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author augus
 */
public class Reserva{
    /*
    Hacer constructores para
    1) sin items, para el registro
    2) con items segun el tipo
    
    Aplicar ifs en los metodos para que no haya
    que fijarse a cada rato si es esporadica o no.
    Como en agregarItem
    */
    
    private int id;
    private String mailDeContacto;
    private int cantidadDeAlumnos;
    private String idCurso;
    private String nomApelDocente;
    private TipoAula tipoAula;
    private LocalDate fechaReserva;
    private boolean esporadica; //para ahorrar dolores de cabeza
    
    //relaciones
    private Bedel bedelResponsable;
    private Periodo periodo;
    private List<EsporadicaItem> itemsEsporadica;
    private List<PeriodicaItem> itemsPeriodica;
    
    public void agregarItem(PeriodicaItem pi){
        itemsPeriodica.add(pi);
    }
    public void agregarItem(EsporadicaItem ei){
        itemsEsporadica.add(ei);
    }
    
    public Reserva(int id, String mailDeContacto, int cantidadDeAlumnos, String idCurso,String nomApelDocente, TipoAula tipoAula, LocalDate fechaReserva, boolean esporadica, Bedel bedelResponsable, Periodo periodo, List<EsporadicaItem> itemsEsporadica, List<PeriodicaItem> itemsPeriodica) {
        this.id = id;
        this.mailDeContacto = mailDeContacto;
        this.cantidadDeAlumnos = cantidadDeAlumnos;
        this.idCurso = idCurso;
        this.nomApelDocente = nomApelDocente;
        this.tipoAula = tipoAula;
        this.fechaReserva = fechaReserva;
        this.esporadica = esporadica;
        this.bedelResponsable = bedelResponsable;
        this.periodo = periodo;
        this.itemsEsporadica = itemsEsporadica;
        this.itemsPeriodica = itemsPeriodica;
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

    public String getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getNomApelDocente() {
        return nomApelDocente;
    }

    public void setNomApelDocente(String nomApelDocente) {
        this.nomApelDocente = nomApelDocente;
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

    public Bedel getBedelResponsable() {
        return bedelResponsable;
    }

    public void setBedelResponsable(Bedel bedelResponsable) {
        this.bedelResponsable = bedelResponsable;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public List<EsporadicaItem> getItemsEsporadica() {
        return itemsEsporadica;
    }

    public void setItemsEsporadica(List<EsporadicaItem> itemsEsporadica) {
        this.itemsEsporadica = itemsEsporadica;
    }

    public List<PeriodicaItem> getItemsPeriodica() {
        return itemsPeriodica;
    }

    public void setItemsPeriodica(List<PeriodicaItem> itemsPeriodica) {
        this.itemsPeriodica = itemsPeriodica;
    }
    
    
    
}
