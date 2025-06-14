package item.service;

public interface InventoryObserver {
    void onInventoryChanged(String ownerId);
}
