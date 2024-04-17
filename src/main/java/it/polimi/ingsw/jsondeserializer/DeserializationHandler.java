package it.polimi.ingsw.jsondeserializer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;


/*
todo. Not ready yet: not to use
 */


public class DeserializationHandler<T> {
    GsonBuilder builder;

    public DeserializationHandler() {
        this.builder = new GsonBuilder();
    }

    public List<T> jsonToList(String filePath) throws FileNotFoundException {
        Gson gson = new Gson();

        gson = builder.create();

        // solve type erasure
        TypeToken<List<T>> listType = new TypeToken<List<T>>() {
        };

        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(filePath));

        List<T> list = gson.fromJson(bufferedReader, listType);

        try {
            bufferedReader.close();
        } catch (IOException e) {
            // already closed, no problem
        }

        return list;
    }

    public void registerAdapter(Class<?> concreteClass) {
        builder.registerTypeAdapter(concreteClass, new InterfaceAdaptor<>());
    }

    public String listToJson(List<T> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
