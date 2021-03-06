/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mundo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que contiene los métodos para las fechas
 * @author nicolas
 */
public class FechaActual {
    
   private Date date;
	
   /**
    * Genera la fecha y hora actual
    * @return String con la fecha y hora actual
    */
    public static String timestamp(){
        FechaActual f = new FechaActual();
        f.date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        return hourdateFormat.format(f.date);
    }
	
    /**
     * Genera la hora y fecha para concatener al token
     * @return String con la fecha y hora sin espacios
     */
    public static String timeToken(){
        FechaActual f = new FechaActual();
        f.date = new Date();
        DateFormat hourDateFormat = new SimpleDateFormat("ddMMyyHHmmss");
        return hourDateFormat.format(f.date);
    }
}