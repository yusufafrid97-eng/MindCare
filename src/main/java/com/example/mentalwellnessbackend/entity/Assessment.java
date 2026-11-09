package com.example.mentalwellnessbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sleep;
    private String stress;
    private String mood;
    private int concentration;
    private int energy;
    private int overwhelmed;
    private int physicalActivity;
    private int socialConnection;
    private int screenTime;
    private int routineSatisfaction;
    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getStress() {
        return stress;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getConcentration() {
        return concentration;
    }

    public void setConcentration(int concentration) {
        this.concentration = concentration;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getOverwhelmed() {
        return overwhelmed;
    }

    public void setOverwhelmed(int overwhelmed) {
        this.overwhelmed = overwhelmed;
    }

    public int getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(int physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public int getSocialConnection() {
        return socialConnection;
    }

    public void setSocialConnection(int socialConnection) {
        this.socialConnection = socialConnection;
    }

    public int getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(int screenTime) {
        this.screenTime = screenTime;
    }

    public int getRoutineSatisfaction() {
        return routineSatisfaction;
    }

    public void setRoutineSatisfaction(int routineSatisfaction) {
        this.routineSatisfaction = routineSatisfaction;
    }
}
