package org.example;

import java.util.*;

public class Repository  {
    private final Map<String, Professor> professores = new HashMap<>();


    public Professor save(Professor professor) {
        if (professor.getId() == null) {
            String id = UUID.randomUUID().toString();
            Professor novoProfessor = new Professor(
                    id,
                    professor.getNome(),
                    professor.getHorarioAtendimento(),
                    professor.getPeriodo(),
                    professor.getSala()
            );
            professores.put(id, novoProfessor);
            return novoProfessor;
        }
        professores.put(professor.getId(), professor);
        return professor;
    }

    public List<Professor> findAll() {
        return new ArrayList<>(professores.values());
    }


    public Optional<Professor> findById(String id) {
        return Optional.ofNullable(professores.get(id));
    }

    public void deleteById(String id) {
        professores.remove(id);
    }

    public Professor updateById(String id, int novaSala, String novoHorarioAtendimento) {
        Professor professorExistente = professores.get(id);

        professorExistente.setSala(novaSala);
        professorExistente.setHorarioAtendimento(novoHorarioAtendimento);

        return professorExistente;
    }
}