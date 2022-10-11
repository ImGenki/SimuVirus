package application;

import gui.Controleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import modele.Environnement;
import modele.Lieu;
import modele.Personne;
import gui.PersonneImg;
import gui.PlaceImg;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import modele.TypeLieu;

import java.awt.Point;
import java.io.File;
import java.net.URL;
import java.util.Optional;

                                    //Thomas SCALABRE et Gabriel Pigot



/**
 * @author Thomas Scalabre/Gabriel Pigot/Emmanuel Adam
 * La classe SimuVirus sert de classe Main et permet de tout gérer
 * Elle possède des méthodes qui vont gérer la création du quadrillage, des personnes
 * de l'environnement, de L'FXML etc etc
 *
 */
public class SimuVirus extends Application implements EventHandler<MouseEvent>
{

    public Environnement env;
    /**hauteur du panneau*/
    private double height;
    /**largeur du panneau*/
    private double width;
    /**decalage pour centrer le dessin*/
    private int decalage;
    /**dimension de la grille*/
    private int dim;

    /** controller fxml*/
    private Controleur controleur=new Controleur();

    /**  nb pixels utilise en largeur pour une "case de la grille"*/
    private double widthStep;
    /**  nb pixels utilise en hauteur pour une "case de la grille"*/
    private double heightStep;

    /**personne selectionnee*/
    private PersonneImg personneSelectionnee;

    /**ensemble des personne*/
    private Group groupe;

    /**active ou non l'animation*/
    boolean go = false;
    /**tempo pour l'animation*/
    Timeline littleCycle;

    /**quelques couleurs*/
    private final Color couleurFond = Color.DARKGRAY;
    private final Color couleurDomicile = Color.ALICEBLUE;
    private final Color couleurEntreprise = Color.DARKRED;
    private final Color couleurMagasin = Color.FORESTGREEN;

    /**
     * lancement de l'application
     *
     */
    public void start(Stage primaryStage) {
        dim = 30;
        decalage = 1;
        width = 500;
        height = 500;
        heightStep = height/dim;
        widthStep = width/dim;
        env = new Environnement(dim,dim, this);
        construirePlateau(primaryStage);
    }

