package sheepfarm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 800;

    public List<GameObject> objects = new ArrayList<>();
    public ResourceLoader resources;
    public Player player;

    public List<GameObject> toAdd = new ArrayList<>();
    public List<GameObject> toRemove = new ArrayList<>();
    
    private Thread gameThread;
    private boolean running = false;
    
    private GameObject hoveredObject = null;
    private GameObject selectedObject = null; // currently selected object
    
    private int grassSpawnCooldown = 0; // ticks until next spawn
    private int grassSpawnRate = 2000;   // spawn a grass every 300 ticks (~5 seconds at 60FPS)
    private int treeSpawnCooldown = 0;
    private int treeSpawnRate = 3000;  // ticks (adjust as desired)


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


    }


    
    private void initObjects() {
        // --- Load sprites ---
    	resources.loadFolder("assets", true);  // animals like sheep
    	resources.loadFolder("assets", false); // static sprites like grass/buildings


        System.out.println("Loaded sprites: " + resources.getLoadedKeys());

        // --- Spawn player ---
        PlayerLoader loader = new PlayerLoader();
        PlayerSprites playerSprites = loader.load("assets/player_spritesheet.png", "assets/player_spritesheet.json");
        if (playerSprites != null) {
        	player = new Player(WIDTH / 2.0, HEIGHT / 2.0, 64, playerSprites, 100);

        	addObject(player);
        } else {
            System.err.println("Failed to load player!");
        }

        // --- Spawn a sheep ---
        BufferedImage[] sheepRight = resources.get("sheep_spritesheet_right");
        BufferedImage[] sheepLeft  = resources.get("sheep_spritesheet_left");
        BufferedImage[] sheepDead  = resources.get("sheep_dead_right");

        if (sheepRight != null && sheepLeft != null && sheepDead != null) {
            for (int i = 0; i < 5; i++) {
                double x = Math.random() * WIDTH;
                double y = Math.random() * HEIGHT;
                Sheep sheep = new Sheep(x, y, 32, sheepRight, sheepLeft, sheepDead);
                addObject(sheep);
            }
        }

        // --- Spawn grass patches, fully grown ---
        BufferedImage[] grassFrames = resources.get("grass");
        if (grassFrames != null && grassFrames.length > 0) {
            for (int i = 0; i < 20; i++) {
                double x = Math.random() * WIDTH;
                double y = Math.random() * HEIGHT;
                Grass g = new Grass(x, y, 20, grassFrames[0]);
                g.setGrowth(g.getMaxGrowth()); // ensure fully grown
                addObject(g);
            }
        }
        
        
        BufferedImage[] treeSprites = resources.get("tree");
        if (treeSprites != null && treeSprites.length >= 4) {
            BufferedImage[] stages = new BufferedImage[4];
            System.arraycopy(treeSprites, 0, stages, 0, 4);

            int treeFood = 50; // example base food value for trees

            for (int i = 0; i < 5; i++) {
                double x = Math.random() * Game.WIDTH;
                double y = Math.random() * Game.HEIGHT;
                Tree t = new Tree(x, y, 32, stages, treeFood);
                t.setGrowth(t.getMaxGrowth()); // fully grown
                addObject(t);
            }
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
        // --- Update all objects ---
        for (GameObject obj : objects) {
            obj.update(this);  // objects can now safely queue additions/removals
        }

        // --- Random grass spawning ---
        if (grassSpawnCooldown <= 0) {
            spawnRandomGrass();
            grassSpawnCooldown = grassSpawnRate;
        } else {
            grassSpawnCooldown--;
        }

        // --- Random tree spawning ---
        if (treeSpawnCooldown <= 0) {
            spawnRandomTree();
            treeSpawnCooldown = treeSpawnRate;
        } else {
            treeSpawnCooldown--;
        }

        // --- Queue eaten plants for removal ---
        for (GameObject obj : objects) {
            if (obj instanceof Plant && ((Plant) obj).isEaten()) {
                removeObject(obj);
            }
        }

        // --- Apply queued additions/removals ---
        objects.addAll(toAdd);
        objects.removeAll(toRemove);
        toAdd.clear();
        toRemove.clear();
    }


    private void renderObjects(Graphics2D g) {
        // Sort objects by their pos.y (lower y = behind, higher y = in front)
        objects.sort((a, b) -> Double.compare(a.pos.y, b.pos.y));

        for (GameObject obj : objects) {
            obj.render(g);
        }
    }

    public void addObject(GameObject obj) {
        toAdd.add(obj);
    }

    public void removeObject(GameObject obj) {
        toRemove.add(obj);
    }


    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Render all objects in y-order
        renderObjects(g);

        // Highlight selected object
        if (selectedObject != null) {
            g.setColor(new Color(255, 255, 0, 128));
            int radius = (selectedObject instanceof Player) ? 32 : selectedObject.size;
            g.fillOval((int)(selectedObject.pos.x - radius), (int)(selectedObject.pos.y - radius),
                       radius * 2, radius * 2);
        }

        // Draw info overlay for hovered or selected object
        GameObject obj = (hoveredObject != null) ? hoveredObject : selectedObject;
        if (obj != null) {
            g.setColor(new Color(0, 0, 0, 200)); // semi-transparent background
            StringBuilder sb = new StringBuilder();
            sb.append("Class: ").append(obj.getClass().getSimpleName()).append("\n");
            sb.append("X: ").append(String.format("%.1f", obj.pos.x)).append(" Y: ").append(String.format("%.1f", obj.pos.y)).append("\n");
            if (obj instanceof Animal) {
                Animal a = (Animal)obj;
                sb.append("Health: ").append(a.health).append("\n");
                sb.append("Hunger: ").append(String.format("%.1f", a.hunger)).append("\n");
                sb.append("Hunger Threshold: ").append(a.hungerThreshold).append("\n");
            }

            if (obj instanceof Plant) {
                Plant p = (Plant) obj;
                sb.append("Growth: ").append(String.format("%.1f", p.getGrowth())).append("/").append(p.getMaxGrowth()).append("\n");
                sb.append("Food Value: ").append(p.getFoodValue()).append("\n");
            }

            String[] lines = sb.toString().split("\n");
            int boxWidth = 0;
            int lineHeight = g.getFontMetrics().getHeight();
            for (String line : lines) boxWidth = Math.max(boxWidth, g.getFontMetrics().stringWidth(line));
            int boxHeight = lineHeight * lines.length + 4;

            int drawX = (int)obj.pos.x + obj.size;
            int drawY = (int)obj.pos.y - obj.size - boxHeight;

            g.fillRect(drawX, drawY, boxWidth + 6, boxHeight);
            g.setColor(Color.WHITE);
            for (int i = 0; i < lines.length; i++) {
                g.drawString(lines[i], drawX + 3, drawY + lineHeight * (i + 1) - 2);
            }
        }

        g.dispose();
    }

    
    private void spawnRandomGrass() {
        BufferedImage[] grassFrames = resources.get("grass_right");
        if (grassFrames != null && grassFrames.length > 0) {
            double x = Math.random() * WIDTH;
            double y = Math.random() * HEIGHT;
            addObject(new Grass(x, y, 20, grassFrames[0]));
        }
    }

 // --- Random tree spawning (reuse grass logic) ---
    private void spawnRandomTree() {
        BufferedImage[] treeSprites = resources.get("tree");
        if (treeSprites != null && treeSprites.length >= 4) {
            BufferedImage[] stages = new BufferedImage[4];
            System.arraycopy(treeSprites, 0, stages, 0, 4);

            double x = Math.random() * WIDTH;
            double y = Math.random() * HEIGHT;
            Tree t = new Tree(x, y, 32, stages, 50); // assign food value
            addObject(t);
        }
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
            if (obj.isPointInBase(e.getX(), e.getY())) {
                selectedObject = obj;
            }

        }

    }

    

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // ---------------- MouseMotionListener ----------------
    @Override public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        hoveredObject = null;
        for (GameObject obj : objects) {
            double dx = obj.pos.x - e.getX();
            double dy = obj.pos.y - e.getY();
            double distance = Math.sqrt(dx*dx + dy*dy);
            if (obj.isPointInBase(e.getX(), e.getY())) {
                hoveredObject = obj;
            }

        }
    }



}
