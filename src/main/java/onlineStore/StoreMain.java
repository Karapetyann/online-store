package onlineStore;

import onlineStore.model.*;
import onlineStore.storeStorage.OrderStorage;
import onlineStore.storeStorage.ProductStorage;
import onlineStore.storeStorage.UserStorage;
import onlineStore.storeUtil.Command;
import onlineStore.storeUtil.DateUtil;
import onlineStore.storeUtil.StorageSerializable;
import onlineStore.storeUtil.StoreIdGenerate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class StoreMain implements Command {
    private final static Scanner scanner = new Scanner(System.in);
    private final static UserStorage userStorage = StorageSerializable.deSerializeUserStorage();
    private final static ProductStorage productStorage = StorageSerializable.deSerializeProductStorage();
    private final static OrderStorage orderStorage = StorageSerializable.deSerializeOrderStorage();

    static {
        userStorage.userPut("admin@mail.ru", new User("12345", "Hakob", "admin@mail.ru", "12345", UserType.ADMIN));
        StorageSerializable.serializeUserStorage(userStorage);
    }

    private static User qurrentUser = null;

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Command.loginCommand();
            String command = scanner.nextLine();
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case LOGIN:
                    login();
                    break;
                case REGISTER:
                    register();
                    break;
                default:
                    System.out.println("Wrong Command!! Try Again");
            }
        }
    }

    private static void register() {
        String userId = StoreIdGenerate.idGenerate();
        System.out.println("please input your name");
        String name = scanner.nextLine();
        System.out.println("please input your register email");
        String email = scanner.nextLine();
        System.out.println("please input your password");
        String password = scanner.nextLine();
        if (!userStorage.getByEmail(email)) {
            User user1 = new User(userId, name, email, password, UserType.USER);
            userStorage.userPut(email, user1);
            System.out.println("successfully registered");
            StorageSerializable.serializeUserStorage(userStorage);
        } else {
            System.out.println("this account already exists");
        }
    }

    private static void login() {
        System.out.println("please input your email");
        String email = scanner.nextLine();
        System.out.println("please input your password");
        String password = scanner.nextLine();
        User byUser = userStorage.getByUser(email, password);
        if (byUser == null) {
            System.out.println("Incorrect login or password");
            return;
        }
        qurrentUser = byUser;
        if (byUser.getUserType() == UserType.ADMIN) {
            Command.adminCommand();
            adminInterface();
        } else {
            Command.userCommand();
            userInterface();
        }
    }

    private static void userInterface() {
        boolean isRun = true;
        while (isRun) {
            String command = scanner.nextLine();
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    break;
                case PRINT_ALL_PRODUCTS:
                    printProducts();
                    break;
                case BUY_PRODUCT:
                    buyProduct();
                    break;
                case PRINT_MY_ORDERS:
                    printMyOrders();
                    break;
                case CANCEL_ORDER_BY_ID:
                    cancelOrderById();
                    break;
                default:
                    System.out.println("Wrong Command!! Try Again");
            }
        }
    }

    private static void cancelOrderById() {
        System.out.println("please input order id");
        String orderId = scanner.nextLine();
        orderStorage.removedOrder(orderId);
        StorageSerializable.serializeOrderStorage(orderStorage);
    }

    private static void printMyOrders() {
        User user = qurrentUser;
        orderStorage.printMyOrders(user);
    }

    private static void buyProduct() {
        printProducts();
        System.out.println("please input product id");
        String productId = scanner.nextLine();
        Product product = productStorage.getById(productId);
        if (product == null) {
            System.out.println("Wrong id!! try again");
            return;
        }
        System.out.println(product);
        System.out.println("please input specify product quantity");
        int qty = 0;
        try {
            qty = Integer.parseInt(scanner.nextLine());
            if (product.getStockQty() < qty || product.getStockQty() <= 0) {
                System.out.println("not enough products in storage");
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong arguments");
        }
        System.out.println("Do you really want to buy this product in" + qty + "quantity with price" + (qty * product.getPrice()) + " ?");
        System.out.println("------------");
        System.out.println("please input YES or NO");
        String yesOrNo = scanner.nextLine();
        if (yesOrNo.equals("yes".toLowerCase())) {
            String orderId = StoreIdGenerate.idGenerate();
            User user = qurrentUser;
            String date1 = DateUtil.dateFormat;
            Date date = DateUtil.stringToDate(date1);
            System.out.println("please input payment method - CARD, CASH, PAYPAL");
            PaymentMethod paymentMethod = PaymentMethod.valueOf(scanner.nextLine());
            Order order = new Order(orderId, user, product, date, product.getPrice(), OrderStatus.NEW, qty, paymentMethod);
            orderStorage.addOrder(order);
            System.out.println("stock doesn't exists");
            StorageSerializable.serializeOrderStorage(orderStorage);
        }
    }

    private static void adminInterface() {
        boolean isRun = true;

        while (isRun) {
            Command.adminCommand();
            String command = scanner.nextLine();
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    break;
                case ADD_PRODUCT:
                    addProduct();
                    break;
                case REMOVE_PRODUCT_BY_ID:
                    removeProductById();
                    break;
                case PRINT_PRODUCTS:
                    printProducts();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case PRINT_ORDERS:
                    printOrders();
                    break;
                case CHANGE_ORDER_STATUS:
                    changeOrderStatus();
                    break;
                case EXPORT_ORDERS_TO_EXEL:
                    exportOrderToExel();
                    break;
                default:
                    System.out.println("Wrong Command!! Try Again");
            }
        }
    }

    private static void exportOrderToExel() {
        System.out.println("please input directory path");
        String directoryPath = scanner.nextLine();
        File file = new File(directoryPath);
        if (file.exists() && file.isDirectory()) {
            List<Order> orders = orderStorage.getOrderList();
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet ordersSheet = workbook.createSheet("orders");
                Row headeRow = ordersSheet.createRow(0);
                Cell headIdCell = headeRow.createCell(0);
                headIdCell.setCellValue("Order id");

                Cell headUserNameSell = headeRow.createCell(1);
                headUserNameSell.setCellValue("User Name");

                Cell headProductName = headeRow.createCell(2);
                headProductName.setCellValue("Product Name");

                Cell headOrderQty = headeRow.createCell(3);
                headOrderQty.setCellValue("Qty");

                Cell headOrderPrice = headeRow.createCell(4);
                headOrderPrice.setCellValue("Price");

                int rowIndex = 1;
                for (Order order : orders) {
                    Row row = ordersSheet.createRow(rowIndex++);
                    Cell idSell = row.createCell(0);
                    idSell.setCellValue(order.getId());

                    Cell userNameCell = row.createCell(1);
                    userNameCell.setCellValue(order.getUser().getName());

                    Cell productNameCell = row.createCell(2);
                    productNameCell.setCellValue(order.getProduct().getName());

                    Cell productQty = row.createCell(3);
                    productQty.setCellValue(order.getQty());

                    Cell orderPrice = row.createCell(4);
                    orderPrice.setCellValue(order.getPrice());
                }

                workbook.write(new FileOutputStream(new File(directoryPath, "report_"+System.currentTimeMillis()+".xlsx")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong directory path");
        }
    }

    private static void changeOrderStatus() {
        orderStorage.printOrders();
        System.out.println("please input order id");
        String orderId = scanner.nextLine();
        Order order = orderStorage.orderById(orderId);
        if (order == null) {
            System.out.println("wrong order id!! try again");
        } else {
            order.setOrderStatus(OrderStatus.DELIVERED);
            Product product = orderStorage.orderByProduct(order);
            if (product != null) {
                productStorage.deleteStockQty(product, order.getQty());
                StorageSerializable.serializeOrderStorage(orderStorage);
            }
        }
    }

    private static void printOrders() {
        orderStorage.printOrders();
    }

    private static void printUsers() {
        userStorage.printUsers();
    }

    private static void printProducts() {
        productStorage.printProducts();
    }

    private static void removeProductById() {
        System.out.println("please input product by id");
        String productId = scanner.nextLine();
        if (productStorage.removeProductById(productId)) {
            System.out.println("create removed");
        }
        StorageSerializable.serializeProductStorage(productStorage);
    }

    private static void addProduct() {
        String productId = StoreIdGenerate.idGenerate();
        Product product = productStorage.getById(productId);
        if (product != null) {
            System.out.println("this product already exists");
        }
        System.out.println("please input product name");
        String name = scanner.nextLine();
        System.out.println("please input product description");
        String description = scanner.nextLine();
        System.out.println("please input product price");
        double price = 0;
        int stockQty = 0;
        try {
            price = Double.parseDouble(scanner.nextLine());
            System.out.println("please input product  stockQty");
            stockQty = Integer.parseInt(scanner.nextLine());

        } catch (NumberFormatException e) {
            System.out.println("Wrong argument try again");
            return;
        }
        if (stockQty <= 0 || price <= 0) {
            return;
        }
        System.out.println("please input product Type -  ELECTRONICS, CLOTHING, BOOKS");
        try {
            ProductType productType = ProductType.valueOf(scanner.nextLine());
            Product product1 = new Product(productId, name, description, price, stockQty, productType);
            productStorage.addProduct(product1);
            StorageSerializable.serializeProductStorage(productStorage);
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong type!! try again");
        }
    }
}
