package gameframe;

public class Camera {

    private int x;
    private int y;
    private int xOffset = 0;
    private int yOffset = 0;

    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getCamX() {
        return x;
    }

    public int getCamY() {
        return y;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
        x += xOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
        y += yOffset;
    }

}
