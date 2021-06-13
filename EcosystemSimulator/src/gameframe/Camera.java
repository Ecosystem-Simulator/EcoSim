package gameframe;


public class Camera{
    private int x_center;
    private int y_center;
    public Camera(){
        x_center = 4;
        y_center = 2;
    }
    
    public void incrementX(){
        x_center++;
    }
    
    public void incrementY(){
        y_center++;
    }
    
    public void decrementX(){
        if (x_center - 1 >= 4){
            x_center--;
        }
    }
    
    public void decrementY(){
        if (y_center - 1 >= 2){
            y_center--;
        }
    }
    
    public int getX(){
        return(x_center);
    }
    
    public int getY(){
        return(y_center);
    }
}
