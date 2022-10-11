package modele;


import application.SimuVirus;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import modele.Lieu;

import static java.lang.Math.*;


public class Environnement {

    int x;//dimension x
    int y;//dimension y
    SimuVirus simu;//Lien vers la Simuvirus



    /**liste de personnes*/
    List<Personne> listepersonnes;

    /**Hashmap*/
    Map<Point,Lieu> plan;
    Map<TypeLieu,List<Lieu>> annuaire;

    /** Lieu*/
    List<Lieu> listelieu;

    public Environnement()
    {
        listepersonnes = new ArrayList<>();
        plan=new HashMap<>();
        annuaire=new HashMap<>();
        listelieu=new ArrayList<>();
    }

    /**
     *
     * @param x dimension x
     * @param y dimension y
     */
    public Environnement(int x, int y)
    {
        this();
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param x dimension x
     * @param y dimension y
     * @param simu Simulation virus(lien)
     */
    public Environnement(int x, int y, SimuVirus simu) {
        this(x, y);
        this.simu = simu;
    }

    /**Creer un plan
     * @param l  Lieu
     * @param p Point(ccordonnées)
     */

    /**
     * @param p
     * @return retour le lieu à la position p
     */
    public Lieu getLieu(Point p) {
        return plan.get(p);//retourne la valeur avec sa clef
    }

    /**
     * ajouter un lieu dans la list
     * @param l
     */
    public void addLieu(Lieu l) {
        Point p=new Point();
        listelieu.add(l);
        plan.put(l.pos,l);
    }

    /**
     * creer un annuaire
     */
    public void creerAnnuaire()
    {
        Stream<Lieu> flux=listelieu.stream();
        annuaire=flux.collect(Collectors.groupingBy(Lieu::getTypeLieu));
    }

    /**
     *
     * @param typeLieu typedelieu
     * @return prend dans l'annuaire un lieu random de type typelieu
     */
    public Lieu selectLieu(TypeLieu typeLieu){
        return annuaire.get(typeLieu).get((int) (round((random()*(annuaire.get(typeLieu).size()-1)))));
    }

    /**demande à chaque personne dans l'environnement de bouger*/
    public void bouger()
    {
        listepersonnes.forEach(Personne::updateErrer);
    }

    /** demande à chaque personne de calculer son temps de contact avec un malade(contagieux)*/
    public void avancerTemps()
    {
        listepersonnes.forEach(Personne::detecteContagion);
    }
    /** ajouter une personne dans la liste de personne*/
    public void addPersonne(Personne p)
    {
        listepersonnes.add(p);
    }
    /** enleve une personne de la liste */
    public void removePersonne(Personne p)
    {
        listepersonnes.remove(p);
    }

    /**
     * récupère la personne en colonne, ligne (x,y) et bascule son état contagieux, fonction lancée par l'application
     * @param x coordonnée x
     * @param y coordonnée y
     */
    public void switchContagieux(int x, int y)
    {
        Point pos= new Point(x,y);
        listepersonnes//source
                .stream()//permet d'executer une serie d'operations de manieres sequentielles ou en parallèles.
                .filter(p->p.pos.equals(pos))
                .findFirst()
                .ifPresent(p->p.setContagieux(!p.isContagieux()));

    }

    /**propage l'info sur le changement de position d'une personne au controleur graphique*/
    public void updatePos(Personne p)
    {
        simu.updatePos(p.getImg(),  p.pos);
    }


    /**Retourne une personne de la liste*/
    public List<Personne> getPersonnes() {
        return listepersonnes;
    }

    public void setx(int x) {
        this.x=x;
    }
    public void sety(int y) {
        this.y=y;
    }

    public  int getx() {
        return x;
    }
    public  int gety() {
        return y;
    }

    /**Retourne le nombre d'infectées*/
    public int getNbInfectees()
    {
        return (int) listepersonnes.stream().filter(Personne::isContagieux).count();
    }
}