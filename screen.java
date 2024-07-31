import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Chip8Screen extends JPanel {
    private static final int WINDOW_WIDTH = 640;  // Adjust to your needs
    private static final int WINDOW_HEIGHT = 320; // Adjust to your needs
    private static final int SCREEN_WIDTH = 64;
    private static final int SCREEN_HEIGHT = 32;
    private static final int SCALE = 10;  // Adjust scale to fit the desired window size

    private BufferedImage image;
    private int[] pixels;

    public Chip8Screen() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void initWindow() {
        JFrame frame = new JFrame("CHIP-8");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void bufferGraphics(int[][] screen) {
        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                int pixel = screen[y][x];
                pixels[y * SCREEN_WIDTH + x] = (pixel != 0 ? 0xFFFFFF : 0x000000);
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE, null);
    }

    public void closeWindow() {
        JFrame frame = (JFrame) this.getTopLevelAncestor();
        if (frame != null) {
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        Chip8Screen screen = new Chip8Screen();
        screen.initWindow();

        Chip8 chip8 = new Chip8();
        // Load ROM, initialize chip8, etc.
        chip8.loadROM("path/to/rom");

        // Emulation loop
        while (true) {
            chip8.emulateCycle();
            screen.bufferGraphics(chip8.getScreen());
        }
    }
}
