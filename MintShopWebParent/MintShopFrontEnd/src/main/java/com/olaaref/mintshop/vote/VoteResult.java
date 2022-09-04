package com.olaaref.mintshop.vote;

public class VoteResult {
	
	private int voteCount;
	private String message;
	private boolean successful;
	
	public static VoteResult fail(String message) {
		return new VoteResult(0, message, false);
	}
	
	public static VoteResult success(String message, int voteCount ) {
		return new VoteResult(voteCount, message, true);
	}
	
	private VoteResult(int voteCount, String message, boolean successful) {
		this.voteCount = voteCount;
		this.message = message;
		this.successful = successful;
	}

	public int getVoteCount() {
		return voteCount;
	}
	
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	
	
}
