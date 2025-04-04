package edu.bsu.cs.service;

import edu.bsu.cs.dao.InterestDAO;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.util.HibernateSessionManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InterestService
{
    private final InterestDAO interestDAO;

    public InterestService(InterestDAO interestDAO){
        this.interestDAO = interestDAO;
    }

    //create interest
    public Interest createInterest(String name){
        //check if interest already exists
        Optional<Interest> existingInterest=interestDAO.findByName(name);
        if(existingInterest.isPresent()){
            return existingInterest.get();
        }
        //else create new interest
        Interest interest=new Interest(name);
        return interestDAO.save(interest);
    }

    public List<Interest> findInterestByNameContaining(String nameContains){
        return interestDAO.findByNameContaining(nameContains);
    }

    public Optional<Interest>findInterestByName(String name){
        return interestDAO.findByName(name);
    }
    public Optional<Interest> findById(UUID id) {
        return interestDAO.findById(id);
    }

    public List<Interest> getAllInterests(){
        return interestDAO.findAll();
    }

    // Update interest
    public Interest updateInterest(Interest interest) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            // Fetch the existing interest from the database
            Interest existingInterest = session.get(Interest.class, interest.getId());

            if (existingInterest == null) {
                throw new RuntimeException("Interest not found with ID: " + interest.getId());
            }

            // Update the name
            existingInterest.setName(interest.getName());

            // Save the updated interest
            session.update(existingInterest);

            return existingInterest;
        });
    }
    // Delete interest
    public void deleteInterest(Interest interest) {
        HibernateSessionManager.executeWithTransaction(session -> {
            // Fetch the existing interest from the database
            Interest existingInterest = session.get(Interest.class, interest.getId());

            if (existingInterest == null) {
                throw new RuntimeException("Interest not found with ID: " + interest.getId());
            }

            // Delete the interest
            session.delete(existingInterest);

            return null;
        });
    }
}
