package gameframe;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;

public class Animal extends Entity {

    //1 is male, 2 is female
    private int gender;
    private int age;
    private int hunger;
    private int thirst;
    private int maxHunger;
    private int maxThirst;
    private int reproductiveUrge;
    private boolean restrictedVision;
    protected Entity target;
    protected int barLength, barHeight;
    public HashMap<String, Integer> favourableness = new HashMap();

    public Animal(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength) {
        super(x, y, entities, entitygrid, gridLength);
        gender = (int) (Math.random() * 2) + 1;
        age = 0;
        hunger = 0;
        thirst = 0;
        reproductiveUrge = 0;
        restrictedVision = false;
        barLength = gridLength / 2;
        barHeight = gridLength / 16;
        favourableness.put("up", 0);
        favourableness.put("down", 0);
        favourableness.put("left", 0);
        favourableness.put("right", 0);
    }

    public int getAge() {
        return (age);
    }

    public int getHunger() {
        return (hunger);
    }

    public int getThirst() {
        return (thirst);
    }

    public int getMaxHunger() {
        return (maxHunger);
    }

    public int getMaxThirst() {
        return (maxThirst);
    }
    
    public void setHunger(int hunger){
        this.hunger = hunger;
    }

    public void setThirst(int thirst){
        this.thirst = thirst;
    }
    
    public void setReproductiveUrge(int reproductiveUrge){
        this.reproductiveUrge = reproductiveUrge;
    }
    
    public void setGender(int gender){
        this.gender = gender;
    }
    
    public void setMaxHunger(int maxHunger) {
        this.maxHunger = maxHunger;
    }

    public void setMaxThirst(int maxThirst) {
        this.maxThirst = maxThirst;
    }

    public int getReproductiveUrge() {
        return (reproductiveUrge);
    }

    public void resetReproductiveUrge() {
        reproductiveUrge = 0;
    }

    public int getGender() {
        return (gender);
    }
    
    public boolean getRestrictedVision(){
        return(restrictedVision);
    }
    
    public void setRestrictedVision(boolean restrictedVision){
        this.restrictedVision = restrictedVision;
    }
    


    @Override
    public void act() {
        age++;
        hunger++;
        thirst++;
        reproductiveUrge++;
        if (getAge() > 1000 || getHunger() > maxHunger || getThirst() > maxThirst) {
            die();
        }
        //look for water
        if (getThirst() >= getHunger() && getThirst() >= maxThirst / 4) {
            int minDistance = Integer.MAX_VALUE;
            for (int k = 0; k < entities.size(); k++) {
                if (entities.get(k) instanceof Water) {
                    if (minDistance > distanceTo(entities.get(k))) {
                        if (!restrictedVision || (restrictedVision && distanceTo(entities.get(k)) < 10)){
                            minDistance = distanceTo(entities.get(k));
                            target = entities.get(k);
                        }

                    }
                }
            }
        }

        if (target == null) {
            wander();
        } 
        else if (distanceTo(target) > 1) {
            move();
        } 
        else if (target instanceof Water && !((Water)target).getHasFish()) {
            drink();
        }
        else if (target instanceof Water && ((Water)target).getHasFish()){
            eat(target);
        }
        else if (target instanceof Food || (target instanceof Deer && this instanceof Wolf)) {
            eat(target);
        
        } 
        else if (this.getClass() == target.getClass()) {
            mate((Animal) target);
        }

    }

