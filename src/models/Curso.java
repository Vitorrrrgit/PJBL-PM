package models;

import java.io.Serializable;
import java.util.Objects;

public class Curso implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String nome;

    public Curso(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome; // Essencial para exibição no JComboBox
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return id == curso.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}