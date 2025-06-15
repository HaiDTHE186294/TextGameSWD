package item.ui;

import item.service.InventoryObserver;

public class InventoryUIObserver implements InventoryObserver {
    private final String ownerId;
    private final InventoryUIUpdater uiUpdater;

    public InventoryUIObserver(String ownerId, InventoryUIUpdater uiUpdater) {
        this.ownerId = ownerId;
        this.uiUpdater = uiUpdater;
    }

    @Override
    public void onInventoryChanged(String changedOwnerId) {
        if (ownerId.equals(changedOwnerId)) {
            uiUpdater.updateInventoryUI();
        }
    }
}