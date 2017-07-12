package com.revature.caliber.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.data.BatchDAO;
import com.revature.caliber.data.SalesforceDAO;
import com.revature.caliber.data.TrainerDAO;

@Service
public class SalesforceService {

	private static final Logger log = Logger.getLogger(SalesforceService.class);

	@Autowired
	private SalesforceDAO salesforceDAO;
	@Autowired
	private BatchDAO batchDAO;
	@Autowired

	private TrainerDAO trainerDAO;
	
	public void setBatchDAO(BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}

	public void setTrainerDAO(TrainerDAO trainerDAO) {
		this.trainerDAO = trainerDAO;
	}
	
	public void setSalesforceDAO(SalesforceDAO salesforceDAO) {
		this.salesforceDAO = salesforceDAO;
	}

	/**
	 * FIND ALL TRAINERS AND MATCH WITH EMAILS
	 * 
	 * Return List of Trainers
	 */
	public List<Trainer> getAllTrainers(){
		List<Trainer> allCaliberTrainers = trainerDAO.findAll();
		for (int cIndex = 0; cIndex < allCaliberTrainers.size(); cIndex++) {
			String cEmails = allCaliberTrainers.get(cIndex).getEmail();
			if (cEmails == null) {
				continue;
			}
			for (int sfIndex = 0; sfIndex < allCaliberTrainers.size(); sfIndex++) {
				if(cEmails.equals(allCaliberTrainers.get(sfIndex))) {
					break;
				}
			}
			
		}
		return allCaliberTrainers;
	}
	
	/**
	 * FIND ALL CURRENT SALESFORCE BATCHES
	 * 
	 * @return List of Batches
	 */
	public List<Batch> getAllRelevantBatches() {
		log.debug("Find all current batches by year");
		List<Batch> allSalesForceBatches = salesforceDAO.getAllRelevantBatches();
		// List<Batch> allSalesForceBatches = salesforceDAO.getFakeReleventBatches();
		List<Batch> allCaliberBatches = batchDAO.findAll();
		
		// load trainer and co-trainer from Caliber DB
		Map<String, Trainer> trainerMap = loadTrainers();
		for(Batch batch : allSalesForceBatches){
			batch.setTrainer(trainerMap.get(batch.getTrainer().getEmail()));
			batch.setCoTrainer(trainerMap.get(batch.getCoTrainer().getEmail()));
		}
		
		for(Batch batch : allSalesForceBatches){
			log.info(batch.getTrainer());
			log.info(batch.getCoTrainer());
		}
		
		//Removing batches already in Caliber database
		for (int cIndex = 0; cIndex < allCaliberBatches.size(); cIndex++) {
			String cResourceId = allCaliberBatches.get(cIndex).getResourceId();
			if (cResourceId == null) {
				continue;
			}
			for (int sfIndex = 0; sfIndex < allSalesForceBatches.size(); sfIndex++) {
				String sfResourceId = allSalesForceBatches.get(sfIndex).getResourceId();
				if(cResourceId.equals(sfResourceId)) {
					allSalesForceBatches.remove(sfIndex);
					break;
				}
			}
		}

		return allSalesForceBatches;
	}

	
	private Map<String, Trainer> loadTrainers() {
		List<Trainer> trainers = trainerDAO.findAll();
		Map<String, Trainer> trainerMap = new HashMap<>();
		for(Trainer t : trainers){
			trainerMap.putIfAbsent(t.getEmail(), t);
		}
		return trainerMap;
	}

	/**
	 * FIND ALL TRAINEES
	 * @return List of Trainees
	 */
	
	public List<Trainee> getAllTraineesFromBatch(String resourceId){
		log.debug("Find all trainees");
		return salesforceDAO.getBatchDetails(resourceId);
	}
	

}
