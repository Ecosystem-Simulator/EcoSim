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
    private int timerDelay = 200;
    //side length of grid
    //int gridLength = 650 / entitygrid.length;
    int gridLength = 50;
    private Graphics2D g2d;
    private KeyLis listener = new KeyLis();
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
        w = new Wolf(0, 0, entities, entitygrid, gridLength);
        //d = new Deer(0, 0, entities, entitygrid, gridLength);
        //d1 = new Deer(4, 3, entities, entitygrid, gridLength);
        //d2 = new Deer(9, 2, entities, entitygrid, gridLength);
        //d3 = new Deer(4, 8, entities, entitygrid, gridLength);
        wa = new Water(5, 5, entities, entitygrid, gridLength, false);
        //wa.setHasFish(true);
        wa2 = new Water(5, 6, entities, entitygrid, gridLength, false);
        wa3 = new Water(4, 5, entities, entitygrid, gridLength, false);
        wa4 = new Water(4, 6, entities, entitygrid, gridLength, false);
        wa5 = new Water(4, 7, entities, entitygrid, gridLength, false);
        wa6 = new Water(5, 7, entities, entitygrid, gridLength, false);
        wa7 = new Water(5, 8, entities, entitygrid, gridLength, false);
        wa8 = new Water(6, 7, entities, entitygrid, gridLength, false);
        //m = new Mud(2, 2, entities, entitygrid, gridLength, false);
        //be = new Bear(0, 1, entities, entitygrid, gridLength);
        //be2 = new Bear(1, 3, entities, entitygrid, gridLength);
        //be3 = new Bear(1, 4, entities, entitygrid, gridLength);
        //r = new Rock(10, 10, entities, entitygrid, gridLength, false);
        //b1 = new Berries(5, 6, entities, entitygrid, gridLength);
        //b2 = new Berries(6, 6, entities, entitygrid, gridLength);
        //b3 = new Berries(7, 6, entities, entitygrid, gridLength);
        //b4 = new Berries(8, 6, entities, entitygrid, gridLength);
        //g = new Grass(9, 1, entities, entitygrid, gridLength);
        //pb = new PoisonBerries(6, 6, entities, entitygrid, gridLength);
        //s = new Salmon(7, 7, entities, entitygrid, gridLength);

    }

    public void updateEntities() {
        for (int k = 0; k < entities.size(); k++) {
            if (entities.get(k).isActive()) {
                entities.get(k).act();
            }
        }
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
        g2d.translate(-cam.getxOffset(), -cam.getyOffset());

        drawStuff(ibg);

        /* this is the actual drawing of your stuff onto the actual panel.  It copies the image buffer to the
         * panel's image. */
        Graphics g = panelDraw.getGraphics();
        g.drawImage(ib, 0, 0, this);
        g2d.translate(cam.getxOffset(), cam.getyOffset());
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
            .addGap(0, 501, Short.MAX_VALUE)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(buttonPause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonPause)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStart)))
                .addContainerGap(210, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        startTimer();
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPauseActionPerformed
        if (timer != null) {
            timer.cancel();
            timer = null;
            System.out.println("Simulation paused");
        } else {
            System.out.println("No timer active!");
        }
    }//GEN-LAST:event_buttonPauseActionPerformed

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
    private javax.swing.JPanel panelDraw;
    // End of variables declaration//GEN-END:variables
    public class KeyLis implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (cam.getyOffset() - gridLength >= 0) {
                    cam.setyOffset(cam.getyOffset() - gridLength);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                cam.setyOffset(cam.getyOffset() + gridLength);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (cam.getxOffset() - gridLength >= 0) {
                    cam.setxOffset(cam.getxOffset() - gridLength);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                cam.setxOffset(cam.getxOffset() + gridLength);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

}
