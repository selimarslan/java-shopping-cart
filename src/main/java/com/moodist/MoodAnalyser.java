package com.moodist;

public class MoodAnalyser {
    public String analyseMethod(String message) {
        if(message.contains("sad")){
            return "SAD";
        }else{
            return "HAPPY";
        }
    }
}
