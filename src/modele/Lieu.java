package modele;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Scalabre/Gabriel Pigot
 * Classe Lieu qui gere les lieus, les personnes dans les lieux
 */
public class Lieu {
    Point pos;//position dans la grille
    TypeLieu type;//type de lieu
    List<Personne> listepersonne;


    public Lieu() {
        listepersonne=new ArrayList<>();
    }

    public Lieu(Point pos) {
        this.pos=pos;
    }

    public Lieu(Point pos,TypeLieu type){
        this.pos=pos;
        this.type=type;
    }

    public Point getPos() {
        return pos;
    }
    public void addPersonne(Personne p) {
        listepersonne.add(p);
    }
    public void removePersonne(Personne p) {
        listepersonne.remove(p);
    }
    public List<Personne> getPersonnes() {
        return listepersonne;
    }

    public TypeLieu getTypeLieu(){
        return type;
    }
}
