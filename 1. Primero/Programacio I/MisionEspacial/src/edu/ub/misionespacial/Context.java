
package edu.ub.misionespacial;

import java.awt.event.KeyEvent;

/** 
 * El context del joc representa l'estat del joc en un moment especific en el
 * temps. 
 * 
 * @author danieldelrio@ub.edu
 */
public class Context {

    private long tempsFrame;
    private Joc joc;
    private boolean[] keyMap = { false, false, false, false };

    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;
    public static final int KEY_LEFT = 2;
    public static final int KEY_RIGHT = 3;
    
    public Context(Joc joc) {
        this.joc = joc;
    }
    
    /**
     * Obte el joc.
     * 
     * @return el joc
     */
    public Joc getJoc() {
        return joc;
    }
    
    /**
     * Obte l'habitacio actual.
     * 
     * @return l'habitacio
     */
    public Habitacio getHabitacio() {
        return joc.getFortalesa().getHabitacio();
    }
        
    public boolean isKeyPressed(int key) {
        return keyMap[key];
    }
    
    public void updateKeys(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                keyMap[KEY_UP] = pressed;
                break;
            case KeyEvent.VK_DOWN:
                keyMap[KEY_DOWN] = pressed;
                break;
            case KeyEvent.VK_LEFT:
                keyMap[KEY_LEFT] = pressed;
                break;
            case KeyEvent.VK_RIGHT:
                keyMap[KEY_RIGHT] = pressed;
                break;
        }        
    }
    
    public long getTempsFrame() {
        return tempsFrame;
    }

    public void setTempsFrame(long tempsFrame) {
        this.tempsFrame = tempsFrame;
    }
                            
}
