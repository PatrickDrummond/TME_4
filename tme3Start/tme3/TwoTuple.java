package tme3;

// Collection of TwoTuple to store state variables
public class TwoTuple<A, B> {
    public A first;
    public B second;
    public TwoTuple(A a, B b) { first = a; second = b; }
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
