package shootingspaceship;

import javax.swing.ImageIcon;
import java.util.ArrayList;

/*
 * 모든 아이템 ArrayList로 보관
 * 아이템 찾기, 꺼내오기 등 인벤토리 역할*/

public class ItemStorage {
    private ArrayList<Item> storage; // 아이템 리스트
    private Item ptr;
    
    public ItemStorage(){
        storage = new ArrayList<Item>();
        
        Item item_defaultMissle = new Item("item_defaultMissle");
        Item item_doubleMissle = new Item("item_doubleMissle");
        
        item_defaultMissle.setDescribe("아이템 설명: 기본 미사일. 아무 능력 없음.");
        item_doubleMissle.setDescribe("아이템 설명: 미사일이 두 개씩 발사된다.");
        
        item_defaultMissle.setImage(new ImageIcon(getClass().getResource("/img/item_defaultMissle.png")));
        item_doubleMissle.setImage(new ImageIcon(getClass().getResource("/img/item_doubleMissle.png")));
        
        storage.add(item_defaultMissle);
        storage.add(item_doubleMissle);
    }
    // 이름(name)으로 아이템 찾아서 포인터(ptr)에 저장
    public boolean itemSearch(String name) {
        for (Item i : storage) {
            if (i.getName().equals(name)) {
                ptr = i; // 찾으면 ptr에 기억
                return true;
            }
        }
        return false; // 못 찾으면 false
    }

    // 이름으로 아이템 꺼내오기 (Item 객체 반환)
    public Item getItem(String name) {
        if (itemSearch(name)) {
            return ptr;
        }
        return null;
    }
    
    public ArrayList<Item> getStorage() { return storage; }
}
