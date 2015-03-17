package com.hybris.service;


public interface ClosureService {

    void execute(Closure closure) throws Exception;

    <T> T execute(ValueReturningClosure<T> closure) throws Exception;

    void waitForCondition(final ConditionChecker checker);
}
