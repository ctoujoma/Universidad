
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
 * El oxigen proporciona un increment en el nivell de vida de l'actor que 
 * col.lisiona amb ella.
 * 
 */
public class Oxigen extends AbstractActor {

    private float quantitat;
    private Image image;
    private int amplada = 20;
    private int alcada = 37;

    
    /**
     * Crea un nou objecte del tipus Oxigen amb l'imatge del fitxer
     * <code>oxigen.png</code>,  i la quantitat proporcionada. La quantitat de oxigen
     * aporten un guany proporcional en la vida del jugador.
     */
    public Oxigen(float quantitatOxigen) {
        this.quantitat = quantitatOxigen;
        this.image = Util.carregarImatge("o2.png", 
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
	        actor.setVida(Math.min(100.0f, actor.getVida() + quantitat));
	        setEstat(Constants.ESTAT_INACTIU);
        }
    }

    public void render(Graphics2D g) {
    	//Com dibuixar a la pantalla principal
        g.drawImage(image, getX(), getY(), observer);
    }
           
}
