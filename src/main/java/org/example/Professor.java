package org.example;


import java.util.List;

public class Professor {
    private String id;
    private String nome;
    private String horarioAtendimento;
    private String periodo;
    private int sala;
    private List<Integer> predios;

    public Professor(String id, String nome, String horarioAtendimento, String periodo, int sala) {
        this.id = id;
        this.nome = nome;
        this.horarioAtendimento = horarioAtendimento;
        this.periodo = periodo;
        this.sala = sala;
        this.predios = calculaPredio(sala);
    }

    public List<Integer> calculaPredio(int sala) {
        int numeroPredio = (sala - 1) / 5 + 1;
        return List.of(Integer.valueOf(numeroPredio));
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getHorarioAtendimento() {
        return horarioAtendimento;
    }

    public String getPeriodo() {
        return periodo;
    }

    public int getSala() {
        return sala;
    }

    public List<Integer> getPredios() {
        return predios;
    }

    public void setSala(int sala) {
        this.sala = sala;
    }

    public void setHorarioAtendimento(String horarioAtendimento) {
        this.horarioAtendimento = horarioAtendimento;
    }
}