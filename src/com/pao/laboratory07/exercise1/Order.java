package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private OrderState currentState;
    private Stack<OrderState> history;

    public Order(OrderState initialState) {
        this.currentState = initialState;
        this.history = new Stack<>();
    }

    public OrderState getCurrentState() {
        return currentState;
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (isFinalState(currentState)) {
            throw new OrderIsAlreadyFinalException();
        }

        history.push(currentState);

        switch (currentState) {
            case PLACED -> currentState = OrderState.PROCESSED;
            case PROCESSED -> currentState = OrderState.SHIPPED;
            case SHIPPED -> currentState = OrderState.DELIVERED;
            default -> throw new OrderIsAlreadyFinalException();
        }
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (isFinalState(currentState)) {
            throw new CannotCancelFinalOrderException();
        }

        history.push(currentState);
        currentState = OrderState.CANCELLED;
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }

        currentState = history.pop();
    }

    private boolean isFinalState(OrderState state) {
        return state == OrderState.DELIVERED || state == OrderState.CANCELLED;
    }
}