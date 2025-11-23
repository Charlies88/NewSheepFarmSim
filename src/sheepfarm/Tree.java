//package sheepfarm;
//
//import java.awt.Graphics2D;
//
//public class Tree extends Plant {
//    private BufferedImage sprite;
//
//    public Tree(double x, double y, int size, BufferedImage sprite) {
//        super(x, y, size, 0); // no food
//        this.sprite = sprite;
//        // footprint only at base
//        footprint = new RectFootprint(pos.x - size, pos.y - 5, size*2, 5);
//    }
//
//    @Override
//    public void update(Game g) {
//        super.update(g); // applies physics, base stays for collisions
//    }
//
//    @Override
//    public void render(Graphics2D g) {
//        g.drawImage(sprite, (int)(pos.x - size), (int)(pos.y - size),
//                    size*2, size*2, null);
//    }
//}
