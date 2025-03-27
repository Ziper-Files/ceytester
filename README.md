# CeyTester - Keyboard Testing Utility

CeyTester is a cross-platform desktop application built with JavaFX that allows users to test keyboard functionality. It provides real-time feedback on keystrokes, displays key codes, characters, modifiers, and locations, and includes diagnostic tools for testing keyboard functionality across different layouts and operating systems.

## Features

- Real-time keystroke detection and visualization
- Visual keyboard layout with key highlighting
- Detailed key information display (key code, character, modifiers, location)
- Event logging with timestamps and response time measurements
- Support for special keys, modifiers, and numpad keys
- Cross-platform compatibility (Windows, macOS, Linux)
- Customizable UI with modern styling

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Building the Application

To build CeyTester, run the following command in the project root directory:

```
mvn clean package
```

This will create an executable JAR file in the `target` directory.

## Running the Application

After building, you can run the application using:

```
java -jar target/CeyTester-1.0-SNAPSHOT.jar
```

Alternatively, you can run it directly with Maven:

```
mvn javafx:run
```

## Usage

1. Launch the application
2. Press any key on your keyboard to see it highlighted in the keyboard visualization
3. View detailed information about the pressed key in the info panel
4. Check the event log for a history of key events and response times
5. Use the "Clear Log" button to reset the event log
6. Toggle "Show Key Codes" and "Log Events" options as needed
7. Use the Settings menu to toggle keyboard view visibility or view application information

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.