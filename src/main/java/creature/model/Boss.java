package creature.model;

import creature.skill.ISkill;
import creature.skill.AttackSkill;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Boss enemy implementation with special abilities
 * @author Ha Duc Hau
 */
public class Boss extends Enemy {
    
    private int currentPhase;
    private int maxPhases;
    private double phaseHealthThreshold;
    private List<String> phaseSkills = new ArrayList<>();
    private Map<String, Object> bossData = new HashMap<>();
    private boolean isEnraged;
    private int enrageThreshold;
    private List<String> specialLoot = new ArrayList<>();

    public Boss() {
        super();
        this.currentPhase = 1;
        this.maxPhases = 3;
        this.phaseHealthThreshold = 0.3; // 30% HP per phase
        this.isEnraged = false;
        this.enrageThreshold = 20; // Enrage at 20% HP
        initializeBossData();
    }

    public Boss(String name, String bossType, int experienceReward, int maxPhases) {
        super(name, bossType, experienceReward);
        this.currentPhase = 1;
        this.maxPhases = maxPhases;
        this.phaseHealthThreshold = 1.0 / maxPhases;
        this.isEnraged = false;
        this.enrageThreshold = 20;
        initializeBossData();
    }

    private void initializeBossData() {
        bossData.put("isBoss", true);
        bossData.put("hasPhases", true);
        bossData.put("canEnrage", true);
        bossData.put("specialLoot", true);
    }

    @Override
    public void act() {
        // Boss AI behavior
        if (isEnraged) {
            System.out.println(name + " is ENRAGED and attacks with fury!");
        } else if (currentPhase > 1) {
            System.out.println(name + " is in phase " + currentPhase + " and uses advanced tactics!");
        } else {
            System.out.println(name + " enters the battlefield with overwhelming presence!");
        }
    }

    @Override
    public void takeDamage(int amount) {
        int oldHp = getStats().getHp();
        super.takeDamage(amount);
        
        // Check for phase transitions
        checkPhaseTransition(oldHp);
        
        // Check for enrage
        checkEnrage();
    }

    private void checkPhaseTransition(int oldHp) {
        double currentHpPercent = (double) getStats().getHp() / getStats().getMaxHp();
        int expectedPhase = Math.max(1, (int) Math.ceil(currentHpPercent / phaseHealthThreshold));
        
        if (expectedPhase > currentPhase && currentPhase < maxPhases) {
            transitionToPhase(expectedPhase);
        }
    }

    private void transitionToPhase(int newPhase) {
        currentPhase = newPhase;
        System.out.println("[PHASE TRANSITION] " + name + " transitions to PHASE " + currentPhase + "!");
        
        // Phase-specific effects
        switch (currentPhase) {
            case 2:
                System.out.println("  - " + name + " gains increased attack power!");
                getStats().setAttack(getStats().getAttack() + 5);
                break;
            case 3:
                System.out.println("  - " + name + " becomes more aggressive!");
                getStats().setSpeed(getStats().getSpeed() + 3);
                break;
        }
        
        // Add phase-specific skills
        addPhaseSkills();
        
        notifyObservers("PhaseChanged");
    }

    private void checkEnrage() {
        double hpPercent = (double) getStats().getHp() / getStats().getMaxHp() * 100;
        
        if (hpPercent <= enrageThreshold && !isEnraged) {
            enrage();
        }
    }

    private void enrage() {
        isEnraged = true;
        System.out.println("[ENRAGE] " + name + " becomes ENRAGED!");
        System.out.println("  - Attack power doubled!");
        System.out.println("  - Speed increased!");
        System.out.println("  - Special abilities unlocked!");
        
        // Enrage effects
        getStats().setAttack(getStats().getAttack() * 2);
        getStats().setSpeed(getStats().getSpeed() + 5);
        
        // Add enrage skills
        addEnrageSkills();
        
        notifyObservers("Enraged");
    }

    private void addPhaseSkills() {
        // Clear existing phase skills
        phaseSkills.clear();
        
        // Add skills based on current phase
        switch (currentPhase) {
            case 2:
                phaseSkills.add("multi_strike");
                phaseSkills.add("defensive_stance");
                break;
            case 3:
                phaseSkills.add("ultimate_attack");
                phaseSkills.add("heal_self");
                break;
        }
        
        // Create and add the skills
        for (String skillId : phaseSkills) {
            ISkill skill = createPhaseSkill(skillId);
            if (skill != null) {
                learnSkill(skill);
            }
        }
    }

    private void addEnrageSkills() {
        // Add enrage-specific skills
        ISkill enrageSkill = new AttackSkill("enrage_blast", "Enrage Blast", 
            "A devastating attack fueled by rage", 0, 2, 3.0);
        learnSkill(enrageSkill);
    }

    private ISkill createPhaseSkill(String skillId) {
        switch (skillId) {
            case "multi_strike":
                return new AttackSkill("multi_strike", "Multi Strike", 
                    "Attacks multiple times", 10, 3, 1.5);
            case "defensive_stance":
                return new AttackSkill("defensive_stance", "Defensive Stance", 
                    "Increases defense temporarily", 5, 4, 0.0);
            case "ultimate_attack":
                return new AttackSkill("ultimate_attack", "Ultimate Attack", 
                    "The boss's most powerful attack", 20, 5, 4.0);
            case "heal_self":
                return new AttackSkill("heal_self", "Heal Self", 
                    "Boss heals itself", 15, 4, 0.0);
            default:
                return null;
        }
    }

    public void addSpecialLoot(String itemId) {
        if (!specialLoot.contains(itemId)) {
            specialLoot.add(itemId);
        }
    }

    public List<String> getSpecialLoot() {
        return new ArrayList<>(specialLoot);
    }

    public List<String> getAllLoot() {
        List<String> allLoot = new ArrayList<>(getLootTable());
        allLoot.addAll(specialLoot);
        return allLoot;
    }

    // Getters and setters
    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = Math.max(1, Math.min(currentPhase, maxPhases));
    }

    public int getMaxPhases() {
        return maxPhases;
    }

    public void setMaxPhases(int maxPhases) {
        this.maxPhases = maxPhases;
        this.phaseHealthThreshold = 1.0 / maxPhases;
    }

    public double getPhaseHealthThreshold() {
        return phaseHealthThreshold;
    }

    public void setPhaseHealthThreshold(double phaseHealthThreshold) {
        this.phaseHealthThreshold = phaseHealthThreshold;
    }

    public List<String> getPhaseSkills() {
        return new ArrayList<>(phaseSkills);
    }

    public boolean isEnraged() {
        return isEnraged;
    }

    public void setEnraged(boolean enraged) {
        isEnraged = enraged;
    }

    public int getEnrageThreshold() {
        return enrageThreshold;
    }

    public void setEnrageThreshold(int enrageThreshold) {
        this.enrageThreshold = enrageThreshold;
    }

    public Map<String, Object> getBossData() {
        return new HashMap<>(bossData);
    }

    public void setBossData(String key, Object value) {
        bossData.put(key, value);
    }

    public boolean isInFinalPhase() {
        return currentPhase == maxPhases;
    }

    public double getHealthPercentage() {
        return (double) getStats().getHp() / getStats().getMaxHp() * 100;
    }

    @Override
    public String toString() {
        return String.format("Boss{name='%s', type='%s', phase=%d/%d, enraged=%s, expReward=%d, %s}", 
                           getName(), getEnemyType(), currentPhase, maxPhases, isEnraged, 
                           getExperienceReward(), getStats().toString());
    }
} 