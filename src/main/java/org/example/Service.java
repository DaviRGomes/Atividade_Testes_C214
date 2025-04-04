package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class Service {
    private final Repository professorRepository;
    private final Gson gson;

    public Service(Repository professorRepository) {
        this.professorRepository = professorRepository;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public String saveProfessor(Professor professor) {
        if (professor == null) {
            throw new IllegalArgumentException("Professor não pode ser nulo");
        }
        if(professor.getNome() ==null){
            throw new IllegalArgumentException("Nome não pode ser nulo");
        }
        if(professor.getSala()<0){
            throw new IllegalArgumentException("Sala não pode ser negativa...");
        }
        if(professor.getHorarioAtendimento()==null && professor.getPeriodo()==null){
            throw new IllegalArgumentException("Informações de horario e periodo não podem ser nulo");
        }
        Professor savedProfessor = professorRepository.save(professor);
        return convertToJson(savedProfessor);
    }

    public String getAllProfessores() {
        List<Professor> professores = professorRepository.findAll();
        return convertToJson(professores);
    }

    public String getProfessorById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado com o ID: " + id));
        return convertToJson(professor);
    }

    public void deleteProfessorById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        professorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado para deletar com o ID: " + id));
        professorRepository.deleteById(id);
    }

    public String updateProfessorById(String id, int novaSala, String novoHorarioAtendimento) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (novaSala <0) {
            throw new IllegalArgumentException("Não existe sala negativa...");
        }
        if (novoHorarioAtendimento == null) {
            throw new IllegalArgumentException("Horario de atendimento não pode ser nulo");
        }

        Professor professorAtualizado = professorRepository.updateById(id, novaSala, novoHorarioAtendimento);
        return convertToJson(professorAtualizado);
    }

    public String convertToJson(Object object) {
        return gson.toJson(object);
    }
}