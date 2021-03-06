package edu.ub.misionespacial.actors;

import edu.ub.misionespacial.Actor;
import edu.ub.misionespacial.Colisio;
import edu.ub.misionespacial.Constants;
import edu.ub.misionespacial.Context;
import edu.ub.misionespacial.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * El arma mata l'actor que 
 * col.lisiona amb ella.
 * 
 */
public class Arma extends AbstractActor {

    private float quantitat;
    private Image image;
    private int amplada = 50;
    private int alcada = 59;

    
    /**
     * Crea un nou objecte del tipus Arma amb l'imatge del fitxer
     * <code>weapon.png</code>,  i la quantitat proporcionada.
     */
    public Arma(float quantitatArma) {
        this.quantitat = quantitatArma;
        this.image = Util.carregarImatge("weapon.png", 
                0, 0, this.amplada, this.alcada);
        setEstat(Constants.ESTAT_ACTIU);
    }
        
    
    public float getQuantitat() {
        return quantitat;
    }
    
  
    public void setQuantitat(float quantitat) {
        this.quantitat = quantitat;
    }

    public void actualitzar(Context context) {
    	// a cada iteracio del joc es crida a actualizar desde la classe Jack al metode actualizarJoc
        // no fem res, es estatic (no se mou)
    }

    public Rectangle getLimits() {
    	// es important per tractar les colisions des de la classe Jack al metode actualizarJoc
        return new Rectangle(getX(), getY(), amplada, alcada);
    }

    public void tractarColisio(Colisio colisio) {
    	// a cada iteracio del joc es crida a actualizar desde la classe Jack al metode actualizarJoc
        Actor actor = colisio.getActor();
        // Cal explicar el que es instanceof
        if (actor instanceof Heroi) {
		Heroi heroi = (Heroi)(actor);
		int[] randomCode = generateCode();
		heroi.desactivaArmaSecreta(randomCode);
		heroi.setHaTrobatArmaSecreta(true); 
	        setEstat(Constants.ESTAT_INACTIU);
        }
    }
    
    public int[] generateCode() {
	int[] combinacio = new int[4];
	for (int i = 0; i < 4; i++)
		combinacio[i] = (int)(Math.random() * 10);
	return combinacio;
    }

    public void render(Graphics2D g) {
    	//Com dibuixar a la pantalla principal
        g.drawImage(image, getX(), getY(), observer);
    }
           
}
