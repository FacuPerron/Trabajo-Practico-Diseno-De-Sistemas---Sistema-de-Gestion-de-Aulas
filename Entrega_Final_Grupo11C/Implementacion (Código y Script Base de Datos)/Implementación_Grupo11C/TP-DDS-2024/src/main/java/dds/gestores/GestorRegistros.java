/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.gestores;

import dds.conexionesExternas.*;
import dds.daos.AulaDAO;
import dds.daos.FactoryDAO;
import dds.daos.ReservaDAO;
import dds.enumerados.*;
import dds.entidades.*;
import dds.dtos.*;
import dds.excepcionesLogicas.AulaNoEncontradoException;
import dds.excepcionesLogicas.BedelNoEncontradoException;
import dds.excepcionesLogicas.SolapamientoReservasException;
import dds.util.Pair;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author augus
 */
public class GestorRegistros {
     //singleton
    private static GestorRegistros instance;
    public static GestorRegistros getInstance(){
        if(GestorRegistros.instance == null)GestorRegistros.instance =  new GestorRegistros();
        return GestorRegistros.instance;
    }
    private GestorRegistros(){
    }
    
    public boolean crearReservaAnual(ReservaDTO rdto) throws BedelNoEncontradoException, AulaNoEncontradoException{
        Reserva r1 = new Reserva(rdto.getId(), rdto.getMailDeContacto(), rdto.getCantidadDeAlumnos(), rdto.getIdCurso(), rdto.getNombreapellidodocente(), rdto.getTipoAula(), 
                   rdto.getFechaReserva(), rdto.isEsporadica(),null,null,null,null);
        Reserva r2 = new Reserva(rdto.getId(), rdto.getMailDeContacto(), rdto.getCantidadDeAlumnos(), rdto.getIdCurso(), rdto.getNombreapellidodocente(), rdto.getTipoAula(), 
                   rdto.getFechaReserva(), rdto.isEsporadica(),null,null,null,null);
        AulaDAO auladao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO();
        rdto.setPeriodo(TipoPeriodo.PRIMER_CUATRIMESTRE);
        armarReservaPeriodica(r1,rdto,auladao);
        rdto.setPeriodo(TipoPeriodo.SEGUNDO_CUATRIMESTRE);
        armarReservaPeriodica(r2,rdto,auladao);
        Bedel b = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().buscarPorIdUsuario(rdto.getIdbedel());

        r1.setBedelResponsable(b);
        r2.setBedelResponsable(b);
        boolean ret = (FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO()).createAnual(r1,r1.getPeriodo(),r2.getPeriodo());

        return ret;
    }
    
    public boolean crearReserva(ReservaDTO rdto) throws BedelNoEncontradoException, AulaNoEncontradoException{
        if(rdto.getPeriodo()==TipoPeriodo.ANUAL){
            
            /*rdto.setPeriodo(TipoPeriodo.PRIMER_CUATRIMESTRE);
            if(!crearReserva(rdto))return false;
           
            rdto.setPeriodo(TipoPeriodo.SEGUNDO_CUATRIMESTRE);
            if(!crearReserva(rdto)){
                //borrar la primera?
                return false;
            }
            //rdto.setPeriodo(TipoPeriodo.ANUAL);
        */
            return crearReservaAnual(rdto);
        }
        Reserva r = new Reserva(rdto.getId(), rdto.getMailDeContacto(), rdto.getCantidadDeAlumnos(), rdto.getIdCurso(), rdto.getNombreapellidodocente(), rdto.getTipoAula(), 
                   rdto.getFechaReserva(), rdto.isEsporadica(),null,null,null,null);



        AulaDAO auladao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getAulaDAO();

        if(r.isEsporadica()){
            r.setItemsEsporadica(new ArrayList<>());
            
            for(EsporadicaItemDTO idto: rdto.getItemsEsporadica()){
                //crear objeto, anexarlo a r, y buscar aula
                EsporadicaItem it = new EsporadicaItem(null, idto.getHorario(), idto.getHoraFin(), idto.getFecha());
                r.agregarItem(it);

                it.setAula(
                   auladao.buscarPorIdYTipo(idto.getAula(), r.getTipoAula())
                );


            }
        }
        else{
            armarReservaPeriodica(r,rdto,auladao);
        }

        Bedel b = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getBedelDAO().buscarPorIdUsuario(rdto.getIdbedel());

        r.setBedelResponsable(b);
        
        boolean ret = (FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO()).create(r,r.getPeriodo());

        return ret;

    }
    
