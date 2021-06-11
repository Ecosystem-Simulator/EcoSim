/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public Entity[][] entitygrid = new Entity[10][10];
    //timer object
    private Timer timer = null;
    private boolean active = false;
    //drawing object
    private Image ib;
    private Graphics ibg;
    private Color backgroundColor = new Color(150, 255, 150);
    //how long to wait between timer calls
    private int timerDelay = 500;
    //side length of grid
    int gridLength = 50;
    /**
     * Creates new form GameFrame
     */

    //testing objects
    Entity e;
    Entity eb;
    Wolf w;
    Deer d;
    Deer d1, d2, d3;
    Berries b;
    PoisonBerries pb;
    Grass g;
    Rock r;
    Water wa;
    Mud m;
    Bear be;
    Salmon s;
    Water wa2, wa3, wa4;

    public void startTimer() {
        if (timer != null) {
            System.out.println("A timer is already working!");
            return;
        }
        //make a new timer object
        timer = new Timer(true);
        //make a timertask that has a job to do (call updateTime)
        TimerTask task = new TimerTask() {
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
        //keyboardCheck();   
        updateEntities();      //most important part of simulation!
        removeDeactivatedEntities();    //removes actors from list that are not active any more
        redraw();
        System.out.println("Bear target: " + be.target.getClass().getName());
        System.out.println("Bear hunger: " + be.getHunger());
        System.out.println("Bear thirst: " + be.getThirst());
        System.out.println();
        System.out.println("Wolf target: " + w.target.getClass().getName());
        System.out.println("Wolf hunger: " + w.getHunger());
        System.out.println("Wolf thirst: " + w.getThirst());
        System.out.println();

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
        w = new Wolf(0, 8, entities, "male", entitygrid, gridLength);
        d = new Deer(9, 0, entities, "male", entitygrid, gridLength);
        d1 = new Deer(8, 8, entities, "male", entitygrid, gridLength);
        d2 = new Deer(9, 9, entities, "male", entitygrid, gridLength);
        d3 = new Deer(4, 8, entities, "male", entitygrid, gridLength);
        wa = new Water(5, 5, entities, entitygrid, gridLength, false);
        //wa2 = new Water (1, 2, entities, entitygrid, gridLength, false);
        //wa3 = new Water (2, 1, entities, entitygrid, gridLength, false);
        //wa4 = new Water (1, 1, entities, entitygrid, gridLength, false);
        //m = new Mud (2, 2, entities, entitygrid, gridLength, false);
        be = new Bear(5, 8, entities, "male", entitygrid, gridLength);
        r = new Rock(2, 4, entities, entitygrid, gridLength, false);

    }

    public void updateEntities() {
        for (Entity e : entities) {
            if (e.isActive()) {
                e.act();
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
        for (int r = 0; r < entitygrid.length; r++) {
            for (int c = 0; c < entitygrid[0].length; c++) {
                g.drawRect(r * gridLength, c * gridLength, gridLength, gridLength);
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

        /* you can certainly just put all your draw code in here, but to stay organized, I am going to pass the
         * image buffer object to my own draw method.  This keeps the code a little cleaner... */
        drawStuff(ibg);

        /* this is the actual drawing of your stuff onto the actual panel.  It copies the image buffer to the
         * panel's image. */
        Graphics g = panelDraw.getGraphics();
        g.drawImage(ib, 0, 0, this);
    }

    public GameFrame() {
        initComponents();
        setupSimulation();
        setUpImageBuffer();
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
        buttonMoveRight = new javax.swing.JButton();
        buttonMoveLeft = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelDraw.setBackground(new java.awt.Color(255, 255, 255));
        panelDraw.setPreferredSize(new java.awt.Dimension(501, 501));

        javax.swing.GroupLayout panelDrawLayout = new javax.swing.GroupLayout(panelDraw);
        panelDraw.setLayout(panelDrawLayout);
        panelDrawLayout.setHorizontalGroup(
            panelDrawLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
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

        buttonMoveRight.setText("Move Right");
        buttonMoveRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMoveRightActionPerformed(evt);
            }
        });

        buttonMoveLeft.setText("Move Left");
        buttonMoveLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMoveLeftActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonStart)
                    .addComponent(buttonMoveLeft)
                    .addComponent(buttonMoveRight))
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelDraw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonMoveRight)
                        .addGap(18, 18, 18)
                        .addComponent(buttonMoveLeft)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonStart)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        startTimer();
    }//GEN-LAST:event_buttonStartActionPerformed

    private void buttonMoveRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMoveRightActionPerformed

    }//GEN-LAST:event_buttonMoveRightActionPerformed

    private void buttonMoveLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMoveLeftActionPerformed

    }//GEN-LAST:event_buttonMoveLeftActionPerformed

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
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonMoveLeft;
    private javax.swing.JButton buttonMoveRight;
    private javax.swing.JButton buttonStart;
    private javax.swing.JPanel panelDraw;
    // End of variables declaration//GEN-END:variables
}
