
package edu.ub.misionespacial;

import edu.ub.misionespacial.actors.Heroi;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;

/**
 * Classe principal que controla i coordina el joc.
 * 
 * @author danieldelrio@ub.edu
 */
public class Jack implements KeyListener {

    private JFrame frame;    
    private Canvas canvas;
    private Context context;
    private Marcador marcador;
    
    private Joc joc;
    private LogicaJoc logica;
    
    private static final int NS_PER_FRAME = 1000 * 1000  * 1000 / 
            Constants.FRAMES_PER_SEGON;
    
    int x = 0;
    int y = 0; 
    
    /**
     * Constructor que posa en marxa el joc proporcionat.
     * 
     * @param joc el joc
     */
    public Jack(Joc joc) {
        this.joc = joc;
        this.logica = new LogicaJoc(joc);
        init();
        run();
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Listener dels events del teclat.
     * 
     * @param e l'event
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                logica.exit();
                break;
            case KeyEvent.VK_M:                
                logica.pausar(); 
                break;
            case KeyEvent.VK_S:
                logica.iniciar();
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_LEFT:
                context.updateKeys(e, true);
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        context.updateKeys(e, false);
    }
    
    // private methods *********************************************************
    
    private void init() {
	GraphicsEnvironment ge = 
	    GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        frame = new JFrame("UB :: Programacio I- Missio Espacial-");
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(Constants.AMPLADA_FINESTRA, 
                Constants.ALCADA_FINESTRA));
        
        frame.setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());        
        frame.add(canvas, BorderLayout.CENTER);        
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.addKeyListener(this);
        frame.pack();                
        
        Point cp = ge.getCenterPoint();
        cp.translate(- frame.getWidth() / 2, - frame.getHeight() / 2);
        frame.setLocation(cp.x, cp.y);
        
        frame.setVisible(true);                
        canvas.createBufferStrategy(2);
        canvas.requestFocus();
        
        marcador = new Marcador();
        
        context = new Context(joc);
    }
    
    private void run() {
        boolean contentsLost = false;
        
	long tempsFramePrevi = System.currentTimeMillis();
	long tempsFrameFinal;
                
        while (logica.getEstat() != LogicaJoc.EstatJoc.EXIT) {
            
		    long ara = System.currentTimeMillis();
		    long duracioFrame = (ara - tempsFramePrevi);
		    tempsFramePrevi = ara;
		    tempsFrameFinal = System.nanoTime() + NS_PER_FRAME;            

            BufferStrategy bufferStrategy = canvas.getBufferStrategy();
            context.setTempsFrame(duracioFrame);
                        
            // aixo controla els estats del joc
            switch (logica.getEstat()) {
                case MENU:
                    mostrarMenu(bufferStrategy);
                    break;
                case INICIANT:
                    break;
                case JUGANT:
                    actualitzarJoc();
                    doRender(bufferStrategy);            
                    break;
                case GAMEOVER:
                    mostrarGameOver(bufferStrategy);
                    break;
                case PAUSAT:
                    break;
            }
                            
            contentsLost = bufferStrategy.contentsLost();
            if (contentsLost) {
                if (bufferStrategy.contentsRestored())
                    contentsLost = false;
            } else {
                bufferStrategy.show();
            }            
            
            esperarFiFrame(tempsFrameFinal);  
        }
        System.out.println("Fins aviat!");
        System.exit(0);
    }
        
    private void actualitzarJoc() {
        Heroi heroi = (Heroi)joc.getHeroi();
        if ((heroi.isMort()) || (heroi.haTrobatArmaSecreta())) {
            logica.setEstat(LogicaJoc.EstatJoc.GAMEOVER);
        } else {        
            heroi.actualitzar(context);
            Habitacio h = joc.getFortalesa().getHabitacio();
            for (Actor actor : h.getActors()) {
                actor.actualitzar(context);
                if (actor.getEstat() == Constants.ESTAT_ACTIU && 
                        heroi.getLimits().intersects(actor.getLimits())) {
                    Colisio colisio = new Colisio(heroi);
                    actor.tractarColisio(colisio);
                }
            }        
        }
    }
    
    private void esperarFiFrame(long tempsFrameFinal) {
        Thread.yield();
        while (System.nanoTime() < tempsFrameFinal) {
            Thread.yield();
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
        }
    }    
    
    private void doRender(BufferStrategy bufferStrategy) {
        Graphics g = bufferStrategy.getDrawGraphics();
        
        Graphics2D g2 = (Graphics2D)g; 
        joc.getFortalesa().render(g2);
        Habitacio h = joc.getFortalesa().getHabitacio();
        for (Actor actor : h.getActors()) {
            if (actor.getEstat() != Constants.ESTAT_INACTIU)
                actor.render(g2);
        }
        
        joc.getHeroi().render(g2);
        
        marcador.render(context, g2);
    }            

    private void mostrarMenu(BufferStrategy bufferStrategy) {
        Graphics g = bufferStrategy.getDrawGraphics();        
        Graphics2D g2 = (Graphics2D)g; 
        dibuixarMarc(g2, Color.BLUE);
        
        Image image = Util.carregarImatge("astronaut.png", 0, 0, Heroi.AMPLADA, Heroi.ALCADA);
        g2.drawImage(image, 100, 50, frame);
        
        Font f = new Font("Dialog", Font.PLAIN, 30);
        g2.setFont(f);
        g2.setColor(Color.white);
        g2.drawString("* Missio espacial a la fortalesa de Morc *", 160, 90);
        
        f = new Font("Dialog", Font.PLAIN, 16);
        g2.setFont(f);
        g2.drawString("'S' Nova Partida", 180, 120);
        g2.drawString("'ESC' Sortir", 180, 150);
        
        image = Util.carregarImatge("portada.jpg", 0, 0, 610, 239);
        g2.drawImage(image, 100, 165, frame);
    }
    
    private void mostrarGameOver(BufferStrategy strategy) {
        Graphics g = strategy.getDrawGraphics();
        Graphics2D g2 = (Graphics2D)g; 
        Heroi heroi = (Heroi)joc.getHeroi();
        Font f = new Font("Dialog", Font.PLAIN, 30);
        String missatge = "";

	if (heroi.haTrobatArmaSecreta()) {
        	if (heroi.getVida() > 0.0f) {
	    		dibuixarMarc(g2, Color.GREEN);
	    		missatge = "* YOU WIN * Has desactivat el Arma Morc.";
		}
		else {
	    		dibuixarMarc(g2, Color.RED);
            		missatge = "* GAME OVER *  Jack ha mort.";
        	} 
	}

        g2.setFont(f);
        g2.setColor(Color.white);
        g2.drawString(missatge, 40, 90);
        f = new Font("Dialog", Font.PLAIN, 14);
        g2.setFont(f);
        
        f = new Font("Dialog", Font.PLAIN, 14);
        g2.setFont(f);
        g2.drawString("'ESC' Sortir", 180, 180);
	
	heroi.setHaTrobatArmaSecreta(false);
    }

    private void dibuixarMarc(Graphics2D g2, Color color) {
        Rectangle r = canvas.getBounds();
        r.setBounds((int)r.getX() + 20, (int)r.getY() + 20, (int)(r.getWidth() - 40),
                (int)(r.getHeight() - 40));
        
        g2.setColor(Color.BLACK);
        g2.fill(canvas.getBounds());
        g2.setColor(color);
        g2.setStroke(new BasicStroke(10.f, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND, 20.0f));        
        g2.draw(r);        
    }
}
