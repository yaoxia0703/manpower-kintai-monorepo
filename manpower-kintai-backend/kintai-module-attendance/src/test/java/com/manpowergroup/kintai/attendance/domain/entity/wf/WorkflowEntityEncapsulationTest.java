package com.manpowergroup.kintai.attendance.domain.entity.wf;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkflowEntityEncapsulationTest {

    @Test
    void workflowEntitiesDoNotExposeBusinessFieldSetters() {
        for (Class<?> type : List.of(
                WfApproval.class,
                WfApprovalStep.class,
                WfApprovalRule.class)) {
            List<String> publicBusinessSetters = Arrays.stream(type.getDeclaredMethods())
                    .filter(method -> Modifier.isPublic(method.getModifiers()))
                    .map(Method::getName)
                    .filter(name -> name.startsWith("set"))
                    .filter(name -> !name.equals("setId"))
                    .sorted()
                    .toList();

            assertEquals(List.of(), publicBusinessSetters, type.getSimpleName());
        }
    }
}
