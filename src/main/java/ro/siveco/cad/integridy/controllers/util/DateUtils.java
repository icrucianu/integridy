/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author roxanam
 */
public class DateUtils {
    private static String dayFormat = "ddMMYYYY";
    private static DateFormat dfDay = new SimpleDateFormat(dayFormat);
    
    public static String dateDayFormat(Date date){
        return dfDay.format(date);
    }
    
}
