/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;

import dds.dtos.EsporadicaItemDTO;
import dds.dtos.PeriodicaItemDTO;
import dds.dtos.ReservaDTO;
import dds.entidades.*;
import dds.enumerados.*;
import dds.util.Pair;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author augus
 */
public class ReservaPGDAO implements ReservaDAO {
    
    private final ConexionSql conector;
    
    public ReservaPGDAO(){
       conector = new ConexionSql();
    }
    
    
    @Override
    public boolean createAnual(Reserva r, Periodo periodo1c,Periodo periodo2c){
    boolean ret = false;
        int parentId1c = 0,parentId2c = 0;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        
        String sql = String.format("""
                INSERT INTO RESERVA (idtablabedel, idperiodo, cantidadalumnos, mail_contacto, idcurso, nom_apel_docente, fechareserva, tipoaula) values (?,?,?,?,?,?,?,?)
            """);
        String sql2 = String.format("""
                INSERT INTO RESERVA (idtablabedel, idperiodo, cantidadalumnos, mail_contacto, idcurso, nom_apel_docente, fechareserva, tipoaula) values (?,?,?,?,?,?,?,?)
            """);
       
       //prepared statement para tablas Usuario y Bedel.
        try(PreparedStatement preparedStatement = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
               PreparedStatement preparedStatement2 = con.prepareStatement(sql2,PreparedStatement.RETURN_GENERATED_KEYS); ){

            //setear valores
            preparedStatement.setInt(1, r.getBedelResponsable().getId());
            preparedStatement.setInt(2, periodo1c.getId());
            preparedStatement.setInt(3, r.getCantidadDeAlumnos());
            preparedStatement.setString(4, r.getMailDeContacto());
            preparedStatement.setString(5, r.getIdCurso());
            preparedStatement.setString(6, r.getNomApelDocente());
            preparedStatement.setDate(7, java.sql.Date.valueOf(r.getFechaReserva()));
            preparedStatement.setString(8, r.getTipoAula().toString());
            
            preparedStatement2.setInt(1, r.getBedelResponsable().getId());
            preparedStatement2.setInt(2, periodo2c.getId());
            preparedStatement2.setInt(3, r.getCantidadDeAlumnos());
            preparedStatement2.setString(4, r.getMailDeContacto());
            preparedStatement2.setString(5, r.getIdCurso());
            preparedStatement2.setString(6, r.getNomApelDocente());
            preparedStatement2.setDate(7, java.sql.Date.valueOf(r.getFechaReserva()));
            preparedStatement2.setString(8, r.getTipoAula().toString());
            
            
            preparedStatement.executeUpdate();
           ResultSet claveGenerada = preparedStatement.getGeneratedKeys();
            claveGenerada.next();
            parentId1c = claveGenerada.getInt(1);
            
            preparedStatement2.executeUpdate();
           ResultSet claveGenerada2 = preparedStatement2.getGeneratedKeys();
            claveGenerada2.next();
            parentId2c = claveGenerada2.getInt(1);
            //crear items
            for(PeriodicaItem it:r.getItemsPeriodica())create(it,parentId1c, con);
            for(PeriodicaItem it:r.getItemsPeriodica())create(it,parentId2c, con);
            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".createAnual() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".createAnual() al abrir conexion" + e.getMessage());
        } 
        
        
        return ret;
}
    
