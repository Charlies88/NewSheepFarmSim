package sheepfarm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public List<GameObject> objects = new ArrayList<>();
    public ResourceLoader resources;
    public Player player;

    private Thread gameThread;
    private boolean running = false;

    private GameObject selectedObject = null; // currently selected object
    private JTextArea infoArea;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(87, 166, 57));

        resources = new ResourceLoader();
        initObjects();

        // Input listeners
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // Info panel setup
        infoArea = new JTextArea(15, 20);
        infoArea.setEditable(false);
        infoArea.setBackground(Color.LIGHT_GRAY);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    public JScrollPane getInfoPanel() {
        return new JScrollPane(infoArea);
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
        updateInfoPanel();
    }

    private void updateInfoPanel() {
        if (selectedObject != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Class: ").append(selectedObject.getClass().getSimpleName()).append("\n");
            sb.append("Position: ").append(String.format("%.1f, %.1f", selectedObject.pos.x, selectedObject.pos.y)).append("\n");
            sb.append("Size: ").append(selectedObject.size).append("\n");
            if (selectedObject instanceof Animal) {
                sb.append("Health: ").append(((Animal) selectedObject).health).append("\n");
            }
            if (selectedObject instanceof Plant) {
                sb.append("Growth: ").append(((Plant) selectedObject).getGrowth()).append("\n");
            }
            infoArea.setText(sb.toString());
        } else {
            infoArea.setText("");
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (GameObject obj : objects) {
            obj.render(g);
        }

        // Highlight selected object
        if (selectedObject != null) {
            g.setColor(new Color(255, 255, 0, 128)); // semi-transparent yellow
            int radius = selectedObject.size;
            g.fillOval((int)(selectedObject.pos.x - radius), (int)(selectedObject.pos.y - radius),
                       radius * 2, radius * 2);
        }

        g.dispose();
    }

    // ---------------- KeyListener ----------------
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) { player.keyPressed(e); }
    @Override public void keyReleased(KeyEvent e) { player.keyReleased(e); }

    // ---------------- MouseListener ----------------
    @Override
    public void mouseClicked(MouseEvent e) {
        selectedObject = null;
        for (GameObject obj : objects) {
            double dx = obj.pos.x - e.getX();
            double dy = obj.pos.y - e.getY();
            double distance = Math.sqrt(dx*dx + dy*dy);
            if (distance <= obj.size) {
                selectedObject = obj;
                break;
            }
        }
        updateInfoPanel();
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // ---------------- MouseMotionListener ----------------
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

}
