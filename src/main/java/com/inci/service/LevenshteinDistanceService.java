package com.inci.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Service
public class LevenshteinDistanceService {

    public double calculateSimilarity(String baseString, String toCompare) {
        LevenshteinDistance levenshtein = new LevenshteinDistance(4);
        double distance = levenshtein.apply(baseString, toCompare);
        String longerString = baseString.length() > toCompare.length() ? baseString : toCompare;
        return 1 - (distance / longerString.length()) * 100;
    }


}