    @Override
    public boolean create(Reserva r, Periodo periodo){
        boolean ret = false;
        int parentId = 0;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        
        String sql = String.format("""
                INSERT INTO RESERVA ( idtablabedel, idperiodo, cantidadalumnos, mail_contacto, idcurso, nom_apel_docente, fechareserva, tipoaula) values (?,?,?,?,?,?,?,?)
            """);
       
       try(PreparedStatement preparedStatement = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){

            //setear valores
            preparedStatement.setInt(1, r.getBedelResponsable().getId());
            if (periodo==null)preparedStatement.setNull(2, Types.INTEGER);
            else preparedStatement.setInt(2, periodo.getId());
            preparedStatement.setInt(3, (int)r.getCantidadDeAlumnos());
            preparedStatement.setString(4, r.getMailDeContacto());
            preparedStatement.setString(5, r.getIdCurso());
            preparedStatement.setString(6, r.getNomApelDocente());
            preparedStatement.setDate(7, java.sql.Date.valueOf(r.getFechaReserva()));
            preparedStatement.setString(8, r.getTipoAula().toString());
            
            
            
            preparedStatement.executeUpdate();
           ResultSet claveGenerada = preparedStatement.getGeneratedKeys();
            claveGenerada.next();
            parentId = claveGenerada.getInt(1);
            //crear items
            if(r.isEsporadica())for(EsporadicaItem it:r.getItemsEsporadica())create(it,parentId, con);
            else for(PeriodicaItem it:r.getItemsPeriodica())create(it,parentId, con);
            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".create() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".create() al abrir conexion" + e.getMessage());
        } 
        
        
        return ret;

}

//creacion de items no deberia ir a la interfaz xq usa cosas de PG
public boolean create(EsporadicaItem it,int parentId, Connection con){
    
    boolean ret = false;
       
        
        
        String sql = String.format("""
                INSERT INTO EsporadicaItem (idreserva, idaula, fecha, hora_inicio, hora_fin) values (?,?,?,?,?)""");
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){

            //setear valores
            preparedStatement.setInt(1, parentId);
            preparedStatement.setInt(2, it.getAula().getId());
            preparedStatement.setDate(3, java.sql.Date.valueOf(it.getFecha()));
            preparedStatement.setTime(4, java.sql.Time.valueOf(it.getHoraInicio()));
            preparedStatement.setTime(5, java.sql.Time.valueOf(it.getHoraFin()));

            preparedStatement.executeUpdate();
           

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".create(Esporadica) al ejecutar transaccion" + e.getMessage());
            }
        
        return ret;
}

public boolean create(PeriodicaItem it,int parentId, Connection con){
    
    boolean ret = false;
        String sql = String.format("""
                INSERT INTO PeriodicaItem (idreserva, idaula, diasemana, hora_inicio, hora_fin) values (?,?,?,?,?)""");
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){

            //setear valores
            preparedStatement.setInt(1, parentId);
            preparedStatement.setInt(2, it.getAula().getId());
            preparedStatement.setString(3, it.getDia().toString());
            preparedStatement.setTime(4, java.sql.Time.valueOf(it.getHoraInicio()));
            preparedStatement.setTime(5, java.sql.Time.valueOf(it.getHoraFin()));

            preparedStatement.executeUpdate();
            
            con.commit();

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".create(Periodica) al ejecutar transaccion" + e.getMessage());
            }
        
        return ret;
}

    @Override
    public Periodo buscarPeriodoEsporadica(LocalDate fecha){
    //puede no caer en ningun periodo, retornaria null
    //en que periodo cae la fecha
    Periodo ret = null;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        String sql = String.format("""
                SELECT id as idperiodo, * FROM periodo
                WHERE fechaInicio <= ? AND fechaFin >= ? --EXTRACT(YEAR FROM fechaInicio) = EXTRACT(YEAR FROM ?)
                                   
            """);
        
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setDate(1, Date.valueOf(fecha));
            preparedStatement.setDate(2, Date.valueOf(fecha));
            ResultSet resultados = preparedStatement.executeQuery();
            if (resultados.next()){
                ret = armarPeriodo(resultados);
            }
            
            con.commit();
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPeriodoEsporadica() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPeriodoEsporadica() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
    
}

    @Override
    public Periodo buscarPeriodoPeriodica(TipoPeriodo tipo){
    //1er o 2do cuatri del año corriente(en back no usamos anual)
       Periodo ret = null;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        String sql = String.format("""
                SELECT id as idperiodo, * FROM periodo
                WHERE tipoperiodo = ? and fechafin>=CURRENT_DATE --EXTRACT(YEAR FROM fechaInicio) = EXTRACT(YEAR FROM CURRENT_DATE)
            """);
        
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setString(1, tipo.toString());
            ResultSet resultados = preparedStatement.executeQuery();
            if (resultados.next()){
                ret = armarPeriodo(resultados);
            }
            
            con.commit(); //o se modifican ambas tablas o ninguna
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPeriodoPeriodica() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPeriodoPeriodica() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
}
 
    
    
