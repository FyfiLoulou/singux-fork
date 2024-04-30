package org.yes.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.yes.Model.Dons;
import org.yes.Model.Facture;
import org.yes.Model.FacturesFactory;
import org.yes.Model.ModePaiements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author Maek Lorman
 * Classe AppGraphicalController
 * cette classe s'occupe d'aller chercher des informations importantes pour la facture
 */
public class AppGraphicalController extends AppController {

    @FXML
    public Text montantDons;
    public TextArea textArea;
    @FXML
    private TextField nomAcheteur;
    @FXML
    private TextField montantSansTaxes;
    @FXML
    private TextField taxes;
    @FXML
    private RadioButton debit;
    @FXML
    private RadioButton credit;
    @FXML
    private RadioButton argent;
    @FXML
    private Button creer;
    @FXML
    private Button rafraichir;
    @FXML
    private Text montantTotal;
    @FXML
    private Text erreurModePaiement;
    @FXML
    private Text erreurNomAcheteur;
    @FXML
    private Text erreurMontantSansTaxes;
    @FXML
    private Text erreurTaxes;
    FacturesFactory facturesFactory = new FacturesFactory();
    Dons dons = new Dons();
    public static final DecimalFormat decfor = new DecimalFormat("0.00");

    @FXML
    private void initialize() {
        // afficher total
        montantSansTaxes.setOnKeyReleased(event -> afficherMontantTotal());
        taxes.setOnKeyReleased(event -> afficherMontantTotal());

        // bouton creer facture
        creer.setOnMouseClicked(event -> {
            String nomAcheteur = getNomAcheteur();
            double montantSansTaxes = getMontantSansTaxes();
            double taxes = getMontantTaxes();
            ModePaiements modePaiement = getModePaiement();
            String erreurs = verificationChamps();

            // afficher les erreurs
            erreurNomAcheteur.textProperty().setValue(erreurs.contains("nom") ? "Le nom de l'acheteur est invalide" : "");
            erreurModePaiement.textProperty().setValue(erreurs.contains("modePaiement") ? "Le mode de paiement est invalide" : "");
            erreurMontantSansTaxes.textProperty().setValue(erreurs.contains("montantSansTaxes") ? "Le montant sans taxes est invalide" : "");
            erreurTaxes.textProperty().setValue(erreurs.contains("montantTaxes") ? "Le montant des taxes est invalide" : "");

            if (erreurs == "") {
                Facture facture = facturesFactory.build(nomAcheteur, montantSansTaxes, modePaiement, taxes); // création d'une facture
                afficherDons(dons.ajouterDons(montantSansTaxes + taxes, modePaiement)); // ajout et affichage des dons
                rafraichirPage();
                afficherDernierDon();
                System.out.println("Nouvelle Facture Créée: " + facture.toString());
            }
        });

        // réinitialise tous les champs
        rafraichir.setOnMouseClicked(event -> rafraichirPage());
    }

    private String verificationChamps() {
        String erreurs = "";
        if (getNomAcheteur() == null) erreurs += "nomAcheteur";
        if (getMontantTaxes() == -1) erreurs += "montantTaxes";
        if (getMontantSansTaxes() == -1) erreurs += "montantSansTaxes";
        if (getModePaiement() == null) erreurs += "modePaiement";
        return erreurs;
    }

    private void afficherMontantTotal() {
        montantTotal.textProperty().setValue(
                !verificationChamps().contains("montantTaxes") && !verificationChamps().contains("montantSansTaxes") ?
                        decfor.format(getMontantSansTaxes() + getMontantTaxes()) + "$"
                        : "inconnu"
        );
    }

    private void rafraichirPage() {
        setNomAcheteur("");
        setMontantTaxes("0.00");
        setMontantSansTaxes("0.00");
        setModePaiement("");
        afficherMontantTotal();
        erreurNomAcheteur.textProperty().setValue("");
        erreurModePaiement.textProperty().setValue("");
        erreurMontantSansTaxes.textProperty().setValue("");
        erreurTaxes.textProperty().setValue("");
    }

    private void setNomAcheteur(String nom) {
        nomAcheteur.textProperty().setValue(nom);
    }

    private void setMontantSansTaxes(String montant) {
        montantSansTaxes.textProperty().setValue(montant);
    }

    private void setMontantTaxes(String montant) {
        taxes.textProperty().setValue(montant);
    }

    private void setModePaiement(String modePaiement) {
        if (modePaiement == "argent") argent.setSelected(true);
        else if (modePaiement == "credit") credit.setSelected(true);
        else if (modePaiement == "debit") debit.setSelected(true);
        else {
            argent.setSelected(false);
            credit.setSelected(false);
            debit.setSelected(false);
        }
    }

    /**
     * @return le nom de l'acheteur ou null si invalide
     */
    private String getNomAcheteur() {
        String nom = nomAcheteur.textProperty().getValue();
        return nom.matches("^[a-zA-ZÀ-ö -]+$") ? nom : null;
    }

    /**
     * @return -> le montant de l'achat sans les taxes et dons ou -1 si invalide
     */
    private double getMontantSansTaxes() {
        double montant = Facture.verificationFormatArgent(montantSansTaxes.textProperty().getValue());
        if (montant > 15000) {
            return -1;
        }
        return montant;
    }

    /**
     * @return -> la valeur des taxes appliquées ou -1 si invalide
     */
    private double getMontantTaxes() {
        return Facture.verificationFormatArgent(taxes.textProperty().getValue());
    }

    /**
     * va chercher le mode de paiement coché par l'utilisateur ou null si invalide
     */
    private ModePaiements getModePaiement() {
        ModePaiements modePaiement;
        try {
            modePaiement = ModePaiements.valueOf(argent.isSelected() ? argent.getId() : credit.isSelected() ? credit.getId() : debit.isSelected() ? debit.getId() : "");
        } catch (Exception e) {
            modePaiement = null;
        }
        return modePaiement;
    }

    private void afficherDons(double montant) {
        montantDons.setText("Total: " + decfor.format(montant) + "$");
    }

    private void afficherDernierDon() {
        List<Double> donAAfficher = dons.getDons();
        String valeur = textArea.textProperty().getValue();
        textArea.textProperty().setValue(valeur + decfor.format(donAAfficher.get(donAAfficher.size() - 1)) + "\n");
    }
}