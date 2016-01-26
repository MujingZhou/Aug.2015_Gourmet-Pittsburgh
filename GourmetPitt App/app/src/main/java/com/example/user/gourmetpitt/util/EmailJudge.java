package com.example.user.gourmetpitt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * tool used to judge email
 */
public class EmailJudge {

    //use this method to judge if a input string is matched with email format
    public boolean judge(String email){

        Pattern pattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }


}