//esporadica sobre periodica o periodica sobre periodica
    @Override
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> consultarSuperpuestasPeriodica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
        LocalTime inicioLocal, LocalTime finLocal,int idPeriodo) {
        Time inicio = Time.valueOf(inicioLocal);
        Time fin = Time.valueOf(finLocal);
	//solapamiento en minutos
	
	String subtabla = mapearTipoANombreDeTabla(tipo);
	//inicializo cada parte por las dudas
	Pair<List<Aula>, Pair<List<Reserva>,Integer>>  ret = new Pair(new ArrayList<Aula>(),new Pair(new ArrayList<Reserva>(),300));
	
        boolean leyoSolapamientoMinimo =false;
        Integer solapamientoMinimo = 400;
	
	try(Connection con = this.conector.conectar();){
	
	con.setAutoCommit(false);
	
	String sql = String.format("""
    SELECT 
        reserva.*, 
        periodicaitem.id as idItem, periodicaitem.*, 
        aula.id as aulaidentificacion, aula.*, %s.*,
        periodo.*,
        CASE
        --abba
        WHEN ?::time <= periodicaitem.hora_inicio and  periodicaitem.hora_fin <= ?::time 
        THEN EXTRACT(EPOCH FROM (periodicaitem.hora_fin - periodicaitem.hora_inicio))/60
        --baab
        WHEN periodicaitem.hora_inicio <= ?::time and  ?::time <= periodicaitem.hora_fin
        THEN EXTRACT(EPOCH FROM (?::time - ?::time))/60
        --abab
        WHEN ?::time <= periodicaitem.hora_inicio and  ?::time <= periodicaitem.hora_fin
        THEN LEAST(EXTRACT(EPOCH FROM (periodicaitem.hora_inicio - ?::time)),EXTRACT(EPOCH FROM (?::time - periodicaitem.hora_inicio)))/60
        --baba
        WHEN periodicaitem.hora_inicio <= ?::time and periodicaitem.hora_fin <= ?::time
        THEN LEAST(EXTRACT(EPOCH FROM (periodicaitem.hora_fin - ?::time)),EXTRACT(EPOCH FROM (?::time - periodicaitem.hora_fin)))/60
        ELSE
          999 --no pasa
        END AS solapamiento
    FROM 
        reserva 
    JOIN 
        periodicaitem ON reserva.id = periodicaitem.idreserva
    JOIN 
        aula ON aula.id = periodicaitem.idaula
    --JOIN Bedel ON bedel.id = reserva.idbedel
    --JOIN Usuario ON bedel.id = usuario.id                      
    JOIN 
        %s ON %s.id = aula.id
    JOIN periodo ON periodo.id = reserva.idperiodo
    WHERE 
        aula.capacidad >= ? 
        AND aula.habilitado = true 
	AND reserva.idperiodo = ? 
        AND periodicaitem.diaSemana = ?
        AND GREATEST(periodicaitem.hora_inicio, ?::time) < LEAST(periodicaitem.hora_fin, ?::time)
    
	""", subtabla, subtabla, subtabla);
        
        
	
        
        sql+="ORDER BY solapamiento ASC, reserva.id ASC;";
	try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
		preparedStatement.setTime(1,inicio);
		preparedStatement.setTime(2,fin);
                preparedStatement.setTime(3,inicio);
		preparedStatement.setTime(4,fin);
                preparedStatement.setTime(5,fin);
		preparedStatement.setTime(6,inicio);
                preparedStatement.setTime(7,inicio);
		preparedStatement.setTime(8,fin);
                preparedStatement.setTime(9,inicio);
		preparedStatement.setTime(10,fin);
                preparedStatement.setTime(11,inicio);
		preparedStatement.setTime(12,fin);
                preparedStatement.setTime(13,inicio);
		preparedStatement.setTime(14,fin);
		preparedStatement.setInt(15,capacidad);
		preparedStatement.setInt(16,idPeriodo);
                preparedStatement.setString(17,diaSemana.toString());
		preparedStatement.setTime(18,inicio);
		preparedStatement.setTime(19,fin);
		
		
		ResultSet resultados = preparedStatement.executeQuery();
		
		//HashSets para no repetir lecturas
		HashSet<Integer> idsAula = new HashSet<>();
                int idReservaCorriente=0;Reserva reservaAux = null;
		while (resultados.next()){
			//anotar solapamiento si no se anoto
			if(!leyoSolapamientoMinimo){
                            solapamientoMinimo = resultados.getInt("solapamiento");
                            leyoSolapamientoMinimo = true;
                        }
                        //si el aula del renglon no esta leida leerla
                        if(idsAula.add(resultados.getInt("idAula"))){
                                Aula aulaAux = armarAula(tipo,resultados);
                                ret.getElement0().add(aulaAux);
                        }
			if(resultados.getInt("solapamiento") == solapamientoMinimo){
                            if(resultados.getInt("idreserva")!=idReservaCorriente){
                                idReservaCorriente=resultados.getInt("idreserva");
                                reservaAux = armarReserva(resultados,false);
                                ret.getElement1().getElement0().add(reservaAux);
                            }
                            Aula aulaAux2 = buscarEntreAulasLeidas(ret.getElement0(),resultados.getInt("idAula"));

                            PeriodicaItem itemAux = armarPeriodicaItem(resultados,aulaAux2);
                            reservaAux.agregarItem(itemAux);
                        }
		}
		System.out.println("ctrl1");
		
		con.commit();
                ret.getElement1().setElement1(solapamientoMinimo);
		System.out.println("ctrl2");
		return ret;
		}
		catch(SQLException e){
			//if(!e.getMessage().equals("ResultSet not positioned properly, perhaps you need to call next."))
                                System.out.println("excepcion en "+this.getClass().getName()+".consultarSuperpuestasPeriodica() al ejecutar transaccion" + e.getMessage());
			con.rollback();
		}
	}
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".consultarSuperpuestasPeriodica() al abrir conexion" + e.getMessage());
	} 
       //for (Reserva r: ret.getElement1().getElement0())System.out.println("Superpuestas periodicas: "+r.getId());
	return ret;
}

