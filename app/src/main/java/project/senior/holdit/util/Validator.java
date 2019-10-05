package project.senior.holdit.util;

import project.senior.holdit.enumuration.OrderStatusEnum;
import project.senior.holdit.model.Order;

public class Validator {

    public static boolean isOrderSuccessOrCancelOrApprovedIssue(Order order){
        return order.getStatus().equals(OrderStatusEnum.SUCCESS) || order.getStatus().equals(OrderStatusEnum.CANCEL)
                || order.getStatus().equals(OrderStatusEnum.APPROVED_ISSUE);
    }
}