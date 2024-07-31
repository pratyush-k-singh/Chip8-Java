import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Chip8Main extends JFrame {

    private static final int CPU_CLOCK_DELAY = 1; // 1 millisecond delay
    private static final int TIMER_CLOCK_DIVISION = 9; // Division cycles for timers

    private Chip8 chip8;
    private Chip8Screen screen;
    private boolean loggingEnabled;
    private boolean timingEnabled;

    public Chip8Main(String romPath, String[] args) {
        chip8 = new Chip8(new Chip8Screen());
        screen = chip8.getScreen();
        loggingEnabled = args.length > 1 && args[1].equals("log");
        timingEnabled = args.length > 1 && args[1].equals("time");

        // Initialize the emulator and load ROM
        chip8.loadROM(romPath);

        // Setup JFrame
        setTitle("CHIP-8 Emulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(screen);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void runEmulator() {
        long startTime = System.currentTimeMillis();

        while (chip8.isRunning()) {
            chip8.emulateCycle();

            if (loggingEnabled) {
                // Print register states or debug information if logging is enabled
                // You can add this feature based on your debugging needs.
            }

            if (chip8.isDrawFlag()) {
                screen.bufferGraphics(chip8.getScreen());
                chip8.setDrawFlag(false);
            }

            // Process user input
            handleInput();

            // Update timers at a different rate
            if (chip8.getCycleCount() % TIMER_CLOCK_DIVISION == 0) {
                chip8.updateTimers();
            }

            // Delay to control CPU speed
            try {
                Thread.sleep(CPU_CLOCK_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Calculate and print timing information if enabled
        if (timingEnabled) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Run time: " + (double) elapsedTime / 1000 + " seconds");
            System.out.println("Cycles: " + chip8.getCycleCount());
            System.out.println("Cycles Per Second: " + (int) (chip8.getCycleCount() / (elapsedTime / 1000.0)));
        }

        // Close the window and cleanup
        screen.closeWindow();
    }

    private void handleInput() {
        // Implement key handling logic here
        // You can use KeyListener or other methods to handle input events
        // Based on your existing implementation in Chip8.java
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Chip8Main path/to/rom [log | time]");
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            Chip8Main emulator = new Chip8Main(args[0], args);
            emulator.runEmulator();
        });
    }
}