//esporadica sobre esporadica
    @Override
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> consultarSuperpuestasEsporadica(LocalDate fecha,int capacidad,TipoAula tipo,
        LocalTime inicioLocal, LocalTime finLocal) {
        Time inicio = Time.valueOf(inicioLocal);
        Time fin = Time.valueOf(finLocal);
	//solapamiento en minutos
	
	String subtabla = mapearTipoANombreDeTabla(tipo);
	//inicializo cada parte por las dudas
	Pair<List<Aula>, Pair<List<Reserva>,Integer>>  ret = new Pair(new ArrayList<Aula>(),new Pair(new ArrayList<Reserva>(),300));
	
	Integer solapamientoMinimo = 400;
        boolean leyoSolapamientoMinimo = false;
	
	try(Connection con = this.conector.conectar();){
	
	con.setAutoCommit(false);
	
	String sql = String.format("""
    SELECT 
        reserva.*, 
        esporadicaitem.id as idItem, esporadicaitem.*, 
        aula.id as aulaidentificacion, aula.*, %s.*,
        CASE
        --abba
        WHEN ?::time <= esporadicaitem.hora_inicio and  esporadicaitem.hora_fin <= ?::time 
        THEN EXTRACT(EPOCH FROM (esporadicaitem.hora_fin - esporadicaitem.hora_inicio))/60
        --baab
        WHEN esporadicaitem.hora_inicio <= ?::time and  ?::time <= esporadicaitem.hora_fin
        THEN EXTRACT(EPOCH FROM (?::time - ?::time))/60
        --abab
        WHEN ?::time <= esporadicaitem.hora_inicio and  ?::time <= esporadicaitem.hora_fin
        THEN LEAST(EXTRACT(EPOCH FROM (esporadicaitem.hora_inicio - ?::time)),EXTRACT(EPOCH FROM (?::time - esporadicaitem.hora_inicio)))/60
        --baba
        WHEN esporadicaitem.hora_inicio <= ?::time and esporadicaitem.hora_fin <= ?::time
        THEN LEAST(EXTRACT(EPOCH FROM (esporadicaitem.hora_fin - ?::time)),EXTRACT(EPOCH FROM (?::time - esporadicaitem.hora_fin)))/60
        ELSE
          999 --no pasa
        END AS solapamiento
    FROM 
        reserva 
    JOIN 
        esporadicaitem ON reserva.id = esporadicaitem.idreserva
    JOIN 
        aula ON aula.id = esporadicaitem.idaula
    --JOIN Bedel ON bedel.id = reserva.idbedel
    --JOIN Usuario ON bedel.id = usuario.id                      
    JOIN 
        %s ON %s.id = aula.id
    WHERE 
        aula.capacidad >= ? 
        AND aula.habilitado = true 
        AND esporadicaitem.fecha = ?
        AND GREATEST(esporadicaitem.hora_inicio, ?::time) < LEAST(esporadicaitem.hora_fin, ?::time)
    
	""", subtabla, subtabla, subtabla);
        
        
	
        
        sql+="ORDER BY solapamiento ASC, reserva.id ASC;";
	try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
		preparedStatement.setTime(1,inicio);
		preparedStatement.setTime(2,fin);
                preparedStatement.setTime(3,inicio);
		preparedStatement.setTime(4,fin);
                preparedStatement.setTime(5,fin);
		preparedStatement.setTime(6,inicio);
                preparedStatement.setTime(7,inicio);
		preparedStatement.setTime(8,fin);
                preparedStatement.setTime(9,inicio);
		preparedStatement.setTime(10,fin);
                preparedStatement.setTime(11,inicio);
		preparedStatement.setTime(12,fin);
                preparedStatement.setTime(13,inicio);
		preparedStatement.setTime(14,fin);
		preparedStatement.setInt(15,capacidad);
                preparedStatement.setDate(16,Date.valueOf(fecha));
		preparedStatement.setTime(17,inicio);
		preparedStatement.setTime(18,fin);
		
		
		ResultSet resultados = preparedStatement.executeQuery();
		
		//HashSets para no repetir lecturas
		HashSet<Integer> idsAula = new HashSet<>();
                int idReservaCorriente=0;Reserva reservaAux = null;
		while (resultados.next()){
			//anotar solapamiento si no se anoto
			if(!leyoSolapamientoMinimo){
                            solapamientoMinimo = resultados.getInt("solapamiento");
                            leyoSolapamientoMinimo = true;
                        }
                        //si el aula del renglon no esta leida leerla
                        if(idsAula.add(resultados.getInt("idAula"))){
                                Aula aulaAux = armarAula(tipo,resultados);
                                ret.getElement0().add(aulaAux);
                        }
			if(resultados.getInt("solapamiento") == solapamientoMinimo){
                            if(resultados.getInt("idreserva")!=idReservaCorriente){
                                idReservaCorriente=resultados.getInt("idreserva");
                                reservaAux = armarReserva(resultados,true);
                                ret.getElement1().getElement0().add(reservaAux);
                            }
                            Aula aulaAux2 = buscarEntreAulasLeidas(ret.getElement0(),resultados.getInt("idAula"));

                            EsporadicaItem itemAux = armarEsporadicaItem(resultados,aulaAux2);
                            reservaAux.agregarItem(itemAux);
                        }
		}
		con.commit();
                ret.getElement1().setElement1(solapamientoMinimo);
		return ret;
		}
		catch(SQLException e){
			//if(!e.getMessage().equals("ResultSet not positioned properly, perhaps you need to call next."))
                                System.out.println("excepcion en "+this.getClass().getName()+".consultarSuperpuestasEsporadica() al ejecutar transaccion" + e.getMessage());
			con.rollback();
		}
	}
	catch(SQLException e){
		System.out.println("excepcion en "+this.getClass().getName()+".consultarSuperpuestasEsporadica() al abrir conexion" + e.getMessage());
	} 
        //for (Reserva r: ret.getElement1().getElement0())System.out.println("Superpuestas esporadicas: "+r.getId());
	return ret;
}
    @Override
    public Pair<List<Aula>, Pair<List<Reserva>,Integer>> consultarSuperpuestasPeriodicaAEsporadica(DiaSemana diaSemana,int capacidad,TipoAula tipo,
        LocalTime inicioLocal, LocalTime finLocal,LocalDate inicio_periodo,LocalDate fin_periodo) {
        
        Time inicio = Time.valueOf(inicioLocal);
        Time fin = Time.valueOf(finLocal);
	//solapamiento en minutos
        
	
	String subtabla = mapearTipoANombreDeTabla(tipo);
	//inicializo cada parte por las dudas
	Pair<List<Aula>, Pair<List<Reserva>,Integer>>  ret = new Pair(new ArrayList<Aula>(),new Pair(new ArrayList<Reserva>(),300));
	
	
        boolean leyoSolapamientoMinimo =false;
        Integer solapamientoMinimo = 400;
	
	try(Connection con = this.conector.conectar();){
	
	con.setAutoCommit(false);
	
	String sql = String.format("""
    SELECT 
        reserva.*, 
        esporadicaitem.id as idItem, esporadicaitem.*, 
        aula.id as aulaidentificacion, aula.*, %s.*, 
        CASE
        --abba
        WHEN ?::time <= esporadicaitem.hora_inicio and  esporadicaitem.hora_fin <= ?::time 
        THEN EXTRACT(EPOCH FROM (esporadicaitem.hora_fin - esporadicaitem.hora_inicio))/60
        --baab
        WHEN esporadicaitem.hora_inicio <= ?::time and  ?::time <= esporadicaitem.hora_fin
        THEN EXTRACT(EPOCH FROM (?::time - ?::time))/60
        --abab
        WHEN ?::time <= esporadicaitem.hora_inicio and  ?::time <= esporadicaitem.hora_fin
        THEN LEAST(EXTRACT(EPOCH FROM (esporadicaitem.hora_inicio - ?::time)),EXTRACT(EPOCH FROM (?::time - esporadicaitem.hora_inicio)))/60
        --baba
        WHEN esporadicaitem.hora_inicio <= ?::time and esporadicaitem.hora_fin <= ?::time
        THEN LEAST(EXTRACT(EPOCH FROM (esporadicaitem.hora_fin - ?::time)),EXTRACT(EPOCH FROM (?::time - esporadicaitem.hora_fin)))/60
        ELSE
          999 --no pasa
        END AS solapamiento
    FROM 
        reserva 
    JOIN 
        esporadicaitem ON reserva.id = esporadicaitem.idreserva
    JOIN 
        aula ON aula.id = esporadicaitem.idaula
    --JOIN Bedel ON bedel.id = reserva.idbedel
    --JOIN Usuario ON bedel.id = usuario.id                      
    JOIN 
        %s ON %s.id = aula.id
    WHERE 
        aula.capacidad >= ? 
        AND aula.habilitado = true 
        AND GREATEST(esporadicaitem.hora_inicio, ?::time) < LEAST(esporadicaitem.hora_fin, ?::time)
    
	""", subtabla, subtabla, subtabla);
        
        sql += "AND esporadicaitem.fecha >= ? AND esporadicaitem.fecha <= ?  --dentro del periodo de interes";
        
	
        
        sql+="ORDER BY solapamiento ASC, reserva.id ASC;";
	try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
		
            	preparedStatement.setTime(1,inicio);
		preparedStatement.setTime(2,fin);
                preparedStatement.setTime(3,inicio);
		preparedStatement.setTime(4,fin);
                preparedStatement.setTime(5,fin);
		preparedStatement.setTime(6,inicio);
                preparedStatement.setTime(7,inicio);
		preparedStatement.setTime(8,fin);
                preparedStatement.setTime(9,inicio);
		preparedStatement.setTime(10,fin);
                preparedStatement.setTime(11,inicio);
		preparedStatement.setTime(12,fin);
                preparedStatement.setTime(13,inicio);
		preparedStatement.setTime(14,fin);
                preparedStatement.setInt(15,capacidad);
                preparedStatement.setTime(16,inicio);
                preparedStatement.setTime(17,fin);
		assert(inicio_periodo!=null);
                
                preparedStatement.setDate(18,Date.valueOf(inicio_periodo));
                preparedStatement.setDate(19,Date.valueOf(fin_periodo));
                
		
		
		ResultSet resultados = preparedStatement.executeQuery();
		
		//HashSets para no repetir lecturas
		HashSet<Integer> idsAula = new HashSet<>();
		int idReservaCorriente=0;Reserva reservaAux = null;
		while (resultados.next()){
			//anotar solapamiento si no se anoto
			if(!leyoSolapamientoMinimo){
                            solapamientoMinimo = resultados.getInt("solapamiento");
                            leyoSolapamientoMinimo = true;
                        }
                        //si el aula del renglon no esta leida leerla
                        if(idsAula.add(resultados.getInt("idAula"))){
                                Aula aulaAux = armarAula(tipo,resultados);
                                ret.getElement0().add(aulaAux);
                        }
			if(resultados.getInt("solapamiento") == solapamientoMinimo){
                            if(resultados.getInt("idreserva")!=idReservaCorriente){
                                idReservaCorriente=resultados.getInt("idreserva");
                                reservaAux = armarReserva(resultados,true);
                                ret.getElement1().getElement0().add(reservaAux);
                            }
                            Aula aulaAux2 = buscarEntreAulasLeidas(ret.getElement0(),resultados.getInt("idAula"));

                            EsporadicaItem itemAux = armarEsporadicaItem(resultados,aulaAux2);
                            reservaAux.agregarItem(itemAux);
                        }
		}
		
		con.commit();
                ret.getElement1().setElement1(solapamientoMinimo);
		
		return ret;
		}
		catch(SQLException e){
                    //if(!e.getMessage().equals("ResultSet not positioned properly, perhaps you need to call next."))
			System.out.println("excepcion en "+this.getClass().getName()+".consultarSuperpuestasPeriodicaAEsporadica() al ejecutar transaccion" + e.getMessage());
                    con.rollback();
		}
	}
	catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".consultarSuperpuestasPeriodicaAEsporadica() al abrir conexion" +e.getClass().getName()+ e.getMessage());
	} 
        //for (Reserva r: ret.getElement1().getElement0())System.out.println("Superpuestas periodicas a esporadicas: "+r.getId());
	return ret;
}



    @Override
    public List<Aula> aulasDisponibles(TipoAula tipo, List<Integer> idsAulasSuperpuestas, int capacidad){
	//SELECT * FROM Aula join subtabla WHERE ID NOT IN (id1, id2, ..., idn)
        String subtabla = mapearTipoANombreDeTabla(tipo);
	List<Aula> ret = new ArrayList<>();
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
        String sql;
        if (!idsAulasSuperpuestas.isEmpty()){
            sql = String.format("""
                SELECT aula.id AS idAula, * FROM Aula join %s on Aula.id=%s.id WHERE aula.ID NOT IN (
            """, subtabla, subtabla);
            int ultimo = idsAulasSuperpuestas.getLast();
            idsAulasSuperpuestas.removeLast();
            for(Integer id:idsAulasSuperpuestas)sql+= id + ", ";

            sql += ultimo + ") and aula.capacidad >= ? AND aula.habilitado = true";
        }else{
            sql = String.format("""
                                  select aula.id AS idAula, * from aula join %s
                                  on aula.id=%s.id 
                                  """, subtabla, subtabla);
        sql+="""
             WHERE aula.capacidad >= ? AND aula.habilitado = true """;
        }
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)){
            preparedStatement.setInt(1, capacidad);
            ResultSet resultados = preparedStatement.executeQuery();
            while (resultados.next()){
                ret.add(armarAula(tipo, resultados));
            }
            
            con.commit();
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".aulasDisponibles() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".aulasDisponibles() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
        
	
}


