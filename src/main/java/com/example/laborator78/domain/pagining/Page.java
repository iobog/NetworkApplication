package com.example.laborator78.domain.pagining;

import java.util.ArrayList;
import java.util.List;

public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalNumberOfElements;

    public Page(Iterable<E> elementsOnPage, int totalNumberOfElements) {
        this.elementsOnPage = elementsOnPage;
        this.totalNumberOfElements = totalNumberOfElements;
    }

    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }

    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    public List<E> getContent() {
        List<E> contentList = new ArrayList<>();
        elementsOnPage.forEach(contentList::add); // Adăugăm toate elementele în listă
        return contentList;
    }

}