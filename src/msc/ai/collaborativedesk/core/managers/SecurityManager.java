package msc.ai.collaborativedesk.core.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import msc.ai.collaborativedesk.core.utils.Utils;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public class SecurityManager
{
    private static SecurityManager instance;
    
    public static final String ASY_ALGORITHM = "RSA";
    public static final String KEY_FILE = "C:/CDesk/";
    public static final String HASH_ALGORITHM = "SHA-512";
    
    public static SecurityManager getIntance()
    {
        if (instance == null)
        {
            instance = new SecurityManager();
        }
        return instance;
    }
    
    public byte[] encryptDetails(String plainText, PublicKey publicKey) 
    {
        try
        {
            final Cipher cipher = Cipher.getInstance(ASY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            return cipherText;
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex)
        {
            Logger.getLogger(SecurityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String decryptDetails(byte[] encText, PrivateKey privateKey)
    {
        try
        {
            final Cipher cipher = Cipher.getInstance(ASY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dectyptedText = cipher.doFinal(encText);
            return new String(dectyptedText);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
        {
            Logger.getLogger(SecurityManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
        
    public byte[] encryptTaskDetails(Task task, PublicKey publicKey)
    {
        String taskText = task.serializedString();
        return encryptDetails(taskText, publicKey);
    }
    
    public Task deccryptTaskDetails(String encText, PrivateKey privateKey)
    {
        String decDetails = decryptDetails(Utils.getByteArray(encText), privateKey);
        Task task = new Task(decDetails);
        return task;
    }
    
    public User deccryptMemberDetails(byte[] memberDetails, PrivateKey privateKey)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    public PublicKey getServersPublicKey()
    {
        return getUsersPublicKey("server");
    }
    
    public PrivateKey getServersPrivateKey()
    {
        return getPrivateKey("server");
    }
        
    public PublicKey getUsersPublicKey(String userID)
    {
        File publicKeyFile = new File(KEY_FILE + userID + ".public" );
        if (publicKeyFile.exists())
        {
            try
            {
                String publicKeyPath = KEY_FILE + userID + ".public";
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(publicKeyPath));
                return (PublicKey) inputStream.readObject();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(SecurityManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public PublicKey getUsersPublicKey(User user)
    {
        return getUsersPublicKey(user.getUserID());
    }
    
    public PrivateKey getPrivateKey()
    {
        String userID = DeskManager.getIntance().getUserID();
        return getPrivateKey(userID);
    }
    
    public PrivateKey getPrivateKey(String userID)
    {
        File keyFile = new File(KEY_FILE + userID + ".private" );
        if (keyFile.exists())
        {
            try
            {
                String keyPath = KEY_FILE + userID + ".private";
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(keyPath));
                return (PrivateKey) inputStream.readObject();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                Logger.getLogger(SecurityManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public String generatePasswordHash(String userID, String password)
    {
        String plainText = password + userID;
        return generateHash(plainText);
    }
        
    public String generatePasswordHash(User user, String password)
    {
        String plainText = password + user.getUserID();
        return generateHash(plainText);
    }
    
    public String generateHash(String inputText)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashedBytes = digest.digest(inputText.getBytes("UTF-8"));

            return getHexString(hashedBytes);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(SecurityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "-";
    }
    
    private static String getHexString(byte[] arrayBytes)
    {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < arrayBytes.length; i++)
        {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
    
    public boolean isAsymetricKeysAvailable(User userDetails)
    {
        File privateKeyFile = new File(KEY_FILE + userDetails.getUserID() + ".private" );
        File publicKeyFile = new File(KEY_FILE + userDetails.getUserID() + ".public" );
        
        if ((privateKeyFile.exists()) || (publicKeyFile.exists()))
        {
            return true;
        }
        return false;
    }
    
    public boolean generateAsymetricKeys(User userDetails)
    {
        try
        {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ASY_ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(KEY_FILE + userDetails.getUserID() + ".private" );
            File publicKeyFile = new File(KEY_FILE + userDetails.getUserID() + ".public" );

            if (privateKeyFile.getParentFile() != null)
            {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null)
            {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile)))
            {
                publicKeyOS.writeObject(key.getPublic());
            }

            try (ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile)))
            {
                privateKeyOS.writeObject(key.getPrivate());
            }
            
            return true;
        }
        catch (NoSuchAlgorithmException | IOException ex)
        {
             Logger.getLogger(SecurityManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
