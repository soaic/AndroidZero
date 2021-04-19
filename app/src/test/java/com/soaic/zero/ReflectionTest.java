package com.soaic.zero;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionTest {

    private static final String text = "123";

    @Test
    public void main() {

        try {
            //Object obj = ReflectionTest.class.newInstance();
            Field textField = ReflectionTest.class.getDeclaredField("text");
            textField.setAccessible(true);

            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(textField, textField.getModifiers() & ~Modifier.FINAL);
            //System.out.println("text="+textField.get(null));
            //textField.set(null, "321");
            //System.out.println("text="+textField.get(null));

            //modifiers.setInt(textField, textField.getModifiers() & ~Modifier.FINAL);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
