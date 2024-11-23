package com.example.laborator78.utils.observer;

import com.example.laborator78.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);

    void notifyObservers(String eventType, Object data);
}
