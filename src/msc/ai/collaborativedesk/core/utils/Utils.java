package msc.ai.collaborativedesk.core.utils;

import java.util.Calendar;
import java.util.Date;
import msc.ai.collaborativedesk.core.managers.ClientCommunicationManager;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.BYTE_ARRAY_SEPARATOR;

/**
 *
 * @author Keshan De Silva
 */
public class Utils
{
    public static String getDateString(Date date)
    {
        if (date == null)
        {
            return "-";
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        stringBuilder.append(calendar.get(Calendar.YEAR)).append(ClientCommunicationManager.COMPONENT_SEPARATOR);
        stringBuilder.append(calendar.get(Calendar.MONTH)).append(ClientCommunicationManager.COMPONENT_SEPARATOR);
        stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH)).append(ClientCommunicationManager.COMPONENT_SEPARATOR);
        return stringBuilder.toString();
    }
    
    public static Date getDate(String string)
    {
        if (string.equals("-"))
        {
            return null;
        }
        
        Calendar calendar = Calendar.getInstance();
        String components[] = string.split(ClientCommunicationManager.COMPONENT_SEPARATOR);
        
        calendar.set(Calendar.YEAR, Integer.parseInt(components[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(components[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(components[2]));
        
        return calendar.getTime();
    }
    
    public static String getString(byte[] array)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(array.length);
        
        for (byte value : array)
        {
            sb.append(BYTE_ARRAY_SEPARATOR).append(String.valueOf(value));
        }
        
        return sb.toString();
    }
    
    public static byte[] getByteArray(String input)
    {
        String[] components = input.split(BYTE_ARRAY_SEPARATOR);
        int arraySize = Integer.parseInt(components[0]);
        byte[] result = new byte[arraySize];
        
        for (int i = 1; i < components.length; i++)
        {
            result[i - 1] = Byte.parseByte(components[i]);
        }
        
        return result;
    }
}
