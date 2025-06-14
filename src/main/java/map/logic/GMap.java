package map.logic;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import map.observer.IMapObserver;

public class GMap {
    private final Map<String, Area> areas = new HashMap<>();
    private final List<IMapObserver> observers = new ArrayList<>();

    public void addObserver(IMapObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IMapObserver observer) {
        observers.remove(observer);
    }

    public void addArea(Area area) {
        areas.put(area.getName(), area);
        notifyMapInitialized();
    }

    public Area getArea(String areaName) {
        return areas.get(areaName);
    }

    public Collection<Area> getAllAreas() {
        return areas.values();
    }

    public boolean hasArea(String areaName) {
        return areas.containsKey(areaName);
    }

    public void removeArea(String areaName) {
        areas.remove(areaName);
    }

    public void clear() {
        areas.clear();
    }

    // Observer notification methods
    private void notifyMapInitialized() {
        for (IMapObserver observer : observers) {
            observer.onMapInitialized();
        }
    }

    public void notifyPlayerMoved(String areaId, String roomId, int row, int col) {
        for (IMapObserver observer : observers) {
            observer.onPlayerMoved(areaId, roomId, row, col);
        }
    }

    public void notifyCellChanged(String areaId, String roomId, int row, int col, boolean isWalkable, String content) {
        for (IMapObserver observer : observers) {
            observer.onCellChanged(areaId, roomId, row, col, isWalkable, content);
        }
    }

    public void notifyRoomTransition(String targetAreaId, String targetRoomId, int targetRow, int targetCol) {
        for (IMapObserver observer : observers) {
            observer.onRoomTransitionRequested(targetAreaId, targetRoomId, targetRow, targetCol);
        }
    }

    public void notifyAreaTransition(String targetAreaId) {
        for (IMapObserver observer : observers) {
            observer.onAreaTransitionRequested(targetAreaId);
        }
    }

    public void notifyMapStateChanged(String areaId, String roomId, String state) {
        for (IMapObserver observer : observers) {
            observer.onMapStateChanged(areaId, roomId, state);
        }
    }
} 