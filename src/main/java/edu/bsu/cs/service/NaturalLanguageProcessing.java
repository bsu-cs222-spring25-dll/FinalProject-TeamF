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

    public static List<Pair<Double,String>> sortWords(List<Pair<Double, String>> inputList){
        Collections.sort(inputList, Collections.reverseOrder());
        return inputList;
    }

    public static List<Pair<Double,String>> extractKeywords(List<List<String>> trainingData, List<String> tokenizedMessages){
        List<Pair<Double, String>> finalList = new ArrayList<>();
        for(String word : tokenizedMessages){
            double wordInverseDocumentFrequency = inverseDocumentFrequency(trainingData,word);
            double wordTermFrequency = termFrequency(tokenizedMessages,word);
            finalList.add(new Pair<Double, String>((wordInverseDocumentFrequency * wordTermFrequency),word));
        }
        sortWords(finalList);
        return finalList;
    }

    public static void main(String[] args) {
        //Corpus will be an array list of every message in the database
        List<String> corpus = new ArrayList<>();
        corpus.add("This is a test message");
        corpus.add("I hope this works!");
        corpus.add("How many do I need?");

        List<List<String>> tokenizedCorpus = new ArrayList<>();
        for(String message : corpus){
            tokenizedCorpus.add(tokenizeMessage(message));
        }

        List<Pair<Double, String>> finalList = new ArrayList<>();

        //in implementation, all messages sent in a group will be added to the same "tokenized message"
        //then we won't get the output of every message ever sent and instead get the output of just that group
        for(List<String> tokenizedMessage : tokenizedCorpus){
            for(String word : tokenizedMessage){
                double wordIDF = inverseDocumentFrequency(tokenizedCorpus,word);
                double wordTF = termFrequency(tokenizedMessage, word);
                finalList.add(new Pair<Double, String>((wordIDF * wordTF),word));
            }
        }
        sortWords(finalList);
        System.out.println(finalList);
    }

}
