package gameframe;

import java.awt.Color;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics2D;

/**
 *
 * @author 1347278
 */
public class GameFrame extends javax.swing.JFrame {

    //keep track how much time has actually passed since last cycle
    private double delta_time;
    private long prev_time;
    public long tickCount = 0;
    //creating arraylist of entities
    ArrayList<Entity> entities = new ArrayList<Entity>();
    //make a grid of entities
    public Entity[][] entitygrid = new Entity[100][100];
    //timer object
    private Timer timer = null;
    private boolean active = false;
    //drawing object
    private Image ib;
    private Graphics ibg;
    private Color backgroundColor = new Color(150, 255, 150);
    //how long to wait between timer calls
    private int timerDelay = 250;
    //side length of grid
    //int gridLength = 650 / entitygrid.length;
    private Graphics2D g2d;
    private KeyLis listener = new KeyLis();
    public boolean drawing = false;
    private int[] gridLengths = {10, 25, 50, 100};
    private int index = 2;
    int gridLength = gridLengths[index];
    /**
     * Creates new form GameFrame
     */

    //testing objects
    Entity e;
    Entity eb;
    Wolf w;
    Deer d;
    Deer d1, d2, d3;
    Berries b1, b2, b3, b4, b5;
    PoisonBerries pb;
    Grass g;
    Rock r;
    Water wa;
    Mud m;
    Bear be, be2, be3;
    Water wa2, wa3, wa4, wa5, wa6, wa7, wa8;
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
        removeDeactivatedEntities();    //removes actors from list that are not active any more
        redraw();
    }

    //updates the tickCount
    public void updateTicker() {
        tickCount++;
        //textTick.setText(""+tickCount);
    }

    public void setUpImageBuffer() {
        ib = panelDraw.createImage(panelDraw.getWidth(), panelDraw.getHeight());
        ibg = ib.getGraphics();
    }

    public void setupSimulation() {
        // setup simulation!
        // entities.add(new ...)
        cam = new Camera(250, 250);
        w = new Wolf(0, 2, entities, entitygrid, gridLength);
        d = new Deer(0, 0, entities, entitygrid, gridLength);
        //d1 = new Deer(4, 3, entities, entitygrid, gridLength);
        //d2 = new Deer(9, 2, entities, entitygrid, gridLength);
        //d3 = new Deer(4, 8, entities, entitygrid, gridLength);
        wa = new Water(4, 4, entities, entitygrid, gridLength, false);
        wa.setHasFish(true);
        wa2 = new Water(5, 6, entities, entitygrid, gridLength, false);
        wa3 = new Water(4, 5, entities, entitygrid, gridLength, false);
        wa4 = new Water(4, 6, entities, entitygrid, gridLength, false);
        wa5 = new Water(4, 7, entities, entitygrid, gridLength, false);
        wa6 = new Water(5, 7, entities, entitygrid, gridLength, false);
        wa7 = new Water(5, 8, entities, entitygrid, gridLength, false);
        wa8 = new Water(6, 7, entities, entitygrid, gridLength, false);
        //m = new Mud(2, 2, entities, entitygrid, gridLength, false);
        be = new Bear(0, 1, entities, entitygrid, gridLength);
        be2 = new Bear(1, 3, entities, entitygrid, gridLength);
        //be3 = new Bear(1, 4, entities, entitygrid, gridLength);
        r = new Rock(10, 10, entities, entitygrid, gridLength, false);
        b1 = new Berries(8, 6, entities, entitygrid, gridLength);
        b2 = new Berries(6, 6, entities, entitygrid, gridLength);
        b3 = new Berries(7, 6, entities, entitygrid, gridLength);
        //b4 = new Berries(8, 6, entities, entitygrid, gridLength);
        //g = new Grass(9, 1, entities, entitygrid, gridLength);
        //pb = new PoisonBerries(6, 6, entities, entitygrid, gridLength);
        //s = new Salmon(7, 7, entities, entitygrid, gridLength);

    }

    public void updateEntities() {
        ArrayList<Water> noFish = new ArrayList();
        ArrayList<Water> fish = new ArrayList();
        for (int k = 0; k < entities.size(); k++) {
            if (entities.get(k).isActive() && !(entities.get(k) instanceof Water)) {
                entities.get(k).act();
            } else if (entities.get(k) instanceof Water) {
                Water water = ((Water) entities.get(k));
                if (water.getHasFish()) {
                    fish.add(water);
                } else {
                    noFish.add(water);
                }
            }
        }
        for (int k = 0; k < noFish.size(); k++) {
            if (noFish.get(k).isActive()) {
                noFish.get(k).act();
            }
        }
        noFish.clear();
        for (int k = 0; k < fish.size(); k++) {
            if (fish.get(k).isActive()) {
                fish.get(k).act();
            }
        }
        fish.clear();
    }

    public void drawStuff(Graphics g) {

        for (Entity temp : entities) {
            if (temp.isActive()) {
                temp.draw(g);
            }
        }

        g.setColor(new Color(0, 0, 0));
        for (int row = 0; row < entitygrid.length; row++) {
            for (int col = 0; col < entitygrid[0].length; col++) {
                g.drawRect(row * gridLength, col * gridLength, gridLength, gridLength);
            }
        }

    }

    public void removeDeactivatedEntities() {
        for (int k = entities.size() - 1; k >= 0; k--) {
            Entity temp = entities.get(k);
            if (!temp.isActive()) {
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
        this.addKeyListener(listener);
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
        buttonStart = new javax.swing.JButton();
        buttonPause = new javax.swing.JButton();
        buttonZoomOut = new javax.swing.JButton();
        buttonZoomIn = new javax.swing.JButton();
        sliderTimerDelay = new javax.swing.JSlider();
        labelTimerDelay = new javax.swing.JLabel();

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

        buttonPause.setText("Pause");
        buttonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPauseActionPerformed(evt);
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

        sliderTimerDelay.setMaximum(1000);
        sliderTimerDelay.setMinimum(100);
        sliderTimerDelay.setValue(250);
        sliderTimerDelay.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderTimerDelayStateChanged(evt);
            }
        });

        labelTimerDelay.setText("Timer delay: 250");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(buttonPause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(sliderTimerDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTimerDelay)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(buttonZoomOut)
                                .addComponent(buttonZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonPause)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStart)
                        .addGap(18, 18, 18)
                        .addComponent(buttonZoomIn)
                        .addGap(17, 17, 17)
                        .addComponent(buttonZoomOut)
                        .addGap(18, 18, 18)
                        .addComponent(labelTimerDelay)
                        .addGap(18, 18, 18)
                        .addComponent(sliderTimerDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
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
        System.out.println(gridLength);
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
        System.out.println(gridLength);
    }//GEN-LAST:event_buttonZoomInActionPerformed

    private void sliderTimerDelayStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderTimerDelayStateChanged
        timerDelay = sliderTimerDelay.getValue();
        labelTimerDelay.setText("Timer delay: " + timerDelay);
    }//GEN-LAST:event_sliderTimerDelayStateChanged

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
    private javax.swing.JButton buttonPause;
    private javax.swing.JButton buttonStart;
    private javax.swing.JButton buttonZoomIn;
    private javax.swing.JButton buttonZoomOut;
    private javax.swing.JLabel labelTimerDelay;
    private javax.swing.JPanel panelDraw;
    private javax.swing.JSlider sliderTimerDelay;
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
                    if (cam.getyOffset() - gridLength >= 0 && !drawing) {
                        cam.setyOffset(cam.getyOffset() - gridLength);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (!drawing) {
                        cam.setyOffset(cam.getyOffset() + gridLength);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (cam.getxOffset() - gridLength >= 0 && !drawing) {
                        cam.setxOffset(cam.getxOffset() - gridLength);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (!drawing) {
                        cam.setxOffset(cam.getxOffset() + gridLength);
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

}
