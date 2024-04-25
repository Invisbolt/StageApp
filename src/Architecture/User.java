package Architecture;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abidine
 */
public class User {

    private String nomUtilisateur;
    private String motDePasse;
    private int codeEmploye;

    public User(String nomUtilisateur, String motDePasse, int codeEmploye) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.codeEmploye = codeEmploye;
    }

    /**
     * @return String return the nomUtilisateur
     */
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * @param nomUtilisateur the nomUtilisateur to set
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    /**
     * @return String return the motDePasse
     */
    public String getMotDePasse() {
        return motDePasse;
    }

    /**
     * @param motDePasse the motDePasse to set
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * @return int return the codeEmploye
     */
    public int getCodeEmploye() {
        return codeEmploye;
    }

    /**
     * @param codeEmploye the codeEmploye to set
     */
    public void setCodeEmploye(int codeEmploye) {
        this.codeEmploye = codeEmploye;
    }

}
