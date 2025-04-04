import org.example.Professor;
import org.example.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestRepository {
    private Repository repository;


    @BeforeEach
    void setUp() {
        repository = new Repository();

    }

    @Test
    void deveSalvarERecuperarProfessor() {

        Professor  professor = new Professor(null, "Christopher Lima", "19:00-20:00", "noturno", 3);
        Professor saved = repository.save(professor);


        // Verifica os dados do professor
        assertEquals("Christopher Lima", saved.getNome());
        assertEquals("19:00-20:00", saved.getHorarioAtendimento());
        assertEquals("noturno", saved.getPeriodo());
        assertEquals(3, saved.getSala());
        assertEquals("[1]", saved.getPredios().toString());
    }

    @Test
    void deveRetornarTodosProfessores() {
        Professor  professor = new Professor(null, "Christopher Lima", "19:00-20:00", "noturno", 3);
        repository.save(professor);
        repository.save(new Professor(null, "Maria Silva", "14:00-15:00", "integral", 8));

        List<Professor> professores = repository.findAll();
        assertEquals(2, professores.size());
    }
}