//util copiado de auladao
private String mapearTipoANombreDeTabla(TipoAula tipo) {
	switch (tipo.toString()){
		case "MULTIMEDIOS" -> {
			return "AulaMultimedios";
		}
		case "INFORMATICA" -> {
			return "AulaInformatica";
		}
		case "SINRECURSOS" -> {
			return "AulaSinRecursos";
		}
	}
	return null;
}


//utilidad
public Aula buscarEntreAulasLeidas(List<Aula> aulas, int id){
	for(Aula a: aulas)if(a.getId()==id)return a;
	return null;
}


//mapeos
private Aula armarAula(TipoAula tipo, ResultSet resultados) {
        //mapeo
        assert (tipo!=null);
        try{
        switch (tipo.toString()){
            case "MULTIMEDIOS" -> {
                return new AulaMultimedios(resultados.getInt("idAula"), resultados.getInt("capacidad"), Ubicacion.valueOf(resultados.getString("ubicacion")), 
                        resultados.getBoolean("habilitado"), resultados.getBoolean("aireAcondicionado"), TipoPizarron.valueOf(resultados.getString("TipoPizarron")), 
                        resultados.getBoolean("ventiladores"), resultados.getBoolean("canon"),
                        resultados.getBoolean("computadora"), resultados.getBoolean("televisor"));
            }
            case "INFORMATICA" -> {
                return new AulaInformatica(resultados.getInt("idAula"), resultados.getInt("capacidad"), Ubicacion.valueOf(resultados.getString("ubicacion")), 
                        resultados.getBoolean("habilitado"), resultados.getBoolean("aireAcondicionado"), TipoPizarron.valueOf(resultados.getString("TipoPizarron")), 
                        resultados.getInt("cantidadPCs"), resultados.getBoolean("canon"));
                
            }
            case "SINRECURSOS" -> {
                return new AulaSinRecursos(resultados.getInt("idAula"), resultados.getInt("capacidad"), Ubicacion.valueOf(resultados.getString("ubicacion")), 
                        resultados.getBoolean("habilitado"), resultados.getBoolean("aireAcondicionado"), TipoPizarron.valueOf(resultados.getString("TipoPizarron")), 
                        resultados.getBoolean("ventiladores"));
            
            }
        
        }
        }
        catch(SQLException e){
                System.out.println("excepcion al armar aula"+e.getMessage());
                
                }
        return null;
        
    
    }
	
	
	
	
