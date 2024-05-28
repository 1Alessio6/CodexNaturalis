package it.polimi.ingsw.jsondeserializer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.model.card.strategies.CalculatePoints;

public class DeserializationHandler<T> {
    GsonBuilder builder;
    Gson gson;

    public DeserializationHandler() {
        // enable flag for serialization of user-defined objects as keys in map
        this.builder = new GsonBuilder().enableComplexMapKeySerialization();
        // registry the adapter to deal with Interfaces.
        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());
        gson = builder.create();
    }

    private String getJson(String jsonPath) throws FileNotFoundException {
        StringBuilder json = new StringBuilder();

        File myObj = new File(jsonPath);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            json.append(myReader.nextLine());
        }
        myReader.close();

        return json.toString();
    }

    public List<T> jsonToList(String jsonPath, TypeToken<List<T>> listType) throws FileNotFoundException {
        String jsonRep = getJson(jsonPath);
        return gson.fromJson(jsonRep, listType);
    }

    public void registerAdapter(Class<?> concreteClass) {
        builder.registerTypeAdapter(concreteClass, new InterfaceAdaptor<>());
    }

}
