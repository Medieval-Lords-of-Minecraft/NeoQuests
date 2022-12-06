package me.neoblade298.neoquests.quests;

public class CompletedQuest {
	private Quest q;
	private int stage;
	private boolean success;
	private long timestamp;
	public CompletedQuest(Quest q, int stage, boolean success) {
		this(q, stage, success, System.currentTimeMillis());
	}
	public CompletedQuest(Quest q, int stage, boolean success, long timestamp) {
		this.q = q;
		this.stage = stage;
		this.success = success;
		this.timestamp = timestamp;
	}
	public Quest getQuest() {
		return q;
	}
	public int getStage() {
		return stage;
	}
	public boolean isSuccess() {
		return success;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