    /**
     * construction du plateau, Gestion des menus
     * @author Thomas Scalabre/Gabriel Pigot/Emmanuel Adam
     */
    private void construirePlateau(Stage primaryStage) {
        groupe = new Group();
        // definir la scene principale
        Scene scene = new Scene(groupe, 2 * widthStep + width, 2 * heightStep + height, Color.ANTIQUEWHITE);
        scene.setFill(couleurFond);
        dessinEnvironnement();
        ajoutPersonne();
        env.creerAnnuaire();

        //Creer un menu
        Menu mParam = new Menu("Menu");
        //creer la barre de menu
        MenuBar mb = new MenuBar();
        //Creer un item de menu
        MenuItem mi = new Menu("Automatisation");
        //définition de l'action(automatiser le stage)
        mi.setOnAction(e->Simulation(primaryStage));
        //ajout l'item au menu
        mParam.getItems().add(mi);
        //ajout du menu à la barre
        mb.getMenus().addAll(mParam);

        //Mise en forme verticale du menu
        VBox vb = new VBox(mb);


        groupe.getChildren().add(vb);

        //-----lancer le timer pour faire vivre la matrice
        //cycle de 4 arret + 1 mouvement
        long tempo = 300;
        int[] step={0};
        littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo),
                event -> {if(step[0] > 4)
                {
                    env.bouger();
                    step[0] = 0;
                }
                else env.avancerTemps();
                    step[0]++;
                    controleur.UpdateNbreInfectes();
                }));
        littleCycle.setCycleCount(Timeline.INDEFINITE);
        System.out.println("tapez b pour bouger, t pour temporiser, g pour une animation automatique");
        scene.setOnKeyTyped(e->agirSelonTouche( e.getCharacter()));

        primaryStage.setTitle("simu propagation ...");
        primaryStage.setScene(scene);
        // afficher le cadrillage
        primaryStage.show();
    }

    /**
     * @author Thomas Scalabre/Gabriel  Pigot
     * @param primaryStage le stage principal
     * cette méthode permet de gérer les dialogues du stage.
     *
     */
    public void Simulation(Stage primaryStage) {
        FXMLLoader fxmlLoader = null;
        AnchorPane page = null;
        try {
            URL url = new File("src/application/controleur.fxml").toURI().toURL(); //L'URL mene au fichier .fxml
            fxmlLoader = new FXMLLoader(url);
            page = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(page!=null) {
            Stage dialogueStage = new Stage();
            dialogueStage.setTitle("Automatisation");
            dialogueStage.initModality(Modality.WINDOW_MODAL);
            dialogueStage.initOwner(primaryStage);
            Scene miniScene = new Scene(page);
            dialogueStage.setScene(miniScene);
            controleur = fxmlLoader.getController();
            controleur.setDialogStage(dialogueStage);
            controleur.setApplication(this);
            dialogueStage.show();
        }
    }



    /**active ou pause l'animation
     * @author Emmanuel Adam
     */

    public void go()
    {
        go = !go;
        if(go) littleCycle.play();
        else littleCycle.pause();
    }

    /**
     * @author Thomas Scalabre/Gabriel Pigot
     * @param c clic
     */
    void agirSelonTouche(String c)
    {
        System.out.println("clic sur " + c);
        switch(c)
        {
            case "b" : env.bouger();
            case "g" : go();
            case "t" : env.avancerTemps();
        }
    }


    /**
     * dessin de l'environnement
     * @author Emmanuel Adam
     */
    private void dessinEnvironnement()
    {
        //Les lignes
        for(int i=0; i<dim; i++)
        {
            Line line1=new Line(decalage*widthStep, (i+decalage)*heightStep, (dim-1+decalage)*widthStep, (i+decalage)*heightStep);
            Line line2=new Line((i+decalage)*widthStep, decalage*heightStep, (i+decalage)*widthStep, (dim-1+decalage)*heightStep);
            line1.setStrokeWidth(widthStep/10);
            line1.setStroke(Color.GRAY);
            line1.setOpacity(0.9);
            line2.setStrokeWidth(6);
            line2.setStroke(Color.GRAY);
            line2.setOpacity(0.9);
            groupe.getChildren().add(line1);
            groupe.getChildren().add(line2);
        }
        ajouterPlaces();
    }

    /**
     * @author Thomas Scalabre/Gabriel Pigot
     * Permet d'ajouter des places, en premier lieu les domiciles puis les magasins/entreprises grâce au random
     */
    private void ajouterPlaces() {
        for(int i=0; i<dim; i++)
        {
            double x = (i+decalage)*widthStep;
            for(int j=dim/2; j<dim; j++)
            {

                double y=(j+decalage)*heightStep;
                PlaceImg c=new PlaceImg(x,y,heightStep/5);
                c.setCouleur(couleurDomicile);
                c.setFill(c.getCouleur());
                c.setOnMouseClicked(this);
                Lieu maison=new Lieu(new Point(i,j), TypeLieu.DOMICILE);
                env.addLieu(maison);
                groupe.getChildren().add(c);

            }
            for(int j=0;j<dim/2;j++)
            {
                double rand=Math.random();
                if(rand>0.33)//permet de répartir 1/3 2/3, d'abord les entreprises ici
                {
                    double y=(j+decalage)*heightStep;
                    PlaceImg c=new PlaceImg(x,y,heightStep/5);//nouvelle place
                    c.setCouleur(couleurEntreprise);
                    c.setFill(c.getCouleur());
                    c.setOnMouseClicked(this);
                    Lieu boulot=new Lieu(new Point(i,j), TypeLieu.ENTREPRISE);
                    env.addLieu(boulot);//ajout du lieu dans la liste
                    groupe.getChildren().add(c);
                }
                else//magasin
                {
                    double y=(j+decalage)*heightStep;
                    PlaceImg c=new PlaceImg(x,y,heightStep/5);
                    c.setCouleur(couleurMagasin);
                    c.setFill(c.getCouleur());
                    c.setOnMouseClicked(this);
                    Lieu shop=new Lieu(new Point(i,j), TypeLieu.MAGASIN);
                    env.addLieu(shop);
                    groupe.getChildren().add(c);
                }
            }
        }
    }


    /**
     * @author Scalabre Thomas/Pigot Gabriel
     * Cette méthode permet d'ajouter une personne  à l'environnement
     * On cree les personnes(sans oublier les images des personnes
     *
     */
    private void ajoutPersonne()
    {
        for(int i=0; i<dim; i++)
        {
            double x=(i+decalage)*widthStep;
            for(int j=dim/2; j<dim; j++)
            {
                double y=(j+decalage)*heightStep;
                PersonneImg pi1=new PersonneImg(x,y,heightStep/3, Color.PINK);
                PersonneImg pi2=new PersonneImg(x,y,heightStep/3,Color.PINK);//2 personnes par domicile
                pi1.setOnMouseClicked(this);
                pi2.setOnMouseClicked(this);
                Point pos=new Point(i,j);
                Personne p1=new Personne(pos, env);
                Personne p2=new Personne(pos,env);
                env.addPersonne(p1);
                env.addPersonne(p2);//ajout des personnes dans la liste de personnes
                p1.setImg(pi1);
                p2.setImg(pi2);
                groupe.getChildren().add(pi1);//ajout de l image dans le groupe
                groupe.getChildren().add(pi2);
            }
        }
    }



    /**gestion de la souris : selection de personne/place
     * @author emmanuel adam
     * */
    @Override
    public void handle(MouseEvent mouseEvent) {

        if(mouseEvent.getSource().getClass() == PersonneImg.class)
        {
            personneSelectionnee=(PersonneImg)mouseEvent.getSource();
            int px = (int)Math.round(personneSelectionnee.getCenterX() / widthStep - decalage);
            int py = (int)Math.round(personneSelectionnee.getCenterY() / heightStep - decalage);
            env.switchContagieux(px, py);
            personneSelectionnee.switchInfected();
            if(!personneSelectionnee.isInfected()) personneSelectionnee =null;
        }
        else if(mouseEvent.getSource().getClass() == PlaceImg.class)
        {
            PlaceImg p=(PlaceImg)mouseEvent.getSource();
            if(personneSelectionnee !=null && personneSelectionnee.isInfected()) {
                int startX=(int)Math.round(personneSelectionnee.getCenterX() / widthStep - decalage);
                int startY=(int)Math.round(personneSelectionnee.getCenterY() / heightStep - decalage);
                int endX=(int)Math.round(p.getCenterX() / widthStep - decalage);
                int endY=(int)Math.round(p.getCenterY() / heightStep - decalage);
                Optional<Personne> op = env.getPersonnes().stream().filter(person->person.getImg()== personneSelectionnee).findFirst();
                op.ifPresent(person->person.setPos(new Point(endX, endY)));
                System.err.println ("startX, startY, endX, endY=" + startX+","+ startY+","+ endX+","+ endY);
                animPersonneVers(p);
            }
        }
    }

    /**
     * @author adam emmanuel
     * deplace l'image de la personne vers le point d'arrivee
     * fonction lancee par l'environnement*/
    public void updatePos(PersonneImg img, Point arrivee)
    {
        double endX=(arrivee.x + decalage )*widthStep ;
        double endY=(arrivee.y + decalage )*heightStep ;

        Timeline timeline=new Timeline();
        KeyFrame bougePersone=new KeyFrame(new Duration(500),
                new KeyValue(img.centerXProperty(), endX),
                new KeyValue(img.centerYProperty(), endY));
        timeline.getKeyFrames().add(bougePersone);
        timeline.play();
    }

    /**
     * @return l'environnement
     */
    public Environnement getEnv(){
        return env;
    }


    /**lancement d'une animation de deplacement du jeton selectionne vers la place
     *@param p la place destination du jeton selectionne */
    private void animPersonneVers(PlaceImg p)
    {
        Timeline timeline=new Timeline();
        double xdest=p.getCenterX();
        double ydest=p.getCenterY();
        KeyFrame bougeVoiture = new KeyFrame(new Duration(500),
                new KeyValue(personneSelectionnee.centerXProperty(), xdest),
                new KeyValue(personneSelectionnee.centerYProperty(), ydest));
        timeline.getKeyFrames().add(bougeVoiture);
        timeline.play();
    }

    /**lancement de la fenetre*/
    public static void main(String[] args) {
        launch(args);
    }
}