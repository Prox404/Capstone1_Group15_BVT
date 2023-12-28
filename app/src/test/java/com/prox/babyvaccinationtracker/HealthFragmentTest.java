package com.prox.babyvaccinationtracker;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.github.mikephil.charting.data.Entry;
import com.prox.babyvaccinationtracker.model.Health;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthFragmentTest {
    private float BMI(float height, float weight){
        height = height / 100f;
        return weight / (height*height);
    }
    @Test
    public void dataValues2Test() {
        ArrayList<Entry> valuesException = new ArrayList<>();
        valuesException.add(new Entry(1,BMI(170,60)));
        valuesException.add(new Entry(2,BMI(160,50)));

        HashMap<Integer,Health> ValueTest = new HashMap<>();
        ValueTest.put(1, new Health((float)170, (float)60));
        ValueTest.put(2, new Health((float)160, (float)50));

        HealthFragment healthFragment = new HealthFragment();
        ArrayList<Entry> valuesActual = healthFragment.dataValues2(ValueTest);


        assertEquals(valuesException.size(), valuesActual.size());

        for(int i = 0 ; i < valuesException.size() ; i ++){
            assertEquals(valuesException.get(i).toString(), valuesActual.get(i).toString());
        }


    }
}