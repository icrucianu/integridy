/*
 * FOLLOWME Project
 * Copyright (c) 2017 SIVECO and/or its affiliates. All rights reserved.
 */
package ro.siveco.cad.integridy.controllers.util;

/**
 * @author SIVECO
 */
public class Constants {
    public final static String DFORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static Long noTimeSeries = 25l;
    
    public final static double OPTIM_SENZITIVITY = 0.7;

    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String CURRENT_CONSUMER = "CURRENT_CONSUMER";
    public static final int DATATABLE_MAX_NUM_RECORDS = 10000;

    public static final boolean ANIMATE_CHART = true;
    public static final String CHART_POS_COLOR = "97ce21";//"60B53F";
    public static final String CHART_NEG_COLOR = "EE2F41";//"D32527";
    public static final String CHART_SERIES_COLORS = CHART_POS_COLOR + ", " + CHART_NEG_COLOR;

    public static final String CHART_TICK_FORMAT = "%d %B,%H:%M";

    /**
     * Bundle
     **/
    public static final String BUNDLE = "/Bundle";
    
    public static final int SEVERITY_NOTIFICATION = 3;
    public static final int SEVERITY_WARNING= 2;
    public static final int SEVERITY_ALERT = 1;
    
    
    
    public static final String USER_APPLICATION_FROM_EMAIL = "roxanab@gmail.com";
    public static final String USER_APPLICATION_FROM_NAME = "Integridy System";
     public static final String USER_APPLICATION_FROM_SUBJECT = "Integridy Account confirmation!";
     

}
