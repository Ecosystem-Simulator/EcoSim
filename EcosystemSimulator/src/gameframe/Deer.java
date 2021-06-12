package gameframe;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Deer extends Animal {

    private int eyeSize;
    private int snoutSize;
    private int noseSize;
    private int earSize;

    private Color brown = new Color(136, 71, 0);
    private Color cream = new Color(253, 187, 121);
    private Color black = new Color(0, 0, 0);
    private Color white = new Color(255, 255, 255);
    private Color darkbrown = new Color(95, 57, 11);

    public Deer(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(gridX, gridY, entities, entitygrid, gridLength);
    }

    @Override
    public void draw(Graphics g) {
        //draw ears
        g.setColor(brown);
        earSize = getLength() * 2 / 3;
        g.fillOval(getX() - getLength() * 2 / 3, getY() - getHeight() / 2, earSize, earSize / 2);
        g.fillOval(getX() + getLength() * 2 / 3 - earSize, getY() - getHeight() / 2, earSize, earSize / 2);

        g.setColor(cream);
        earSize = earSize / 2;
        g.fillOval(getX() - getLength() / 3 - earSize / 2, getY() - getHeight() / 2 + earSize / 4, earSize, earSize / 2);
        g.fillOval(getX() + getLength() / 3 - earSize / 2, getY() - getHeight() / 2 + earSize / 4, earSize, earSize / 2);

        //draw head
        g.setColor(brown);
        g.fillOval(getX() - getLength() * 1 / 3, getY() - getHeight() / 2, getLength() * 2 / 3, getHeight());

        //draw eyes
        g.setColor(black);
        eyeSize = getLength() / 5;
        g.fillOval(getX() - getLength() / 4 - eyeSize / 2, getY() - getHeight() / 4, eyeSize, eyeSize);
        g.fillOval(getX() + getLength() / 4 - eyeSize / 2, getY() - getHeight() / 4, eyeSize, eyeSize);

        g.setColor(white);
        g.fillOval(getX() - getLength() / 4 - eyeSize / 4, getY() - getHeight() / 4, eyeSize / 2, eyeSize / 2);
        g.fillOval(getX() + getLength() / 4 - eyeSize / 4, getY() - getHeight() / 4, eyeSize / 2, eyeSize / 2);

        //draw snout
        g.setColor(cream);
        snoutSize = getHeight() / 4;
        g.fillOval(getX() - snoutSize / 2, getY() + snoutSize / 2, snoutSize, snoutSize);

        //draw nose
        g.setColor(darkbrown);
        noseSize = snoutSize / 2;
        g.fillOval(getX() - noseSize / 2, getY() + snoutSize / 2, noseSize, noseSize);

    }

    public void act() {
        target = null;
        if (getHunger() > getThirst() && getHunger() > 25) {
            int minDistance = Integer.MAX_VALUE;
            int row = 0;
            int col = 0;
            for (int k = 0; k < entities.size(); k++) {
                if (entities.get(k) instanceof Berries || entities.get(k) instanceof Grass) {
                    if (minDistance > distanceTo(entities.get(k))) {
                        minDistance = distanceTo(entities.get(k));
                        target = entities.get(k);
                    }
                }
            }
        } else if (getReproductiveUrge() > 10) {
            //set back to 200
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if (entities.get(k) instanceof Deer) {
                    Deer tempDeer = ((Deer) entities.get(k));
                    if (tempDeer.getGender() != (getGender()) && tempDeer.getReproductiveUrge() > 10) {
                        if (minDistance > distanceTo(entities.get(k))) {
                            minDistance = distanceTo(entities.get(k));
                            target = entities.get(k);
                        }
                    }
                }
            }
        }
        super.act();
    }
}
