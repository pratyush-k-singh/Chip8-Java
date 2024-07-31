import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Chip8 extends JFrame implements KeyListener {

    private static final int SCREEN_WIDTH = 64;
    private static final int SCREEN_HEIGHT = 32;
    private static final int NUM_KEYS = 16;
    private static final boolean TRUE = true;
    private static final boolean FALSE = false;

    private int[][] screen;
    private boolean drawScreenFlag;
    private int pcReg;
    private int spReg;
    private int[] stack;
    private int currentOp;
    private int[] V;
    private int IReg;
    private int delayTimer;
    private int soundTimer;
    private boolean[] keyboard;
    private boolean wasKeyPressed;
    private int[] ram;

    private Chip8Screen chip8Screen;
    private Instructions instructions;

    public Chip8(Chip8Screen screen) {
        this.screen = new int[SCREEN_HEIGHT][SCREEN_WIDTH];
        drawScreenFlag = FALSE;
        pcReg = 0x200; // Program counter starts at 0x200
        spReg = 0;
        stack = new int[16];
        currentOp = 0;
        V = new int[16];
        IReg = 0;
        delayTimer = 0;
        soundTimer = 0;
        keyboard = new boolean[NUM_KEYS];
        wasKeyPressed = FALSE;
        ram = new int[4096];
        chip8Screen = screen;
        addKeyListener(this);
    }

    // Fetch the next opcode
    private void fetchOpcode() {
        currentOp = (ram[pcReg] << 8) | ram[pcReg + 1];
    }

    // Execute the fetched opcode
    // Execute the fetched opcode
    private void executeOpcode() {
        int opcode = currentOp & 0xF000;
        switch (opcode) {
            case 0x0000:
                switch (currentOp & 0x00FF) {
                    case 0x00E0:
                        instructions.cls(this);
                        break;
                    case 0x00EE:
                        instructions.returnFromSubroutine(this);
                        break;
                    default:
                        break;
                }
                break;
            case 0x1000:
                instructions.jump(this);
                break;
            case 0x2000:
                instructions.callSubroutine(this);
                break;
            case 0x3000:
                instructions.seVxKk(this);
                break;
            case 0x4000:
                instructions.sneVxKk(this);
                break;
            case 0x5000:
                instructions.seVxVy(this);
                break;
            case 0x6000:
                instructions.ldVx(this);
                break;
            case 0x7000:
                instructions.addVxImm(this);
                break;
            case 0x8000:
                switch (currentOp & 0x000F) {
                    case 0x0000:
                        instructions.moveVxVy(this);
                        break;
                    case 0x0001:
                        instructions.orVxVy(this);
                        break;
                    case 0x0002:
                        instructions.andVxVy(this);
                        break;
                    case 0x0003:
                        instructions.xorVxVy(this);
                        break;
                    case 0x0004:
                        instructions.addVxVy(this);
                        break;
                    case 0x0005:
                        instructions.subVxVy(this);
                        break;
                    case 0x0006:
                        instructions.shr(this);
                        break;
                    case 0x0007:
                        instructions.subnVxVy(this);
                        break;
                    case 0x000E:
                        instructions.shl(this);
                        break;
                }
                break;
            case 0x9000:
                instructions.sneVxVy(this);
                break;
            case 0xA000:
                instructions.ldi(this);
                break;
            case 0xB000:
                instructions.jumpV0(this);
                break;
            case 0xC000:
                instructions.rnd(this);
                break;
            case 0xD000:
                instructions.drw(this);
                break;
            case 0xE000:
                switch (currentOp & 0x00FF) {
                    case 0x009E:
                        instructions.skp(this);
                        break;
                    case 0x00A1:
                        instructions.sknp(this);
                        break;
                }
                break;
            case 0xF000:
                switch (currentOp & 0x00FF) {
                    case 0x0007:
                        instructions.ldVxDT(this);
                        break;
                    case 0x000A:
                        instructions.ldVxK(this);
                        break;
                    case 0x0015:
                        instructions.ldDTVx(this);
                        break;
                    case 0x0018:
                        instructions.ldSTVx(this);
                        break;
                    case 0x001E:
                        instructions.addIVx(this);
                        break;
                    case 0x0029:
                        instructions.ldFVx(this);
                        break;
                }
                break;
        }
    }

    // Emulator cycle
    public void emulateCycle() {
        fetchOpcode();
        executeOpcode();
        updateTimers();
        if (drawScreenFlag) {
            chip8Screen.bufferGraphics(screen);
            drawScreenFlag = FALSE;
        }
    }

    // Update the timers
    private void updateTimers() {
        if (delayTimer > 0) {
            delayTimer--;
        }
        if (soundTimer > 0) {
            soundTimer--;
        }
    }

    // Load ROM into memory
    public void loadROM(String filePath) {
        try {
            byte[] rom = Files.readAllBytes(Paths.get(filePath));
            if (rom.length > (ram.length - 0x200)) {
                System.err.println("ERROR: ROM file too large");
                System.exit(1);
            }
            for (int i = 0; i < rom.length; i++) {
                ram[i + 0x200] = rom[i] & 0xFF;
            }
        } catch (IOException e) {
            System.err.println("ERROR: ROM file does not exist");
            System.exit(1);
        }
    }

    // Handle user input
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_SPACE:
                // Handle pause
                break;
            case KeyEvent.VK_F5:
                // Handle reset
                break;
            default:
                break;
        }

        // Update keyboard array
        for (int i = 0; i < NUM_KEYS; i++) {
            if (e.getKeyCode() == Chip8Screen.KEYMAP[i]) {
                keyboard[i] = TRUE;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Update keyboard array
        for (int i = 0; i < NUM_KEYS; i++) {
            if (e.getKeyCode() == Chip8Screen.KEYMAP[i]) {
                keyboard[i] = FALSE;
            }
        }
    }

    public int[][] getScreen() {
        return screen;
    }

    public static void main(String[] args) {
        Chip8Screen screen = new Chip8Screen();
        screen.initWindow();
        
        Chip8 emulator = new Chip8(screen);
        emulator.loadROM("path/to/rom");

        // Main emulation loop
        while (true) {
            emulator.emulateCycle();
        }
    }
}
