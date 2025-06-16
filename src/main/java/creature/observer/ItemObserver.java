/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.observer;

import creature.model.ACreature;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Observer for item-related changes in creatures
 * @author Ha Duc Hau
 */
public class ItemObserver implements ICreatureObserver {
    
    @Override
    public void onCreatureChanged(ACreature creature, String changeType) {
        switch (changeType) {
            case "EquipmentChanged":
                handleEquipmentChange(creature);
                break;
            case "ItemUsed":
                handleItemUsed(creature);
                break;
            case "ItemAdded":
                handleItemAdded(creature);
                break;
            case "ItemRemoved":
                handleItemRemoved(creature);
                break;
            default:
                // Ignore other change types
                break;
        }
    }
    
    private void handleEquipmentChange(ACreature creature) {
        System.out.println("[ItemObserver] " + creature.getName() + " changed equipment!");
        System.out.println("Current equipment count: " + creature.getEquipment().size());
        
        // Log equipment details
        creature.getEquipment().forEach(item -> 
            System.out.println("  - " + item.getName() + " (ID: " + item.getId() + ")")
        );
    }
    
    private void handleItemUsed(ACreature creature) {
        System.out.println("[ItemObserver] " + creature.getName() + " used an item!");
    }
    
    private void handleItemAdded(ACreature creature) {
        System.out.println("[ItemObserver] " + creature.getName() + " received a new item!");
    }
    
    private void handleItemRemoved(ACreature creature) {
        System.out.println("[ItemObserver] " + creature.getName() + " lost an item!");
    }
}
