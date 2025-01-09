Spreadsheet Implementation (Ex2Sheet)     

Overview    
This project implements a basic spreadsheet system in Java. It allows for creating, managing, and evaluating cells, where each cell can contain different types of data: text, numbers, or formulas. The Ex2Sheet class is the main class that manages a 2D grid of Cell objects. Each cell is represented by SCell, and CellEntry is used to represent the cell's reference.    

Classes    
1. CellEntry

This class represents a reference to a cell in the spreadsheet. 
A CellEntry object holds the coordinates of a cell using two characters: one for the column and one for the row.

Key Methods:

isValid(): Checks if the CellEntry is valid.

getX(): Returns the column index corresponding to the letter in the cell reference.

getY(): Returns the row index of the cell.

toString(): Returns the cell reference as a string.

letterToNum(char c): Converts a letter to a corresponding numeric index.


2. SCell

This class represents a cell in the spreadsheet, which can contain different types of data: text, number, or formula. It implements the Cell interface, allowing cells to store and retrieve data, determine their type, and track dependencies through computational order.

Key Methods:

setData(String s): Sets the content of the cell and determines its type.

getData(): Returns the data of the cell.

getType(): Returns the type of the cell.

setType(int t): Sets the type of the cell.

setOrder(int t): Sets the computational order of the cell.

toString(): Returns the string representation of the cell data.

isNumber(String text): Checks if the string represents a valid number.

isForm(String text): Checks if the string represents a valid formula.

isText(String text): Checks if the string is text (not a number or formula).

isCellRef(String form): Checks if a string contains a valid cell reference.

parseCellRef(String cellRef): Parses a cell reference string into column and row.

3. Ex2Sheet
   
Ex2Sheet is the main class representing the entire spreadsheet. It contains a 2D array of SCell objects and supports various operations such as setting, getting, and evaluating cell values.

Key Methods:

value(int x, int y) - Returns the value of a cell at the given coordinates, evaluating it if necessary.

get(int x, int y) - Returns the Cell object at the given coordinates.

get(String cords) - Returns the Cell object corresponding to the provided cell reference.

set(int x, int y, String s) - Sets the value of a cell at the specified coordinates.

eval() - Evaluates all the cells in the sheet.

isIn(int xx, int yy) - Checks if a given cell coordinate is within the sheet's bounds.

load(String fileName) - Loads spreadsheet data from a CSV file.

save(String fileName) - Saves the spreadsheet data to a CSV file.

eval(int x, int y) - Evaluates a single cell based on its contents.

dependencies(String form) - Finds and returns a list of cell references in a formula.

compOrder(String text, int x, int y, HashSet<String> used) - Calculates the evaluation order of a formula, checking for circular dependencies.

compForm(String text) - Computes the result of a formula by parsing and evaluating it.

isOper(char operChar) - Checks if a character is a valid mathematical operator.

hasConsecutiveOperaters(String text) - Checks if a formula contains consecutive operators.


