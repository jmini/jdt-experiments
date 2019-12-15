package fr.jmini.some.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LibraryTest {

    @Test
    void testSomeLibraryMethod() {
        Library classUnderTest = new Library();
        Assertions.assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'");
    }
}
