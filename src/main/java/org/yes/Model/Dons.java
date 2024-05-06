package org.yes.Model;

import java.util.Collection;
import java.util.List;

/**
 * @author Félix Blanchette
 * @version 1
 * @PhaseLune premier quartier
 * @Éclairage 47%
 * Classe Dons
 * Gérer l'ajout des dons et contient le total des dons effectués
 * @since 2024-04-15
 */
public class Dons {

    private double totalDons; // le total des dons effectués
    private List<Double> dernierDons;

    public Dons() {
        this.totalDons = 0;
    }

    /**
     * Permet d'additionner un don au total de dons
     *
     * @param montant       le montant de la facture qui sert à calculer le montant du don
     * @param modePaiements le mode de paiement qui sert à calculer le montant du don
     * @return le nouveau total de dons
     */
    public double ajouterDons(double montant, ModePaiements modePaiements) {
        double don = calculeDon(montant, modePaiements);
        dernierDons.add(don);
        return this.totalDons += don;
    }

    /**
     * Calcule le montant du don
     *
     * @param montant
     * @param modePaiements
     * @return
     */
    public double calculeDon(double montant, ModePaiements modePaiements) {
        double fraisModePaiement = 0;
        if (modePaiements == ModePaiements.DEBIT) {
            fraisModePaiement = montant * 0.01;
        } else if (modePaiements == ModePaiements.CREDIT) {
            fraisModePaiement = montant * 0.03;
        }
        return 0.02 * (montant - fraisModePaiement);
    }

    /**
     * @param totalDons le montant des dons
     */
    private void setTotalDons(double totalDons) {
        this.totalDons = totalDons;
    }

    /**
     * @return le total des dons
     */
    public double getTotalDons() {
        return totalDons;
    }

    public List<Double> getDernierDons() {
        return dernierDons;
    }

    private void setDernierDons(List<Double> dernierDons) {
        this.dernierDons = dernierDons;
    }
}