    public Pair<List<AulaDTO>, List<ReservaDTO>> listadoOpcionesDeAula(ReservaDTO reservaArmandoseDTO,PeriodicaItemDTO itemdto, int capacidad, TipoAula tipo){
        //System.out.print(reservaArmandoseDTO.getPeriodo().toString());
        Pair<List<Aula>, Pair<List<Reserva>,Integer>> aulasSup;
        if(reservaArmandoseDTO.getPeriodo() == TipoPeriodo.ANUAL){
            
            reservaArmandoseDTO.setPeriodo(TipoPeriodo.PRIMER_CUATRIMESTRE);
            Pair<List<Aula>, Pair<List<Reserva>,Integer>> aulasSup1c = aulasSuperpuestas(reservaArmandoseDTO,itemdto,capacidad,tipo);
            reservaArmandoseDTO.setPeriodo(TipoPeriodo.SEGUNDO_CUATRIMESTRE);
            Pair<List<Aula>, Pair<List<Reserva>,Integer>> aulasSup2c = aulasSuperpuestas(reservaArmandoseDTO,itemdto,capacidad,tipo);
            reservaArmandoseDTO.setPeriodo(TipoPeriodo.ANUAL);
            aulasSup = mergearParaAnual(aulasSup1c,aulasSup2c);
            
        }
        else{
        //para cada item
        //validar que no se superpongan los items de reservaArmandoseDTO?
        
        //todavia no se crean los objs, estamos en cu 5
        
         aulasSup = aulasSuperpuestas(reservaArmandoseDTO,itemdto,capacidad,tipo);
        }
        //CONFIGURAR INTERFAZ        
        ReservaDAO dao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO();
        
        ArrayList<Integer> idsAulasSuperpuestas = new ArrayList<>();
        for(Aula a: aulasSup.getElement0())idsAulasSuperpuestas.add(a.getId());
        List<Aula> aulasDisp = dao.aulasDisponibles(tipo, idsAulasSuperpuestas, capacidad);
        
        if(!aulasDisp.isEmpty()){
            //tomar solo las 3 primeras en orden de capacidad asc
            aulasDisp.sort(Comparator.comparingInt(Aula::getCapacidad));
            aulasDisp = aulasDisp.subList(0, Math.min(3, aulasDisp.size()));
           
            
            List<AulaDTO> ret = convertirAulasEnDTO(aulasDisp,tipo);
            return new Pair(ret,null);
            
        }
        else{
            //2do filtro
            List<Reserva> reservasMin = aulasSup.getElement1().getElement0();
            
            List<ReservaDTO> ret = convertirReservasEnDTO(reservasMin);
            
            return new Pair(null,ret);
        }
        
    }
    
