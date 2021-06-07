package gameframe;
public class Weather {
    
    //test
    String weather = ""; 
    String yesterdayWeather = "";
    int weatherCounter = 1;
    
    public void chooseWeather(){
        int chance = (int)(Math.random()*10)+1;
        if (weatherCounter < 7){
            if (chance <= 7){
                weather  = "sunny";      
            }
            else{ 
                weather = "rainy";    
            }
            if (weather.equals(yesterdayWeather)){
                weatherCounter++;
            }
            else{
                weatherCounter = 1; 
            }
        }
        else if (yesterdayWeather.equals("rainy")){
           weather = "flood";      
        }
        else if (yesterdayWeather.equals("flood")){
            int x = (int)(Math.random()*10)+1;
            if (x <= 3){
                weather = "flood";
            }
            else{
                weatherCounter = 1;
                int c = (int)(Math.random()*10)+1;
                if (c <= 7){
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
        else if (yesterdayWeather.equals("drought")){
            int a = (int)(Math.random()*10)+1;
            if (a <= 3){
                weather = "drought";
            }
            else{
                weatherCounter = 1;
                int r = (int)(Math.random()*10)+1;
                if (r <= 7){
                    weather = "sunny";
                }
                else{
                    weather = "rainy";
                }
            }
        }
        yesterdayWeather = weather;      
    } 
    
    public void rain(){
        //floodwater goes away
        //tint blue
        //growth++
        
    }
    
    public void sun(){
        //floodwater goes away
        //no tint
        //
    }
    
    public void day(){
        //no tint
    }
    
    public void night(){
        //tint grey?
        //vision = vision-1;
    }
    
    public void flood(){
        //if water block + 1 pixel left, right, up, down != water, make those blocks floodwater. 
    }
    
    public void drought(){
        //tint world yellow   
    }
}

