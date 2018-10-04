package es.source.code.Interface;

import es.source.code.model.Food;

/**
 * @author taoye
 * @date 2018/10/4
 * @classname Interface_AddOrderItem
 * @description 接口。实现在Fragment中点击“点菜”按钮，将菜单项传给Activity中的List<OrderItem>中
 */
public interface Interface_AddOrderItem {
    void addOrderItem(Food food);
}
