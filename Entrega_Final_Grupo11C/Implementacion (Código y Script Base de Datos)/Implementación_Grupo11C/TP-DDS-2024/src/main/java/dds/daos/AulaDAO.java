/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;


import dds.dtos.*;
import dds.util.Pair;
import dds.entidades.*;
import dds.enumerados.TipoAula;
import dds.excepcionesLogicas.AulaNoEncontradoException;
import java.util.List;

/**
 *
 * @author augus
 */
public interface AulaDAO {

    
    
    public Aula buscarPorIdYTipo(int nrDeAula, TipoAula tipo) throws AulaNoEncontradoException;
    
    
    public List<Aula> buscarPorTipoYCapacidad(TipoAula tipo, int capacidad);
    
    
    public boolean update(AulaInformatica aula);
    public boolean update(AulaSinRecursos aula);
    public boolean update(AulaMultimedios aula);
    
}
