package com.hybris.service;

public abstract class AbstractConditionChecker implements ConditionChecker {

    private static final int TIMEOUT_IN_SECONDS = 120;
    private final String name;

    public AbstractConditionChecker(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getTimeoutInSeconds() {
        return TIMEOUT_IN_SECONDS;
    }

    @Override
    public boolean isConditionFailed() {
        return !isConditionPassed();
    }

    @Override
    public String getContext() {
        return name;
    }
}
