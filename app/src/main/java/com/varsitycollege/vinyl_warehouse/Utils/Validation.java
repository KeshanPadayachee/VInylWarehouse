package com.varsitycollege.vinyl_warehouse.Utils;

//Imports for Regex ENRM

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validation {
    //Code Attribution
    //Link: https://www.geeksforgeeks.org/write-regular-expressions/
    //Author: GeeksforGeeks
    //End
    // Method to check if input contains letters only [Aa-Zz]
    public static boolean isAlphabetOnly(String inputText) {
        //checks if the parameter only has alphabets using reGex
        return inputText.matches("([a-zA-Z]+\\s?)*$");
    }

    // Method to check if input is alphanumeric [Aa-Zz and 0-9]
    public static boolean isAlphanumeric(String inputText) {

        //creates the regex, setting it to numbers and alphabets only
        Pattern newPatternAlphanumeric = Pattern.compile("^([a-zA-Z0-9]+\\s?)*$");

        //finds the match between the string and the RegEx numbers and alphabets only
        Matcher newMatcher = newPatternAlphanumeric.matcher(inputText);

        return newMatcher.matches();
    }

    //Code Attribution
    //Link: https://www.geeksforgeeks.org/how-to-check-if-string-contains-only-digits-in-java/
    //Author: GeeksforGeeks
    //End
    // Method to check if the input is empty
    public static boolean isNullOrEmpty(String inputText) {

        boolean isNull = false;

        //checks if the parameter is null or empty
        if (inputText == null || inputText.equals("")) {

            isNull = true;

        }
        return isNull;
    }

    //Method to check if a string only has numbers
    public static boolean isNumOnly(String inputText) {

        //creates the regex, setting it to numbers only
        Pattern newPatternNumOnly = Pattern.compile("[0-9]+");

        //finds the match between the string and the RegEx number only
        Matcher newMatcher = newPatternNumOnly.matcher(inputText);

        return newMatcher.matches();
    }

    //method which checks if the email address is valid
    public static boolean validateEmail(String input) {
        //Validate if email
        if (!isNullOrEmpty(input)) {
            //checks if the string contains .com or .co or .za to validate it
            if (input.contains(".com") || input.contains(".co") || input.contains(".za")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}


