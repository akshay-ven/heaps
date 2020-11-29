package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    static final int DEFAULT_LENGTH = 8;

    int size;
    Map<T, Integer> itemsIndex;
    List<PriorityNode<T>> items;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>(DEFAULT_LENGTH);
        size = items.size();
        itemsIndex = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        if (size > 0) {
            PriorityNode<T> temp = items.get(a);
            items.set(a, items.get(b));
            items.set(b, temp);
            itemsIndex.put(temp.getItem(), b);
            itemsIndex.put(items.get(a).getItem(), a);
        }
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item already exists.");
        }
        items.add(new PriorityNode<T>(item, priority));
        itemsIndex.put(item, size);
        size++;
        percolateUp(size - 1);
    }

    @Override
    public boolean contains(T item) {
        return (indexOf(item) != -1);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override // Wrong invariant, index out of bounds, wrong values
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        swap(START_INDEX, size - 1);
        T val = items.remove(size - 1).getItem();
        itemsIndex.remove(val);
        size--;
        percolateDown(START_INDEX);
        return val;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = indexOf(item);


        if (index >= 0 && index < size) {
            items.get(index).setPriority(priority);
            // if (items.get(index).getPriority()
            //     < (items.get((index - 1) / 2)).getPriority()) {
            percolateDown(index);
            percolateUp(index);
            // call both so that everything is in place
            // } else {

            // }
        }
        // else {
        //     percolateDown(index);
        // }
    }

    @Override
    public int size() {
        return size;
    }


    private int indexOf(T item) {
        if (!itemsIndex.isEmpty() && itemsIndex.containsKey(item)) { // my guess has to do that we're calling .get(null)
            return itemsIndex.get(item);
        }
        return -1;
    }

    private void percolateUp(int currIndex) {
        int percolatedIndex = (currIndex - 1) / 2;
        if (percolatedIndex >= 0) {
            if (items.get(currIndex).getPriority()
                < items.get(percolatedIndex).getPriority()) {
                swap(percolatedIndex, currIndex);
                percolateUp(percolatedIndex);
            }
        }
    }

    private void percolateDown(int currIndex) {
        int percolatedIndex = (2 * currIndex) + 1;
        if (percolatedIndex <= size - 1) {
            double parent = items.get(currIndex).getPriority();
            double left = items.get(percolatedIndex).getPriority();
            if (percolatedIndex + 1 < size) {
                double right = items.get(percolatedIndex + 1).getPriority();
                if (left < right && left < parent) {
                    swap(currIndex, percolatedIndex);
                    percolateDown(percolatedIndex);
                } else if (right < left && right < parent) {
                    swap(currIndex, percolatedIndex + 1);
                    percolateDown(percolatedIndex + 1);
                }
            } else {
                if (left < parent) {
                    swap(currIndex, percolatedIndex);
                    percolateDown(percolatedIndex);
                }
            }
        }
    }

}

