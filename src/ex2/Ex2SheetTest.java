package ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class Ex2SheetTest {

    @Test
    public void testBasicFunctionality() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        assertEquals(5, sheet.width());
        assertEquals(5, sheet.height());
        assertEquals("", sheet.value(0, 0)); // Empty cell

        sheet.set(0, 0, "Hello");
        assertEquals("Hello", sheet.value(0, 0));

        sheet.set(1, 1, "123");
        assertEquals("123.0", sheet.value(1, 1)); // Numeric cell
    }

    @Test
    public void testFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);

        sheet.set(0, 0, "=1+2");
        assertEquals("3.0", sheet.value(0, 0)); // Simple formula

        sheet.set(1, 1, "=A0+5");
        assertEquals("8.0", sheet.value(1, 1)); // Reference formula

        sheet.set(2, 2, "=B1+C3");
        sheet.set(1, 1, "10");
        sheet.set(2, 3, "20");
        assertEquals("30.0", sheet.value(2, 2)); // Multi-reference formula
    }

    @Test
    public void testCircularReferences() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);

        sheet.set(0, 0, "=A0");
        assertEquals(Ex2Utils.ERR_CYCLE, sheet.value(0, 0)); // Direct cycle

        sheet.set(0, 1, "=B1");
        sheet.set(1, 0, "=A1");
        assertEquals(Ex2Utils.ERR_CYCLE, sheet.value(0, 0)); // Indirect cycle
    }

    @Test
    public void testFileIO() throws IOException {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 0, "Hello");
        sheet.set(1, 1, "=10*2");
        sheet.set(2, 2, "123");

        String fileName = "test_sheet.csv";
        sheet.save(fileName);

        Ex2Sheet loadedSheet = new Ex2Sheet();
        loadedSheet.load(fileName);

        assertEquals("Hello", loadedSheet.value(0, 0));
        assertEquals("20.0", loadedSheet.value(1, 1));
        assertEquals("123.0", loadedSheet.value(2, 2));

        // Clean up
        new File(fileName).delete();
    }

    @Test
    public void testEdgeCases() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);

        sheet.set(0, 0, "");
        assertEquals("", sheet.value(0, 0)); // Empty cell

        sheet.set(1, 1, "Not a number");
        assertEquals("Not a number", sheet.value(1, 1)); // Text cell

        sheet.set(2, 2, "=10/0");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(2, 2)); // Division by zero

        sheet.set(3, 3, "=(5+3");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(3, 3)); // Malformed formula
    }
}