package com.example.miniproject.objectException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class URLException {

    public static boolean URLException(String url){

        try{
            new URL(url).toURI();
            return true;
        }catch (URISyntaxException | MalformedURLException exception){
            throw new IllegalArgumentException("imageUrl 이 유효하지 않습니다");
        }

    }
}
