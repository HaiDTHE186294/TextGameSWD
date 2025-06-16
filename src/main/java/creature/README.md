# Creature Observer Pattern Implementation

## Tổng quan

Dự án này implement Observer pattern cho hệ thống creature trong game, cho phép theo dõi và phản ứng với các thay đổi của creature một cách linh hoạt. Hệ thống cũng tích hợp với ItemService để quản lý inventory và items. Bao gồm cả Boss system với các tính năng đặc biệt như phases, enrage, và special loot.

## Cấu trúc Observer Pattern

### 1. Interface ICreatureObserver
```java
public interface ICreatureObserver {
    void onCreatureChanged(ACreature creature, String changeType);
}
```

### 2. Subject (ACreature)
- Quản lý danh sách observers
- Cung cấp methods `attach()` và `detach()`
- Gọi `notifyObservers()` khi có thay đổi
- Tích hợp với ItemService để quản lý inventory

### 3. Concrete Observers
- **HealthObserver**: Theo dõi thay đổi HP, healing, death, revival
- **ItemObserver**: Theo dõi thay đổi equipment, sử dụng item
- **SkillObserver**: Theo dõi học skill, sử dụng skill, thay đổi MP
- **InventoryObserver**: Theo dõi inventory changes với ItemService
- **BossObserver**: Theo dõi boss-specific changes (phases, enrage)

## Boss System

### Boss Class Features
Boss class extend từ Enemy với các tính năng đặc biệt:

#### Phase System
```java
Boss boss = new Boss("Dragon Lord", "Beast", 200, 3);
// Boss có 3 phases, mỗi phase có HP threshold khác nhau
boss.getCurrentPhase(); // Phase hiện tại
boss.getMaxPhases();    // Tổng số phases
boss.isInFinalPhase();  // Kiểm tra phase cuối
```

#### Enrage System
```java
boss.setEnrageThreshold(25); // Enrage khi HP <= 25%
boss.isEnraged();            // Kiểm tra trạng thái enrage
```

#### Special Loot
```java
boss.addSpecialLoot("legendary_sword_1");
boss.getSpecialLoot();       // Lấy special loot
boss.getAllLoot();          // Lấy cả regular và special loot
```

### Boss Types
- **Goblin King**: 2 phases, enrage at 30% HP
- **Dragon Lord**: 3 phases, enrage at 25% HP  
- **Dark Wizard**: 3 phases, enrage at 20% HP
- **Ancient Golem**: 2 phases, enrage at 40% HP

### Boss Observer
```java
BossObserver bossObserver = new BossObserver();
boss.attach(bossObserver);

// Theo dõi các events:
// - PhaseChanged: Khi boss chuyển phase
// - Enraged: Khi boss enrage
// - HPChanged: Khi HP thay đổi
// - Died: Khi boss bị đánh bại
```

## Tích hợp với ItemService

### Inventory Management
```java
// Thêm item vào inventory
player.addItem("sword_1", 1);

// Kiểm tra item
boolean hasItem = player.hasItem("sword_1");
int quantity = player.getItemQuantity("sword_1");

// Lấy thông tin inventory
Map<String, Integer> inventory = player.getInventoryItems();
int size = player.getInventorySize();
int maxSize = player.getMaxInventorySize();
```

### Equipment Management
```java
// Equip item từ inventory
Item sword = player.getItemFromService("sword_1");
player.equipItem(sword);

// Unequip item
player.unequipItem(sword);
```

### Consumable Items
```java
// Sử dụng consumable item
player.useItem(potion);
player.removeItem("potion_1", 1);
```

## CreatureItemManager

Utility class để quản lý tương tác giữa creatures và items:

### Auto-Equip
```java
CreatureItemManager manager = new CreatureItemManager(itemService);
manager.autoEquipBestItems(player);
```

### Loot Management
```java
// Cho loot từ enemy
manager.giveLootFromEnemy(goblin, player);

// Cho loot từ boss (bao gồm special loot)
manager.giveLootFromBoss(dragonLord, player);
```

### Boss-Specific Features
```java
// Display boss information
manager.displayBossInfo(boss);

// Boss loot với bonus rewards
manager.giveLootFromBoss(boss, player);
// - Regular loot
// - Special loot (RARE items)
// - Bonus rewards cho enraged boss
// - Bonus rewards cho final phase
```

### Consumable Effects
```java
// Sử dụng potion với effect tự động
manager.useConsumableItem(player, "potion_1");
```

### Stats Calculation
```java
// Tính tổng stats (base + equipment)
Map<String, Integer> totalStats = manager.getTotalCombatStats(player);

// Stats từ equipment
Map<String, Integer> equipStats = manager.getEquipmentStats(player);
```

### Item Recommendations
```java
// Lấy items được khuyến nghị cho creature
List<Item> recommendations = manager.getRecommendedItems(player);

// Boss-specific recommendations
List<Item> bossRecommendations = manager.getRecommendedItems(boss);
```

## Các loại thay đổi được theo dõi

### Health Changes
- `HPChanged`: HP thay đổi
- `Healed`: Được hồi phục
- `Died`: Chết
- `Revived`: Hồi sinh

