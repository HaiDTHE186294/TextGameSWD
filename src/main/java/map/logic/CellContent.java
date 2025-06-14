package map.logic;

public interface CellContent {
    void onEnter(); // Khi player bước vào
    String render(); // Trả về ký hiệu/mô tả để hiển thị
}