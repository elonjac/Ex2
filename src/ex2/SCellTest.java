package ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SCellTest {

    @Test
    public void testBasicCellCreation() {
        SCell cell = new SCell("Test");
        assertEquals("Test", cell.getData());
        assertEquals(Ex2Utils.TEXT, cell.getType());
        assertEquals(0, cell.getOrder());
    }

    @Test
    public void testNumberCell() {
        SCell cell1 = new SCell("123");
        assertTrue(SCell.isNumber("123"));
        assertEquals(Ex2Utils.NUMBER, cell1.getType());

        SCell cell2 = new SCell("123.456");
        assertTrue(SCell.isNumber("123.456"));
        assertEquals(Ex2Utils.NUMBER, cell2.getType());

        SCell cell3 = new SCell("-123.456");
        assertTrue(SCell.isNumber("-123.456"));
        assertEquals(Ex2Utils.NUMBER, cell3.getType());
    }

    @Test
    public void testFormulaCell() {
        SCell cell1 = new SCell("=A0+B1");
        assertTrue(SCell.isForm("=A0+B1"));
        assertEquals(Ex2Utils.FORM, cell1.getType());

        SCell cell2 = new SCell("=1+2");
        assertTrue(SCell.isForm("=1+2"));
        assertEquals(Ex2Utils.FORM, cell2.getType());

        SCell cell3 = new SCell("=(1+2)*3");
        assertTrue(SCell.isForm("=(1+2)*3"));
        assertEquals(Ex2Utils.FORM, cell3.getType());
    }

    @Test
    public void testTextCell() {
        SCell cell1 = new SCell("Hello World");
        assertTrue(SCell.isText("Hello World"));
        assertEquals(Ex2Utils.TEXT, cell1.getType());

        SCell cell2 = new SCell("Test 123 text");
        assertTrue(SCell.isText("Test 123 text"));
        assertEquals(Ex2Utils.TEXT, cell2.getType());
    }

    @Test
    public void testValidCellReferences() {
        // Valid references within bounds
        assertTrue(SCell.isCellRef("A0")); // First column, first row
        assertTrue(SCell.isCellRef("I15")); // Last column, last row
        assertTrue(SCell.isCellRef("E7")); // Middle cell

        // Test all columns with valid row
        assertTrue(SCell.isCellRef("A5"));
        assertTrue(SCell.isCellRef("B5"));
        assertTrue(SCell.isCellRef("C5"));
        assertTrue(SCell.isCellRef("D5"));
        assertTrue(SCell.isCellRef("E5"));
        assertTrue(SCell.isCellRef("F5"));
        assertTrue(SCell.isCellRef("G5"));
        assertTrue(SCell.isCellRef("H5"));
        assertTrue(SCell.isCellRef("I5"));
    }

    @Test
    public void testCellReferenceParser() {
        int[] ref1 = SCell.parseCellRef("A0");
        assertEquals(0, ref1[0]); // Column A = 0
        assertEquals(0, ref1[1]); // Row 0

        int[] ref2 = SCell.parseCellRef("I15");
        assertEquals(8, ref2[0]); // Column I = 8
        assertEquals(15, ref2[1]); // Row 15

        int[] ref3 = SCell.parseCellRef("E7");
        assertEquals(4, ref3[0]); // Column E = 4
        assertEquals(7, ref3[1]); // Row 7
    }


    @Test
    public void testComplexFormulas() {
        assertTrue(SCell.isForm("=A0+B0*C0"));
        assertTrue(SCell.isForm("=(A0+B0)/C0"));
        assertTrue(SCell.isForm("=A0+5"));
        assertTrue(SCell.isForm("=5+A0"));
        assertTrue(SCell.isForm("=(A0+B0)*(C0+D0)"));
    }

    @Test
    public void testEmptyAndNullCells() {
        SCell emptyCell = new SCell("");
        assertEquals(Ex2Utils.TEXT, emptyCell.getType());
        assertEquals("", emptyCell.getData());

        SCell nullCell = new SCell(null);
        assertEquals(Ex2Utils.TEXT, nullCell.getType());
        assertNull(nullCell.getData());
    }


}