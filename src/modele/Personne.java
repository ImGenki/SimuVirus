package modele;

import gui.PersonneImg;

import java.awt.Point;
import java.util.Objects;

/**
 * @author Thomas Scalabre/Gabriel Pigot
 * Modèle d'une personne
 */
public class Personne {
    Point pos;//position initiale
    Point posDomicile;//lieu du domicile
    boolean etat;//malade ou non
    boolean contagieux;//contagieuse ou non
    PersonneImg img;//lien vers sa représentation graphique
    Environnement env;//lien vers l'environnement
    int duree;//duree de contact
    int id=0;//id de la personne
    static int nb=0;//incrémente les id
    Activite activite=Activite.REPOS;//activite de départ
    Lieu domicile;


    public Personne(Point pos)
    {
        this.pos = this.posDomicile=pos;
        id= ++nb;
    }

    /**
     * @param pos Position dans le plan
     * @param env Lien environnement
     */
    public Personne(Point pos, Environnement env)
    {
        this(pos);
        this.env=env;
        this.domicile=env.getLieu(pos);
    }

    /**
     *
     * @return la position
     */
    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos=pos;
    }


    /** changement de position*/
    public void moveTo(int x,int y)
    {
        pos.x=x;
        pos.y=y;
    }


    /**
     * @author emmanuel adam
     * prend une position ligne,colonne (x,y) aléatoire et en informe l'environnement*/
    public void errer()
    {
        int x=(int) (Math.random()*env.x);
        int y=(int) (Math.random()*env.y);
        moveTo(x,y);
        env.updatePos(this);
    }


    /**
     * @author Thomas Scalabre/Gabriel Pigot
     * autre version de errer qui prend en compte les activités
     */
    public void updateErrer()
    {
        Lieu ls=null;
        if(activite==Activite.REPOS) {
            activite = Activite.TRAVAIL; //passe du repos au travail
            ls= env.selectLieu(TypeLieu.ENTREPRISE);
        }
        else if(activite==Activite.TRAVAIL){
            activite=Activite.COURSE;//passe du travail aux courses
            ls= env.selectLieu(TypeLieu.MAGASIN);
        }
        else if(activite==Activite.COURSE){
            activite=Activite.REPOS;//retourne au repos
            ls= domicile; //attribution d'un domicile fixe

        }
        moveTo((int)ls.getPos().getX(), (int)ls.getPos().getY()); //bouge la personne
        env.updatePos(this);//update la position
    }


    /** incrémente la durée de contact si un malade se trouve dans la même place, ou l'annule sinon*/
    public void detecteContagion()
    {
        boolean cas = env.listepersonnes
                .stream()
                .anyMatch(p-> Objects.equals(p.pos, pos) && p.contagieux);
        if(cas) duree++;
        else duree = 0;//annulation de contagion
        if(duree>3)//si la personne est exposée plus de 3 tours, elle devient malade/contagieuse
        {
            setContagieux(true);
            img.setInfected();
        }
    }



    public Point getPosDomicile() {
        return posDomicile;
    }

    public void setPosDomicile(Point posDomicile) {
        this.posDomicile = posDomicile;
    }

    public boolean isMalade() {
        return etat;
    }

    public void setMalade(boolean malade) {
        this.etat = malade;
    }

    public boolean isContagieux() {
        return contagieux;
    }

    public void setContagieux(boolean contagieux) {
        this.contagieux = contagieux;
    }

    public PersonneImg getImg() {
        return img;
    }

    public void setImg(PersonneImg img) {
        this.img = img;
    }


    /**Redefinition toString pour afficher les info d'une personne*/
    @Override
    public String toString() {
        return "Personne"+"d'id"+ id +"en position"+pos+"avec pour position de domicile"+posDomicile+". Etat de malade:"+etat+"contagion:"+contagieux;
    }
}