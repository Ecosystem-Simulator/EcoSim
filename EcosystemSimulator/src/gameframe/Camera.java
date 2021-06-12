package gameframe;

import java.awt.event.KeyEvent;

public class Camera{
    private boolean key_w;
    private boolean key_a;
    private boolean key_s;
    private boolean key_d;
    private int x_center;
    private int y_center;
    public Camera(){
        key_w = false;
        key_a = false;
        key_s = false;
        key_d = false;
        x_center = 400;
        y_center = 400;
    }
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == e.VK_D) {
                key_d = true;
            } 
            else if (e.getKeyCode() == e.VK_A) {
                key_a = true;
            } 
            else if (e.getKeyCode() == e.VK_S) {
                key_s = true;
            } 
            else if (e.getKeyCode() == e.VK_W) {
                key_w = true;
            }
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == e.VK_D) {
                key_d = false;
            } 
            else if (e.getKeyCode() == e.VK_A) {
                key_a = false;
            }
            if (e.getKeyCode() == e.VK_W) {
                key_w = false;
            } 
            else if (e.getKeyCode() == e.VK_S) {
                key_s = false;
            }
        }
        return false;
    }
    
    public void keyboardCheck(){
        if (key_d == true){
            x_center += 10;
        }
        if (key_a == true){
            x_center -= 10;
        }
        if (key_s == true){
            y_center += 10;
        }
        if (key_w == true){
            y_center -= 10;
        }
        System.out.println(x_center + ", " + y_center);
    }
}
