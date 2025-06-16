/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.service;

import creature.model.ACreature;
import creature.model.Player;
import creature.skill.ISkill;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Utility class for creature operations
 * @author Ha Duc Hau
 */
public class CreatureUtils {

    /**
     * Calculate damage based on attack and defense
     */
    public static int calculateDamage(int attack, int defense) {
        return Math.max(1, attack - defense);
    }

    /**
     * Check if a creature can use a skill
     */
    public static boolean canUseSkill(ACreature creature, ISkill skill) {
        return creature.getStats().getMp() >= skill.getMpCost() && !skill.isOnCooldown();
    }

    /**
     * Apply experience gain and check for level up
     */
    public static void gainExperience(Player player, int exp) {
        player.gainExperience(exp);
    }
}
