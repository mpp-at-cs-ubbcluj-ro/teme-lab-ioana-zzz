import org.example.Domain.Utilizator;
import org.example.Domain.validators.UtilizatorValidator;
import org.example.Domain.validators.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UtilizatorTests {

    @Test
    @DisplayName("Teste User")
    public void testUser() {
        Utilizator user1 = new Utilizator("@ionpop", "Ion", "Pop", "1234", null);
        try {
            new UtilizatorValidator().validate(user1);
            assert true;
        } catch (ValidationException e) {
            assert false;
        }


        assertEquals(user1.getFirstName(), "Ion");
        assertEquals(user1.getLastName(), "Pop");
        assertEquals(user1.getPassword(), "1234");
        assertNull(user1.getLastSeen());
        assertNull(user1.getImage());
    }


    @Test
    @DisplayName("Teste User 2")
    public void testUser2() {
        Utilizator user2 = new Utilizator("@ionpop", "Ion", "Pop", "", null);
        try {
            new UtilizatorValidator().validate(user2);
            assert false;
        } catch (ValidationException e) {
            assert true;
        }
    assertEquals(user2.getFirstName(), "Ion");
    assertEquals(user2.getLastName(), "Pop");
    assertEquals(user2.getPassword(), "");
    assertNull(user2.getLastSeen());
    assertEquals(user2.getId(), "@ionpop");

    }
}
