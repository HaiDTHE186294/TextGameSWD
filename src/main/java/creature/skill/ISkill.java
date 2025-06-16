package creature.skill;

import creature.model.ACreature;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Interface for creature skills
 * @author Ha Duc Hau
 */
public interface ISkill {
    String getId();
    String getName();
    String getDescription();
    int getMpCost();
    int getCooldown();
    int getCurrentCooldown();
    boolean canUse(ACreature caster);
    void use(ACreature caster, ACreature target);
    void updateCooldown();
    boolean isOnCooldown();
} 