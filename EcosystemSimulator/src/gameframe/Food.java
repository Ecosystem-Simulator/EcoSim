package gameframe;

import java.util.ArrayList;

/**
 *
 * @author thuph
 */
public class Food extends Entity {

    private int age;
    private int ripeAge;
    private int nutritionVal;

    public Food(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        age = 0;
    }

    public int getNutritionVal() {
        return nutritionVal;
    }

    public void setNutritionVal(int nutritionVal) {
        this.nutritionVal = nutritionVal;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRipeAge() {
        return ripeAge;
    }

    public void setRipeAge(int ripeAge) {
        this.ripeAge = ripeAge;
    }

    @Override
    public void act() {
        age++;
    }
}
