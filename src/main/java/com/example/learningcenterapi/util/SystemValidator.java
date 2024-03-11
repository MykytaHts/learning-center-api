package com.example.learningcenterapi.util;

import com.example.learningcenterapi.exception.SystemException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class SystemValidator {
    private static final String NULL_MESSAGE = "%s cannot be null.";
    private static final String NULL_MESSAGE_TWO_ARGS = "%s and %s cannot be null.";
    private static final String EMPTY_MESSAGE = "%s cannot be empty.";

    public static void checkNull(Object object, String objectName) {
        if (object == null) {
            throw new SystemException(NULL_MESSAGE.formatted(objectName), BAD_REQUEST);
        }
    }

    public static void checkNull(Object object1, Object object2, String object1Name, String object2Name) {
        if (object1 == null || object2 == null) {
            throw new SystemException(NULL_MESSAGE_TWO_ARGS.formatted(object1Name, object2Name), BAD_REQUEST);
        }
    }

    public static void checkEmpty(Collection<?> object, String objectName) {
        if (CollectionUtils.isEmpty(object)) {
            throw new SystemException(EMPTY_MESSAGE.formatted(objectName), BAD_REQUEST);
        }
    }
}
