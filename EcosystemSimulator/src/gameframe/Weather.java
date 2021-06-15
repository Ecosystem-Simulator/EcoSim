package gameframe;

import java.util.ArrayList;

public class Weather extends Entity{
    
    public Weather(int gridX, int gridY, ArrayList<Entity> entities, Entity[][] entitygrid, int gridLength ){
        super(gridX, gridY, entities, entitygrid, gridLength);
    }
    //test
    private static String weather = ""; 
    private static String yesterdayWeather = "";
    private static int weatherCounter = 1;
    private static boolean day;
    private static boolean restrictedVision;
    private static int growthMultiplier = 1;
    
    public static void chooseWeather(){
        int chance = (int)(Math.random()*10)+1;
        //70% chance of sun, 30% chance of rain
        if (weatherCounter < 5){
            if (chance <= 5){
                weather  = "sunny";      
            }
            else{ 
                weather = "rainy";    
            }
            //check how many days in a row of the same weather
            if (weather.equals(yesterdayWeather)){
                weatherCounter++;
            }
            else{
                weatherCounter = 1; 
            }
        }
        //7 days in a row of the same weather brings natural disasters
        else if (yesterdayWeather.equals("rainy")){
           weather = "flood";      
        }
        //There's a 30% chance of flood after a day of flood
        else if (yesterdayWeather.equals("flood")){
            int x = (int)(Math.random()*10)+1;
            if (x <= 5){
                weather = "flood";
            }
            //Otherwise, weather goes back to normal, and counter = 1
            else{
                weatherCounter = 1;
                int c = (int)(Math.random()*10)+1;
                if (c <= 5){
                    weather = "sunny";
                }
                else{
                    weather = "rainy";
                }
            }
        }
        else if(yesterdayWeather.equals("sunny")){
            weather = "drought";
        }
        //There's a 30% chance of drought after a day of drought
        else if (yesterdayWeather.equals("drought")){
            int a = (int)(Math.random()*10)+1;
            if (a <= 5){
                weather = "drought";
            }
            //Otherwise, weather goes back to normal, and counter = 1
            else{
                weatherCounter = 1;
                int r = (int)(Math.random()*10)+1;
                if (r <= 5){
                    weather = "sunny";
                }
                else{
                    weather = "rainy";
                }
            }
        }
        //set yesterday's weather
        yesterdayWeather = weather;      
    } 
    
    public static void draw(){   
    }
    
    public static String getWeather(){
        return (weather);
    }
    
    
    public static void rain(){
        for(int r = 0; r < GameFrame.gridLength; r++){
            for (int c = 0; c < GameFrame.gridLength; c++){
                if (GameFrame.entitygrid[r][c] instanceof FloodWater){
                    GameFrame.entitygrid[r][c] = null;
                }
            }
        }
        growthMultiplier = 2;
        //tint blue
        //growth++    
    }
    
    public static void sun(){
        for(int r = 0; r < GameFrame.gridLength; r++){
            for (int c = 0; c < GameFrame.gridLength; c++){
                if (GameFrame.entitygrid[r][c] instanceof FloodWater){
                    GameFrame.entitygrid[r][c] = null;
                }
            }
        }
        growthMultiplier = 1;
        //no tint
    }
    
    public static boolean day(){
        restrictedVision = false;
        return restrictedVision;
    }
    
    public static boolean night(){
        growthMultiplier = 1;
        restrictedVision = true;
        return restrictedVision;
        
    }
    
    public static void flood(){
        for(int r = 0; r < GameFrame.gridLength; r++ ){
            for (int c = 0; c < GameFrame.gridLength; c++){
                if (GameFrame.entitygrid[r][c] instanceof Water && !(GameFrame.entitygrid[r][c] instanceof FloodWater)){
                    if (r - 1 > -1 && !(GameFrame.entitygrid[r-1][c] instanceof Water) && !(GameFrame.entitygrid[r-1][c] instanceof FloodWater)){
                        if (GameFrame.entitygrid[r-1][c] instanceof Animal){
                            GameFrame.entitygrid[r-1][c].die();
                        }
                        FloodWater fl = new FloodWater(r-1, c, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                    if (r + 1 <= GameFrame.gridLength - 1 && !(GameFrame.entitygrid[r+1][c] instanceof Water) && !(GameFrame.entitygrid[r+1][c] instanceof FloodWater)){
                        if (GameFrame.entitygrid[r+1][c] instanceof Animal){
                            GameFrame.entitygrid[r+1][c].die();
                        }
                        FloodWater fl0 = new FloodWater(r+1, c, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                    if (c - 1 > -1 && !(GameFrame.entitygrid[r][c-1] instanceof Water) && !(GameFrame.entitygrid[r][c-1] instanceof FloodWater)){ 
                        if (GameFrame.entitygrid[r][c-1] instanceof Animal){
                            GameFrame.entitygrid[r][c-1].die();
                        }
                        FloodWater fl1 = new FloodWater(r, c-1, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                    if (c + 1 > GameFrame.gridLength && !(GameFrame.entitygrid[r][c+1] instanceof Water) && !(GameFrame.entitygrid[r][c+1] instanceof FloodWater)){
                       if (GameFrame.entitygrid[r][c+1] instanceof Animal){
                            GameFrame.entitygrid[r][c+1].die();
                        }
                        FloodWater fl2 = new FloodWater(r, c+1, GameFrame.entities, GameFrame.entitygrid, GameFrame.gridLength);
                    }
                }
            }
        }
    }    
    
    public static void drought(){
        //tint world yellow   
    }
}