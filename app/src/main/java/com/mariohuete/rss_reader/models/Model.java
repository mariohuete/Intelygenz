package com.mariohuete.rss_reader.models;


/**
 * Created by mariobama on 09/02/15.
 */
public class Model {
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

}
