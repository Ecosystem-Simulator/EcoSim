package gameframe;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Bear extends Animal {

    private int eyeSize;
    private int earSize;
    private int snoutSize;

    private Color black = new Color(0, 0, 0);
    private Color brown = new Color(141, 74, 22);
    private Color cream = new Color(201, 158, 105);
    private Color white = new Color(255, 255, 255);

    public Bear(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        setMaxHunger(100);
        setMaxThirst(100);
        if (getGender() == 2) {
            setHeight(getHeight() * 3 / 4);
            setLength(getLength() * 3 / 4);
        }
    }

    public Bear(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, int gender, int hunger, int thirst, int reproductiveUrge) {
        super(x, y, entities, entitygrid, gridLength);
        setMaxHunger(200);
        setMaxThirst(100);
        setGender(gender);
        setHunger(hunger);
        setThirst(thirst);
        setReproductiveUrge(reproductiveUrge);
        if (getGender() == 2) {
            setHeight(getHeight() * 3 / 4);
            setLength(getLength() * 3 / 4);
        }
    }
    @Override
    public void draw(Graphics g) {

        //draw ears
        g.setColor(brown);
        earSize = getLength() / 2;
        g.fillOval(getX() - getLength() / 2 - earSize / 4, getY() - getLength() / 2 - earSize / 4, earSize, earSize);
        g.fillOval(getX() + getLength() / 2 - earSize * 3 / 4, getY() - getLength() / 2 - earSize / 4, earSize, earSize);
        //draw inside of ears
        earSize = getLength() / 4;
        g.setColor(cream);
        g.fillOval(getX() - getLength() / 2, getY() - getLength() / 2, earSize, earSize);
        g.fillOval(getX() + getLength() / 2 - earSize, getY() - getLength() / 2, earSize, earSize);
        //draw body
        g.setColor(brown);
        g.fillOval(getX() - getLength() / 2, getY() - getHeight() / 2, getLength(), getHeight());
        //draw snout
        g.setColor(cream);
        snoutSize = getLength() * 5 / 12;
        g.fillOval(getX() - snoutSize / 2, getY(), snoutSize, snoutSize);
        //draw eyes
        g.setColor(black);
        eyeSize = getLength() / 5;
        g.fillOval(getX() - getLength() / 4 - eyeSize / 2, getY() - getHeight() / 4, eyeSize, eyeSize);
        g.fillOval(getX() + getLength() / 4 - eyeSize / 2, getY() - getHeight() / 4, eyeSize, eyeSize);

        g.setColor(white);
        g.fillOval(getX() - getLength() / 4 - eyeSize / 4, getY() - getHeight() / 4, eyeSize / 2, eyeSize / 2);
        g.fillOval(getX() + getLength() / 4 - eyeSize / 4, getY() - getHeight() / 4, eyeSize / 2, eyeSize / 2);

        //draw nose
        g.setColor(black);
        g.fillOval(getX() - snoutSize / 6, getY() + snoutSize / 8, snoutSize / 3, snoutSize / 4);

        //draw bars
        //hunger
        barLength = getGridLength() / 2;
        barHeight = getGridLength() / 16;
        g.setColor(new Color(255, 0, 0));
        g.fillRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight, barLength * getHunger() / getMaxHunger(), barHeight);

        //thirst
        g.setColor(new Color(0, 0, 255));
        g.fillRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight * 2, barLength * getThirst() / getMaxThirst(), barHeight);

        g.setColor(black);
        g.drawRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight, barLength, barHeight);
        g.drawRect(getX() - barLength / 2, getY() + getHeight() / 2 + barHeight * 2, barLength, barHeight);
    }

    @Override
    public void act() {
        target = null;
        //food
        if (getHunger() > getThirst() && getHunger() > getMaxHunger() / 4) {
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if ((entities.get(k) instanceof Berries) && ((Food) entities.get(k)).getAge() >= ((Food) entities.get(k)).getRipeAge()) {
                    if (minDistance > distanceTo(entities.get(k))) {
                        if (!getRestrictedVision() || (getRestrictedVision() && distanceTo(entities.get(k)) < 5)){
                            minDistance = distanceTo(entities.get(k));
                            target = entities.get(k);
                        }

                    }
                }
                else if (entities.get(k) instanceof Water){
                    if (((Water) entities.get(k)).getHasFish()){
                        if (minDistance > distanceTo(entities.get(k))) {
                            if (!getRestrictedVision() || (getRestrictedVision() && distanceTo(entities.get(k)) < 5)){
                                minDistance = distanceTo(entities.get(k));
                                target = entities.get(k);
                            }
                            
                        }
                    }
                }
            }
        }
        //mating
        else if (getReproductiveUrge() > 250) {
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if (entities.get(k) instanceof Bear) {
                    Bear tempBear = ((Bear) entities.get(k));
                    if (tempBear.getGender() != (getGender()) && tempBear.getReproductiveUrge() > 250) {
                        if (minDistance > distanceTo(entities.get(k))) {
                            if (!getRestrictedVision() || (getRestrictedVision() && distanceTo(entities.get(k)) < 5)) {
                                minDistance = distanceTo(entities.get(k));
                                target = entities.get(k);
                            }
                        }
                    }
                }
            }
        }
        super.act();
    }
}
