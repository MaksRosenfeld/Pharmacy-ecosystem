package ru.budgetapteka.pharmacyecosystem.rest.newmock;

import lombok.Data;
import org.junit.jupiter.api.Test;
import ru.budgetapteka.pharmacyecosystem.rest.mock.AnotherMockClass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class MockClass {

    private Set<Integer> classIntegers;



    @Test
    public void letsCheck() {
        Set<Integer> integers = new HashSet<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        System.out.println(integers);
        Set<Integer> copyIntegers = integers;
        integers.remove(2);
        System.out.println(copyIntegers);






    }
}
