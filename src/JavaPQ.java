import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class JavaPQ<E extends Comparable<? super E>> 
	implements BinaryMinHeapI<E>{
	PriorityQueue<E> pq;

	public JavaPQ() {
		this.pq = new PriorityQueue<E>();
	}

	public boolean isEmpty() {
		return pq.isEmpty();
	}

	public E removeMin() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return pq.remove();
	}

	public void insert(E element) {
		if (element == null) {
			throw new NullPointerException();
		}
		pq.add(element);
	}

	public void updateKey(E oldElem, E newElem) {
		if (oldElem == null || newElem == null) {
			throw new NullPointerException();
		}
		if (!pq.remove(oldElem)) {
			throw new NoSuchElementException();
		}
		pq.add(newElem);

	}

}
