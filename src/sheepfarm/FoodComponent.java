package sheepfarm;

public class FoodComponent {
    private int foodValue;
    private boolean consumed;

    public FoodComponent(int foodValue) {
        this.foodValue = foodValue;
        this.consumed = false;
    }
    
    public int getFoodValue() {
		return foodValue;
	}

    public int consume() {
        if (consumed) return 0;
        consumed = true;
        return foodValue;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void refill() {
        consumed = false;
    }


}
