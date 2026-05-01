/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;
import dds.daos.ConexionSql;
import dds.dtos.BedelDTO;
import dds.daos.BedelDAO;
import dds.entidades.Bedel;
import dds.enumerados.Turno;
import dds.excepcionesLogicas.BedelNoEncontradoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author augus
 */
public class BedelPGDAO implements BedelDAO{
    
    private final ConexionSql conector;
    
    public BedelPGDAO(){
       conector = new ConexionSql();
    }
    @Override
    public Bedel buscarPorIdUsuario(String idbedel)  throws BedelNoEncontradoException { //solo sobre activos
       Bedel ret = null;

        try(Connection con = this.conector.conectar();){

        con.setAutoCommit(false);

       //prepared statement para tablas Usuario y Bedel.
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            SELECT *  FROM Usuario Join Bedel on Bedel.id = Usuario.id 
                                                                            Where usuario.idusuario = ? and activo = true""")){



            preparedStatement.setString(1, idbedel);
            ResultSet resultados = preparedStatement.executeQuery();
            int contador = 0;
           while (resultados.next()){
                contador++;
                ret = armarBedel(resultados);
            }

            con.commit(); //o se modifican ambas tablas o ninguna
            if(contador==0) throw new BedelNoEncontradoException();
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPorIdUsuario() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPorIdUsuario() al abrir conexion" + e.getMessage());
        } 


        return ret;

    }
    
    @Override
    public boolean create(Bedel b) {
        boolean ret = false;
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
       
       //prepared statement para tablas Usuario y Bedel.
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            INSERT INTO Usuario (idUsuario,nombre,apellido,email,contrasena) 
                                                                            VALUES (?,?,?,?,?);;""", 
                PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement preparedStatement2 = con.prepareStatement("INSERT INTO Bedel (id,nombreturno,activo)  VALUES (?,?,TRUE);");){

            //setear valores
            preparedStatement.setString(1, b.getIdUsuario());
            preparedStatement.setString(2, b.getNombre());
            preparedStatement.setString(3, b.getApellido());
            preparedStatement.setString(4, b.getEmail());
            preparedStatement.setString(5, b.getPassword());

            preparedStatement.executeUpdate();


            ResultSet claveGenerada = preparedStatement.getGeneratedKeys();
            claveGenerada.next();
            int parentId = claveGenerada.getInt(1);


            //setear valores
            preparedStatement2.setInt(1, parentId);
            preparedStatement2.setString(2, b.getTurno().toString());


            preparedStatement2.executeUpdate(); 

            con.commit(); //o se modifican ambas tablas o ninguna

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

    
     @Override
    public boolean update(Bedel b, boolean cambiopw) {
        boolean ret = false;
        try(Connection con = this.conector.conectar();){

        con.setAutoCommit(false);
        String sqlpw = """
                                                                            UPDATE Usuario 
                                                                            SET nombre = ?, apellido = ?,email = ?,contrasena = ? 
                                                                            WHERE id = ?""";
        String sqlSinpw = """
                                                                            UPDATE Usuario 
                                                                            SET nombre = ?, apellido = ?,email = ?
                                                                            WHERE id = ?""";
        String sql; 
        if(cambiopw)sql = sqlpw;
        else sql = sqlSinpw;
       //prepared statement para tablas Usuario y Bedel.
        try(PreparedStatement preparedStatement = con.prepareStatement(sql ); 
                PreparedStatement preparedStatement2 = con.prepareStatement("UPDATE Bedel SET nombreturno =? ,activo = ?  WHERE id = ?;");){

            //setear valores
            preparedStatement.setString(1, b.getNombre());
            preparedStatement.setString(2, b.getApellido());
            preparedStatement.setString(3, b.getEmail());
            if(cambiopw)preparedStatement.setString(4, b.getPassword());
            if(cambiopw)preparedStatement.setInt(5, b.getId());
            else preparedStatement.setInt(4, b.getId());
            preparedStatement.executeUpdate();

            //setear valores
            preparedStatement2.setString(1, b.getTurno().toString());
            preparedStatement2.setBoolean(2, b.getActivo());
            preparedStatement2.setInt(3, b.getId());
            
            preparedStatement2.executeUpdate(); 

            con.commit(); //o se modifican ambas tablas o ninguna

            ret = true;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".update() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".update() al abrir conexion" + e.getMessage());
        } 
        return ret;
    }

    @Override
    public List<Bedel> listar() { //solo activos
         List<Bedel> ret = new ArrayList<>();
        
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
       //prepared statement para tablas Usuario y Bedel.
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            SELECT (id,idusuario,nombre,apellido,email,contrasena,nombreturno) 
                                                                       FROM Usuario Join Bedel on Bedel.id = Usuario.id 
                                                                       where activo = true
                                                                       """)){
            
            
            ResultSet resultados = preparedStatement.executeQuery();
           while (resultados.next()){
               Bedel tmp = armarBedel(resultados);
                ret.add(tmp);
            }
            
            con.commit(); //o se modifican ambas tablas o ninguna
            return ret;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".listar() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".listar() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
    }

    @Override
    public Bedel buscarPorId(int id) throws BedelNoEncontradoException { //solo sobre activos
       Bedel ret = null;
        
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
       //prepared statement para tablas Usuario y Bedel.
        try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            SELECT *  FROM Usuario Join Bedel on Bedel.id = Usuario.id 
                                                                            Where usuario.id = ? and activo = true""")){
            
            
            
            preparedStatement.setInt(1, id);
            ResultSet resultados = preparedStatement.executeQuery();
            int contador = 0;
           while (resultados.next()){
                contador++;
                ret = armarBedel(resultados);
            }
            
            con.commit(); //o se modifican ambas tablas o ninguna
            if(contador==0) throw new BedelNoEncontradoException();
            return ret;
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPorId() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(SQLException e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPorId() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
    }
    
    
    
    
    
    @Override
    public int cuantosActivos(String idUsuario) {
        int contador=0;
        
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
       try(PreparedStatement preparedStatement = con.prepareStatement("""
                                                                            SELECT * FROM Usuario Join Bedel on Bedel.id = Usuario.id 
                                                                            Where activo = true and idUsuario=?""")){
            preparedStatement.setString(1, idUsuario);
            
            
            
            ResultSet resultados = preparedStatement.executeQuery();
            
            while(resultados.next()){
                    contador++;
            }
            
            con.commit(); 
            return contador;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".existenNActivos() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".existenNActivos() al abrir conexion" + e.getMessage());
        } 
        

        return contador;
    }

    

    /*@Override
    public boolean idUsuarioOcupado(String idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
*/

    @Override
    public ArrayList<Bedel> buscarPorApelYTurno(String apellido, Turno t) {
        String nombreturno;
        if(t!=null) nombreturno = t.toString();
        else nombreturno = "";
        ArrayList<Bedel> ret = new ArrayList<>();
        
        String query = "(SELECT * "
                + "FROM Usuario Join Bedel on Bedel.id = Usuario.id"
                + " WHERE UPPER(usuario.apellido) LIKE ? and bedel.activo=true)"
                +" INTERSECT (SELECT * "
                + "FROM Usuario Join Bedel on Bedel.id = Usuario.id"
                + " WHERE bedel.nombreturno LIKE ? and bedel.activo=true)";
        /*if(!apellido.equals("")){
            query+="apellido LIKE %"+apellido+"% ";
            if(!nombreturno.equals(""))query+="and ";
        }
        if(!nombreturno.equals(""))query+="nombreturno = "+nombreturno;
        }*/
        
        try(Connection con = this.conector.conectar();){
        
        con.setAutoCommit(false);
        
       
        try(PreparedStatement preparedStatement = con.prepareStatement(query)){
            
            preparedStatement.setString(1,"%"+apellido.toUpperCase()+"%");
            preparedStatement.setString(2,"%"+nombreturno+"%");
            
            ResultSet resultados = preparedStatement.executeQuery();
           while (resultados.next()){
                Bedel tmp = armarBedel(resultados);
                ret.add(tmp);
            }
            
            con.commit(); 
            return ret;
            }
            catch(Exception e){
                System.out.println("excepcion en "+this.getClass().getName()+".buscarPorApelYTurno() al ejecutar transaccion" + e.getMessage());
                con.rollback();
            }
        }
        catch(Exception e){
            System.out.println("excepcion en "+this.getClass().getName()+".buscarPorApelYTurno() al abrir conexion" + e.getMessage());
        } 
        

        return ret;
    }

    private Bedel armarBedel(ResultSet resultados) {
        //(Turno turno, Boolean activo, List<Reserva> reservas, int id, String idUsuario, String nombre, String apellido, String email, String password)
        try{
        return new Bedel(
                       Turno.valueOf(resultados.getString("nombreturno")),
                        true,
                        null,  //lista de reservas
                        resultados.getInt("id"),
                        resultados.getString("idUsuario"),
                        resultados.getString("nombre"),
                        resultados.getString("apellido"),
                        resultados.getString("email"),
                        resultados.getString("contrasena")
                );
    
    }
    catch(SQLException e){
    System.out.println("excepcion al armarBedel");
    }
    return null;
    }
    
    
   
    
}

/*@Override
    public boolean yaExisteYNoEliminado(BedelDTO bdto) {
        //la consulta esta sobre tabla usuario: esto significa que no pueden haber dos usuarios con mismo IdUsuario,
        //mas alla de si es bedel o admin
        //Es decir, no es exactamente lo del CU pero es razonable
        boolean ret = false;
        /*try(Connection con = this.conector.conectar();){
            
      
            con.setAutoCommit(false);

           //prepared statement para tablas Usuario y Bedel.
            try(PreparedStatement preparedStatement = conector.con.prepareStatement("""
                                                                                SELECT idUsuario FROM Usuario
                                                                                WHERE idUsuario=?;""");){
            

            //setear valores
            preparedStatement.setString(1, bdto.getIdUsuario());

            ResultSet resultados = preparedStatement.executeQuery();
            int contador=0;
            while(resultados.next()){
                    contador++;
                    if (contador==1) break;
            }

            preparedStatement.close();
            this.conector.cerrar();
            if (contador==1) return true;
			return false;
        }
        catch(Exception e){
            System.out.println("excepcion en BedelPGDAO.yaExisteYNoEliminado() " + e.getMessage());
            return false;
        } 
        
        return ret;
    }
*/