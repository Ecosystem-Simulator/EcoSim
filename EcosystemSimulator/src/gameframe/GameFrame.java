package gameframe;

//graphics
import java.awt.Color;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.Image;
//arraylist
import java.util.ArrayList;
//writing
import java.io.BufferedWriter;
import java.io.FileWriter;
//reading
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
//timer
import java.util.Timer;
import java.util.TimerTask;

public class GameFrame extends javax.swing.JFrame {

    //keep track how much time has actually passed since last cycle
    private double delta_time;
    private long prev_time;
    public long tickCount = 0;
    private int weatherCount;
    private int timeCount;
    //creating arraylist of entities
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    //make a grid of entities
    public static Entity[][] entitygrid = new Entity[100][100];
    //timer object
    private Timer timer = null;
    //drawing/color objects
    private Image ib;
    private Graphics ibg;
    private Color backgroundColor = new Color(150, 255, 150);
    //how long to wait between timer calls
    private int timerDelay = 250;
    private Graphics2D g2d;
    //make a keyboard listener & moust listener
    private KeyLis keylistener = new KeyLis();
    private MouseLis mouselistener = new MouseLis();
    //position of mouse
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    //position of nearest grid to mouse;
    private int nearestGridX = 0;
    private int nearestGridY = 0;
    public boolean drawing = false;
    static private int[] gridLengths = {10, 25, 50, 100};
    static private int index = 2;
    //side length of grid
    static int gridLength = gridLengths[index];
    static int cameraSpeed = 100;
    //variables for adding entities at the start of the game
    private boolean addingWolf = false;
    private boolean addingBear = false;
    private boolean addingDeer = false;
    private boolean addingRock = false;
    private boolean addingGrass = false;
    private boolean addingWater = false;
    private boolean addingBerries = false;
    private boolean addingMud = false;
    //time of day; timeOfDay -> day; !timeOfDay -> night
    public boolean timeOfDay;

    Camera cam;

    public void startTimer() {
        if (timer != null) {
            System.out.println("A timer is already working!");
            return;
        }
        //make a new timer object
        timer = new Timer(true);
        //make a timertask that has a job to do (call updateTime)
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
        //tell timer to start repeating the task
        timer.scheduleAtFixedRate(task, 0, timerDelay);
        prev_time = System.nanoTime();
    }

    public void timeAdjust() {
        long curr_time = System.nanoTime();
        delta_time = (curr_time - prev_time) / 1000000000.0;
        System.out.println(delta_time);
        prev_time = curr_time;
    }

    //runs every tick when the timer is on.
    public void tick() {
        updateTicker();
        requestFocusInWindow();
        updateEntities();      //most important part of simulation!
        removeDeactivatedEntities(); //removes entities from list that are not active any more
        redraw();
        textWeather.setText(Weather.getWeather());
    }

    //updates the tickCount
    public void updateTicker() {
        tickCount++;
    }

    public void setUpImageBuffer() {
        ib = panelDraw.createImage(panelDraw.getWidth(), panelDraw.getHeight());
        ibg = ib.getGraphics();
    }

    public void setupSimulation() {
        cam = new Camera(250, 250);
    }

    public void updateEntities() {
        //weather and day/night cycle
        timeCount++;
        if (timeCount % 5 == 0){
            if (timeOfDay){
                textTime.setText("day");
            }
            else {
                textTime.setText("night");
            }
        }
        
        ArrayList<Water> noFish = new ArrayList();
        ArrayList<Water> fish = new ArrayList();
        //getting non-water entities to act first
        for (int k = 0; k < entities.size(); k++) {
            if (entities.get(k).isActive() && !(entities.get(k) instanceof Water)) {
                if (timeOfDay && entities.get(k) instanceof Animal){
                    ((Animal)entities.get(k)).setRestrictedVision(false);
                }
                else if (!timeOfDay && entities.get(k) instanceof Animal){
                    ((Animal)entities.get(k)).setRestrictedVision(true);
                }
                entities.get(k).act();
            } 
            //separating water into 2 categories (fish and no fish); storing in arraylists
            else if (entities.get(k) instanceof Water && entities.get(k).isActive()) {
                Water water = ((Water) entities.get(k));
                if (water.getHasFish()) {
                    fish.add(water);
                } else {
                    noFish.add(water);
                }
            }
        }
        //getting water entities without fish to act
        for (int k = 0; k < noFish.size(); k++) {
            if (noFish.get(k).isActive()) {
                noFish.get(k).act();
            }
        }
        noFish.clear();
        //getting water entities with fish to act
        for (int k = 0; k < fish.size(); k++) {
            if (fish.get(k).isActive()) {
                fish.get(k).act();
            }
        }
        fish.clear();
        
        weatherCount++;
        if (weatherCount % 5 == 0){
            timeOfDay = Weather.time();
            Weather.chooseWeather();
            if (Weather.getWeather().equals("rainy")){
                Weather.rain();
                backgroundColor = new Color(150, 225, 200);
            }
            else if (Weather.getWeather().equals("sunny")){
                Weather.sun();
                backgroundColor = new Color(150, 255, 150);
            }
            else if (Weather.getWeather().equals("flood")){
                Weather.flood();
                backgroundColor = new Color(115, 225, 200);
            }
            else if (Weather.getWeather().equals("drought")){
                Weather.drought();
                backgroundColor = new Color(210, 241,150);
            }
        }
    }