private PeriodicaItem armarPeriodicaItem(ResultSet resultados, Aula aulaAux) {
//mapeo

try{
               

                return new PeriodicaItem(aulaAux, resultados.getTime("hora_inicio").toLocalTime(), 
                        resultados.getTime("hora_fin").toLocalTime(), DiaSemana.valueOf(resultados.getString("diasemana")));
        }
catch(SQLException e){
        System.out.println("excepcion al armar PeriodicaItem");
}
return null;
}
private EsporadicaItem armarEsporadicaItem(ResultSet resultados, Aula aulaAux) {
try{
               

                return new EsporadicaItem(aulaAux, resultados.getTime("hora_inicio").toLocalTime(), 
                        resultados.getTime("hora_fin").toLocalTime(), resultados.getDate("fecha").toLocalDate());
        }
catch(SQLException e){
        System.out.println("excepcion al armar EsporadicaItem");
}
return null;
}
	
private Reserva armarReserva(ResultSet resultados, boolean esporadica){
try{
        Periodo p=null;
        if (!esporadica) p=armarPeriodo(resultados);
        return new Reserva(resultados.getInt("idReserva"), 
                resultados.getString("mail_contacto"),resultados.getInt("cantidadAlumnos"),
                 resultados.getString("idcurso"),resultados.getString("nom_apel_docente") ,
                TipoAula.valueOf(resultados.getString("tipoAula")),resultados.getDate("fechaReserva").toLocalDate(),esporadica,
                null
               , p, new ArrayList<>(), new ArrayList<>());

}
catch(SQLException e){
    System.out.println("excepcion al armarReserva");
}
    return null;

}
private Periodo armarPeriodo(ResultSet resultados) {
try{

    return new Periodo(resultados.getInt("idperiodo"), 
            //sql date a java.time.LocalDate
            resultados.getDate("fechaInicio").toLocalDate(), 
            resultados.getDate("fechaFin").toLocalDate(),
            
            TipoPeriodo.valueOf(resultados.getString("tipoperiodo")));
}
catch(SQLException e){
        System.out.println(e.getMessage());
}
return null;
}
    
    
}
