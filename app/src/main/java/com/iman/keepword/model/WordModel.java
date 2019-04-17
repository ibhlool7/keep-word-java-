package com.iman.keepword.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordModel {

    private String word;
    private String user;
    private String definition;
    private String example;
    private String nativeMean;

    public WordModel(String word, String user, String definition, String example, String nativeMean) {
        this.word = word;
        this.user = user;
        this.definition = definition;
        this.example = example;
        this.nativeMean = nativeMean;
    }

}
