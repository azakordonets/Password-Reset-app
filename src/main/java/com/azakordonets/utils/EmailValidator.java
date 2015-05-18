package com.azakordonets.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    //from here : http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
    private static final Pattern ptr = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static boolean isValid(String email) {
        return ptr.matcher(email).matches();
    }
}