    public void drawStuff(Graphics g) {
        //drawing out active entities
        for (int k = 0; k < entities.size(); k++) {
            if (entities.get(k).isActive()) {
                entities.get(k).draw(g);
            }
        }
        //drawing out grid
        g.setColor(new Color(0, 0, 0));
        for (int row = 0; row < entitygrid.length; row++) {
            for (int col = 0; col < entitygrid[0].length; col++) {
                g.drawRect(row * gridLength, col * gridLength, gridLength, gridLength);
            }
        }
    }

    public void removeDeactivatedEntities() {
        //removing dead entities
        for (int k = entities.size() - 1; k >= 0; k--) {
            Entity temp = entities.get(k);
            if (!temp.isActive()) {
                entitygrid[entities.get(k).getGridX()][entities.get(k).getGridY()] = null;
                entities.remove(k);
            }
        }
    }

    public void redraw() {
        //switch to white and draw a white rectangle over the entire image buffer to clear it
        ibg.setColor(backgroundColor);
        ibg.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        ibg.fillRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());

        g2d = (Graphics2D) ibg;
        /* you can certainly just put all your draw code in here, but to stay organized, I am going to pass the
         * image buffer object to my own draw method.  This keeps the code a little cleaner... */
        drawing = true;
        g2d.translate(-cam.getxOffset(), -cam.getyOffset());

        drawStuff(ibg);

