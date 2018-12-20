package br.com.mobilemind.veloster.droidtest.pack;

import java.security.InvalidParameterException;

/**
 *
 * @author Ricardo Bocchi
 */
public enum PersonType {
    PERSON_1 (1, "Person One"),
    PERSON_2 (2, "Person Two");
    
    private int id;
    private String name;
    
    private PersonType(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }    
    
    @Override
    public String toString() {
        return this.name;        
    }
    
    public static PersonType parse(int i){
        PersonType[] values = values();
        for (PersonType t : values) {
            if(t.getId() == i){
                return t;
            }
        }
        throw new InvalidParameterException("enum number " + i + " not found");
    }    
}
