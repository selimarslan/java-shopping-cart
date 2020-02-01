package com.moodist;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class MoodAnalyserTest {
    @Test
    public void testMoodAnalysis() {
        MoodAnalyser moodAnalyser = new MoodAnalyser();
        String mood = moodAnalyser.analyseMethod("This is a sad message");
        Assert.assertThat(mood, CoreMatchers.is("SAD"));
    }

    @Test
    public void testCommerce(){

    }

    @Test
    public void HappyMood() {
        MoodAnalyser moodAnalyser = new MoodAnalyser();
        String mood = moodAnalyser.analyseMethod("This is a happy message");
        Assert.assertThat(mood, CoreMatchers.is("HAPPY"));
    }

    
}
