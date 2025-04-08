package edu.bsu.cs.service;

import org.javatuples.Pair;

import java.util.*;

class NaturalLanguageProcessing {

    public static List<String> tokenizeMessage(String sampleText) {
        List<String> document = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(sampleText, " ,.?!");
        while (tokenizer.hasMoreTokens()) {
            document.add(tokenizer.nextToken().toLowerCase());
        }
        return document;
    }

    public static double termFrequency(List<String> tokenizedMessage, String term) {
        long numberOfTerms = tokenizedMessage.stream().filter(word -> word.equals(term)).count();
        return (double) numberOfTerms / tokenizedMessage.size();
    }

    public static double inverseDocumentFrequency(List<List<String>> trainingData, String term) {
        long totalTerms = trainingData.stream().filter(message -> message.contains(term)).count();
        return Math.log((double) trainingData.size() / (1 + totalTerms));
    }

    public static List<Pair<Double, String>> sortWords(List<Pair<Double, String>> inputList) {
        inputList.sort((pair1, pair2) -> Double.compare(pair2.getValue0(), pair1.getValue0()));
        return inputList;
    }

    public static List<Pair<Double, String>> extractKeywords(List<List<String>> trainingData, List<String> tokenizedMessages) {
        List<Pair<Double, String>> finalList = new ArrayList<>();
        for (String word : tokenizedMessages) {
            double wordInverseDocumentFrequency = inverseDocumentFrequency(trainingData, word);
            double wordTermFrequency = termFrequency(tokenizedMessages, word);
            finalList.add(new Pair<Double, String>((wordInverseDocumentFrequency * wordTermFrequency), word));
        }
        sortWords(finalList);
        return finalList;
    }

    public static void main(String[] args) {
        List<String> corpus = Arrays.asList(
                "This is a test message",
                "I hope this works!",
                "How many do I need?"
        );

        List<List<String>> tokenizedCorpus = new ArrayList<>();
        for (String message : corpus) {
            tokenizedCorpus.add(tokenizeMessage(message));
        }

        List<Pair<Double, String>> finalList = new ArrayList<>();
        for (List<String> tokenizedMessage : tokenizedCorpus) {
            for (String word : tokenizedMessage) {
                double wordIDF = inverseDocumentFrequency(tokenizedCorpus, word);
                double wordTF = termFrequency(tokenizedMessage, word);
                finalList.add(new Pair<Double, String>((wordIDF * wordTF), word));
            }
        }
        sortWords(finalList);
        System.out.println(finalList);
    }

}
