package gameframe;

import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

public class Deer extends Animal {

    private int eyeSize;
    private int snoutSize;
    private int noseSize;
    private int earSize;
    private int antlerSize;

    private Color brown = new Color(170, 106, 44);
    private Color cream = new Color(253, 187, 121);
    private Color black = new Color(0, 0, 0);
    private Color white = new Color(255, 255, 255);
    private Color darkbrown = new Color(95, 57, 11);

    public Deer(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(gridX, gridY, entities, entitygrid, gridLength);
        setMaxHunger(150);
        setMaxThirst(100);
    }

    @Override
    public void draw(Graphics g) {
        //draw antlers on males
        if (getGender() == 1) {
            g.setColor(darkbrown);
            antlerSize = getHeight() * 2 / 3;
            g.fillRect(getX() - getLength() / 6 - antlerSize / 16, getY() - getHeight() / 2 - antlerSize / 2, antlerSize / 8, antlerSize);
            g.fillRect(getX() + getLength() / 6 - antlerSize / 16, getY() - getHeight() / 2 - antlerSize / 2, antlerSize / 8, antlerSize);
        }
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
        //g.fillOval(getX() - getLength() / 3, getY() - getHeight() / 2, getLength() * 2 / 3, getHeight());
        g.fillOval(getX() - getLength() / 3, getY() - getHeight() / 3, getLength() * 2 / 3, getHeight() * 2 / 3);
        g.fillOval(getX() - getLength() / 4, getY() - getHeight() / 2, getLength() / 2, getHeight());
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
        g.fillOval(getX() - snoutSize / 2, getY() + getHeight() / 4, snoutSize, snoutSize);

        //draw nose
        g.setColor(darkbrown);
        noseSize = snoutSize / 2;
        g.fillOval(getX() - noseSize / 2, getY() + getHeight() / 4 + noseSize / 2, noseSize, noseSize / 2);

        //draw bars
        g.setColor(new Color(255, 0, 0));
        g.fillRect(getX() - barLength / 2, getY() + getHeight() * 2 / 3 + barHeight / 2, barLength * getHunger() / getMaxHunger(), barHeight);

        g.setColor(new Color(0, 0, 255));
        g.fillRect(getX() - barLength / 2, getY() + getHeight() * 2 / 3 + barHeight * 3 / 2, barLength * getThirst() / getMaxThirst(), barHeight);

        g.setColor(black);
        g.drawRect(getX() - barLength / 2, getY() + getHeight() * 2 / 3 + barHeight / 2, barLength, barHeight);
        g.drawRect(getX() - barLength / 2, getY() + getHeight() * 2 / 3 + barHeight * 3 / 2, barLength, barHeight);
    }

    public void act() {
        target = null;
        //look for food
        if (getHunger() > getThirst() && getHunger() > getMaxHunger() / 4) {
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if ((entities.get(k) instanceof Berries || entities.get(k) instanceof Grass) && ((Food) entities.get(k)).getAge() >= ((Food) entities.get(k)).getRipeAge()) {
                    if (minDistance > distanceTo(entities.get(k))) {
                        minDistance = distanceTo(entities.get(k));
                        target = entities.get(k);
                    }
                }
            }
            //look for mate
        } else if (getReproductiveUrge() > 50) {
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
