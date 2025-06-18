package minesweeper;

import minesweeper.util.InputValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    private InputValidator inputValidator;

    @BeforeEach
    void setUp() {
        inputValidator = new InputValidator();
    }

    @Test
    @DisplayName("Test Valid Input for Grid Sizes")
    void testIsValidGridSize_ValidSizes() {
        assertTrue(inputValidator.isValidGridSize("3")); // Min Valid Size
        assertTrue(inputValidator.isValidGridSize("8")); // Middle Range Size
        assertTrue(inputValidator.isValidGridSize("15")); // Middle Range Size
        assertTrue(inputValidator.isValidGridSize("20")); // Max Valid Size
    }

    @Test
    @DisplayName("Test Invalid Input for Grid Sizes")
    void testIsValidGridSize_InvalidSizes() {
        assertFalse(inputValidator.isValidGridSize("2")); // Under Min Valid Size
        assertFalse(inputValidator.isValidGridSize("21")); // Over Max Valid Size
        assertFalse(inputValidator.isValidGridSize("-1")); // Negative Integer
        assertFalse(inputValidator.isValidGridSize("abc")); // Alphabetical Input
        assertFalse(inputValidator.isValidGridSize("abc123")); // Alphanumerical Input
        assertFalse(inputValidator.isValidGridSize("@@#21")); // Symbol Input
        assertFalse(inputValidator.isValidGridSize("")); // Empty String
    }

    @Test
    @DisplayName("Test Valid Input for Mine Counts")
    void testIsValidMineCount_ValidCounts() {
        assertTrue(inputValidator.isValidMineCount("1", 4, 5));
        assertTrue(inputValidator.isValidMineCount("5", 4, 5));
        assertTrue(inputValidator.isValidMineCount("3", 4, 5));
    }

    @Test
    @DisplayName("Test Invalid Input for Mine Counts")
    void testIsValidMineCount_InvalidCounts() {
        assertFalse(inputValidator.isValidMineCount("6", 4, 5));
        assertFalse(inputValidator.isValidMineCount("0", 4, 5));
        assertFalse(inputValidator.isValidMineCount("-1", 4, 5));
        assertFalse(inputValidator.isValidMineCount("abc", 4, 5));
        assertFalse(inputValidator.isValidMineCount("@*&#", 4, 5));
    }

    @Test
    @DisplayName("Test Valid Input for Coordinates")
    void testParseCoordinates_ValidCoordinates() {
        assertArrayEquals(new int[]{0, 0}, inputValidator.parseCoordinates("A1", 4));
        assertArrayEquals(new int[]{1, 2}, inputValidator.parseCoordinates("B3", 4));
        assertArrayEquals(new int[]{3, 3}, inputValidator.parseCoordinates("D4", 4));
        assertArrayEquals(new int[]{4, 5}, inputValidator.parseCoordinates("E6", 6));
        assertArrayEquals(new int[]{11, 10}, inputValidator.parseCoordinates("L11", 12));
    }

    @Test
    @DisplayName("Test Invalid Input for Coordinates")
    void testParseCoordinates_InvalidCoordinates() {
        assertNull(inputValidator.parseCoordinates(null, 4)); // Null Input
        assertNull(inputValidator.parseCoordinates("", 4)); // Empty String
        assertNull(inputValidator.parseCoordinates("A", 4)); // Input Length < 2
        assertNull(inputValidator.parseCoordinates("BB1", 4)); // Input Length > 2
        assertNull(inputValidator.parseCoordinates("A0", 4)); // Non Existent Column
        assertNull(inputValidator.parseCoordinates("E1", 4)); // Non Existent Row
        assertNull(inputValidator.parseCoordinates("3A", 4)); // Invalid Format
    }

    @Test
    @DisplayName("Test Lowercase Input for Coordinates")
    void testParseCoordinate_CaseInsensitive() {
        assertArrayEquals(new int[]{0, 0}, inputValidator.parseCoordinates("a1", 4));
        assertArrayEquals(new int[]{3, 3}, inputValidator.parseCoordinates("d4", 4));
    }
}
