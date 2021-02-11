package com.wildfirelabs.androidfinalproject;

import android.app.AlertDialog;
import android.content.Context;

import com.wildfirelabs.androidfinalproject.models.Cars;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities
{
    private static boolean manager = false;
    private static Cars car;

    public static boolean isManager()
    {
        return manager;
    }

    public static void setManager(boolean manager)
    {
        Utilities.manager = manager;
    }

    public static void showAlertDialog(int title, int message, Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.okay, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public static String digest(String input)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();
            return encodeHex(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private static String encodeHex(byte[] digest)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++)
        {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static Cars getCar()
    {
        return car;
    }

    public static void setCar(Cars car)
    {
        Utilities.car = car;
    }
}
