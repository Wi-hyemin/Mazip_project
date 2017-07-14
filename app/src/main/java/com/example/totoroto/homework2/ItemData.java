package com.example.totoroto.homework2;


public class ItemData {
    int storeImage; //가게 이미지
    String storeName; //가게 이름
    String storeDetail; //가게 설명
    int numDistance; //거리
    int numPopular; //인기도
    int numRecent; //최근 순
    boolean isCheck; //버튼 체크 여부

    public ItemData() {}
    public ItemData(int storeImage, String storeName, String storeDetail,
                    int numDistance, int numPopular, int numRecent, boolean isCheck) {
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeDetail = storeDetail;

        this.numDistance = numDistance;
        this.numPopular = numPopular;
        this.numRecent = numRecent;
        this.isCheck = isCheck;
    }
    public boolean getCheck(){
        return isCheck;
    }
}
