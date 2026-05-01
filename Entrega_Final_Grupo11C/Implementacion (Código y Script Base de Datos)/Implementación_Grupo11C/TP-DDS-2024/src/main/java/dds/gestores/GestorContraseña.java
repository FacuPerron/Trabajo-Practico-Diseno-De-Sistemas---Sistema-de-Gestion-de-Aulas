/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dds.gestores;

import dds.daos.FactoryDAO;
import dds.excepcionesLogicas.ContraseñaEquivocadaException;
import dds.excepcionesLogicas.PoliticaDeContraseñaException;
import dds.excepcionesLogicas.UsuarioInexistenteException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import static javax.management.Query.or;

/**
 *
 * @author Joaquín
 */
public class GestorContraseña {
    
    /*
    clase encargada de todas las cuestiones no especificas a bedeles
    Junto a UsuarioDAO
    
    IdUsuario de admin es "admin{numero}"
    
    */
    
    //singleton
    private static GestorContraseña instance;
    public static GestorContraseña getInstance(){
        if(GestorContraseña.instance == null)GestorContraseña.instance =  new GestorContraseña();
        return GestorContraseña.instance;
    }
    
    private GestorContraseña(){}

    public boolean login(String idUsuario, String pw, boolean adminObedel) throws UsuarioInexistenteException, ContraseñaEquivocadaException, NoSuchAlgorithmException, InvalidKeySpecException{
        //ver si existe el usuario. Leer el hash de la pw y validarlo
        
        String storedPassword = FactoryDAO.getFactory(FactoryDAO.PG_FACTORY).getUsuarioDAO().validarLogin(idUsuario, adminObedel);
        
        
        if(!validatePassword(pw, storedPassword))throw new ContraseñaEquivocadaException();
        
        return true;
    }
    
    public boolean validarPW(String password, String confirmacion) throws PoliticaDeContraseñaException{
        boolean alcanzaCaracteresEspeciales=false, tieneMayusculas=false, tieneDigitos=false;
        int contador=0;
        //String regexEspeciales = "[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>/?`~]";  
        //Pattern patron = Pattern.compile(regexEspeciales);
        //Matcher matcher = patron.matcher(password);
        String caracteresNormales = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i=0; i<password.length(); i++){
            char c=password.charAt(i);
            //if (matcher.find()) contador++;
            if(caracteresNormales.indexOf(c) == -1)contador++;
            if (Character.isDigit(c)) tieneDigitos=true;
            if (Character.isUpperCase(c)) tieneMayusculas=true;
        }
        if(contador>=2) alcanzaCaracteresEspeciales=true;
        if(password.length()<8 || !alcanzaCaracteresEspeciales || !tieneMayusculas || !tieneDigitos) throw new PoliticaDeContraseñaException();
        
        return true;
    }
    
    
    //PBKDF2

    
    //https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    public String generateStrongPasswordHash(String password) 
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    
    //validacion de pw

    public boolean validatePassword(String originalPassword, String storedPassword) 
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), 
            salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
    
    
    //Otros metodos
    public boolean validarMail(String mail){
        //podrian validarse mas cosas
        
        if(Character.isDigit(mail.charAt(0)))return false;
        
        Integer lugarArroba = null, lugarPunto = null; 
        
        //pablito144@gmail.com
        //buscar el @, el punto, y fijarse que sea todo letra o nro
        for (int i=0; i<mail.length(); i++){
            char c=mail.charAt(i);
            if(c == '@' && i>0 && lugarArroba==null){
                lugarArroba=i;
            }
            if(c == '.' && lugarArroba!=null && i>lugarArroba+1)lugarPunto=i;
            if(c == '@' && lugarArroba!=null && i!=lugarArroba) return false;
        }
        if(lugarPunto==null || !Character.isLetter(mail.charAt(mail.length()-1)))return false;
        return true;
    }

    
}
