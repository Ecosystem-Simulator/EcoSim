package gameframe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author 1090720
 */
public class Berries extends Food {

    public Berries(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        setNutritionVal((int) (Math.random() * 10) + 20);
        setRipeAge(10);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 153, 0));
        g.fillOval(getX() - getLength() / 2, getY() - getHeight() / 2, getLength(), getHeight());
        g.setColor(new Color(204, 0, 0));
        if (getAge() < getRipeAge()) {
            g.fillOval(getX() - getLength() / 4, getY() - getHeight() / 4, getLength() / 4 * getAge() / getRipeAge(), getHeight() / 4 * getAge() / getRipeAge());
            g.fillOval(getX() + getLength() / 4, getY() - getHeight() / 8, getLength() / 4 * getAge() / getRipeAge(), getHeight() / 4 * getAge() / getRipeAge());
        } else {
            g.fillOval(getX() - getLength() / 4, getY() - getHeight() / 4, getLength() / 4, getHeight() / 4);
            g.fillOval(getX() + getLength() / 4, getY() - getHeight() / 8, getLength() / 4, getHeight() / 4);

        }
    }
}
