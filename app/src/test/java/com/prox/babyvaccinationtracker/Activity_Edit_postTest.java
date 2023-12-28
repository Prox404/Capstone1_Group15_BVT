package com.prox.babyvaccinationtracker;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_Edit_postTest {

    @Test
    public void getHashtag() {
        Activity_Edit_post a = new Activity_Edit_post();
        ArrayList<String> valueTest = a.getHashtag("#aa #bb");

        ArrayList<String> valuesException = new ArrayList<>();
        valuesException.add("#aa");
        valuesException.add("#bb");

        assertEquals(valueTest.size(),valuesException.size());

        for(int i = 0 ; i < valueTest.size() ; i ++){
            assertEquals(valueTest.get(i), valuesException.get(i));
        }

    }
}