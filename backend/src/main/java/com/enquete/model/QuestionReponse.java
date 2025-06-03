package com.enquete.model;

public class QuestionReponse {
	private String question;
	private String reponse;
	
	public QuestionReponse(String question, String reponse) {
		this.question = question;
		this.reponse = reponse;
	}
	
	public String getQuestion() {return question;}
	public String getReponse() {return reponse;}
	
	@Override
	public String toString() {
		return question + "->" + reponse;
	}

}