        /* this is the actual drawing of your stuff onto the actual panel.  It copies the image buffer to the
         * panel's image. */
        Graphics g = panelDraw.getGraphics();
        g.drawImage(ib, 0, 0, this);
        drawing = false;
        g2d.translate(cam.getxOffset(), cam.getyOffset());
        //g2d.translate(cam.getxOffset(), cam.getyOffset());
    }

    public GameFrame() {
        initComponents();
        setupSimulation();
        setUpImageBuffer();
        setVisible(true);
        setFocusable(true);
        requestFocusInWindow();
        this.addKeyListener(keylistener);
        panelDraw.addMouseListener(mouselistener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDraw = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        buttonStart = new javax.swing.JButton();
        buttonZoomOut = new javax.swing.JButton();
        buttonZoomIn = new javax.swing.JButton();
        sliderTimerDelay = new javax.swing.JSlider();
        labelTimerDelay = new javax.swing.JLabel();
        labelCameraSpeed = new javax.swing.JLabel();
        sliderCameraSpeed = new javax.swing.JSlider();
        buttonPause = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        labelEntities = new javax.swing.JLabel();
        tButtonBear = new javax.swing.JToggleButton();
        tButtonDeer = new javax.swing.JToggleButton();
        tButtonGrass = new javax.swing.JToggleButton();
        tButtonWater = new javax.swing.JToggleButton();
        tButtonWolf = new javax.swing.JToggleButton();
        tButtonRock = new javax.swing.JToggleButton();
        tButtonMud = new javax.swing.JToggleButton();
        tButtonBerries = new javax.swing.JToggleButton();
        textWeather = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        buttonSave = new javax.swing.JButton();
        buttonLoad = new javax.swing.JButton();
        labelTime = new javax.swing.JLabel();
        textTime = new javax.swing.JTextField();
        buttonClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setSize(new java.awt.Dimension(1200, 700));

        panelDraw.setBackground(new java.awt.Color(255, 255, 255));
        panelDraw.setPreferredSize(new java.awt.Dimension(1171, 651));

        javax.swing.GroupLayout panelDrawLayout = new javax.swing.GroupLayout(panelDraw);
        panelDraw.setLayout(panelDrawLayout);
        panelDrawLayout.setHorizontalGroup(
            panelDrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1001, Short.MAX_VALUE)
        );
        panelDrawLayout.setVerticalGroup(
            panelDrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1001, Short.MAX_VALUE)
        );

        buttonStart.setText("Start Simulation");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        buttonZoomOut.setText("Zoom out");
        buttonZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonZoomOutActionPerformed(evt);
            }
        });

        buttonZoomIn.setText("Zoom in");
        buttonZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonZoomInActionPerformed(evt);
            }
        });

        sliderTimerDelay.setMajorTickSpacing(50);
        sliderTimerDelay.setMaximum(1000);
        sliderTimerDelay.setMinimum(50);
        sliderTimerDelay.setPaintTicks(true);
        sliderTimerDelay.setSnapToTicks(true);
        sliderTimerDelay.setValue(250);
        sliderTimerDelay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderTimerDelayStateChanged(evt);
            }
        });

        labelTimerDelay.setText("Timer delay: 250");

        labelCameraSpeed.setText("Camera speed: 100");

        sliderCameraSpeed.setMajorTickSpacing(100);
        sliderCameraSpeed.setMaximum(200);
        sliderCameraSpeed.setMinimum(100);
        sliderCameraSpeed.setPaintLabels(true);
        sliderCameraSpeed.setPaintTicks(true);
        sliderCameraSpeed.setSnapToTicks(true);
        sliderCameraSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderCameraSpeedStateChanged(evt);
            }
        });

        buttonPause.setText("Pause");
        buttonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPauseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sliderTimerDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelTimerDelay)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(buttonZoomOut)
                                        .addComponent(buttonZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(sliderCameraSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPause, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(labelCameraSpeed)
                        .addGap(77, 77, 77))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPause)
                .addGap(18, 18, 18)
                .addComponent(buttonZoomIn)
                .addGap(17, 17, 17)
                .addComponent(buttonZoomOut)
                .addGap(18, 18, 18)
                .addComponent(labelTimerDelay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sliderTimerDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCameraSpeed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sliderCameraSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        labelEntities.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        labelEntities.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEntities.setText("Entities");

        tButtonBear.setText("Bear");
        tButtonBear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonBearActionPerformed(evt);
            }
        });

        tButtonDeer.setText("Deer");
        tButtonDeer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonDeerActionPerformed(evt);
            }
        });

        tButtonGrass.setText("Grass");
        tButtonGrass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonGrassActionPerformed(evt);
            }
        });

        tButtonWater.setText("Water");
        tButtonWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonWaterActionPerformed(evt);
            }
        });

        tButtonWolf.setText("Wolf");
        tButtonWolf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonWolfActionPerformed(evt);
            }
        });

        tButtonRock.setText("Rock");
        tButtonRock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonRockActionPerformed(evt);
            }
        });

        tButtonMud.setText("Mud");
        tButtonMud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonMudActionPerformed(evt);
            }
        });

        tButtonBerries.setText("Berries");
        tButtonBerries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonBerriesActionPerformed(evt);
            }
        });

        textWeather.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textWeather.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textWeatherActionPerformed(evt);
            }
        });

        jLabel1.setText("Weather");

        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonLoad.setText("Load");
        buttonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadActionPerformed(evt);
            }
        });

        labelTime.setText("Time");

        textTime.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textTimeActionPerformed(evt);
            }
        });

        buttonClear.setText("Clear");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tButtonWater, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tButtonGrass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tButtonDeer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tButtonBear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tButtonWolf, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(labelEntities, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tButtonRock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tButtonMud, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tButtonBerries, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                .addComponent(textWeather)
                                .addComponent(textTime))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(labelTime)
                                        .addGap(21, 21, 21))
                                    .addComponent(buttonClear)))
                            .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelEntities, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonWolf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonBear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonDeer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonGrass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonWater)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonRock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonMud)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tButtonBerries)
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(labelTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClear)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(939, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        sliderTimerDelay.setEnabled(false);
        startTimer();

    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPauseActionPerformed
        if (timer != null) {
            timer.cancel();
            timer = null;
            sliderTimerDelay.setEnabled(true);
        }
    }//GEN-LAST:event_buttonPauseActionPerformed

    private void buttonZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonZoomOutActionPerformed
        if (index - 1 >= 0) {
            index--;
            gridLength = gridLengths[index];
            for (Entity e : entities) {
                if (e.isActive()) {
                    e.setGridLength(gridLength);
                }
            }
        }
    }//GEN-LAST:event_buttonZoomOutActionPerformed

    private void buttonZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonZoomInActionPerformed
        if (index + 1 < gridLengths.length) {
            index++;
            gridLength = gridLengths[index];
            for (Entity e : entities) {
                if (e.isActive()) {
                    e.setGridLength(gridLength);
                }
            }
        }
    }//GEN-LAST:event_buttonZoomInActionPerformed

    private void sliderTimerDelayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderTimerDelayStateChanged
        timerDelay = sliderTimerDelay.getValue();
        labelTimerDelay.setText("Timer delay: " + timerDelay);
    }//GEN-LAST:event_sliderTimerDelayStateChanged

    private void tButtonWolfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonWolfActionPerformed
        if (!addingWolf) {
            addingWolf = true;
            tButtonBear.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonMud.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingWolf = false;
            tButtonBear.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonMud.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }
    }//GEN-LAST:event_tButtonWolfActionPerformed

    private void tButtonBearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonBearActionPerformed
        if (!addingBear) {
            addingBear = true;
            tButtonWolf.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonMud.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingBear = false;
            tButtonWolf.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonMud.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }

    }//GEN-LAST:event_tButtonBearActionPerformed

    private void tButtonDeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonDeerActionPerformed
        if (!addingDeer) {
            addingDeer = true;
            tButtonWolf.setEnabled(false);
            tButtonBear.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonMud.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingDeer = false;
            tButtonWolf.setEnabled(true);
            tButtonBear.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonMud.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }

    }//GEN-LAST:event_tButtonDeerActionPerformed

    private void tButtonGrassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonGrassActionPerformed
        if (!addingGrass) {
            addingGrass = true;
            tButtonWolf.setEnabled(false);
            tButtonBear.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonMud.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingGrass = false;
            tButtonWolf.setEnabled(true);
            tButtonBear.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonMud.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }

    }//GEN-LAST:event_tButtonGrassActionPerformed

    private void tButtonWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonWaterActionPerformed
        if (!addingWater) {
            addingWater = true;
            tButtonWolf.setEnabled(false);
            tButtonBear.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonMud.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingWater = false;
            tButtonWolf.setEnabled(true);
            tButtonBear.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonMud.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }
    }//GEN-LAST:event_tButtonWaterActionPerformed

    private void tButtonRockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonRockActionPerformed
        if (!addingRock) {
            addingRock = true;
            tButtonWolf.setEnabled(false);
            tButtonBear.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonMud.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingRock = false;
            tButtonWolf.setEnabled(true);
            tButtonBear.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonMud.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }
    }//GEN-LAST:event_tButtonRockActionPerformed

    private void tButtonMudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonMudActionPerformed
        if (!addingMud) {
            addingMud = true;
            tButtonWolf.setEnabled(false);
            tButtonBear.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonBerries.setEnabled(false);
        } else {
            addingMud = false;
            tButtonWolf.setEnabled(true);
            tButtonBear.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonBerries.setEnabled(true);
        }
    }//GEN-LAST:event_tButtonMudActionPerformed

    private void tButtonBerriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonBerriesActionPerformed
        if (!addingBerries) {
            addingBerries = true;
            tButtonWolf.setEnabled(false);
            tButtonBear.setEnabled(false);
            tButtonDeer.setEnabled(false);
            tButtonGrass.setEnabled(false);
            tButtonWater.setEnabled(false);
            tButtonRock.setEnabled(false);
            tButtonMud.setEnabled(false);
        } else {
            addingBerries = false;
            tButtonWolf.setEnabled(true);
            tButtonBear.setEnabled(true);
            tButtonDeer.setEnabled(true);
            tButtonGrass.setEnabled(true);
            tButtonWater.setEnabled(true);
            tButtonRock.setEnabled(true);
            tButtonMud.setEnabled(true);
        }

    }//GEN-LAST:event_tButtonBerriesActionPerformed

    private void textWeatherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textWeatherActionPerformed
        
    }//GEN-LAST:event_textWeatherActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        String currentDirectory = System.getProperty("user.dir");
        String fullFileName = currentDirectory + "/game.txt";
        
        try {
            FileWriter fw = new FileWriter(fullFileName);
            BufferedWriter br = new BufferedWriter(fw);
            for(Entity e : entities){
                br.write(e.getClass().getSimpleName() + "*" + e.getGridX() + "*" + e.getGridY());
                if(e instanceof Animal){
                    Animal temp = (Animal) e;
                    br.write("*" + temp.getGender() + "*" + temp.getHunger() + "*" + temp.getThirst() + "*" + temp.getReproductiveUrge());
                }
                else if(e instanceof Water){
                    if(((Water) e).getHasFish()){
                        br.write("*" + "true");
                    }
                    else{
                        br.write("*" + "false");
                    }
                }
                br.newLine();
            }
            System.out.println("File written");
            br.close();
            JOptionPane.showMessageDialog(null, "File saved!");
        }
        catch(Exception e) {
            System.out.println("Error writing to file");
        }
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadActionPerformed
        try{
            String currentDirectory = System.getProperty("user.dir");
            String fullFileName = currentDirectory + "/game.txt";

            FileReader fr = new FileReader(fullFileName);
            BufferedReader br = new BufferedReader(fr); 
            
            ArrayList<String> fileStuff = new ArrayList();
            
            String line = "";
            while((line = br.readLine()) != null){
                fileStuff.add(line);
            }
            
            //clear everything
            entities.clear();
            for(int r = 0; r < entitygrid.length; r++){
                for(int c = 0; c < entitygrid[0].length; c++){
                    entitygrid[r][c] = null;
                }
            }
            
            //load file
            for(String s : fileStuff){
                String entityname;
                Class classname;
                int gridX, gridY, gender, hunger, thirst, reproductiveUrge;
                boolean hasFish;
                int index = s.indexOf("*");
                entityname = s.substring(0, index);
                classname = Class.forName("gameframe." + entityname);
                s = s.substring(index + 1);
                index = s.indexOf("*");
                gridX = Integer.parseInt(s.substring(0, index));
                s = s.substring(index + 1);
                if(Animal.class.isAssignableFrom(classname)){
                    index = s.indexOf("*");
                    gridY = Integer.parseInt(s.substring(0, index));
                    
                    
                    s = s.substring(index + 1);
                    index = s.indexOf("*");
                    gender = Integer.parseInt(s.substring(0, index));
                    
                    s = s.substring(index + 1);
                    index = s.indexOf("*");
                    hunger = Integer.parseInt(s.substring(0, index));
                    
                    s = s.substring(index + 1);
                    index = s.indexOf("*");
                    thirst = Integer.parseInt(s.substring(0, index));
                    
                    s = s.substring(index + 1);
                    reproductiveUrge = Integer.parseInt(s);
                    
                    //gridX, gridY, entities, entitygrid, gridLength, gender, hunger, thirst, reproductiveUrge
                    Constructor<?> cons = classname.getConstructor(int.class, int.class, ArrayList.class, Entity[][].class, int.class, int.class, int.class, int.class, int.class);
                    Object entity = cons.newInstance(gridX, gridY, entities, entitygrid, gridLength, gender, hunger, thirst, reproductiveUrge);
                   
                }
                else if(Water.class.getSimpleName().equals(entityname)){
                    index = s.indexOf("*");
                    gridY = Integer.parseInt(s.substring(0, index));
                    
                    s = s.substring(index + 1);
                    hasFish = Boolean.parseBoolean(s);
                    //
                    Constructor<?> cons = classname.getConstructor(int.class, int.class, ArrayList.class, Entity[][].class, int.class);
                    Object entity = cons.newInstance(gridX, gridY, entities, entitygrid, gridLength);
                    ((Water)entity).setHasFish(hasFish);
                }
                
                else{
                    gridY = Integer.parseInt(s);
                    Constructor<?> cons = classname.getConstructor(int.class, int.class, ArrayList.class, Entity[][].class, int.class);
                    Object entity = cons.newInstance(gridX, gridY, entities, entitygrid, gridLength);
                }
                
            }
            
            br.close();
        }
        catch(Exception e){
            System.out.println("404 File not found" + e);
        }
    }//GEN-LAST:event_buttonLoadActionPerformed

    private void textTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textTimeActionPerformed

    private void sliderCameraSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderCameraSpeedStateChanged
        cameraSpeed = sliderCameraSpeed.getValue();
        labelCameraSpeed.setText("Camera speed: " + cameraSpeed);
    }//GEN-LAST:event_sliderCameraSpeedStateChanged

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        entities.clear();
        for (int r = 0; r < entitygrid.length; r++){
            for (int c = 0; c < entitygrid[0].length; c++){
                entitygrid[r][c] = null;
            }
        }
    }//GEN-LAST:event_buttonClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new GameFrame().setVisible(true);
                //maximizing JFrame automatically
                GameFrame gf = new GameFrame();
                gf.setVisible(true);
                gf.setExtendedState(JFrame.MAXIMIZED_BOTH);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonLoad;
    private javax.swing.JButton buttonPause;
    private javax.swing.JButton buttonSave;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonZoomIn;
    private javax.swing.JButton buttonZoomOut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelCameraSpeed;
    private javax.swing.JLabel labelEntities;
    private javax.swing.JLabel labelTime;
    private javax.swing.JLabel labelTimerDelay;
    private javax.swing.JPanel panelDraw;
    private javax.swing.JSlider sliderCameraSpeed;
    private javax.swing.JSlider sliderTimerDelay;
    private javax.swing.JToggleButton tButtonBear;
    private javax.swing.JToggleButton tButtonBerries;
    private javax.swing.JToggleButton tButtonDeer;
    private javax.swing.JToggleButton tButtonGrass;
    private javax.swing.JToggleButton tButtonMud;
    private javax.swing.JToggleButton tButtonRock;
    private javax.swing.JToggleButton tButtonWater;
    private javax.swing.JToggleButton tButtonWolf;
    private javax.swing.JTextField textTime;
    private javax.swing.JTextField textWeather;
    // End of variables declaration//GEN-END:variables
    public class KeyLis implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (cam.getyOffset() - cameraSpeed >= 0 && !drawing) {
                        cam.setyOffset(cam.getyOffset() - cameraSpeed);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (!drawing) {
                        cam.setyOffset(cam.getyOffset() + cameraSpeed);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (cam.getxOffset() - cameraSpeed >= 0 && !drawing) {
                        cam.setxOffset(cam.getxOffset() - cameraSpeed);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (!drawing) {
                        cam.setxOffset(cam.getxOffset() + cameraSpeed);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public class MouseLis implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            lastMouseX = me.getX();
            lastMouseY = me.getY();
            if (lastMouseX >= 0 && lastMouseX + cam.getxOffset() < entitygrid[0].length * gridLength && lastMouseY >= 0 && lastMouseY + cam.getyOffset() < entitygrid.length * gridLength) {
                nearestGridX = (int) Math.round((double) (lastMouseX + cam.getxOffset() - gridLength / 2) / gridLength);
                nearestGridY = (int) Math.round((double) (lastMouseY + cam.getyOffset()  - gridLength / 2) / gridLength);
                if (entitygrid[nearestGridX][nearestGridY] == null){
                    if (addingBear){
                        Bear bear = new Bear (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    else if (addingWolf){
                        Wolf wolf = new Wolf (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    else if (addingDeer){
                        Deer deer = new Deer (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    else if (addingWater){
                        Water water = new Water (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    else if (addingBerries){
                        int rand = (int)(Math.random()*10)+1;
                        if (rand == 1){
                            PoisonBerries poisonBerries = new PoisonBerries(nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                        }
                        else{
                            Berries berries = new Berries (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                        }
                    }
                    else if (addingGrass){
                        Grass grass = new Grass (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    else if (addingMud){
                        Mud mud = new Mud (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    else if (addingRock){
                        Rock rock = new Rock (nearestGridX, nearestGridY, entities, entitygrid, gridLength);
                    }
                    redraw();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }

    }
}