    public Pair<List<AulaDTO>, List<ReservaDTO>> listadoOpcionesDeAula(ReservaDTO reservaArmandoseDTO,EsporadicaItemDTO itemdto, int capacidad, TipoAula tipo){
        //para cada item
        //validar que no se superpongan los items de reservaArmandoseDTO?
        
        //todavia no se crean los objs, estamos en cu 5
        
        Pair<List<Aula>, Pair<List<Reserva>,Integer>> aulasSup = aulasSuperpuestas(reservaArmandoseDTO,itemdto,capacidad,tipo);
        
        //CONFIGURAR INTERFAZ        
        ReservaDAO dao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO();
        
        ArrayList<Integer> idsAulasSuperpuestas = new ArrayList<>();
        for(Aula a: aulasSup.getElement0())idsAulasSuperpuestas.add(a.getId());
        
        List<Aula> aulasDisp = dao.aulasDisponibles(tipo, idsAulasSuperpuestas, capacidad);
        
        if(!aulasDisp.isEmpty()){
            //tomar solo las 3 primeras en orden de capacidad asc
            aulasDisp.sort(Comparator.comparingInt(Aula::getCapacidad));
            aulasDisp = aulasDisp.subList(0, Math.min(3, aulasDisp.size()));
            
            List<AulaDTO> ret = convertirAulasEnDTO(aulasDisp,tipo);
            
            return new Pair(ret,null);
            
        }
        else{
            //2do filtro no seria necesario al final
            List<Reserva> reservasMin = aulasSup.getElement1().getElement0();
            
            List<ReservaDTO> ret = convertirReservasEnDTO(reservasMin);
            
            return new Pair(null,ret);
        }
        
        
    }
    
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> aulasSuperpuestas(ReservaDTO reservaArmandoseDTO,PeriodicaItemDTO itemdto, int capacidad, TipoAula tipo){
	
        

        //CONFIGURAR INTERFAZ        
        ReservaDAO dao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO();
        
        /*
        consultarSuperpuestasPeriodica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
            Time inicio, Time fin,int idPeriodo)
	consultarSuperpuestasEsporadica(LocalDate fecha,int capacidad,TipoAula tipo,
            Time inicio, Time fin,int idPeriodo)
        consultarSuperpuestasPeriodicaAEsporadica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
            Time inicio, Time fin,LocalDate inicio_periodo,LocalDate fin_periodo)
        */
        
        //buscar periodo antes
        Periodo periodo = dao.buscarPeriodoPeriodica(reservaArmandoseDTO.getPeriodo());
        assert(periodo!=null);
        LocalTime inicio = itemdto.getHorario();
        LocalTime fin = itemdto.getHoraFin();
        
		
	return mergear(
                dao.consultarSuperpuestasPeriodica(itemdto.getDia(), capacidad, 
                        tipo, inicio, fin, periodo.getId()),
		dao.consultarSuperpuestasPeriodicaAEsporadica(itemdto.getDia(), capacidad, 
                        tipo, inicio, fin, periodo.getFechaInicio(),periodo.getFechaFin()));
}
    
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> aulasSuperpuestas(ReservaDTO reservaArmandoseDTO,EsporadicaItemDTO itemdto, int capacidad, TipoAula tipo){
	
        

        //CONFIGURAR INTERFAZ        
        ReservaDAO dao = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO();
        
        /*
        consultarSuperpuestasPeriodica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
            Time inicio, Time fin,int idPeriodo)
	consultarSuperpuestasEsporadica(LocalDate fecha,int capacidad,TipoAula tipo,
            Time inicio, Time fin,int idPeriodo)
        consultarSuperpuestasPeriodicaAEsporadica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
            Time inicio, Time fin,LocalDate inicio_periodo,LocalDate fin_periodo)
        */
        
        //buscar periodo antes
        Periodo periodo = dao.buscarPeriodoEsporadica(itemdto.getFecha());
        LocalTime inicio = itemdto.getHorario();
        LocalTime fin = itemdto.getHoraFin();
        
        if(periodo == null){
            //si no cae en un cuatri no tiene sentido calcular superposicion con periodicas
            return dao.consultarSuperpuestasEsporadica(itemdto.getFecha(), capacidad, 
                            tipo, inicio, fin);
        }
		
	return mergear(
                dao.consultarSuperpuestasPeriodica(itemdto.getDiaSemana(), capacidad, 
                        tipo, inicio, fin, periodo.getId()),
		dao.consultarSuperpuestasEsporadica(itemdto.getFecha(), capacidad, 
                        tipo, inicio, fin));
}
    
    
    
    
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> mergear(Pair<List<Aula>, Pair<List<Reserva>,Integer>> consulta1,Pair<List<Aula>, Pair<List<Reserva>,Integer>> consulta2){
        //si una tiene menor solapamiento retornar esa, sino se mergea sobre la primera
        
        
        
        if(consulta1.getElement1().getElement1() < consulta2.getElement1().getElement1()) {
            unirAulasSinDuplicados(consulta1.getElement0(), consulta2.getElement0());
            return consulta1;
        }
        if(consulta1.getElement1().getElement1() > consulta2.getElement1().getElement1()) {
            unirAulasSinDuplicados(consulta2.getElement0(), consulta1.getElement0());
            return consulta2;
        }
        
        //r1 es periodica y r2 esporadica, siempre(ver aulasSuperpuestas()). Asi que hay que concatenar
        for(Reserva r2: consulta2.getElement1().getElement0()){
           // assert(r2.isEsporadica()!=consulta1.getElement1().getElement0().get(0).isEsporadica());//que se cumpla que son distinto tipo
            consulta1.getElement1().getElement0().add(r2);
        }
        
        
	
        unirAulasSinDuplicados(consulta1.getElement0(), consulta2.getElement0());
        return consulta1;
    }
    
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> mergearParaAnual(Pair<List<Aula>, Pair<List<Reserva>,Integer>> primercuat,Pair<List<Aula>, Pair<List<Reserva>,Integer>> segundocuat){
        //si una tiene menor solapamiento retornar esa, sino se mergea sobre la primera
        
        
        
        if(primercuat.getElement1().getElement1() < segundocuat.getElement1().getElement1()) {
            unirAulasSinDuplicados(primercuat.getElement0(), segundocuat.getElement0());
            return primercuat;
        }
        if(primercuat.getElement1().getElement1() > segundocuat.getElement1().getElement1()) {
            unirAulasSinDuplicados(segundocuat.getElement0(), primercuat.getElement0());
            return segundocuat;
        }
        
        //como son de cuatrimestres distintos, nunca esta la misma reserva en ambos parametros
        for(Reserva r2: segundocuat.getElement1().getElement0()){
            primercuat.getElement1().getElement0().add(r2);
        }
        
        
	
        unirAulasSinDuplicados(primercuat.getElement0(), segundocuat.getElement0());
        return primercuat;
    }
    
    
    //utils para mergear
    private void unirAulasSinDuplicados(List<Aula> l1,List<Aula> l2){
        //como interesan todas las aulas con solapamiento, mergear estas
        //puede haber solapamiento con las aulas. Para mergear, fijarse que no
        int aux=l2.size();
        for(int i=0; i<aux; i++){
            Aula a = l2.get(i);
            if(!l1.contains(a))l1.add(a);
        }
    }

