package shootingspaceship.model;

import javax.swing.ImageIcon;

/*
 * 아이템 1개의 정보를 담고 있음
 * 이름, 설명, 이미지 세 정보 보관
 * 아이템 선택, 인벤토리, 구매 등에 활용 예쩡*/

public class Item {
    private String name;       // 아이템 이름
    private String describe;   // 아이템 설명
    private ImageIcon image;   // 아이템 썸네일

    public Item(String name){
        this.name = name;
    }
    
    public String getName() { return name; }
    public String getDescribe() { return describe; }
    public ImageIcon getImage() { return image; }
    
    public void setName(String name) { this.name = name; }
    public void setDescribe(String describe) { this.describe = describe; }
    public void setImage(ImageIcon image) { this.image = image; }
}
