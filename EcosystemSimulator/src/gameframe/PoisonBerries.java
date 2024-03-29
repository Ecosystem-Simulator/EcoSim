package gameframe;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PoisonBerries extends Berries {

    public PoisonBerries(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        setNutritionVal(0);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 153, 0));
        g.fillOval(getX() - getLength() / 2, getY() - getHeight() / 2, getLength(), getHeight());
        g.setColor(new Color(153, 0, 76));
        if (getAge() < getRipeAge()) {
            g.fillOval(getX() - getLength() / 4, getY() - getHeight() / 4, getLength() / 4 * getAge() / getRipeAge(), getHeight() / 4 * getAge() / getRipeAge());
            g.fillOval(getX() + getLength() / 4, getY() - getHeight() / 8, getLength() / 4 * getAge() / getRipeAge(), getHeight() / 4 * getAge() / getRipeAge());
        } else {
            g.fillOval(getX() - getLength() / 4, getY() - getHeight() / 4, getLength() / 4, getHeight() / 4);
            g.fillOval(getX() + getLength() / 4, getY() - getHeight() / 8, getLength() / 4, getHeight() / 4);

        }
    }
}