    public void move() {
        favourableness.replace("up", 0);
        favourableness.replace("down", 0);
        favourableness.replace("left", 0);
        favourableness.replace("right", 0);
        //check how favourable up is
        if (getGridY() - 1 >= 0) {
            if (entitygrid[getGridX()][getGridY() - 1] != null) {
                favourableness.replace("up", Integer.MIN_VALUE);
            } else if (target.getGridY() < getGridY()) {
                favourableness.replace("up", 1);
            }
        } else {
            favourableness.replace("up", Integer.MIN_VALUE);
        }
        //check how favourable down is
        if (getGridY() + 1 < entitygrid.length) {
            if (entitygrid[getGridX()][getGridY() + 1] != null) {
                favourableness.replace("down", Integer.MIN_VALUE);
            } else if (target.getGridY() > getGridY()) {
                favourableness.replace("down", 1);
            }
        } else {
            favourableness.replace("down", Integer.MIN_VALUE);
        }
        //check how favourable left is
        if (getGridX() - 1 >= 0) {
            if (entitygrid[getGridX() - 1][getGridY()] != null) {
                favourableness.replace("left", Integer.MIN_VALUE);
            } else if (target.getGridX() < getGridX()) {
                favourableness.replace("left", 1);
            }
        } else {
            favourableness.replace("left", Integer.MIN_VALUE);
        }
        //check how favourable right is
        if (getGridX() + 1 < entitygrid[0].length) {
            if (entitygrid[getGridX() + 1][getGridY()] != null) {
                favourableness.replace("right", Integer.MIN_VALUE);
            } else if (target.getGridX() > getGridX()) {
                favourableness.replace("right", 1);
            }
        } else {
            favourableness.replace("right", Integer.MIN_VALUE);
        }

        //if one direction stands out, move in that direction
        if (favourableness.get("up") > favourableness.get("down") && favourableness.get("up") > favourableness.get("left") && favourableness.get("up") > favourableness.get("right")) {
            moveUp();
        } else if (favourableness.get("down") > favourableness.get("up") && favourableness.get("down") > favourableness.get("left") && favourableness.get("down") > favourableness.get("right")) {
            moveDown();
        } else if (favourableness.get("left") > favourableness.get("up") && favourableness.get("left") > favourableness.get("down") && favourableness.get("left") > favourableness.get("right")) {
            moveLeft();
        } else if (favourableness.get("right") > favourableness.get("up") && favourableness.get("right") > favourableness.get("down") && favourableness.get("right") > favourableness.get("left")) {
            moveRight();
        } else {
            int random = (int) (Math.random() * 2) + 1;
            //if up is equally favourable as sth else
            if (Objects.equals(favourableness.get("up"), favourableness.get("down")) && favourableness.get("up") > favourableness.get("left")) {
                if (random == 1) {
                    moveUp();
                } else {
                    moveDown();
                }
            } else if ((Objects.equals(favourableness.get("up"), favourableness.get("left"))) && favourableness.get("up") > favourableness.get("down")) {
                if (random == 1) {
                    moveUp();
                } else {
                    moveLeft();
                }
            } else if (Objects.equals(favourableness.get("up"), favourableness.get("right")) && favourableness.get("up") > favourableness.get("down")) {
                if (random == 1) {
                    moveUp();
                } else {
                    moveRight();
                }
            } //down
            else if (Objects.equals(favourableness.get("down"), favourableness.get("up")) && favourableness.get("down") > favourableness.get("left")) {
                if (random == 1) {
                    moveDown();
                } else {
                    moveUp();
                }
            } else if (Objects.equals(favourableness.get("down"), favourableness.get("left")) && favourableness.get("down") > favourableness.get("up")) {
                if (random == 1) {
                    moveDown();
                } else {
                    moveLeft();
                }
            } else if (Objects.equals(favourableness.get("down"), favourableness.get("right")) && favourableness.get("down") > favourableness.get("up")) {
                if (random == 1) {
                    moveDown();
                } else {
                    moveRight();
                }
            } //left
            else if (Objects.equals(favourableness.get("left"), favourableness.get("up")) && favourableness.get("left") > favourableness.get("right")) {
                if (random == 1) {
                    moveLeft();
                } else {
                    moveUp();
                }
            } else if (Objects.equals(favourableness.get("left"), favourableness.get("down")) && favourableness.get("left") > favourableness.get("right")) {
                if (random == 1) {
                    moveLeft();
                } else {
                    moveDown();
                }
            } else if (Objects.equals(favourableness.get("left"), favourableness.get("right")) && favourableness.get("left") > favourableness.get("down")) {
                if (random == 1) {
                    moveLeft();
                } else {
                    moveRight();
                }
            } //right
            else if (Objects.equals(favourableness.get("right"), favourableness.get("up")) && favourableness.get("right") > favourableness.get("left")) {
                if (random == 1) {
                    moveRight();
                } else {
                    moveUp();
                }
            } else if (Objects.equals(favourableness.get("right"), favourableness.get("down")) && favourableness.get("right") > favourableness.get("left")) {
                if (random == 1) {
                    moveRight();
                } else {
                    moveDown();
                }
            } else if (Objects.equals(favourableness.get("right"), favourableness.get("left")) && favourableness.get("right") > favourableness.get("down")) {
                if (random == 1) {
                    moveRight();
                } else {
                    moveLeft();
                }
            }
        }
    }

