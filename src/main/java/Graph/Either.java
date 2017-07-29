package Graph;

import lombok.Getter;

public class Either<A, B> {
    private Either() { }

    public A left() { return null; }
    public B right() { return null; }

    public final static class Left<A, B> extends Either<A, B> {
        private A a;
        public Left(A a) { this.a = a; }
        public A left() { return a; }
    }

    public final static class Right<A, B> extends Either<A, B> {
        private B b;
        public Right(B b) { this.b = b; }
        public B right() { return b; }
    }

    public <C> C either(MapE<A, C> f, MapE<B, C> g) {
        if(this instanceof Left)
            return f.apply(left());
        return g.apply(right());
    }
}

interface MapE<A, B> {
    B apply(A a);
}

