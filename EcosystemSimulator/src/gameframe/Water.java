package gameframe;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Water extends Terrain{
    private boolean hasFish;
    public Water(int x, int y, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength, boolean passable){
        super(x, y, entities, entitygrid, gridLength, passable);
    }
    
    public boolean getHasFish(){
        return(hasFish);
    }
    
    public void setHasFish(boolean hasFish){
        this.hasFish = hasFish;
    }
    @Override
    public void draw(Graphics g){
        g.setColor(new Color(58, 142, 216));
        g.fillRect(getX() - getGridLength()/2, getY() - getGridLength()/2, getGridLength(), getGridLength());
        if (hasFish == true){
            g.setColor(new Color(253, 162, 180));
            g.fillOval(getX() - getLength() / 4, getY() - getHeight() / 2, getLength() / 2, getHeight());
        }
    }
    
    @Override
    public void act(){
        if (this.getHasFish()) {
            int rand = (int)(Math.random()*4)+1;
            if (rand == 1){
                if (getGridX() + 1 < entitygrid[0].length) {
                    if (entitygrid[getGridX() + 1][getGridY()] instanceof Water && entitygrid[getGridX() + 1][getGridY()] != null) {
                        Water water = ((Water)entitygrid[getGridX() + 1][getGridY()]);
                        if (!water.getHasFish()){
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
            else if (rand == 2){
                if (getGridX() - 1 >= 0) {
                    if (entitygrid[getGridX() - 1][getGridY()] instanceof Water && entitygrid[getGridX() - 1][getGridY()] != null) {
                        Water water = ((Water)entitygrid[getGridX() - 1][getGridY()]);
                        if (!water.getHasFish()){
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
            else if (rand == 3){
                if (getGridY() + 1 < entitygrid.length) {
                    if (entitygrid[getGridX()][getGridY() + 1] instanceof Water && entitygrid[getGridX()][getGridY() + 1] != null) {
                        Water water = ((Water)entitygrid[getGridX()][getGridY() + 1]);
                        if (!water.getHasFish()){
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
            else if (rand == 4){
                if (getGridY() - 1 >= 0) {
                    if (entitygrid[getGridX()][getGridY() - 1] instanceof Water && entitygrid[getGridX()][getGridY() - 1] != null) {
                    Water water = ((Water)entitygrid[getGridX()][getGridY() - 1]);
                        if (!water.getHasFish()){
                            this.setHasFish(false);
                            water.setHasFish(true);
                        }
                    }
                }
            }
        }
        else{
            int rand = (int)(Math.random()*500)+1;
            if (rand == 1){
                this.setHasFish(true);
            }
        }
    }
}
