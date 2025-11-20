package sheepfarm;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel  implements Runnable, KeyListener, MouseListener, MouseMotionListener  {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public List<GameObject> objects = new ArrayList<>();

    public ResourceLoader resources;
    public Player player;

    private Thread gameThread;
    private boolean running = false;
    
    private GameObject selectedObject = null; // currently selected object
    private JPanel infoPanel;
    private JTextArea infoArea;

    

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(87, 166, 57));

        resources = new ResourceLoader();
        initObjects();

        // Register this panel to receive key events
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseListener((MouseListener) this);
        
        
        // Setup info panel
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(200, HEIGHT));
        infoPanel.setBackground(Color.LIGHT_GRAY);
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

    }


    public JPanel getInfoPanel() {
        return infoPanel;
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
            sb.append("Position: ").append(selectedObject.pos.x).append(", ").append(selectedObject.pos.y).append("\n");
            sb.append("Size: ").append(selectedObject.size).append("\n");
            if (selectedObject instanceof Animal) {
                sb.append("Health: ").append(((Animal)selectedObject).health).append("\n");
            }
            if (selectedObject instanceof Plant) {
                sb.append("Growth: ").append(((Plant)selectedObject).getGrowth()).append("\n");
            }
            infoArea.setText(sb.toString());
        } else {
            infoArea.setText("");
        }
    }


    @Override
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();

        for (GameObject obj : objects) obj.render(g);

        g.dispose();
        
        if (selectedObject != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.WHITE);
            g2.fillRect(10, 10, 200, 120); // info panel background

            g2.setColor(Color.BLACK);
            g2.drawString("Type: " + selectedObject.getClass().getSimpleName(), 20, 30);
            g2.drawString("Position: " + selectedObject.pos.x + ", " + selectedObject.pos.y, 20, 50);
            
            if (selectedObject instanceof Animal) {
                Animal a = (Animal) selectedObject;
                g2.drawString("Health: " + a.health, 20, 70);
                g2.drawString("Alive: " + a.isAlive, 20, 90);
            }
            
            if (selectedObject instanceof Plant) {
                Plant p = (Plant) selectedObject;
                g2.drawString("Growth: " + p.getGrowth(), 20, 110);
            }
            
            g2.dispose();
        }

    }


    // ---------------- KeyListener ----------------
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

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
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}


    

}
