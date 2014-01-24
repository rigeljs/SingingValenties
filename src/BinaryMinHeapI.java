/**
 * Defines an interface for a binary min heap
 */

public interface BinaryMinHeapI<K extends Comparable<? super K>>{

    public boolean isEmpty();

    /**
     * Returns the least element in the heap
     * @throws NoSuchElementException
     *         - if the heap is empty
     */
    public K removeMin();

    /**
     * Adds the element to the heap
     * @throws NullPointerException
     *         - if element is null
     */
    public void insert(K element);

    /**
     * This function should update the value of the key
     * oldElem to the value of newElem
     *
     * @param oldElem
     *          - current version of the element in the heap
     * @param newElem
     *          - new version of the element to be added to the heap
     * @throws NoSuchElementException
     *         - if oldElem is not in the heap
     * @throws NullPointerException
     *         - if oldElem or newElem are null
     */
    public void updateKey(K oldElem, K newElem);
}