    //List<Reserva> buscarReservasSegunDocente(String nomapel)

    private List<AulaDTO> convertirAulasEnDTO(List<Aula> aulas, TipoAula tipo) {
        List<AulaDTO> ret = new ArrayList<>();
        for(Aula a: aulas)ret.add(convertirAulasEnDTO(a,tipo)) ;
        return ret;
    
    }
    private AulaDTO convertirAulasEnDTO(Aula a, TipoAula tipo){
        switch (tipo.toString()){
            case "MULTIMEDIOS" -> {
                AulaMultimedios a2 = (AulaMultimedios) a;
                return new AulaMultimediosDTO(a2.getId(), a2.getCapacidad(), a2.getUbicacion(),
                        a2.getHabilitada(), a2.getAireAcondicionado(), a2.getTipoPizarron(), 
                        a2.isVentiladores(), a2.isCañon(), a2.isComputadora(), a2.isTelevisor());
            }
            case "INFORMATICA" -> {
                AulaInformatica a2 = (AulaInformatica) a;
                return new AulaInformaticaDTO(a2.getId(), a2.getCapacidad(), a2.getUbicacion(),
                        a2.getHabilitada(), a2.getAireAcondicionado(), a2.getTipoPizarron(),
                        a2.getCantidadPCs(),a2.isCañon()
                );
                
            }
            case "SINRECURSOS" -> {
                /*(int id, int capacidad, Ubicacion ubicacion, Boolean habilitada, Boolean aireAcondicionado, 
                TipoPizarron tipoPizarron, boolean ventiladores)
                */
                AulaSinRecursos a2 = (AulaSinRecursos) a;
                return new AulaSinRecursosDTO(a2.getId(), a2.getCapacidad(), a2.getUbicacion(),
                        a2.getHabilitada(), a2.getAireAcondicionado(), a2.getTipoPizarron(),
                        a2.isVentiladores()
                );
            }
        
        }
        return null;
    }
    
    private List<ReservaDTO> convertirReservasEnDTO(List<Reserva> reservasMin) {
        List<ReservaDTO> ret = new ArrayList<>();
        for(Reserva r: reservasMin)ret.add(convertirReservasEnDTO(r)) ;
        
        return ret;
    
    
    }
    
    private ReservaDTO convertirReservasEnDTO(Reserva r){
        if (r.isEsporadica()){
            List<EsporadicaItemDTO> listaesp = new ArrayList<>();
            for(EsporadicaItem it: r.getItemsEsporadica())listaesp.add(convertirItemsEnDTO(it)) ;
            return new ReservaDTO(r.getId(),r.getMailDeContacto(),r.getCantidadDeAlumnos(),r.getIdCurso(),
            r.getNomApelDocente(),r.getTipoAula(), r.getFechaReserva(), r.isEsporadica(), null,
            null, listaesp, null);
        }else {
            List<PeriodicaItemDTO> listaper = new ArrayList<>();
            for(PeriodicaItem it: r.getItemsPeriodica())listaper.add(convertirItemsEnDTO(it)) ;
            return new ReservaDTO(r.getId(),r.getMailDeContacto(),r.getCantidadDeAlumnos(),r.getIdCurso(),
            r.getNomApelDocente(),r.getTipoAula(), r.getFechaReserva(), r.isEsporadica(), null,
            r.getPeriodo().getTipo(), null, listaper);
        }


    }
    
    

