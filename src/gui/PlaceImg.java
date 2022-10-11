package gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * @author ScalabreThomas/GabrielPigot
 * Image de la class Place qui permet de crée la représentation
 * d'une place(un cercle)
 */
public class PlaceImg extends Circle {

    private  Paint Couleur;

    /**@param x centre x
     * @param y centre y
     * @param rayon rayon
     * */
    public PlaceImg(double x, double y, double rayon) {
        super(x, y, rayon, Color.WHITE);
        setOpacity(0.5);
    }

    public Paint getCouleur() {
        return Couleur;
    }

    public void setCouleur(Paint maCouleur) {
        this.Couleur = maCouleur;
    }


    public String toString()
    {
        return "place en " + getCenterX() + ", " + getCenterY();
    }
}