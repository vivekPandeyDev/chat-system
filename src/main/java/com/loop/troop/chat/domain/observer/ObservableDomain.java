package com.loop.troop.chat.domain.observer;

import com.loop.troop.chat.domain.event.DomainEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for aggregates that can record domain events during their lifecycle and
 * release them after persistence.
 */
public abstract class ObservableDomain<E extends DomainEvent> {

	private final List<E> domainEvents = new ArrayList<>();

	private final List<DomainObserver<E>> observers = new ArrayList<>();

	protected void recordEvent(E event) {
		domainEvents.add(event);
	}

	public void addObserver(DomainObserver<E> observer) {
		observers.add(observer);
	}

	public void removeObserver(DomainObserver<E> observer) {
		observers.remove(observer);
	}

	public void notifyObservers(E event) {
		observers.forEach(observer -> observer.onEvent(event));
	}

	public List<E> releaseEvents() {
		var copy = List.copyOf(domainEvents);
		domainEvents.clear();
		copy.forEach(this::notifyObservers);
		return copy;
	}

	public boolean hasPendingEvents() {
		return !domainEvents.isEmpty();
	}

}