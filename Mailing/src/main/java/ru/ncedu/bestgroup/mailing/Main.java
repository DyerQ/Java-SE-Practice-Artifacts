package ru.ncedu.bestgroup.mailing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<BusinessCard> businessCards = new HashSet<BusinessCard>();
        businessCards.add(new BusinessCard("a@b.com", new HashMap<String, String>()));
        BusinessCard notCopy = new BusinessCard("a@b.com", null);
        System.out.println(notCopy == businessCards.iterator().next());
        System.out.println(businessCards.contains(notCopy));
    }
}
