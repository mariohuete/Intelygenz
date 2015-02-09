package com.mariohuete.rss_reader.models;


import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by mariobama on 09/02/15.
 */
public class Model implements Serializable, Comparable {

    //ATTRIBUTES:
    private String name;
    private String instructions;
    private String photo;

    //METHODS:
    public Model(String name, String instr, String photo) {
        this.name = name;
        this.instructions = instr;
        this.photo = photo;
    }

    // Comparator to filter text when set the items in listView
    public static Comparator<Model> ByNameComparator = new Comparator<Model>() {
        @Override
        public int compare(Model lhs, Model rhs) {
            String modelName1 = lhs.getName().toUpperCase();
            String modelName2 = rhs.getName().toUpperCase();
            return modelName1.compareTo(modelName2);
        }
    };

    // Get and set methods
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getInstructions() {
        return this.instructions;
    }
    public void setInstruction(String instr) {
        this.instructions = instr;
    }
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String pht) {
        this.photo = pht;
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }

}
