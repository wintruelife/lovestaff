package love.wintrue.com.lovestaff.observer;

/**
 * Created by dell on 2016/7/20.
 * 被观察者行为常量
 */
public interface ObsAction {

    /**
     * 更新购物车中总价格和总优惠等信息
     */
    String ACTION_UPDATE_SHOPPING_CAR_TOTAL = "update_shopping_car_total";

    /**
     * 更新商品编辑界面的工厂信息
     */
    String ACTION_UPDATE_PRODUCT_EDIT_FACTORY_INFO = "update_product_edit_factory_info";

}
