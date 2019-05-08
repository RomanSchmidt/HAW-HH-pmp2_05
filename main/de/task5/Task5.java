package de.task5;

/**
 * @author Stanislaw Brug, Roman Schmidt
 * source: http://wouter.coekaerts.be/2015/puzzle-tweet-solution
 * <p>
 * Class A which has a Long which is an instance of it self, which has a static class B.
 * While instantiating the not static B it will create an instance of the static B and use Long from A which overrides
 * compare which does not deliver 0, so no division by zero.
 */
class A {

    A Long = this;

    Long compare(Long a, Long b) {
        return 42L;
    }

    static class B extends A {
        B(Long whoCares) {
        }
    }
}

class B extends A {
    B(Long i) {
        new B(i / Long.compare(i, i));
        System.out.println("Win");
    }
}

public class Task5 {
    public static void main(String[] args) {
        new B(21L);
    }
}
