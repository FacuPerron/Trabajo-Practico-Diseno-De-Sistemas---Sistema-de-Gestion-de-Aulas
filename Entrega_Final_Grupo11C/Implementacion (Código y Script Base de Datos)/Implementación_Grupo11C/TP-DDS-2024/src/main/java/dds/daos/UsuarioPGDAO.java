/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.daos;

import dds.excepcionesLogicas.ContraseñaEquivocadaException;
import dds.excepcionesLogicas.UsuarioInexistenteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author augus
 */
public class UsuarioPGDAO implements UsuarioDAO {
    
    private final ConexionSql conector;
    
    public UsuarioPGDAO(){
       conector = new ConexionSql();
    }
    
    
    @Override
    public String validarLogin(String idUsuario, boolean adminObedel) throws UsuarioInexistenteException{
            String ret = null;
            try(Connection con = this.conector.conectar();){

            //con.setAutoCommit(false);
            String sql;
            if (adminObedel){
                sql = String.format("""
                                   SELECT u.contrasena
                                   FROM Usuario u
                                   JOIN Administrador a ON u.id = a.id WHERE u.idusuario = ?;""");
            }else {
                sql = String.format("""
                                   SELECT u.contrasena
                                   FROM Usuario u
                                   JOIN Bedel b ON u.id = b.id
                                   WHERE b.activo = TRUE AND u.idusuario = ?;""");
            }
           //filtro los no activos
            try(PreparedStatement preparedStatement = con.prepareStatement(sql)){

               preparedStatement.setString(1, idUsuario);
               ResultSet resultados = preparedStatement.executeQuery();
               boolean existeUsuario = false;
               //String pwDeLaBaseDeDatos = null;
               if (resultados.next()){
                   existeUsuario = true;
                   //pwDeLaBaseDeDatos = resultados.getString(2);
                   ret = resultados.getString(1);
               }


                //con.commit(); //o se modifican ambas tablas o ninguna

                if(!existeUsuario) throw new UsuarioInexistenteException();
                //if(!validatePassword(pw, pwDeLaBaseDeDatos)) throw new ContraseñaEquivocadaException();

                }
                catch(SQLException e){
                    System.out.println("excepcion en "+this.getClass().getName()+".validarLogin() al ejecutar transaccion" + e.getClass().getName());
                    //con.rollback();
                }
            }
            catch(SQLException e){
                System.out.println("excepcion en "+this.getClass().getName()+".validarLogin() al abrir conexion" + e.getMessage());
            } 
            return ret;   
        }


}
    