### Item Changes
- `EquipmentChanged`: Thay đổi trang bị
- `ItemUsed`: Sử dụng item
- `ItemAdded`: Nhận item mới
- `ItemRemoved`: Mất item

### Skill Changes
- `SkillLearned`: Học skill mới
- `SkillUsed`: Sử dụng skill
- `SkillUpgraded`: Nâng cấp skill
- `MPChanged`: Thay đổi MP

### Boss-Specific Changes
- `PhaseChanged`: Boss chuyển phase
- `Enraged`: Boss enrage
- `BossDefeated`: Boss bị đánh bại

### Other Changes
- `LevelUp`: Lên cấp
- `AchievementUnlocked`: Mở khóa thành tích
- `StateAdded`: Thêm trạng thái
- `StateRemoved`: Xóa trạng thái

## Cách sử dụng

### 1. Khởi tạo với ItemService
```java
// Tạo ItemService và load items
ItemService itemService = new ItemService();
Map<String, Item> items = ItemLoader.loadItems();
items.values().forEach(itemService::registerItem);

// Set ItemService trong CreatureFactory
CreatureFactory.setItemService(itemService);

// Tạo creature với inventory
Player player = CreatureFactory.createWarrior("Hero");
```

### 2. Tạo Boss
```java
// Tạo boss với phases
Boss dragonLord = CreatureFactory.createDragonLord();
Boss goblinKing = CreatureFactory.createGoblinKing();
Boss darkWizard = CreatureFactory.createDarkWizard();

// Boss tự động có BossObserver attached
```

### 3. Boss Combat
```java
// Boss sẽ tự động chuyển phase khi HP giảm
boss.takeDamage(100);

// Boss sẽ enrage khi HP <= threshold
boss.takeDamage(50);

// Boss có skills đặc biệt cho từng phase
boss.useSkill(phaseSkill, target);
```

### 4. Quản lý Inventory
```java
// Thêm items
player.addItem("sword_1", 1);
player.addItem("potion_1", 3);

// Kiểm tra inventory
System.out.println("Has sword: " + player.hasItem("sword_1"));
System.out.println("Inventory size: " + player.getInventorySize());

// Equip items
Item sword = player.getItemFromService("sword_1");
player.equipItem(sword);
```

### 5. Sử dụng CreatureItemManager
```java
CreatureItemManager manager = new CreatureItemManager(itemService);

// Auto-equip best items
manager.autoEquipBestItems(player);

// Use consumable with effects
manager.useConsumableItem(player, "potion_1");

// Get loot from boss
manager.giveLootFromBoss(dragonLord, player);

// Display detailed inventory
manager.displayDetailedInventory(player);

// Display boss information
manager.displayBossInfo(boss);
```

## Ví dụ Demo

### Basic Observer Demo
```bash
java creature.demo.CreatureObserverDemo
```

### Item Integration Demo
```bash
java creature.demo.CreatureItemIntegrationDemo
```

### Boss Demo
```bash
java creature.demo.BossDemo
```

Demo sẽ test:
- Boss creation và phases
- Phase transitions và enrage
- Boss combat simulation
- Special loot distribution
- Boss observer functionality
- Boss-specific rewards

## Lợi ích của Observer Pattern + ItemService + Boss System

1. **Loose Coupling**: Creature không cần biết chi tiết về observers
2. **Extensibility**: Dễ dàng thêm observers mới và boss types
3. **Real-time Monitoring**: Theo dõi inventory changes và boss states
4. **Persistent Storage**: Items được lưu trữ qua ItemService
5. **Flexible Management**: CreatureItemManager cung cấp utilities
6. **Type Safety**: Strong typing cho items và effects
7. **Boss Complexity**: Phase system và enrage mechanics
8. **Special Rewards**: Unique loot và bonus rewards
9. **Dynamic Combat**: Boss behavior thay đổi theo phase

## Mở rộng

Có thể thêm các observers mới như:
- **CombatObserver**: Theo dõi combat events
- **QuestObserver**: Theo dõi tiến độ nhiệm vụ
- **TradeObserver**: Theo dõi giao dịch items
- **CraftingObserver**: Theo dõi crafting activities
- **BossPhaseObserver**: Theo dõi chi tiết phase transitions

Có thể thêm các boss types mới:
- **Raid Boss**: Boss cho nhiều players
- **World Boss**: Boss xuất hiện định kỳ
- **Dungeon Boss**: Boss trong dungeon
- **Event Boss**: Boss cho special events

## Design Patterns được sử dụng

1. **Observer Pattern**: Theo dõi thay đổi creatures và bosses
2. **Factory Pattern**: `CreatureFactory` tạo creatures và bosses với observers
3. **Strategy Pattern**: `ISkill` interface cho các loại skill khác nhau
4. **State Pattern**: `ICreatureState` cho status effects và boss phases
5. **Template Method**: `ACreature` abstract class
6. **Service Pattern**: `ItemService` quản lý items
7. **Manager Pattern**: `CreatureItemManager` quản lý tương tác
8. **Inheritance**: `Boss` extend `Enemy` với additional features 