import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.example.Professor;
import org.example.Repository;
import org.example.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestService {

    @Mock
    private Repository professorRepository;

    private Service service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new Service(professorRepository);
    }

    @Test
    void saveProfessor_ValidProfessor_ReturnsValidJson() {
        Professor professor = new Professor(null, "Carlos", "14:00-16:00", "Integral", 3);
        Professor savedProfessor = new Professor("1", "Carlos", "14:00-16:00", "Integral", 3);

        when(professorRepository.save(professor)).thenReturn(savedProfessor);

        String result = service.saveProfessor(professor);
        System.out.println(result);

        var json = JsonParser.parseString(result).getAsJsonObject();
        assertEquals("Carlos", json.get("nome").getAsString());
        assertEquals("14:00-16:00", json.get("horarioAtendimento").getAsString());
        assertEquals("Integral", json.get("periodo").getAsString());
        assertEquals(3, json.get("sala").getAsInt());
        assertEquals(1, json.get("predios").getAsJsonArray().get(0).getAsInt());
    }

    @Test
    void getAllProfessors_ReturnsValidJsonArray() {
        List<Professor> professors = List.of(
                new Professor("1", "João", "10:00-12:00", "Integral", 5),
                new Professor("2", "Maria", "14:00-16:00", "Noturno", 7)
        );

        when(professorRepository.findAll()).thenReturn(professors);

        String result = service.getAllProfessores();
        JsonArray jsonArray = JsonParser.parseString(result).getAsJsonArray();

        // Verify first professor
        var primeiroProfessor = jsonArray.get(0).getAsJsonObject();
        assertEquals("João", primeiroProfessor.get("nome").getAsString());
        assertEquals("10:00-12:00", primeiroProfessor.get("horarioAtendimento").getAsString());
        assertEquals("Integral", primeiroProfessor.get("periodo").getAsString());
        assertEquals(5, primeiroProfessor.get("sala").getAsInt());
        assertEquals(1, primeiroProfessor.get("predios").getAsJsonArray().get(0).getAsInt());

        // Verify second professor
        var segundoProfessor = jsonArray.get(1).getAsJsonObject();
        assertEquals("Maria", segundoProfessor.get("nome").getAsString());
        assertEquals("14:00-16:00", segundoProfessor.get("horarioAtendimento").getAsString());
        assertEquals("Noturno", segundoProfessor.get("periodo").getAsString());
        assertEquals(7, segundoProfessor.get("sala").getAsInt());
        assertEquals(2, segundoProfessor.get("predios").getAsJsonArray().get(0).getAsInt());

        verify(professorRepository).findAll();
    }

    @Test
    void updateProfessorById_DeveRetornarProfessorAtualizado_QuandoDadosValidos() {
        Professor professorAtualizado = new Professor("123", "João", "14:00-16:00", "Manhã", 202);

        when(professorRepository.updateById("123", 202, "14:00-16:00"))
                .thenReturn(professorAtualizado);

        String jsonResult = service.updateProfessorById("123", 202, "14:00-16:00");

        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("202") && jsonResult.contains("14:00-16:00"));
    }

    @Test
    void getProfessorById_ExistingId_ReturnsValidJson() {
        String id = "1";
        Professor professor = new Professor(id, "Ana", "19:00-21:00", "Noturno", 8);

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        String result = service.getProfessorById(id);
        var json = JsonParser.parseString(result).getAsJsonObject();


        assertEquals("Ana", json.get("nome").getAsString());
        assertEquals("19:00-21:00", json.get("horarioAtendimento").getAsString());
        assertEquals("Noturno", json.get("periodo").getAsString());
        assertEquals(8, json.get("sala").getAsInt());
        assertEquals(2, json.get("predios").getAsJsonArray().get(0).getAsInt());

        verify(professorRepository).findById(id);
    }

    @Test
    void deleteProfessorById_ExistingId_DeletesSuccessfully() {
        String id = "1";
        Professor professor = new Professor(id, "John", "10:00-12:00", "Integral", 5);

        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        doNothing().when(professorRepository).deleteById(id);

        assertDoesNotThrow(() -> service.deleteProfessorById(id));
        verify(professorRepository).findById(id);
        verify(professorRepository).deleteById(id);
    }

    @Test
    void updateById() {
        String id = "123";

        Professor professorAtualizado = new Professor(id, "Carlos", "14:00-16:00", "Integral", 15);


        when(professorRepository.updateById(id, 202, "14:00-16:00"))
                .thenReturn(professorAtualizado);

        String result = service.updateProfessorById(id, 202, "14:00-16:00");
        var json = JsonParser.parseString(result).getAsJsonObject();
        System.out.println("Professor atualizado: " + result);

        assertEquals(15, json.get("sala").getAsInt());

    }

    @Test
    void saveProfessorSalaZero() {
        Professor professor = new Professor(null, "Lucas", "08:00-10:00", "Noturno", 0);
        Professor savedProfessor = new Professor("2", "Lucas", "08:00-10:00", "Noturno", 0);

        when(professorRepository.save(professor)).thenReturn(savedProfessor);

        String result = service.saveProfessor(professor);
        var json = JsonParser.parseString(result).getAsJsonObject();

        assertEquals("Lucas", json.get("nome").getAsString());
        assertEquals(0, json.get("sala").getAsInt());
    }

    @Test
    void getAllListaVazia() {
        when(professorRepository.findAll()).thenReturn(List.of());

        String result = service.getAllProfessores();
        JsonArray jsonArray = JsonParser.parseString(result).getAsJsonArray();

        assertEquals(0, jsonArray.size());
    }
    @Test
    void updateByIdHorario() {
        String id = "123";

        Professor professorAtualizado = new Professor(id, "Carlos", "16:00-18:00", "Integral", 15);

        when(professorRepository.updateById(id, 15, "16:00-18:00"))
                .thenReturn(professorAtualizado);

        String result = service.updateProfessorById(id, 15, "16:00-18:00");
        var json = JsonParser.parseString(result).getAsJsonObject();
        System.out.println("Professor atualizado: " + result);

        assertEquals("16:00-18:00", json.get("horarioAtendimento").getAsString());
    }

    @Test
    void saveComDadosMinimos() {
        Professor professor = new Professor(null, "Zé", "", "", 1);
        Professor savedProfessor = new Professor("15", "Zé", "", "", 1);

        when(professorRepository.save(professor)).thenReturn(savedProfessor);

        String result = service.saveProfessor(professor);
        var json = JsonParser.parseString(result).getAsJsonObject();
        System.out.println(result);

        assertEquals("Zé", json.get("nome").getAsString());
        assertEquals("", json.get("horarioAtendimento").getAsString());
        assertEquals(1, json.get("sala").getAsInt());
    }


    //Negative
    // Espirito de Porco🐷
    @Test
    void saveProfessor_NullProfessor_ThrowsException() {
        String expectedMessage = "Professor não pode ser nulo";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveProfessor(null)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void getProfessorById_NonExistingId_ThrowsException() {
        String id = "SCCP/03/27/2025";
        String expectedMessage = "Professor não encontrado com o ID: " + id;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getProfessorById(id)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verify(professorRepository).findById(id);
    }

    @Test
    void deleteProfessorById_NonExistingId_ThrowsException() {
        String id = "80";
        String expectedMessage = "Professor não encontrado para deletar com o ID: " + id;

        when(professorRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteProfessorById(id)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verify(professorRepository).findById(id);
        verify(professorRepository, never()).deleteById(id);
    }

    @Test
    void getProfessorById_NullId_ThrowsException() {
        String expectedMessage = "ID não pode ser nulo";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getProfessorById(null)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void deleteProfessorById_NullId_ThrowsException() {
        String expectedMessage = "ID não pode ser nulo";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.deleteProfessorById(null)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void updateSalaNegativa_DeveLancarExcecao() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateProfessorById("321", -10, "15:00-17:00")
        );

        // Update this to match your actual exception message
        assertEquals("Não existe sala negativa...", exception.getMessage());
        verifyNoInteractions(professorRepository);
    }
    @Test
    void saveSemHorarioEPeriodo() {
        Professor professor = new Professor("10", "Zé", null, null, 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveProfessor(professor)
        );

        assertEquals("Informações de horario e periodo não podem ser nulo", exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void saveSemId() {
        Professor professor = new Professor("10", null, "15:00-17:00", "8", 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveProfessor(professor)
        );

        assertEquals("Nome não pode ser nulo", exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void saveComSalaNegativa() {
        Professor professor = new Professor("10", "Yuri Alberto", "15:00-17:00", "8", -1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveProfessor(professor)
        );

        assertEquals("Sala não pode ser negativa...", exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void updateSemId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateProfessorById(null, 10, "15:00-17:00")
        );

        // Update this to match your actual exception message
        assertEquals("ID não pode ser nulo", exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

    @Test
    void updateSemHorario() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateProfessorById("2012", 10, null)
        );

        // Update this to match your actual exception message
        assertEquals("Horario de atendimento não pode ser nulo", exception.getMessage());
        verifyNoInteractions(professorRepository);
    }

}