    private PeriodicaItemDTO convertirItemsEnDTO(PeriodicaItem it) {
        return new PeriodicaItemDTO(it.getAula().getId(), it.getHoraInicio(), it.getHoraFin(), it.getDia());
    }
    
    
    private EsporadicaItemDTO convertirItemsEnDTO(EsporadicaItem it) {
        return new EsporadicaItemDTO(it.getAula().getId(), it.getHoraInicio(), it.getHoraFin(), it.getFecha());
    }

    public LocalTime horarioFin(LocalTime horaInicio,int duracion){
        int minutos = duracion * 30;
        return horaInicio.plusMinutes(minutos);
    }
    
    public boolean entreHorarios(LocalTime horarioinicio, LocalTime horariofin, LocalTime horariomedio){
        return horariomedio.isAfter(horariofin) && horariomedio.isBefore(horarioinicio);
        //(entreHorarios(item.getHorario(), item.getHoraFin(), pi.getHorario()) 
                    //|| entreHorarios(item.getHorario(), item.getHoraFin(), pi.getHoraFin()))
    }
    
    public void validarItems(List<PeriodicaItemDTO> itemsPeriodica, PeriodicaItemDTO pi) throws SolapamientoReservasException {
        for(PeriodicaItemDTO item: itemsPeriodica){
            if(item.getAula()==pi.getAula() && 
                    item.getDia()==pi.getDia() && 
                    exiteSolapamiento(item,pi)
                    ) throw new SolapamientoReservasException();
        }
    }
    public void validarItems(List<EsporadicaItemDTO> itemsEsporadica, EsporadicaItemDTO ei) throws SolapamientoReservasException {
        for(EsporadicaItemDTO item: itemsEsporadica){
            if(item.getAula()==ei.getAula() && 
                    item.getFecha().equals(ei.getFecha()) && 
                    this.exiteSolapamiento(item, ei)
                    ) throw new SolapamientoReservasException();
        }
    }
    
    public boolean exiteSolapamiento(PeriodicaItemDTO item, PeriodicaItemDTO pi) {
        //greatest(horas inicio) < least(horas fin) 
        LocalTime inicioMayor, finMenor;
        if(item.getHorario().isAfter(pi.getHorario()))inicioMayor = item.getHorario();
        else inicioMayor = pi.getHorario();
        if(item.getHoraFin().isBefore(pi.getHoraFin()))finMenor = item.getHoraFin();
        else finMenor = pi.getHoraFin();

        return inicioMayor.isBefore(finMenor);
    
    }
    public boolean exiteSolapamiento(EsporadicaItemDTO item, EsporadicaItemDTO pi) {
        //greatest(horas inicio) < least(horas fin) 
        LocalTime inicioMayor, finMenor;
        if(item.getHorario().isAfter(pi.getHorario()))inicioMayor = item.getHorario();
        else inicioMayor = pi.getHorario();
        if(item.getHoraFin().isBefore(pi.getHoraFin()))finMenor = item.getHoraFin();
        else finMenor = pi.getHoraFin();

        return inicioMayor.isBefore(finMenor);
    
    }

    private void armarReservaPeriodica(Reserva r, ReservaDTO rdto, AulaDAO auladao) throws AulaNoEncontradoException {
        //setear el periodo

            r.setItemsPeriodica(new ArrayList<>());
            r.setPeriodo(
            //CONFIGURAR INTERFAZ
            (FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getReservaDAO()).buscarPeriodoPeriodica(rdto.getPeriodo())
            );
            for(PeriodicaItemDTO idto: rdto.getItemsPeriodica()){
                    //crear objeto, anexarlo a r, y buscar aula
                    PeriodicaItem it = new PeriodicaItem(null, idto.getHorario(), idto.getHoraFin(), idto.getDia());
                    r.agregarItem(it);

                    it.setAula(
                       auladao.buscarPorIdYTipo(idto.getAula(), r.getTipoAula())
                    );


                }}
    
    
    public ArrayList<String> getDocentes(){
        ConexionSistemaDocentes con;
        con = new ConexionSistemaDocentes();
        return con.get();
    }
    public ArrayList<String> getMaterias(){
        ConexionSistemaMaterias con;
        con = new ConexionSistemaMaterias();
        return con.get();
    }

   
}
