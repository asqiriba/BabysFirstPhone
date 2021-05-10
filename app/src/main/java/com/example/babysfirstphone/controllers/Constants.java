package com.example.babysfirstphone.controllers;

public class Constants {
    private static final String TEL_APPEND = "tel:";
    public static String EXAMPLE_PHONE_NO = TEL_APPEND.concat("805-401-7700");

    private String EMERGENCY_NO1 = "1-989-390-8100";
    private String EMERGENCY_NO2 = "1-906-370-1900";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String REGEX_CLEAN_PHONE_NO = "(?<!^\\s)\\+|[^\\d+]+";

    public static final String DEFAULT_IMG = "/storage/emulated/0/Download/download.jpeg";

    /**
     * Getters
     */
    public String getEMERGENCY_NO1() {
        return TEL_APPEND.concat(EMERGENCY_NO1);
    }

    public String getEMERGENCY_NO2() {
        return TEL_APPEND.concat(EMERGENCY_NO2);
    }

    /**
     * Setters
     */
    public void setEMERGENCY_NO1(String EMERGENCY_NO1) {
        this.EMERGENCY_NO1 = EMERGENCY_NO1;
    }

    public void setEMERGENCY_NO2(String EMERGENCY_NO2) {
        this.EMERGENCY_NO2 = EMERGENCY_NO2;
    }

    /**
     * Debug support function for console monitoring.
     * Prints under Terminal a message, if the message is
     * not set, it defaults a PRINT_ALERT.
     *
     * Both arguments are optional.
     *
     @param colour A String containing one of the ANSI
     * colours constants from controllers/Constants.
     @param message A string containing the message to
     * to be sent to standard output.
     */
    public static void PRINT_CHECKPOINT(String colour, String message){
        message = "\n\n\n".concat(message).concat("\n\n\n");
        System.out.println(colour + message + ANSI_RESET);
    }
    /**
     * Debug support function for console monitoring.
     * Prints under Terminal a message, if the message is
     * not set, it defaults a PRINT_ALERT.
     *
     * Both arguments are optional.
     *
     @param colour A String containing one of the ANSI
      * colours constants from controllers/Constants.
     */
    public static void PRINT_CHECKPOINT(String colour){
        System.out.println(colour + "\n\n\n::::::PRINT_ALERT::::::.\n\n\n" + ANSI_RESET);
    }
    /**
     * Debug support function for console monitoring.
     * Prints under Terminal a message, if the message is
     * not set, it defaults a PRINT_ALERT.
     *
     * Both arguments are optional.
     *
     */
    public static void PRINT_CHECKPOINT(){
        System.out.println(ANSI_YELLOW + "\n\n\n::::::PRINT_ALERT::::::.\n\n\n" + ANSI_RESET);
    }
}