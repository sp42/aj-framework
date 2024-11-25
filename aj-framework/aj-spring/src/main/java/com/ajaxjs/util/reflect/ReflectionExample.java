package com.ajaxjs.util.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ReflectionExample {
    private String secret = "This is a secret";

    public static void main(String[] args) throws Throwable {
        ReflectionExample obj = new ReflectionExample();

        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(ReflectionExample.class, MethodHandles.lookup());
        MethodHandle mh = lookup.findGetter(ReflectionExample.class, "secret", String.class);

        String secretValue = (String) mh.invoke(obj);
        System.out.println("Secret value: " + secretValue);
    }
}