/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.csvexporter.core.util;

import java.util.Objects;

public class Pair<E, V> {

    E element1;
    V element2;

    public Pair(E element1, V element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    public E getElement1() {
        return element1;
    }

    public void setElement1(E element1) {
        this.element1 = element1;
    }

    public V getElement2() {
        return element2;
    }

    public void setElement2(V element2) {
        this.element2 = element2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.element1);
        hash = 71 * hash + Objects.hashCode(this.element2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.element1, other.element1)) {
            return false;
        }
        if (!Objects.equals(this.element2, other.element2)) {
            return false;
        }
        return true;
    }

}
