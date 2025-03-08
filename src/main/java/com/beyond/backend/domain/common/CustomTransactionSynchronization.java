package com.beyond.backend.domain.common;

import org.springframework.transaction.support.TransactionSynchronization;

public abstract class CustomTransactionSynchronization implements TransactionSynchronization {

    @Override
    public void suspend() {}

    @Override
    public void resume() {}

    @Override
    public void flush() {}

    @Override
    public void beforeCommit(boolean readOnly) {}

    @Override
    public void beforeCompletion() {}

    @Override
    public void afterCompletion(int status) {}
}
