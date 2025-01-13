Ex2

Overview

The Ex2Sheet project is a simple spreadsheet implementation in Java that simulates spreadsheet operations such as storing, editing, evaluating formulas, and managing cell dependencies. The project involves three main components:

CellEntry - This class is responsible for handling cell references and conversions between column letters and numbers.

SCell - A class that represents a single cell within the spreadsheet. It supports different types of cell contents such as text, numbers, and formulas.

Ex2Sheet - This is the main spreadsheet class. It supports methods for interacting with cells, evaluating formulas, loading and saving data, and handling errors such as circular dependencies.

Key Features:
Support for text, numbers, and formulas in spreadsheet cells.
Formulas can reference other cells, and the spreadsheet resolves dependencies.
Includes checks for circular references and invalid formulas.
Methods for loading from and saving to CSV files.

Components

1. CellEntry
CellEntry is a utility class that helps convert between the textual representation of a cell (e.g., "A1", "B2") and its corresponding row/column indices.

Key Methods:

letterToNum(char letter) - Converts a letter (A-Z) to a corresponding column number.

numToLetter(int num) - Converts a column number back to a letter.

2. SCell
SCell is the class that represents a single cell within the spreadsheet. Each SCell can contain a value, which can be one of the following types:

Text (string)

Number (numeric value)

Formula (string that begins with = and represents an expression)

Key Methods:

setData(String s) - Sets the data of the cell, determining its type (text, number, formula, or error).

getData() - Returns the data stored in the cell.

getType() - Returns the type of data stored in the cell (e.g., text, number, formula, error).

isNumber(String text) - Determines if a string is a valid number.

isText(String text) - Determines if a string is valid text.

isForm(String text) - Determines if a string is a valid formula.

isCellRef(String form) - Checks if a string is a valid cell reference.

parseCellRef(String cellRef) - Parses a cell reference into row and column indices.

3. Ex2Sheet
Ex2Sheet is the main class representing the entire spreadsheet. It contains a 2D array of SCell objects and supports various operations such as setting, getting, and evaluating cell values.

Key Methods:

value(int x, int y) - Returns the value of a cell at the given coordinates, evaluating it if necessary.

get(int x, int y) - Returns the Cell object at the given coordinates.

get(String cords) - Returns the Cell object corresponding to the provided cell reference (e.g., "A1").

set(int x, int y, String s) - Sets the value of a cell at the specified coordinates.

eval() - Evaluates all the cells in the sheet.

isIn(int xx, int yy) - Checks if a given cell coordinate is within the sheet's bounds.

load(String fileName) - Loads spreadsheet data from a CSV file.

save(String fileName) - Saves the spreadsheet data to a CSV file.

eval(int x, int y) - Evaluates a single cell based on its contents (text, number, or formula).

dependencies(String form) - Finds and returns a list of cell references in a formula.

compOrder(String text, int x, int y, HashSet<String> used) - Calculates the evaluation order of a formula, checking for circular dependencies.

compForm(String text) - Computes the result of a formula by parsing and evaluating it.

isOper(char operChar) - Checks if a character is a valid mathematical operator.

hasConsecutiveOperaters(String text) - Checks if a formula contains consecutive operators (e.g., ++, --).


![Ex2 scrnsht](https://github.com/user-attachments/assets/1c34512f-1455-45e9-a83f-f332c75d28b4)
