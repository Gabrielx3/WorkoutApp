package com.example.workoutapp.model;
import java.io.Serializable;

public class Exercise implements Serializable {
    private String nome;
    private int serie, ripetizioni, secondiRecupero;

    public Exercise(String nome, int serie, int ripetizioni, int secondiRecupero) {
        this.nome = nome;
        this.serie = serie;
        this.ripetizioni = ripetizioni;
        this.secondiRecupero = secondiRecupero;
    }

    public String getNome() { return nome; }
    public int getSerie() { return serie; }
    public int getRipetizioni() { return ripetizioni; }
    public int getSecondiRecupero() { return secondiRecupero; }

    @Override
    public String toString() {
        return nome + " - " + serie + "x" + ripetizioni + " (Recupero: " + secondiRecupero + "s)";
    }
}