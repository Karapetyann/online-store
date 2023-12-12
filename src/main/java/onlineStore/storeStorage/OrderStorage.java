package onlineStore.storeStorage;
import onlineStore.model.Order;
import onlineStore.model.OrderStatus;
import onlineStore.model.Product;
import onlineStore.model.User;

import java.io.Serializable;;
import java.util.LinkedList;
import java.util.List;

public class OrderStorage implements Serializable {
    private List<Order> orderList = new LinkedList<>();


    public void printOrders() {
        for (Order order : orderList) {
            System.out.println(order);
        }
    }

    public void printMyOrders(User user) {
        for (Order order : orderList) {
            if (order.getUser().equals(user)) {
                System.out.println(order);
            }
        }
    }


    public void removedOrder(String orderId) {
        for (Order order : orderList) {
            if (order.getId().equals(orderId)) {
                orderList.remove(order);
            }
        }
    }

    public Order orderById(String orderId) {
        for (Order order : orderList) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    public void changeOrderStatus(Order order) {
        order.setOrderStatus(OrderStatus.DELIVERED);
    }

    public Product orderByProduct(Order order) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).equals(order)) {
                return orderList.get(i).getProduct();
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public List<Order> getOrderList() {
        return orderList;
    }
}
