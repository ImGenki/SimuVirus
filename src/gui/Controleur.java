package gui;

import application.SimuVirus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Thomas Scalabre/Pigot Gabriel
 * A partir de cette classe, on controle la fenetre fxml
 * Elle possede des champs FXML qui seront liés à SceneBuilder
 * ainsi qu'un lien vers simuvirus+ stage
 */
public class Controleur {

    public SimuVirus application;
    @FXML TextField nbPersonne ;
    @FXML TextField nbInfectes;
    @FXML Button start;
    @FXML Button exit;
    private Stage dialogStage;

    public Controleur() { }

    /**
     * ajoute le nombre de personne dans la fenetre
     * @param application
     */
    public void setApplication(SimuVirus application)
    {
        this.application=application;
        nbPersonne.setText(String.valueOf(application.getEnv().getPersonnes().size()));

    }

    /**
     * Update le nombre d'infectés de telle sorte que le nombre s'actualise dans la fenetre FXML
     */
    public void UpdateNbreInfectes()
    {
        int nb=application.env.getNbInfectees();
        nbInfectes.setText(String.valueOf(nb));
    }

    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage=dialogStage;
    }

    /**
     * pour le bouton start
     */
    public void start()
    {
        System.out.println("Début simulation");
        application.go();
    }

    /**
     * pour le bouton exit
     */
    public void exit()
    {
        dialogStage.close();
    }





}
