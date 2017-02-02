/*
 * Copyright 2017 web4enterprise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.web4enterprise.pdf.layout.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Defines a "meta list" that can contain many list of the same type of element.
 * 
 * 
 * @author Régis Ramillien
 * @param <E> the type of elements in this list
 */
public class CompositeList<E> implements List<E> {
	/**
	 * The list contained in this one.
	 */
	protected List<List<E>> lists = new ArrayList<>();
	
	/**
	 * Add a list this meta one.
	 * 
	 * @param list The list to add.
	 */
	public void addList(List<E> list) {
		lists.add(list);
	}
	
	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index index of the element to return.
	 * @return the element at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public List<E> getList(int index) {
		return lists.get(index);
	}
	
	/**
	 * Returns all the elements in this meta-list.
	 * 
	 * @return All elements.
	 */
	public List<List<E>> getLists() {
		return lists;
	}

	@Override
	public int size() {
		int size = 0;
		for(List<E> list : lists) {
			size = list.size();
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		for(List<E> list : lists) {
			if(!list.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean contains(Object o) {
		for(List<E> list : lists) {
			if(list.contains(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CompositeListIterator iterator() {
		return new CompositeListIterator();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("Implement when needed.");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("Implement when needed.");
	}

	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("Implement when needed.");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public E get(int index) {
		int currentListIndex = 0;
		int currentSize = 0;
		while(index > currentSize - 1) {
			if(currentListIndex == lists.size()) {
				throw new IndexOutOfBoundsException();
			}
			index -= currentSize;
			currentSize = lists.get(currentListIndex).size();
			currentListIndex++;
		}

		return lists.get(currentListIndex - 1).get(index);
	}

	@Override
	public E  set(int index, E element) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("This is a logical list and does not supports writing operations");
	}

	@Override
	public int indexOf(Object o) {
		for(List<E> list : lists) {
			int index = list.indexOf(o);
			if(index != 0) {
				return index;
			}
		}
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		for(List<E> list : lists) {
			int index = list.lastIndexOf(o);
			if(index != 0) {
				return index;
			}
		}
		return 0;
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException("Implement when needed.");
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException("Implement when needed.");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Implement when needed.");
	}
	
	/**
	 * Iterator to iterate over all elements of all inner lists.
	 * 
	 * 
	 * @author Régis Ramillien
	 */
	public class CompositeListIterator implements Iterator<E> {
		/**
		 * Index of previous list.
		 */
		protected int previousListIndex = 0;
		/**
		 * Index of current list.
		 */
		protected int currentListIndex = 0;
		/**
		 * Iterator on current list.
		 */
		protected Iterator<E> currentListIterator = CompositeList.this.getList(0).iterator();
		
		/**
		 * Get the current list.
		 * 
		 * @return The current list.
		 */
		public List<E> getCurrentList() {
			return CompositeList.this.getList(currentListIndex);
		}
		
		/**
		 * Get the index of current list.
		 * 
		 * @return The index of current list.
		 */
		public int getCurrentListIndex() {
			return currentListIndex;
		}
		
		/**
		 * Check if list has changed.
		 * 
		 * @return True if list has changed.
		 */
		public boolean hasListChanged() {
			if(previousListIndex != currentListIndex) {
				previousListIndex = currentListIndex;
				return true;
			}
			return false;
		}

		@Override
		public boolean hasNext() {
			if(currentListIterator.hasNext()) {
				return true;
			} else {
				while(currentListIndex < CompositeList.this.getLists().size() - 1) {
					currentListIndex++;
					currentListIterator = CompositeList.this.getList(currentListIndex).iterator();
					if(currentListIterator.hasNext()) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public E next() {
			return currentListIterator.next();
		}
		
	}
}
