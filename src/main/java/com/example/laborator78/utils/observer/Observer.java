package com.example.laborator78.utils.observer;

import com.example.laborator78.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);

    void update(String eventType, Object data);
}