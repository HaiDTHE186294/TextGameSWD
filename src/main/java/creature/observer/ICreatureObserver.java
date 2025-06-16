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
 *
 * @author Ha Duc Hau
 */
public interface ICreatureObserver {
    void onCreatureChanged(ACreature creature, String changeType);
}