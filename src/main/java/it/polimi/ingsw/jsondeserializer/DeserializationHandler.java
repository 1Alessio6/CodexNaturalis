package it.polimi.ingsw.jsondeserializer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.model.card.strategies.CalculatePoints;

/**
 * Handles the deserialization of the object <code>T</code>
 *
 * @param <T> the object to deserialize
 */
public class DeserializationHandler<T> {
    GsonBuilder builder;
    Gson gson;

    /**
     * Constructs the <code>DeserializationHandler</code>
     */
    public DeserializationHandler() {
        // enable flag for serialization of user-defined objects as keys in map
        this.builder = new GsonBuilder().enableComplexMapKeySerialization();
        // registry the adapter to deal with Interfaces.
        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());
        gson = builder.create();
    }

    /**
     * Returns the json file found in the given <code>jsonPath</code> as a <code>String</code>.
     *
     * @param jsonPath the path to the json file.
     * @return the json file as a String.
     * @throws FileNotFoundException if an error occurs during the opening of the file.
     */
    private String getJson(String jsonPath) throws FileNotFoundException {
        StringBuilder json = new StringBuilder();
        try (InputStream jsonAsStream = DeserializationHandler.class.getResourceAsStream(jsonPath)) {
            if (jsonAsStream != null) {
                Scanner myReader = new Scanner(jsonAsStream);
                while (myReader.hasNextLine()) {
                    json.append(myReader.nextLine());
                }
                myReader.close();
                return json.toString();
            } else {
                throw new FileNotFoundException("Json file " + jsonPath + " not found!!!");
            }
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    /**
     * Deserializes the json file found in <code>jsonPath</code>.
     *
     * @param jsonPath the path to the json file.
     * @param listType to which it deserializes.
     * @return the deserialized json file in a list.
     * @throws FileNotFoundException if an error occurs during the opening of the file.
     */
    public List<T> jsonToList(String jsonPath, TypeToken<List<T>> listType) throws FileNotFoundException {
        String jsonRep = getJson(jsonPath);
        return gson.fromJson(jsonRep, listType);
    }

    /**
     * Registers the custom adapter.
     *
     * @param concreteClass the class.
     */
    public void registerAdapter(Class<?> concreteClass) {
        builder.registerTypeAdapter(concreteClass, new InterfaceAdaptor<>());
    }

}
