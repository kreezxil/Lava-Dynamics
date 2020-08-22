package com.eleksploded.lavadynamics.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

public class IndexedMap<T,V> {
	private List<T> t = new ArrayList<T>();
	private List<V> v = new ArrayList<V>();
	
	public void add(T key, V item) {
		if(!t.contains(key)) {
			t.add(key);
			v.add(item);
		} else {
			throw new RuntimeException("Cannot add duplicate items");
		}
	}
	
	public void add(int index, T key, V item) {
		if(!t.contains(key)) {
			t.add(index, key);
			v.add(index, item);
		} else {
			throw new RuntimeException("Cannot add duplicate items");
		}
	}
	
	public V get(T key) {
		return v.get(t.indexOf(key));
	}
	
	public Set<Pair<T,V>> pairSet() {
		Set<Pair<T,V>> set = new HashSet<Pair<T,V>>();
		for(int i = 0; i < t.size(); i++) {
			set.add(Pair.of(t.get(i), v.get(i)));
		}
		return set;
	}
	
	public void remove(int index) {
		t.remove(index);
		v.remove(index);
	}

	public boolean containsKey(T key) {
		return t.contains(key);
	}
	
	public int size() {
		return t.size();
	}
}
