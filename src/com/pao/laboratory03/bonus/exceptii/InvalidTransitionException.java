package com.pao.laboratory03.bonus.exceptii;
import com.pao.laboratory03.bonus.model.Status;

public class InvalidTransitionException extends RuntimeException {
    private Status fromStatus, toStatus;
    public InvalidTransitionException(Status from, Status to) {
        super("Invalid transition from " + from + " to " + to);
        this.fromStatus = from;
        this.toStatus = to;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    
}
