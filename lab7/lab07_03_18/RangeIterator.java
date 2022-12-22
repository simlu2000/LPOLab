package lab07_03_18;

import java.util.Iterator;
import java.util.NoSuchElementException;

class RangeIterator implements Iterator<Integer> {

    // aggiungere i campi e i costruttori necessari

    private int next;
    private final int end;

    RangeIterator(int next,int end){
        this.next = next;
        this.end = end;
    }

    @Override
    public boolean hasNext() {
        return next < end;
    }

    @Override
    public Integer next() {
        if(!hasNext())
            throw new NoSuchElementException();
        return next++;

    }

}
