package org.iii.converter.datamodel;

public class TwoTuples<L, R> {
    public final L left;
    public final R right;

    public TwoTuples(L _left, R _right) {
        this.left = _left;
        this.right = _right;
    }
}
