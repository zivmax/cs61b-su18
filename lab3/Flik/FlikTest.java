import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {

    @Test
    public void testIsSameNumber() {
        assertEquals(true, Flik.isSameNumber(1, 1));
        assertEquals(true, Flik.isSameNumber(0, 0));
        assertEquals(false, Flik.isSameNumber(20, 1));
        assertEquals(false, Flik.isSameNumber(128, 500));
        assertEquals(true, Flik.isSameNumber(127, 127));
        assertEquals(true, Flik.isSameNumber(128, 128));
        assertEquals(true, Flik.isSameNumber(1, 1));
        assertEquals(true, Flik.isSameNumber(500, 500));
    }

}
