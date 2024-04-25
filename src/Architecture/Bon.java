/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Architecture;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Abidine
 */
public class Bon {
    private Integer codeBon;
    private Employe employe;
    private String type_bon;
    private LocalDate dateBon;
    private LocalTime heureBon;
    private LocalTime heureV;
    private String motif;
    private char etatBon;

    public Bon(int codeBon, Employe employe, String type_bon, LocalDate dateBon, LocalTime heureBon, LocalTime heureV, String motif, char etatBon) {
        this.codeBon = codeBon;
        this.employe = employe;
        this.type_bon = type_bon;
        this.dateBon = dateBon;
        this.heureBon = heureBon;
        this.heureV = heureV;
        this.motif = motif;
        this.etatBon = etatBon;
    }

    public int getCodeBon() {
        return codeBon;
    }

    public void setCodeBon(int codeBon) {
        this.codeBon = codeBon;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public String getType_bon() {
        return type_bon;
    }

    public void setType_bon(String type_bon) {
        this.type_bon = type_bon;
    }

    public LocalDate getDateBon() {
        return dateBon;
    }

    public void setDateBon(LocalDate dateBon) {
        this.dateBon = dateBon;
    }

    public LocalTime getHeureBon() {
        return heureBon;
    }

    public void setHeureBon(LocalTime heureBon) {
        this.heureBon = heureBon;
    }

    public LocalTime getHeureV() {
        return heureV;
    }

    public void setHeureV(LocalTime heureV) {
        this.heureV = heureV;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public char getEtatBon() {
        return etatBon;
    }

    public void setEtatBon(char etatBon) {
        this.etatBon = etatBon;
    }

    
}