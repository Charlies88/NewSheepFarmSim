package sheepfarm;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel  implements Runnable, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public List<GameObject> objects = new ArrayList<>();

    public ResourceLoader resources;
    public Player player;

    private Thread gameThread;
    private boolean running = false;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(87, 166, 57));

        resources = new ResourceLoader();
        initObjects();

        // Register this panel to receive key events
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    private void initObjects() {
        // Spawn player at center
        player = new Player(WIDTH / 2.0, HEIGHT / 2.0, 32, resources.playerSheet);
        objects.add(player);

        // Spawn a sheep
        Sheep sheep = new Sheep(100, 100, 32, resources.sheepRightSprite);
        objects.add(sheep);

        // Spawn grass patches
        for (int i = 0; i < 20; i++) {
            double x = Math.random() * WIDTH;
            double y = Math.random() * HEIGHT;
            objects.add(new Grass(x, y, 20, resources.grassSprite));

        }
    }

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this, "GameThread");
        gameThread.start();
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long TARGET_TIME = 1000 / FPS;

        while (running) {
            long start = System.currentTimeMillis();

            update();
            repaint();

            long elapsed = System.currentTimeMillis() - start;
            long wait = TARGET_TIME - elapsed;
            if (wait > 0) {
                try { Thread.sleep(wait); } catch (InterruptedException e) {}
            }
        }
    }

    private void update() {
        for (GameObject obj : objects) obj.update(this);
    }


    @Override
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();

        for (GameObject obj : objects) obj.render(g);

        g.dispose();
    }


    

    // ------- KEY INPUT FOR PLAYER -------
    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
