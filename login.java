/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplicationpart1;

import java.util.Scanner;

public class login {
    
     public static boolean loginUser(Scanner input, JavaApplicationPart1 user) {

        boolean loginSuccessful = false;

        System.out.println();
        System.out.println("        LOGIN");
        System.out.println("=============================");

        while (!loginSuccessful) {

            System.out.print("Enter your username: ");
            String enteredUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String enteredPassword = input.nextLine();

            if (enteredUsername.equals(user.username)
                    && enteredPassword.equals(user.password)) {

                loginSuccessful = true;

                System.out.println();
                System.out.println(returnLoginStatus(user, true));

            } else {

                System.out.println();
                System.out.println(returnLoginStatus(user, false));
                System.out.println("Please try again.");
                System.out.println();
            }
        }

        return loginSuccessful;
    }

    public static String returnLoginStatus(
            JavaApplicationPart1 user,
            boolean loginSuccessful) {

        if (loginSuccessful) {

            return "Welcome " + user.name + ", "
                    + user.surname
                    + " To QuickChat.";

        } else {

            return "Username or password is incorrect, please try again.";
        }
    }
}