    public void wander() {
        int random = (int) (Math.random() * 4) + 1;
        switch (random) {
            case 1:
                if (getGridY() - 1 >= 0) {
                    if (entitygrid[getGridX()][getGridY() - 1] == null) {
                        moveUp();
                    }
                }
                break;
            case 2:
                if (getGridY() + 1 < entitygrid.length) {
                    if (entitygrid[getGridX()][getGridY() + 1] == null) {
                        moveDown();
                    }
                }
                break;
            case 3:
                if (getGridX() - 1 >= 0) {
                    if (entitygrid[getGridX() - 1][getGridY()] == null) {
                        moveLeft();
                    }
                }
                break;
            case 4:
                if (getGridX() + 1 < entitygrid[0].length) {
                    if (entitygrid[getGridX() + 1][getGridY()] == null) {
                        moveRight();
                    }
                }
                break;
        }
    }

    public void eat(Entity f) {
        //eat code
        if (f instanceof Food) {
            if (hunger - ((Food) f).getNutritionVal() > 0) {
                hunger -= ((Food) f).getNutritionVal();
            } else {
                hunger = 0;
            }
        } 
        else if (f instanceof Deer) {
            if (hunger - 100 > 0) {
                hunger -= 100;
            } else {
                hunger = 0;
            }
        }
        if (f instanceof Deer) {
            f.die();
        } 
        else if (f instanceof Food) {
            ((Food) f).setAge(0);
        }
        if (f instanceof PoisonBerries) {
            die();
        }
        
        if (f instanceof Water && ((Water)f).getHasFish()){
            hunger -= 35;
            ((Water)f).setHasFish(false);
        }
    }

    public void drink() {
        if (thirst - 10 > 0) {
            thirst -= 10;
        } else {
            thirst = 0;
        }
    }

    public void mate(Animal m) {
        //fix out of bounds
        if (this instanceof Bear && this.getGender() == 2) {
            if (getGridX() + 1 < entitygrid[0].length) {
                if (entitygrid[getGridX() + 1][getGridY()] == null) {
                    Bear bear = new Bear(getGridX() + 1, getGridY(), entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridX() - 1 >= 0) {
                if (entitygrid[getGridX() - 1][getGridY()] == null) {
                    Bear bear = new Bear(getGridX() - 1, getGridY(), entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridY() + 1 < entitygrid.length) {
                if (entitygrid[getGridX()][getGridY() + 1] == null) {
                    Bear bear = new Bear(getGridX(), getGridY() + 1, entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridY() - 1 >= 0) {
                if (entitygrid[getGridX()][getGridY() - 1] == null) {
                    Bear bear = new Bear(getGridX(), getGridY() - 1, entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
        }

        if (this instanceof Wolf && this.getGender() == 2) {
            if (getGridX() + 1 < entitygrid[0].length) {
                if (entitygrid[getGridX() + 1][getGridY()] == null) {
                    Wolf wolf = new Wolf(getGridX() + 1, getGridY(), entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridX() - 1 >= 0) {
                if (entitygrid[getGridX() - 1][getGridY()] == null) {
                    Wolf wolf = new Wolf(getGridX() - 1, getGridY(), entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridY() + 1 < entitygrid.length) {
                if (entitygrid[getGridX()][getGridY() + 1] == null) {
                    Wolf wolf = new Wolf(getGridX(), getGridY() + 1, entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridY() - 1 >= 0) {
                if (entitygrid[getGridX()][getGridY() - 1] == null) {
                    Wolf wolf = new Wolf(getGridX(), getGridY() - 1, entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
        }
        if (this instanceof Deer && this.getGender() == 2) {
            if (getGridX() + 1 < entitygrid[0].length) {
                if (entitygrid[getGridX() + 1][getGridY()] == null) {
                    Deer deer = new Deer(getGridX() + 1, getGridY(), entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridX() - 1 >= 0) {
                if (entitygrid[getGridX() - 1][getGridY()] == null) {
                    Deer deer = new Deer(getGridX() - 1, getGridY(), entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridY() + 1 < entitygrid.length) {
                if (entitygrid[getGridX()][getGridY() + 1] == null) {
                    Deer deer = new Deer(getGridX(), getGridY() + 1, entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
            if (getGridY() - 1 >= 0) {
                if (entitygrid[getGridX()][getGridY() - 1] == null) {
                    Deer deer = new Deer(getGridX(), getGridY() - 1, entities, entitygrid, getGridLength());
                    resetReproductiveUrge();
                    m.resetReproductiveUrge();
                    return;
                }
            }
        }

    }

}
