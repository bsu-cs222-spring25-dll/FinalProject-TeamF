package edu.bsu.cs.service;

import org.javatuples.Pair;

import java.util.*;


class NaturalLanguageProcessing {

    public static List<String> tokenizeMessage(String sampleText){
        List<String> document = new ArrayList<>();
        final String punctuation = " ,.?!";
        StringTokenizer tokenizer = new StringTokenizer(sampleText, punctuation);
        while(tokenizer.hasMoreTokens()){
            document.add(tokenizer.nextToken().toLowerCase());
        }
        return document;
    }
    public static double termFrequency(List<String> tokenizedMessage, String term){
        double numberOfTerms = 0;
        for(String word : tokenizedMessage){
            if(word.equals(term)){numberOfTerms++;}
        }
        return numberOfTerms/ tokenizedMessage.size();
    }
    public static double inverseDocumentFrequency(List<List<String>> trainingData, String term){
        double totalTerms = 0;
        for(List<String> message : trainingData){
            for(String word : message){
                if(word.equals(term)){
                    totalTerms++;
                    break;
                }
            }
        }
        return Math.log(trainingData.size()/totalTerms);
    }

    public static void main(String[] args) {
        List<String> corpus = new ArrayList<>();
        corpus.add("This is a test message");
        corpus.add("I hope this works!");
        corpus.add("How many do I need?");

        List<List<String>> tokenizedCorpus = new ArrayList<>();
        for(String message : corpus){
            tokenizedCorpus.add(tokenizeMessage(message));
        }

        List<Pair<Double, String>> finalList = new ArrayList<>();

        for(List<String> tokenizedMessage : tokenizedCorpus){
            for(String word : tokenizedMessage){
                double wordIDF = inverseDocumentFrequency(tokenizedCorpus,word);
                double wordTF = termFrequency(tokenizedMessage, word);
                finalList.add(new Pair<Double, String>((wordIDF * wordTF),word));
            }
        }
        System.out.println(finalList);
    }

}
