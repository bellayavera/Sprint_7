import io.qameta.allure.Step;

import java.util.List;

public class GetOrders {
    private List<GetOrder> orders;

    @Step("Get list of orders")
    public List<GetOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<GetOrder> orders) {
        this.orders = orders;
    }
}
