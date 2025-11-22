package sheepfarm;

public class FoodComponent {
    private int nutrition;
    private boolean consumed = false;

    public FoodComponent(int nutrition) {
        this.nutrition = nutrition;
    }

    public int consume() {
        if (consumed) return 0;
        consumed = true;
        return nutrition;
    }

    public boolean isConsumed() {
        return consumed;
    }



}
