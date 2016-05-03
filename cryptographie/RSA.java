package cryptographie;

import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
import javax.crypto.Cipher;  
 
public class RSA  {    
	// ********************** cle publique et privee **********************
	private RSAPrivateKey privateKey;
	private RSAPublicKey  publicKey;
	/**
	 * pour obtenir les cles qui ont produire par algo RSA de java
	 * */
    public void getKey () {
        try   {   
            KeyPairGenerator keyPairGen  =  KeyPairGenerator.getInstance("RSA");  
            keyPairGen.  initialize(2048);    
            KeyPair keyPair = keyPairGen.generateKeyPair();    
            privateKey      =  (RSAPrivateKey)keyPair .getPrivate();    // produire le cle de privee
            publicKey       =  (RSAPublicKey)keyPair  .getPublic();     // produire le cle de publique         
        }   
        catch (Exception e)   {  
            e.printStackTrace();   
        }  
    }
    /**
     *  retour le cle publique
     * */
    public RSAPublicKey getPubKey () {
    	return publicKey;
    }
    /**
     *  retour le cle privee
     * */
    public RSAPrivateKey getPriKey () {
    	return privateKey;
    }
    /**
     * crypter par RSA
     * */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] srcBytes)  {   
        if (publicKey != null)   {   
            try  {   
                Cipher cipher = Cipher.getInstance("RSA");   
                // pour initialisation 
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);   
                // crypter le messages par RSA
                byte[] resultBytes  = cipher.doFinal(srcBytes);  
                return resultBytes;  
            }   
            catch (Exception e)  {   
                e.printStackTrace();   
            }   
        }   
        return null;   
    }  
    /**
     * decrypter par RSA
     * */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] encBytes)   {   
        if (privateKey != null)   {   
            try  {   
                Cipher cipher = Cipher.getInstance("RSA");  
                // pour initialisation 
                cipher.init(Cipher.DECRYPT_MODE, privateKey);  
                // crypter le messages par RSA
                byte[] decBytes  = cipher.doFinal(encBytes);  
                return decBytes;  
            }   
            catch (Exception e)   {   
                e.printStackTrace();   
            }   
        }   
        return null;   
    }   
}  