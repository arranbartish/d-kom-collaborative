package com.hybris.service;


public interface ConditionChecker {

    String getName();

    int getTimeoutInSeconds();

    boolean isConditionPassed();

    String getErrorMessage();

    boolean isConditionFailed();

    String getContext();
}
